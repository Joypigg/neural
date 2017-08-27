package cn.ms.neural.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.ms.neural.extension.Extension;
import cn.ms.neural.extension.ExtensionLoader;

public class NeuralChain<M> {

	private Logger logger = LoggerFactory.getLogger(NeuralChain.class);

	public static final String PRE = "PRE";
	public static final String POST = "POST";
	public static final String ERROR = "ERROR";
	public static final String[] TYPES = { PRE, POST, ERROR };

	@SuppressWarnings("rawtypes")
	private final ConcurrentHashMap<String, List<Filter>> filterMap = new ConcurrentHashMap<String, List<Filter>>();

	@SuppressWarnings("rawtypes")
	public NeuralChain() {
		List<Filter> filters = ExtensionLoader.getLoader(Filter.class).getExtensions();
		if (filters.size() > 0) {
			for (Filter filter : filters) {
				logger.debug("The add filter: {}", filters.getClass().getName());
				Extension extension = filter.getClass().getAnnotation(Extension.class);
				if (extension != null) {
					String[] categories = extension.category();
					if (categories != null) {
						for (int i = 0; i < categories.length; i++) {
							List<Filter> filterList = filterMap.get(categories[i]);
							if (filterList == null) {
								filterMap.put(categories[i], filterList = new ArrayList<Filter>());
							}
							filterList.add(filter);
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public ConcurrentHashMap<String, List<Filter>> getFilterMap() {
		return filterMap;
	}

	public void doChains(M m) throws Exception {
		try {
			this.doChain(m, PRE);
		} catch (Throwable t) {
			this.doChain(m, ERROR);
			logger.error("The execute class pre filter exceptions", t);
		} finally {
			this.doChain(m, POST);
		}
	}

	@SuppressWarnings("rawtypes")
	public void doChain(M m, String... type) throws Exception {
		List<Filter> filters = null;
		if (type == null || type.length == 0) {
			filters = filterMap.get("");
		} else if (type.length == 1) {
			filters = filterMap.get(type[0]);
		} else {
			throw new IllegalArgumentException("tpye length:" + type.length);
		}

		FilterChain<M> filterChain = new FilterChain<M>(filters);
		filterChain.doFilter(filterChain, m);
	}

}
