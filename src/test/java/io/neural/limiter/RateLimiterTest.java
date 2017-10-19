package io.neural.limiter;

public class RateLimiterTest {

	public static void main(String[] args) {
		RateLimiter rateLimiter = RateLimiter.create(5);
		for (int i = 0; i < 10; i++) {
			System.out.println(rateLimiter.acquire());
		}
		System.out.println(rateLimiter.toString());
	}
	
}
