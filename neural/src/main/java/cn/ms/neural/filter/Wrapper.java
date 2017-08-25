package cn.ms.neural.filter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * The Filter Parameter Wrapper.
 * 
 * @author lry
 *
 * @param <REQ>
 * @param <RES>
 */
public class Wrapper<MSG> {

	/**
	 * The Filter Key.
	 */
	String key;
	/**
	 * The Filter Need to skip the filter ID.
	 */
	String skip;
	/**
	 * Filter execution sequence number.
	 */
	AtomicInteger no = new AtomicInteger(0);
	/**
	 * The Filter Chain.
	 */
	FilterChain<MSG> filterChain;

	public Wrapper() {
	}

	public Wrapper(String key, String skip, FilterChain<MSG> filterChain) {
		this.key = key;
		this.skip = skip;
		this.filterChain = filterChain;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSkip() {
		return skip;
	}

	public void setSkip(String skip) {
		this.skip = skip;
	}

	public AtomicInteger getNo() {
		return no;
	}

	public void setNo(AtomicInteger no) {
		this.no = no;
	}

	public FilterChain<MSG> getFilterChain() {
		return filterChain;
	}

	public void setFilterChain(FilterChain<MSG> filterChain) {
		this.filterChain = filterChain;
	}
	
}