package cn.ms.neural.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.ms.neural.extension.ExtensionLoader;
import cn.ms.neural.extension.Activation;


/**
 * The Filter Chain.
 * 
 * @author lry
 *
 * @param <REQ>
 * @param <RES>
 */
public class FilterChain<MSG> {

	private Logger logger = LoggerFactory.getLogger(FilterChain.class);
	
	/**
	 * The Preprocessing filter
	 */
	public final static String PRE = "PRE";
	/**
	 * The Post filter
	 */
	public final static String POST = "POST";
	/**
	 * The Error filter
	 */
	public final static String ERROR = "ERROR";
	
	
	private volatile Map<String, Map<Integer, IFilter<MSG>>> filters = new ConcurrentHashMap<String, Map<Integer, IFilter<MSG>>>();

	public Map<String, Map<Integer, IFilter<MSG>>> getFilters() {
		return filters;
	}
	
	@SuppressWarnings("unchecked")
	public void scaner(String... ignoreFilterIds) throws Exception {
		logger.info("To load and initialize the filter...");

		// Add filters to be screened
		Set<String> ignoreFilterIdsSet = new HashSet<String>();
		if(!(ignoreFilterIds==null||ignoreFilterIds.length<1)){
			ignoreFilterIdsSet.addAll(Arrays.asList(ignoreFilterIds));
		}
		
		@SuppressWarnings("rawtypes")
		List<IFilter> list = ExtensionLoader.getExtensionLoader(IFilter.class).getExtensions("");
		for (IFilter<MSG> filter : list) {
			// Filter filter
			if(!ignoreFilterIdsSet.isEmpty()){// Use the scene: configure the shield part of the filter
				if(filter.getFilterId()!=null){
					if(!ignoreFilterIdsSet.contains(filter.getFilterId())){
						continue;
					}					
				}
			}
			
			// Compute packet key
			Set<String> groupSet = new HashSet<String>();
			Activation activation = filter.getClass().getAnnotation(Activation.class);
			if (activation == null || activation.category() == null || activation.category().length != 1) {
				continue;
			} else {
				groupSet.add(activation.category()[0]);
				// Packet collection filter
				AtomicInteger no = new AtomicInteger(0);
				for (String key : groupSet) {
					Map<Integer, IFilter<MSG>> map = filters.get(key);
					if (map == null) {
						filters.put(key, map = new ConcurrentHashMap<Integer, IFilter<MSG>>());
					} else { // Already exists, select the maximum value of +1
						no.set(Collections.max(map.keySet())+1);
					}
					
					logger.info("The Scan to [{}] filter.", filter);
					map.put(no.getAndIncrement(), filter);
				}
			}
		}
		
		logger.info("Filter loaded and initialized successfully!!!");
	}
	
	/**
	 * Initialize filter
	 * 
	 * @throws Exception
	 */
	public void initFilter() throws Exception{
		logger.info("The Filter initialization...");
		
		String[] filterTypes = {PRE, POST, ERROR};
		for (String filterType : filterTypes) {
			Map<Integer, IFilter<MSG>> typefilters = filters.get(filterType);
			if(typefilters == null || typefilters.isEmpty()) {
				continue;
			}
			
			List<IFilter<MSG>> list =new ArrayList<IFilter<MSG>>();
			list.addAll(typefilters.values());
			
			Collections.sort(list, new Comparator<IFilter<MSG>>() {
				@Override
				public int compare(IFilter<MSG> o1, IFilter<MSG> o2) {
					Activation a1 = o1.getClass().getAnnotation(Activation.class);
					Activation a2 = o2.getClass().getAnnotation(Activation.class);
					return a1.order()-a2.order();
				}
			});
			
			for (IFilter<MSG> filter:list) {
				logger.info("The start initialized filters [{}] - [{}]...", filterType, filter);
				filter.init();
				logger.info("The successed initialized filters [{}] - [{}]...", filterType, filter);
			}
		}
		
		logger.info("Filter initialization completed!!!");
	}
	
	/**
	 * Get the specified filter
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getFilter(Class<T> filterClazz) {
		String key;
		Activation activation = filterClazz.getAnnotation(Activation.class);
		if(activation==null||activation.category()==null||activation.category().length==0){
			key="default";
		}else{
			key=activation.category()[0];
		}
		
		Map<Integer, IFilter<MSG>> map = filters.get(key);
		for (Map.Entry<Integer, IFilter<MSG>> entry : map.entrySet()) {
			if (filterClazz.getName().equals(entry.getValue().getClass().getName())) {
				return (T) entry.getValue();
			}
		}
		
		return null;
	}
	
	/**
	 * Responsibility chain recursive function
	 * 
	 * @throws Throwable
	 */
	public boolean doFilterChain(FilterChain<MSG> filterChain, MSG msg, Object... args) throws Throwable {
//		Map<Integer, IFilter<MSG>> filterGroup = filters.get(wrapper.getKey());
//		// Recursive end identifier
//		if (filterGroup == null || filterGroup.isEmpty() || wrapper.getNo().get() == filterGroup.size()) {
//			return true;
//		}
//		
//		IFilter<MSG> filter = filterGroup.get(wrapper.getNo().getAndIncrement());
//		
//		if (filter.check(msg, args)) {// Determine whether to skip the current filter
//			return filter.doFilterChain(filterChain, msg, args);
//		} else {
//			return doFilterChain(filterChain, msg, args);
//		}
		return false;
	}
	
}