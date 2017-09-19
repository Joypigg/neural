package io.neural.degrader;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 降级类型：<br>
 * 1.屏蔽降级<br>
 * 2.Mock降级<br>
 * 3.熔断降级<br>
 * <br>
 * 降级模式：<br>
 * 1.自动降级<br>
 * 2.手动降级<br>
 * 
 * @author lry
 */
public class Degrader {

	ConcurrentHashMap<String, Degrader> degradeMap = new ConcurrentHashMap<String, Degrader>();

	public static Degrader build() {
		return new Degrader();
	}

}
