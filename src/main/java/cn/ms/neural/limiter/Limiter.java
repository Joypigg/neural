package cn.ms.neural.limiter;

import java.util.List;

import cn.ms.micro.common.URL;
import cn.ms.micro.extension.Scope;
import cn.ms.micro.extension.Spi;

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

	boolean start(URL url);

	boolean increment(String... keys);
	boolean increment(Long expire, String... keys);

	boolean setLimiterRule(LimiterRule limiterRule);
	List<LimiterRule> queryLimiterRules(String keywords);

	void shutdown();
}
