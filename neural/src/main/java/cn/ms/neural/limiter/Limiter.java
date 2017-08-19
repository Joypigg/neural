package cn.ms.neural.limiter;

import java.util.List;

import cn.ms.neural.MURL;
import cn.ms.neural.extension.Scope;
import cn.ms.neural.extension.Spi;
import cn.ms.neural.limiter.cluster.LimiterRule;

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

	boolean start(MURL url);

	boolean increment(String... keys);
	boolean increment(Long expire, String... keys);

	boolean setLimiterRule(LimiterRule limiterRule);
	List<LimiterRule> queryLimiterRules(String keywords);

	void shutdown();
}
