package cn.ms.neural.filter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.ms.neural.extension.ExtensionLoader;

public class NeuralChain<M> {

	private Logger logger = LoggerFactory.getLogger(NeuralChain.class);

	public static final String PRE = "PRE";
	public static final String POST = "POST";
	public static final String ERROR = "ERROR";
	public static final String[] TYPES = { PRE, POST, ERROR };

	public static final String DEFAULT = "DEFAULT";

	@SuppressWarnings("rawtypes")
	private final ConcurrentHashMap<String, List<Filter>> filterMap = new ConcurrentHashMap<String, List<Filter>>();

	@SuppressWarnings("rawtypes")
	public NeuralChain(String pkg) {
		for (String type : TYPES) {
			List<Filter> filters = ExtensionLoader.getLoader(Filter.class)
					.getExtensions(type);
			logger.debug("The add filter: {}", filters.getClass().getName());
			filterMap.put(type, filters);
		}

		List<Filter> filters = ExtensionLoader.getLoader(Filter.class)
				.getExtensions();
		if (filters.size() > 0) {
			for (Map.Entry<String, List<Filter>> entry : filterMap.entrySet()) {
				filters.removeAll(entry.getValue());
			}
			if (filters.size() > 0) {
				filterMap.put(DEFAULT, filters);
			}
		}
	}

	public void doChains(M m) {
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
	public void doChain(M m, String... type) {
		List<Filter> filters = null;
		if (type == null || type.length == 0) {
			filters = filterMap.get(type[0]);
		} else if (type.length == 1) {
			filters = filterMap.get(type[0]);
		} else {
			throw new IllegalArgumentException("tpye length:" + type.length);
		}

		FilterChain<M> filterChain = new FilterChain<M>(filters);
		try {
			filterChain.doFilter(filterChain, m);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
