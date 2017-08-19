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
import cn.ms.neural.limiter.LimStatus;
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
	public static final String LIMITER_RULE_BATCH_SET_NAME = "limiter_rule_batch_set.lua";
	public static final String LIMITER_RULE_QUERY_NAME = "limiter_rule_query.lua";
	
	private static final Logger logger = LoggerFactory.getLogger(RedisLimiter.class);

	private JedisPool jedisPool;

	public String NAMESPACE_KEY;
	private String LIMITER_SCRIPT;
	private String LIMITER_RULE_BATCH_SET_SCRIPT;
	private String LIMITER_RULE_QUERY_SCRIPT;

	public synchronized JedisPool getJedisPool() {
		return jedisPool;
	}

	@Override
	public boolean start(MURL url) {
		try {
			JedisPoolConfig config = new JedisPoolConfig();
			Map<String, String> parameters = url.getParameters();
			BeanUtils.copyProperties(config, parameters);

			jedisPool = new JedisPool(config, url.getHost(), url.getPort());

			NAMESPACE_KEY = url.getPath();
			
			LIMITER_SCRIPT = getScript(LIMITER_NAME);
			LIMITER_RULE_BATCH_SET_SCRIPT = getScript(LIMITER_RULE_BATCH_SET_NAME);
			LIMITER_RULE_QUERY_SCRIPT = this.getScript(LIMITER_RULE_QUERY_NAME);
		} catch (Exception e) {
			logger.error("The start " + this.getClass().getSimpleName() + " is exception.", e);
		}
		return false;
	}

	@Override
	public LimStatus increment(String[]... keys) {
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
			
			String script = String.format(LIMITER_SCRIPT, NAMESPACE_KEY, sb.toString(), NAMESPACE_KEY, sb.toString());
			logger.debug("The script is: {}", script);
			
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
					return LimStatus.valueOf(result);
				} catch (Exception e) {
					return LimStatus.ILLAGE;
				}
			}
		} catch (Exception e) {
			logger.error("The do increment is exception.", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		
		return LimStatus.UNKNOWN;
	}
	
	@Override
	public boolean addOrUpRule(LimiterRule limiterRule) {
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
			
			String script = String.format(LIMITER_RULE_BATCH_SET_SCRIPT, NAMESPACE_KEY, limiterRule.getKeys());
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
	public List<LimiterRule> search(Integer type, String keywords) {
		List<LimiterRule> limiterRules = new ArrayList<LimiterRule>();
		
		Jedis jedis = null;
		try {
			jedis = this.getJedisPool().getResource();
			List<String> argKeys = Arrays.asList(NAMESPACE_KEY);
			List<String> argValues = Arrays.asList(keywords==null?"":keywords);
			
			Object result = jedis.eval(LIMITER_RULE_QUERY_SCRIPT, argKeys, argValues);
			logger.debug("执行结果：{}", result);
			if(result == null || !(result instanceof List)){
				return limiterRules;
			} else {
				List list = (List)result;
				if(list.size()%2==0){
					throw new IllegalArgumentException();
				} else {
					//Long time = Long.valueOf(String.valueOf(list.get(0)));// 返回Redis中的时间戳
					for (int i = 1; i < list.size(); i+=2) {
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
							limiterRules.add(new LimiterRule(key, data));
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
		}
		
		return limiterRules;
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
