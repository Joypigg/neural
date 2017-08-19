package cn.ms.neural.limiter;

import java.util.List;

import cn.ms.neural.MURL;
import cn.ms.neural.extension.Scope;
import cn.ms.neural.extension.Spi;

/**
 * The Current limiter <br>
 * <br>
 * 1.管理端添加限流规则<br>
 * 2.流量到来,开始校验流量是否超额<br>
 * 3.更新流量统计<br>
 * 
 * @author lry
 */
@Spi(scope = Scope.SINGLETON)
public interface Limiter {

	boolean start(MURL murl);

	/**
	 * 限流校验与更新 <br>
	 * 数据结构：[[key1,value1], [key2,value2], ……]
	 * 
	 * @param keys
	 * @return
	 */
	OptStatus increment(String[]... keys);

	/**
	 * 添加或更新配置规则
	 * 
	 * @param limiterRule
	 * @return
	 */
	boolean addOrUpRule(LimiterRule limiterRule);

	/**
	 * 搜索规则列表
	 * 
	 * @param type 1表示只查限流规则,2表示只查实时限流数据,3表示查询所有数据
	 * @param keywords
	 * @return
	 */
	List<LimiterRule> search(Integer type, String keywords);

	void shutdown();
}
