package cn.ms.mconf.ui.service;

import java.util.List;

import cn.ms.neural.limiter.LimiterRule;

public interface LimiterService {

	List<LimiterRule> queryLimiterRules(String keywords);

}
