package cn.ms.mconf.ui.service.impl;

import org.springframework.stereotype.Service;

import cn.ms.mconf.ui.service.LimiterService;
import cn.ms.neural.NURL;
import cn.ms.neural.limiter.Limiter;
import cn.ms.neural.limiter.RuleData;
import cn.ms.neural.limiter.cluster.RedisLimiter;

@Service
public class LimiterServiceImpl implements LimiterService {

	Limiter limiter;

	public LimiterServiceImpl() {
		limiter = new RedisLimiter();
		limiter.start(NURL.valueOf("redis://127.0.0.1:6379/ms"));
	}

	@Override
	public RuleData search(String keywords) {
		return limiter.search(keywords);
	}

}
