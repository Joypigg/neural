package cn.ms.neural.limiter.cluster;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import cn.ms.neural.MURL;
import cn.ms.neural.Store;
import cn.ms.neural.extension.SpiMeta;
import cn.ms.neural.limiter.LimiterData;
import cn.ms.neural.limiter.OptStatus;
import cn.ms.neural.limiter.LimiterRule;

import com.google.common.io.CharStreams;

/**
 * 基于Redis实现分布式限流<br>
 * <br>
 * 1.限流规则：Hash<rate_limiter_rule:[KEY], Map<[SECOND/MINUTE/HOUR/DAY/MONTH/YEAR/CUSTOM], [最大资源数量]>><br>
 * 2.限流统计：String<rate_limiter_incr:[KEY]:[SECOND/MINUTE/HOUR/DAY/MONTH/YEAR/CUSTOM], [已用资源数量]><br>
 * 3.超额记录：Hash<rate_limiter_mark:[KEY], Map<[SECOND/MINUTE/HOUR/DAY/MONTH/YEAR/CUSTOM]_time, [超额次数]>><br>
 * <br>
 * @author lry
 */
@SpiMeta(name = "redis")
public class RedisLimiter extends ClusterLimiter {
	
	public static final String LIMITER_NAME = "limiter.lua";
	public static final String LIMITER_ADDUP_RULE_NAME = "limiter_addup_rule.lua";
	public static final String LIMITER_SEARCH_NAME = "limiter_search.lua";
	
	private static final Logger logger = LoggerFactory.getLogger(RedisLimiter.class);

	private JedisPool jedisPool;

	private String LIMITER_SCRIPT;
	private String LIMITER_ADDUP_RULE_SCRIPT;
	private String LIMITER_SEARCH_SCRIPT;

	public synchronized JedisPool getJedisPool() {
		return jedisPool;
	}

	@Override
	public boolean start(MURL murl) {
		try {
			JedisPoolConfig config = new JedisPoolConfig();
			Map<String, String> parameters = murl.getParameters();
			BeanUtils.copyProperties(config, parameters);
			jedisPool = new JedisPool(config, murl.getHost(), murl.getPort());
			
			LIMITER_SCRIPT = getScript(LIMITER_NAME);
			LIMITER_ADDUP_RULE_SCRIPT = getScript(LIMITER_ADDUP_RULE_NAME);
			LIMITER_SEARCH_SCRIPT = this.getScript(LIMITER_SEARCH_NAME);
		} catch (Exception e) {
			logger.error("The start " + this.getClass().getSimpleName() + " is exception.", e);
		}
		return false;
	}

	@Override
	public OptStatus increment(String scene, String[]... keys) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < keys.length; i++) {
			sb.append(keys[i][0]).append("=").append(keys[i][1]);
			if (i < keys.length - 1) {
				sb.append("&");
			}
		}

		Jedis jedis = null;
		try {
			jedis = this.getJedisPool().getResource();
			List<String> argKeys = Arrays.asList(sb.toString());
			List<String> argValues = new ArrayList<String>();
			
			String script = String.format(LIMITER_SCRIPT, scene, sb.toString(), scene, sb.toString());
			Object resultObject = jedis.eval(script, argKeys, argValues);
			logger.debug("The jedis eval result is: {}", resultObject);
			if (resultObject == null || !(resultObject instanceof List<?>)) {
				throw new UnknownError("resultObject=" + resultObject);
			}

			List<?> resultArgs = (List<?>) resultObject;
			if (resultArgs.isEmpty()) {
				throw new IllegalStateException("The result is empty: " + resultObject);
			} else {
				String result = String.valueOf(resultArgs.get(0));
				try {
					return OptStatus.valueOf(result);
				} catch (Exception e) {
					return OptStatus.ILLAGE;
				}
			}
		} catch (Exception e) {
			logger.error("The do increment is exception.", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		
		return OptStatus.UNKNOWN;
	}
	
	@Override
	public boolean addOrUpRule(String scene, LimiterRule limiterRule) {
		Jedis jedis = null;
		try {
			jedis = this.getJedisPool().getResource();
			
			//[key1, key2, …… , keyN]
			List<String> argKeys = new ArrayList<String>();
			//[vlaue1, value2, …… , valueN]
			List<String> argValues = new ArrayList<String>();
			for (Map.Entry<String, Store<Long, Long>> entry:limiterRule.getData().entrySet()) {
				argKeys.add(entry.getKey());
				argValues.add(String.valueOf(entry.getValue().getKey()));
			}
			
			String script = String.format(LIMITER_ADDUP_RULE_SCRIPT, scene, limiterRule.getKeys());
			Object result = jedis.eval(script, argKeys, argValues);
			return Boolean.valueOf(String.valueOf(result));
		} catch (Exception e) {
			logger.error("The do setRule is exception.", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		
		return false;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public LimiterData search(String keywords) {
		LimiterData limiterData = new LimiterData(keywords);
		Map<LimiterRule, String> rules = new HashMap<LimiterRule, String>();
		
		Jedis jedis = null;
		try {
			jedis = this.getJedisPool().getResource();
			List<String> argKeys = Arrays.asList();
			List<String> argValues = Arrays.asList(keywords==null?"":keywords);
			
			Object result = jedis.eval(LIMITER_SEARCH_SCRIPT, argKeys, argValues);
			logger.debug("执行结果：{}", result);
			if(result == null || !(result instanceof List)){
				return limiterData;
			} else {
				List list = (List)result;
				if(list.size()%3==0){
					throw new IllegalArgumentException();
				} else {
					Long time = Long.valueOf(String.valueOf(list.get(0)));// 返回Redis中的时间戳
					limiterData.setTime(time);
					
					for (int i = 1; i < list.size(); i+=3) {
						String key = String.valueOf(list.get(i));
						Object value = list.get(i+1);
						if(value instanceof List){
							Map<String, Store<Long, Long>> data = new HashMap<String, Store<Long,Long>>();
							List<List<Object>> granularityList = (List<List<Object>>)value;
							for (List<Object> granularity:granularityList) {
								String category = String.valueOf(granularity.get(0));
								Long maxAmount = Long.valueOf(String.valueOf(granularity.get(1)));
								Long nowAmount = Long.valueOf(String.valueOf(granularity.get(2)));
								
								data.put(category, new Store<Long, Long>(maxAmount, nowAmount));
							}
							rules.put(new LimiterRule(key, data), String.valueOf(list.get(i+2)));
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("The do setRule is exception.", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
			if(!rules.isEmpty()){
				limiterData.setRules(rules);
			}
		}
		
		return limiterData;
	}
	
	@Override
	public void shutdown() {
		if (this.getJedisPool() != null) {
			this.getJedisPool().close();
		}
	}
	
	/**
	 * 加载Lua代码
	 * 
	 * @param name
	 * @return
	 */
	private String getScript(String name) {
		try {
			Reader reader = null;
			InputStream inputStream = null;
			try {
				inputStream = this.getClass().getClassLoader().getResourceAsStream(name);
				reader = new InputStreamReader(inputStream);
				return CharStreams.toString(reader);
			} finally {
				if (reader != null) {
					reader.close();
				}
			}
		} catch (Exception e) {
			logger.error("The getScript " + this.getClass().getSimpleName() + " is exception.", e);
		}
		
		return "";
	}

}
