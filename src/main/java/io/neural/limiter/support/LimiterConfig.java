package io.neural.limiter.support;

public class LimiterConfig {

	private String appName;
	private String limiterId;
	
	private Boolean enable;
	private Integer maxRate;
	private Integer maxConcurrent;
	private Integer strategy;
	
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getLimiterId() {
		return limiterId;
	}
	public void setLimiterId(String limiterId) {
		this.limiterId = limiterId;
	}
	public Boolean getEnable() {
		return enable;
	}
	public void setEnable(Boolean enable) {
		this.enable = enable;
	}
	public Integer getMaxRate() {
		return maxRate;
	}
	public void setMaxRate(Integer maxRate) {
		this.maxRate = maxRate;
	}
	public Integer getMaxConcurrent() {
		return maxConcurrent;
	}
	public void setMaxConcurrent(Integer maxConcurrent) {
		this.maxConcurrent = maxConcurrent;
	}
	public Integer getStrategy() {
		return strategy;
	}
	public void setStrategy(Integer strategy) {
		this.strategy = strategy;
	}

}
