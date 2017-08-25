package cn.ms.neural.limiter;

import java.util.HashMap;
import java.util.Map;

import cn.ms.neural.util.Store;

/**
 * 限流统计
 * 
 * @author lry
 */
public class LimiterRule {

	private String keys;
	private Map<String, Store<Long, Long>> data = new HashMap<String, Store<Long,Long>>();

	public LimiterRule() {
	}

	public LimiterRule(String keys, Map<String, Store<Long, Long>> data) {
		this.keys = keys;
		this.data = data;
	}

	public LimiterRule buildKeys(String[]... keys) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < keys.length; i++) {
			sb.append(keys[i][0]).append("=").append(keys[i][1]);
			if (i < keys.length - 1) {
				sb.append("&");
			}
		}
		this.keys = sb.toString();
		return this;
	}
	
	public LimiterRule putData(String category, Long maxThreshold) {
		this.data.put(category, new Store<Long, Long>(maxThreshold));
		return this;
	}
	
	public LimiterRule putData(String category, Long maxThreshold, Long nowAmount) {
		this.data.put(category, new Store<Long, Long>(maxThreshold, nowAmount));
		return this;
	}

	public String getKeys() {
		return keys;
	}

	public void setKeys(String keys) {
		this.keys = keys;
	}

	public Map<String, Store<Long, Long>> getData() {
		return data;
	}

	public void setData(Map<String, Store<Long, Long>> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "LimiterRule [keys=" + keys + ", data=" + data + "]";
	}

}
