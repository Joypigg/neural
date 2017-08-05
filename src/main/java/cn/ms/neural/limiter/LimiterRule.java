package cn.ms.neural.limiter;

import java.util.List;

/**
 * 限流统计
 * 
 * @author lry
 */
public class LimiterRule {

	private String keys;
	private List<Granularity> granularity;

	public LimiterRule() {
	}

	public LimiterRule(String keys, List<Granularity> granularity) {
		this.keys = keys;
		this.granularity = granularity;
	}

	public String getKeys() {
		return keys;
	}

	public void setKeys(String keys) {
		this.keys = keys;
	}

	public List<Granularity> getLimiterRes() {
		return granularity;
	}

	public void setLimiterRes(List<Granularity> granularity) {
		this.granularity = granularity;
	}

	@Override
	public String toString() {
		return "LimiterStatistics [keys=" + keys + ", limiterRes=" + granularity
				+ "]";
	}

}
