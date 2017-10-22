package io.neural.limiter.support;

import io.neural.limiter.ILimiter;

import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractLimiter implements ILimiter {

	protected static final Logger logger = LoggerFactory.getLogger(AbstractLimiter.class);
	
	protected LimiterConfig config = null;
	protected volatile boolean isStart = false;
	private final AtomicLong nowConcurrentCount = new AtomicLong(0);
	
	private final AtomicLong concurrentCount = new AtomicLong(0);
	private final AtomicLong successCount = new AtomicLong(0);
	private final AtomicLong failureCount = new AtomicLong(0);
	private final AtomicLong elapsedCount = new AtomicLong(0);

	@Override
	public void initialize() {
		this.isStart = true;
	}
	
	@Override
	public void refresh(LimiterConfig config) {
		this.config = config;
	}

	@Override
	public Object doLimiter(ILimiterProcessor processor) throws Throwable {
		if(isStart){
			return processor.doProcessor();
		}
		
		long startTime = System.currentTimeMillis();
		try {
			// 加流量
			concurrentCount.addAndGet(nowConcurrentCount.incrementAndGet());
			// 并发校验
			if(this.isConcurrent()){
				try {
					// 速率校验
					if(this.isRate()){
						// 交易成功计数
						successCount.incrementAndGet();
						return processor.doProcessor();
					} else{
						logger.warn("Traffic rate limited.");
					}
				} finally {
					// 释放并发资源
					this.releaseConcurrent();
				}
			} else {
				logger.warn("Concurrent traffic limited.");
			}

			// 交易失败计数
			failureCount.incrementAndGet();
			return null;
		} finally {
			// 减流量
			elapsedCount.addAndGet(System.currentTimeMillis() - startTime);
			// 统计器校验、重置
			this.checkCounterAndReset();
		}
	}
	
	public abstract boolean isConcurrent() throws Exception;
	public abstract boolean isRate() throws Exception;
	public abstract void releaseConcurrent() throws Exception;

	@Override
	public void destroy() {
		this.isStart = false;
	}

	@Override
	public synchronized Statistics getStatisticsAndSet0() {
		Statistics statistics = new Statistics();
		statistics.setConcurrent(concurrentCount.getAndSet(0));
		statistics.setElapsed(elapsedCount.getAndSet(0));
		statistics.setSuccessCount(successCount.getAndSet(0));
		statistics.setFailureCount(failureCount.getAndSet(0));
		return statistics;
	}

	private void checkCounterAndReset() {
		if (concurrentCount.get() == Long.MAX_VALUE || concurrentCount.get() < 0
				|| successCount.get() == Long.MAX_VALUE || successCount.get() < 0
				|| failureCount.get() == Long.MAX_VALUE || failureCount.get() < 0
				|| elapsedCount.get() == Long.MAX_VALUE || elapsedCount.get() < 0) {
			concurrentCount.set(0L);
			successCount.set(0L);
			failureCount.set(0L);
			elapsedCount.set(0L);
		}
	}

}
