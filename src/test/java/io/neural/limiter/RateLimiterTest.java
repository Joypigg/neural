package io.neural.limiter;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class RateLimiterTest {

	public static void main(String[] args) throws Exception {
		RateLimiter rateLimiter = RateLimiter.create(5, 2);
		for (int i = 0; i < 10; i++) {
			System.out.println(rateLimiter.acquire());
		}
		System.out.println(rateLimiter.toString());
		System.out.println(rateLimiter.getRate());
	}

	/**
	 * 平均200ms获取1个
	 *
	 * @throws Exception
	 */
	@Test
	public void testAvgRate() throws Exception {
		RateLimiter rateLimiter = RateLimiter.create(5);
		assert 0 == rateLimiter.acquire();
		assert 2 == Math.round(rateLimiter.acquire() * 10);
		assert 2 == Math.round(rateLimiter.acquire() * 10);
		assert 2 == Math.round(rateLimiter.acquire() * 10);
		assert 2 == Math.round(rateLimiter.acquire() * 10);
		assert 2 == Math.round(rateLimiter.acquire() * 10);
	}

	/**
	 * 测试突发
	 *
	 * @throws Exception
	 */
	@Test
	public void burstTest() throws Exception {
		RateLimiter rateLimiter = RateLimiter.create(5);
		assert 0 == rateLimiter.acquire(5);
		assert 1 == Math.round(rateLimiter.acquire(1));
		assert 2 == Math.round(rateLimiter.acquire(1) * 10);
		assert 2 == Math.round(rateLimiter.acquire(1) * 10);
		assert 2 == Math.round(rateLimiter.acquire(1) * 10);

		RateLimiter rateLimiter1 = RateLimiter.create(5);
		assert 0 == Math.round(rateLimiter1.acquire(10));
		assert 2 == Math.round(rateLimiter1.acquire(1));
		assert 2 == Math.round(rateLimiter1.acquire(1) * 10);
	}

	/**
	 * 测试积攒量
	 *
	 * @throws Exception
	 */
	@Test
	public void accumulateTest() throws Exception {
		RateLimiter rateLimiter = RateLimiter.create(2);
		assert 0 == rateLimiter.acquire();
		Thread.sleep(4000);
		assert 0 == rateLimiter.acquire();
		assert 0 == rateLimiter.acquire();
		assert 0 == rateLimiter.acquire();
		assert 5 == Math.round(rateLimiter.acquire() * 10);
		assert 5 == Math.round(rateLimiter.acquire() * 10);
		Thread.sleep(2000);
		assert 0 == rateLimiter.acquire();
		assert 0 == rateLimiter.acquire();
		assert 0 == rateLimiter.acquire();
		assert 5 == Math.round(rateLimiter.acquire() * 10);
		assert 5 == Math.round(rateLimiter.acquire() * 10);
	}

	@Test
	public void smoothWarmingTest() throws Exception {
		/**
		 * 每秒新增令牌数为5, 1000ms内速率缓慢降至平均速率
		 * <p>
		 * 平均200ms新增一个
		 */
		RateLimiter limiter = RateLimiter.create(5, 1000, TimeUnit.MILLISECONDS);
		for (int i = 0; i < 10; i++) {
			System.out.println(limiter.acquire());

		}
		Thread.sleep(2000);
		for (int i = 0; i < 10; i++) {
			System.out.println(limiter.acquire());
		}
	}
	
	@Test
	public void testTry() throws Exception {
		RateLimiter rateLimiter = RateLimiter.create(1);
		for (int i = 0; i < 10; i++) {
			System.out.println(rateLimiter.tryAcquire(1000, TimeUnit.MILLISECONDS));	
		}
	}

}
