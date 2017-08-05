package cn.ms.neural.limiter;

import cn.ms.micro.common.URL;

public class RedisLimiterTest {
	
//	public static void main(String[] args) {
//		LimiterRule limiterRule = new LimiterRule();
//		limiterRule.setKeys("app");
//		Map<String, Long> balance = new HashMap<String, Long>();
//		balance.put("HOUR", 100l);
//		balance.put("MINUTE", 20l);
//		balance.put("SECOND", 6l);
//		limiterRule.setBalance(balance);
//		
//		RedisLimiter redisLimiter = new RedisLimiter();
//		redisLimiter.start(URL.valueOf("redis://127.0.0.1:6379"));
//		redisLimiter.setRule(limiterRule);
//	}
	
	public static void main(String[] args) {
		RedisLimiter redisLimiter = new RedisLimiter();
		redisLimiter.start(URL.valueOf("redis://127.0.0.1:6379"));
		for (int i = 0; i < 5; i++) {
			redisLimiter.increment("app");
		}
		
		System.out.println(redisLimiter.queryStatistics(""));;
	}
	
}
