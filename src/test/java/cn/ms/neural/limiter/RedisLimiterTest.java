package cn.ms.neural.limiter;

import cn.ms.micro.common.URL;

public class RedisLimiterTest {
	
	public static void main(String[] args) {
		RedisLimiter redisLimiter = new RedisLimiter();
		redisLimiter.start(URL.valueOf("redis://127.0.0.1:6379"));
		for (int i = 0; i < 5; i++) {
			redisLimiter.increment("weixin");
		}
		
		System.out.println(redisLimiter.queryStatistics(""));;
	}
	
}
