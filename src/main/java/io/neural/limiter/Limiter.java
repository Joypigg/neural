package io.neural.limiter;

import io.neural.NURL;
import io.neural.extension.NSPI;

/**
 * The Current limiter <br>
 * <br>
 * 1.管理端添加限流规则<br>
 * 2.流量到来,开始校验流量是否超额<br>
 * 3.更新流量统计<br>
 * 
 * @author lry
 */
@NSPI
public interface Limiter {

	boolean start(NURL nurl);

	/**
	 * 限流校验与更新 <br>
	 * 数据结构：[[key1,value1], [key2,value2], ……]
	 * 
	 * @param keys
	 * @return
	 */
	OptStatus increment(String scene, String[]... keys);

	/**
	 * 添加或更新配置规则
	 * 
	 * @param limiterRule
	 * @return
	 */
	boolean addOrUpRule(String scene, LimiterRule limiterRule);

	/**
	 * 搜索规则列表
	 * 
	 * @param keywords
	 * @return
	 */
	RuleData search(String keywords);

	void shutdown();
}
