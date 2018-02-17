package io.neural.limiter;

import java.util.Random;

import io.neural.URL;
import io.neural.common.Constants;
import io.neural.common.Identity;
import io.neural.common.OriginalCall;
import io.neural.limiter.LimiterConfig.Config;
import io.neural.limiter.LimiterConfig.Strategy;

/**
 * @author lry
 **/
public class LimiterTest {
    public static void main(String[] args) throws Throwable {
        String application = "gateway";
        System.setProperty(Constants.APP_NAME_KEY, application);
        Limiter limiter = Limiter.LIMITER;

        //query order
        Identity identity1 = new Identity(application, "order", "queryOrder");
        Config config1 = new Config(0L, 0L, 1L,
                LimiterConfig.Unit.SEC, 0L, 0L, Strategy.NON);
        config1.setName("查询订单");
        limiter.addLimiter(new LimiterConfig(identity1, config1));
        //insert order
        Identity identity2 = new Identity(application, "order", "insertOrder");
        Config config2 = new Config(0L, 0L, 1L,
                LimiterConfig.Unit.SEC, 0L, 0L, Strategy.NON);
        config2.setName("新增订单");
        limiter.addLimiter(new LimiterConfig(identity2, config2));
        //delete order
        Identity identity3 = new Identity(application, "order", "deleteOrder");
        Config config3 = new Config(0L, 0L, 1L,
                LimiterConfig.Unit.SEC, 0L, 0L, Strategy.NON);
        config3.setName("删除订单");
        limiter.addLimiter(new LimiterConfig(identity3, config3));

        URL url = URL.valueOf("redis://localhost:6379/limiter?minIdle=2");
        limiter.start(url);

        for (int i = 0; i < 100000; i++) {
            Object result = limiter.doLimiter(identity1, new OriginalCall() {
                @Override
                public Object call() throws Throwable {
                    Thread.sleep(new Random().nextInt(100) + 20);
                    return "ok";
                }

                @Override
                public Object fallback() throws Throwable {
                    return "fallback";
                }
            });
            if (i == 50) {
                config1.setRate(3000L);
                System.out.println("1发布配置");
                limiter.getStoreConfig().publish(identity1, config1);
                System.out.println("2发布配置");
            }
        }
    }
}
