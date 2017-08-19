package cn.ms.neural.limiter;

import org.junit.Test;

import cn.ms.neural.MURL;
import cn.ms.neural.limiter.cluster.RedisLimiter;

public class RedisLimiterTest {

	RedisLimiter redisLimiter = new RedisLimiter();

	String[][] rule_keys = {{"area", "beijing"}, {"channel", "web"}, {"env", "sit"}};
	
	public RedisLimiterTest() {
		redisLimiter.start(MURL.valueOf("redis://127.0.0.1:6379/ms"));
	}

	@Test
	public void setLimiterRulesTest() {
		LimiterRule limiterRule = new LimiterRule();
		limiterRule.buildKeys(rule_keys).putData("HOUR", 400l).putData("MINUTE", 80l).putData("SECOND", 24l);
		redisLimiter.addOrUpRule(limiterRule);
	}

	@Test
	public void incrementTest() throws Exception {
		for (int i = 0; i < 20; i++) {
			redisLimiter.increment(rule_keys);
			Thread.sleep(600);
		}
	}

	@Test
	public void queryLimiterRulesTest() {
		System.out.println(redisLimiter.search(1, null));
	}

}
