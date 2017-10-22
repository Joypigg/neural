package io.neural.limiter;

import io.neural.limiter.support.ILimiterProcessor;
import io.neural.limiter.support.Statistics;

public interface ILimiter {

	void initialize();

	Object doLimiter(ILimiterProcessor processor) throws Throwable;

	void destroy();

	/**
	 * 获取统计并重置计数器为0
	 * 
	 * @return
	 */
	Statistics getStatisticsAndSet0();

}
