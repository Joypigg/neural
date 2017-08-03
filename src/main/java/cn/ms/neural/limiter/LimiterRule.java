package cn.ms.neural.limiter;

import java.util.Map;

/**
 * 限流规则
 * 
 * @author lry
 */
public class LimiterRule {

	private String keys;
	private Map<String, Long> balance;

	public LimiterRule() {
	}
	
	public LimiterRule(String keys, Map<String, Long> balance) {
		this.keys = keys;
		this.balance = balance;
	}

	public String getKeys() {
		return keys;
	}

	public void setKeys(String keys) {
		this.keys = keys;
	}

	public Map<String, Long> getBalance() {
		return balance;
	}

	public void setBalance(Map<String, Long> balance) {
		this.balance = balance;
	}

	@Override
	public String toString() {
		return "LimiterRule [keys=" + keys + ", balance=" + balance + "]";
	}

}
