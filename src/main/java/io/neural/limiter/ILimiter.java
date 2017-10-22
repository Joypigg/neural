package io.neural.limiter;

import io.neural.limiter.support.ILimiterProcessor;
import io.neural.limiter.support.LimiterConfig;
import io.neural.limiter.support.Statistics;

public interface ILimiter {

	void initialize();
	
	/**
	 * 刷新配置
	 * 
	 * @param config
	 */
	void refresh(LimiterConfig config);

	/**
	 * 执行限流
	 * 
	 * @param processor
	 * @return
	 * @throws Throwable
	 */
	Object doLimiter(ILimiterProcessor processor) throws Throwable;

	/**
	 * 获取统计并重置计数器为0
	 * 
	 * @return
	 */
	Statistics getStatisticsAndSet0();

	/**
	 * 销毁
	 */
	void destroy();
	
}
