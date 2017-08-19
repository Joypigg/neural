package cn.ms.mconf.ui.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.ms.mconf.ui.service.LimiterService;
import cn.ms.neural.MURL;
import cn.ms.neural.limiter.Limiter;
import cn.ms.neural.limiter.LimiterRule;
import cn.ms.neural.limiter.RedisLimiter;

@Service
public class LimiterServiceImpl implements LimiterService {

	Limiter limiter;

	public LimiterServiceImpl() {
		limiter = new RedisLimiter();
		limiter.start(MURL.valueOf("redis://127.0.0.1:6379"));
	}

	@Override
	public List<LimiterRule> queryLimiterRules(String keywords) {
		return limiter.queryLimiterRules(keywords);
	}

}
