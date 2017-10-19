package io.neural.limiter;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class AtomicLongLimitingTest {
	public static void main(String[] args) throws Exception {
		LoadingCache<Long, AtomicLong> counter = CacheBuilder.newBuilder()
				.expireAfterWrite(2, TimeUnit.SECONDS)
				.build(new CacheLoader<Long, AtomicLong>() {
					@Override
					public AtomicLong load(Long seconds) throws Exception {
						return new AtomicLong(0);
					}
				});
		long limit = 10;
		for (int i = 0; i < 100; i++) {
			try {
				long currentSeconds = System.currentTimeMillis() / 1000;
				if (counter.get(currentSeconds).incrementAndGet() > limit) {
					System.out.println((i + 1) + "->限流了：" + currentSeconds);
					continue;
				}
				System.out.println((i + 1) + "->执行业务处理");
			} finally {
				Thread.sleep(20);
			}
		}
	}
}
