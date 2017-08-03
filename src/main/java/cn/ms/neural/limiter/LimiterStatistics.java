package cn.ms.neural.limiter;

import java.util.List;

/**
 * 限流统计
 * 
 * @author lry
 */
public class LimiterStatistics {

	private String keys;
	private List<LimiterRes> limiterRes;

	public LimiterStatistics() {
	}

	public LimiterStatistics(String keys, List<LimiterRes> limiterRes) {
		this.keys = keys;
		this.limiterRes = limiterRes;
	}

	public String getKeys() {
		return keys;
	}

	public void setKeys(String keys) {
		this.keys = keys;
	}

	public List<LimiterRes> getLimiterRes() {
		return limiterRes;
	}

	public void setLimiterRes(List<LimiterRes> limiterRes) {
		this.limiterRes = limiterRes;
	}

	@Override
	public String toString() {
		return "LimiterStatistics [keys=" + keys + ", limiterRes=" + limiterRes
				+ "]";
	}

}
