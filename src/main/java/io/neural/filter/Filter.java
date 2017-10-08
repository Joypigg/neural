package io.neural.filter;

import io.neural.extension.NPI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Abstract Filter.
 * 
 * @author lry
 *
 * @param <M>
 */
@NPI
public abstract class Filter<M> {

	private final static Logger logger = LoggerFactory.getLogger(Filter.class);

	public String getId() {
		return this.getClass().getName();
	}

	public void init() throws Exception {
		logger.debug("The initializing...");
	}

	public void destroy() throws Exception {
		logger.debug("The destroing...");
	}

	public abstract void doFilter(Chain<M> chain, M m) throws Exception;

}
