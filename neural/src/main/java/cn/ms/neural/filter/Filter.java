package cn.ms.neural.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.ms.neural.extension.NSPI;

/**
 * The Abstract Filter.
 * 
 * @author lry
 *
 * @param <M>
 */
@NSPI
public abstract class Filter<M> {

	private final static Logger logger = LoggerFactory.getLogger(Filter.class);

	public void init() throws Exception {
		logger.debug("The initializing...");
	}

	public void destroy() throws Exception {
		logger.debug("The destroing...");
	}

	public abstract void doFilter(FilterChain<M> chain, M m) throws Exception;

}
