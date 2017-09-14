package cn.ms.neural.limiter;

import java.util.Map;

public class RuleData {

	String keywords;
	Long time;
	Map<LimiterRule, String> rules;

	public RuleData() {
	}

	public RuleData(String keywords) {
		this.keywords = keywords;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Map<LimiterRule, String> getRules() {
		return rules;
	}

	public void setRules(Map<LimiterRule, String> rules) {
		this.rules = rules;
	}

	@Override
	public String toString() {
		return "LimiterData [keywords=" + keywords + ", time=" + time
				+ ", rules=" + rules + "]";
	}

}
