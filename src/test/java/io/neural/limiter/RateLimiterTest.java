package io.neural.limiter;

public class RateLimiterTest {

	public static void main(String[] args) throws Exception {
		RateLimiter rateLimiter = RateLimiter.create(5, 2);
		for (int i = 0; i < 10; i++) {
			System.out.println(rateLimiter.acquire());
		}
		System.out.println(rateLimiter.toString());
		System.out.println(rateLimiter.getRate());
	}
	
}
