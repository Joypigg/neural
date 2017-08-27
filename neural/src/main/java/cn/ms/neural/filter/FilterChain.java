package cn.ms.neural.filter;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Filter Chain.
 * 
 * @author lry
 *
 * @param <M>
 */
@SuppressWarnings("rawtypes")
public class FilterChain<M> extends Filter<M> {

	private Logger logger = LoggerFactory.getLogger(FilterChain.class);

	private final List<Filter> filters;
	private final AtomicInteger index;

	public FilterChain(List<Filter> filters) {
		this.filters = filters;
		this.index = new AtomicInteger(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doFilter(FilterChain<M> chain, M m) throws Exception {
		if (index.get() == filters.size()) {
			return;
		}

		Filter<M> filter = filters.get(index.getAndIncrement());
		logger.debug("The next filter to be executed is: {}", filter.getClass().getName());

		filter.doFilter(chain, m);
	}

}