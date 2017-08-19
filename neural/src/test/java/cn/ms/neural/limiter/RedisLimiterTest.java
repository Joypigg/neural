package cn.ms.neural.limiter;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import cn.ms.neural.MURL;
import cn.ms.neural.limiter.cluster.Granularity;
import cn.ms.neural.limiter.cluster.LimiterRule;
import cn.ms.neural.limiter.cluster.RedisLimiter;

public class RedisLimiterTest {

	RedisLimiter redisLimiter = new RedisLimiter();

	public RedisLimiterTest() {
		redisLimiter.start(MURL.valueOf("redis://127.0.0.1:6379"));
	}

	@Test
	public void setLimiterRulesTest() {
		LimiterRule limiterRule = new LimiterRule();
		limiterRule.setKeys("app");
		List<Granularity> list = new ArrayList<Granularity>();
		list.add(new Granularity("HOUR", 100l, 0l));
		list.add(new Granularity("MINUTE", 20l, 0l));
		list.add(new Granularity("SECOND", 6l, 0l));
		limiterRule.setLimiterRes(list);
		redisLimiter.setLimiterRule(limiterRule);
	}

	@Test
	public void incrementTest() throws Exception {
		for (int i = 0; i < 20; i++) {
			redisLimiter.increment("app");
			Thread.sleep(600);
		}
	}

	@Test
	public void queryLimiterRulesTest() {
		System.out.println(redisLimiter.queryLimiterRules(""));
	}

}
