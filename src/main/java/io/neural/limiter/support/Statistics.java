package io.neural.limiter.support;

public class Statistics {
	// 计算调用耗时
	private Long elapsed;
	// 当前并发数
	private Long concurrent;
	private Long successCount;
	private Long failureCount;

	public Long getElapsed() {
		return elapsed;
	}

	public void setElapsed(Long elapsed) {
		this.elapsed = elapsed;
	}

	public Long getConcurrent() {
		return concurrent;
	}

	public void setConcurrent(Long concurrent) {
		this.concurrent = concurrent;
	}

	public Long getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(Long successCount) {
		this.successCount = successCount;
	}

	public Long getFailureCount() {
		return failureCount;
	}

	public void setFailureCount(Long failureCount) {
		this.failureCount = failureCount;
	}

}
