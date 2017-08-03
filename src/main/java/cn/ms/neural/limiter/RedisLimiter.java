package cn.ms.neural.limiter;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import cn.ms.micro.common.URL;
import cn.ms.micro.extension.SpiMeta;

import com.google.common.io.CharStreams;

/**
 * 限流规则：Map<limiter:[KEY], [SECOND/MINUTE/HOUR/DAY/MONTH/YEAR], [最大资源数量]>
 * 限流统计<limiter:[KEY]:[SECOND/MINUTE/HOUR/DAY/MONTH/YEAR], [当前已被使用的资源数量]>
 * 
 * @author lry
 */
@SpiMeta(name = "redis")
public class RedisLimiter implements Limiter {

	public static final String LIMITER_NAME = "limiter.lua";

	private static final Logger logger = LoggerFactory
			.getLogger(RedisLimiter.class);

	private static JedisPool jedisPool;
	private String script;

	public synchronized JedisPool getJedisPool() {
		return jedisPool;
	}

	@Override
	public boolean start(URL url) {
		try {
			JedisPoolConfig config = new JedisPoolConfig();
			Map<String, String> parameters = url.getParameters();
			BeanUtils.copyProperties(config, parameters);

			jedisPool = new JedisPool(config, url.getHost(), url.getPort());

			// 加载Lua代码
			Reader reader = null;
			InputStream inputStream = null;
			try {
				inputStream = this.getClass().getClassLoader()
						.getResourceAsStream(LIMITER_NAME);
				reader = new InputStreamReader(inputStream);
				script = CharStreams.toString(reader);
			} finally {
				if (reader != null) {
					reader.close();
				}
			}
		} catch (Exception e) {
			logger.error("The start " + this.getClass().getSimpleName() + " is exception.", e);
		}
		return false;
	}

	@Override
	public boolean increment(String... keys) {
		return this.increment(null, keys);
	}

	@Override
	public boolean increment(List<String> keys) {
		return this.increment(null, keys);
	}

	@Override
	public boolean increment(Long expire, List<String> keys) {
		if (keys == null || keys.size() == 0) {
			return this.increment(expire);
		} else {
			String[] tempKeys = new String[keys.size()];
			return this.increment(expire, keys.toArray(tempKeys));
		}
	}

	@Override
	public boolean increment(Long expire, String... keys) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < keys.length; i++) {
			sb.append(keys[i]);
			if (i < keys.length - 1) {
				sb.append("/");
			}
		}

		Jedis jedis = null;
		try {
			jedis = this.getJedisPool().getResource();
			List<String> argKeys = Arrays.asList(sb.toString());
			List<String> argValues = new ArrayList<String>();
			if (expire != null) {
				argValues.add(String.valueOf(expire));
			}

			Object resultObject = jedis.eval(script, argKeys, argValues);
			logger.debug("The jedis eval result is: ", resultObject);
			if (resultObject == null || !(resultObject instanceof List<?>)) {
				throw new UnknownError("resultObject=" + resultObject);
			}

			List<?> result = (List<?>) resultObject;
			switch (String.valueOf(result.get(0))) {
			case "OK":
				break;
			case "NORULE":
				break;
			case "FULL":
				break;
			default:
				break;
			}

			return true;
		} catch (Exception e) {
			logger.error("The do increment is exception.", e);
			return false;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public List<LimiterRule> queryRules(String keywords) {
		List<LimiterRule> list = new ArrayList<LimiterRule>();
		Jedis jedis = null;
		try {
			jedis = this.getJedisPool().getResource();
			Set<String> ruleKeys = jedis.keys("rate_limiter_rule:*" + keywords + "*");
			for (String ruleKey:ruleKeys) {
				Map<TimeGranularity, Long> balance = new HashMap<TimeGranularity, Long>();
				Map<String, String> map = jedis.hgetAll(ruleKey);
				for (Map.Entry<String, String> entry:map.entrySet()) {
					balance.put(TimeGranularity.valueOf(entry.getKey()), Long.parseLong(entry.getValue()));
				}
				list.add(new LimiterRule(ruleKey.substring("rate_limiter_rule:".length()), balance));
			}
		} catch (Exception e) {
			logger.error("The do increment is exception.", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

		return list;
	}

	@Override
	public List<LimiterStatistics> queryStatistics(String keywords) {
		List<LimiterRule> limiterRules = this.queryRules(keywords);
		
		List<LimiterStatistics> list = new ArrayList<LimiterStatistics>();
		Jedis jedis = null;
		try {
			jedis = this.getJedisPool().getResource();
			
			for (LimiterRule limiterRule:limiterRules) {
				for (Map.Entry<TimeGranularity,Long> entry:limiterRule.getBalance().entrySet()) {
					String value = jedis.get("rate_limiter_incr:" + limiterRule.getKeys() + ":"+entry.getKey().toString());
					String keys = limiterRule.getKeys();
					String timeGranularity = entry.getKey().toString();
					Long maxAmount = entry.getValue();
					Long nowAmount = value==null?0:Long.parseLong(value);
					list.add(new LimiterStatistics(keys, timeGranularity, maxAmount, nowAmount));
				}
			}
		} catch (Exception e) {
			logger.error("The do increment is exception.", e);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

		return list;
	
	}

	@Override
	public void shutdown() {
		if (this.getJedisPool() != null) {
			this.getJedisPool().close();
		}
	}

}
