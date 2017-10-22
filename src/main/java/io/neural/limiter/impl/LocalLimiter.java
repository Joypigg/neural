package io.neural.limiter.impl;

import io.neural.limiter.support.AbstractLimiter;
import io.neural.limiter.support.LimiterConfig;

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
	}
	
	@Override
	public void refresh(LimiterConfig config) {
		super.refresh(config);
		if(null == config){
			return;
		}
		if(null == config.getEnable() || config.getEnable()){
			semaphore = null;
			rateLimiter = null;
		}
		if(null != config.getMaxConcurrent() && config.getMaxConcurrent() > 0){
			semaphore = new Semaphore(config.getMaxConcurrent(), true);	
		}
		if(null != config.getMaxRate() && config.getMaxRate() > 0){
			if(null == rateLimiter){
				rateLimiter = RateLimiter.create(config.getMaxRate(), 1000, TimeUnit.MILLISECONDS);				
			} else {
				rateLimiter.setRate(config.getMaxRate());
			}
		}
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
