package cn.ms.neural.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Abstract Filter.
 * 
 * @author lry
 *
 * @param <REQ>
 * @param <RES>
 */
public abstract class Filter<MSG> implements IFilter<MSG> {

	private final static Logger logger = LoggerFactory.getLogger(Filter.class);
	
	@Override
	public String getFilterId() {
		return this.getClass().getSimpleName();
	}

	@Override
	public void init() throws Exception {
	}

	@Override
	public boolean check(MSG msg, Object... args) throws Exception {
		return true;
	}

	@Override
	public boolean doFilterChain(FilterChain<MSG> filterChain, MSG msg, Object... args) throws Throwable {
		boolean tempFlag = true;
		long doPreTime = 0;
		try {
			long filterPreStartTime = System.nanoTime();

			try {
				tempFlag = doPre(msg, args);
			} finally {
				doPreTime = System.nanoTime() - filterPreStartTime;
			}

			if (tempFlag) {
				return filterChain.doFilterChain(filterChain, msg, args);
			}
		} finally {
			long filterPostStartTime = System.nanoTime();

			try {
				doPost(msg, args);
			} finally {// The Support Printing time for each filter
				if(logger.isDebugEnabled()){
					logger.debug("The filter[{}] elapsed[doPre={}ms, doPost={}ms]", 
							this.getClass().getSimpleName(),String.format("%.4f", (double) doPreTime / 1000000), 
							(String.format("%.4f", (double) (System.nanoTime() - filterPostStartTime) / 1000000)));					
				}
			}
		}

		return tempFlag;
	}

	@Override
	public void doPost(MSG msg, Object... args) throws Throwable {
	}
	
	@Override
	public void destroy() throws Exception {
	}

}
