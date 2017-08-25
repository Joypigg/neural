package cn.ms.neural.filter;

import cn.ms.neural.extension.NSPI;
import cn.ms.neural.extension.Scope;

/**
 * The Responsibility Chain Filter.
 * 
 * @author lry
 *
 * @param <REQ>
 *            Request object
 * @param <RES>
 *            Response object
 */
@NSPI(scope = Scope.PROTOTYPE)
public interface IFilter<MSG> {

	/**
	 * Filter ID, defaults to class.getSimpleName ().
	 * 
	 * @return
	 */
	String getFilterId();

	/**
	 * The Initialization.
	 * 
	 * @throws Exception
	 */
	void init() throws Exception;

	/**
	 * Verify whether the filter is executed.
	 * 
	 * @throws Exception
	 */
	boolean check(MSG msg, Object... args) throws Exception;

	/**
	 * Recursive call chain.
	 * 
	 * @throws Throwable
	 */
	boolean doFilterChain(FilterChain<MSG> filterChain, MSG msg, Object... args) throws Throwable;

	/**
	 * Request preprocessing.
	 * 
	 * @throws Throwable
	 */
	boolean doPre(MSG msg, Object... args) throws Throwable;

	/**
	 * Response pretreatment.
	 * 
	 * @throws Throwable
	 */
	void doPost(MSG msg, Object... args) throws Throwable;

	/***
	 * The destroy filter.
	 * 
	 * @throws Exception
	 */
	void destroy() throws Exception;

}