package io.neural.limiter.impl;

import io.neural.limiter.support.AbstractLimiter;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.RateLimiter;

/**
 * 基于本地的分布式限流(分而治之思想)
 * 
 * @author lry
 */
public class LocalLimiter extends AbstractLimiter {

	private volatile Semaphore semaphore = null;
	private volatile RateLimiter rateLimiter = null;

	@Override
	public void initialize() {
		super.initialize();
		this.semaphore = new Semaphore(10, true);
		this.rateLimiter = RateLimiter.create(10000, 1000, TimeUnit.MILLISECONDS);
		this.isStart = true;
	}

	@Override
	public boolean isConcurrent() throws Exception {
		return semaphore == null || semaphore.tryAcquire(200, TimeUnit.MILLISECONDS);
	}

	@Override
	public boolean isRate() throws Exception {
		return rateLimiter == null || rateLimiter.tryAcquire(200, TimeUnit.MILLISECONDS);
	}
	
	@Override
	public void releaseConcurrent() throws Exception {
		if(semaphore!=null){
			semaphore.release(); // 并发释放
		}		
	}
	
	@Override
	public void destroy() {
		super.destroy();
		semaphore = null;
		rateLimiter = null;
	}
	
}
