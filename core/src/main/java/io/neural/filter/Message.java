package io.neural.filter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Message implements Serializable {

	private static final long serialVersionUID = 2501549768235869537L;

	private Long time = System.currentTimeMillis();
	private final Map<String, Object> headers = new HashMap<String, Object>();
	private Object body;

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	public Map<String, Object> getHeaders() {
		return headers;
	}

	public void addHeader(String key, Object value) {
		headers.put(key, value);
	}

	public void addHeaders(Map<String, Object> headers) {
		headers.putAll(headers);
	}

	@Override
	public String toString() {
		return "Message [time=" + time + ", headers=" + headers + ", body="
				+ body + "]";
	}

}
