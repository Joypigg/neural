package cn.ms.neural.limiter;

/**
 * 限流统计
 * 
 * @author lry
 */
public class LimiterStatistics {

	private String keys;
	private String timeGranularity;
	private Long maxAmount;
	private Long nowAmount;

	public LimiterStatistics() {
	}
	
	public LimiterStatistics(String keys, String timeGranularity,
			Long maxAmount, Long nowAmount) {
		this.keys = keys;
		this.timeGranularity = timeGranularity;
		this.maxAmount = maxAmount;
		this.nowAmount = nowAmount;
	}

	public String getKeys() {
		return keys;
	}

	public void setKeys(String keys) {
		this.keys = keys;
	}

	public String getTimeGranularity() {
		return timeGranularity;
	}

	public void setTimeGranularity(String timeGranularity) {
		this.timeGranularity = timeGranularity;
	}

	public Long getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(Long maxAmount) {
		this.maxAmount = maxAmount;
	}

	public Long getNowAmount() {
		return nowAmount;
	}

	public void setNowAmount(Long nowAmount) {
		this.nowAmount = nowAmount;
	}

	@Override
	public String toString() {
		return "LimiterStatistics [keys=" + keys + ", timeGranularity="
				+ timeGranularity + ", maxAmount=" + maxAmount + ", nowAmount="
				+ nowAmount + "]";
	}

}
