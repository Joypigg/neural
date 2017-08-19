package cn.ms.neural.limiter.cluster;

import java.util.List;

/**
 * 限流统计
 * 
 * @author lry
 */
public class LimiterRule {

	private String keys;
	private Long time;
	private List<Granularity> granularity;

	public LimiterRule() {
	}

	public LimiterRule(String keys, Long time, List<Granularity> granularity) {
		this.keys = keys;
		this.time = time;
		this.granularity = granularity;
	}

	public String getKeys() {
		return keys;
	}

	public void setKeys(String keys) {
		this.keys = keys;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public List<Granularity> getLimiterRes() {
		return granularity;
	}

	public void setLimiterRes(List<Granularity> granularity) {
		this.granularity = granularity;
	}

	@Override
	public String toString() {
		return "LimiterRule [keys=" + keys + ", time=" + time
				+ ", granularity=" + granularity + "]";
	}

}
