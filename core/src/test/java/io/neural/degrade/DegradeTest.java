package io.neural.degrade;

import io.neural.NURL;
import io.neural.common.Constants;
import io.neural.common.Identity;
import io.neural.common.OriginalCall;
import io.neural.common.config.IConfigCenter;
import io.neural.degrade.DegradeConfig.Config;
import io.neural.degrade.DegradeConfig.Level;
import io.neural.degrade.DegradeConfig.Strategy;
import io.neural.degrade.DegradeConfig.Mock;

import java.util.Random;

/**
 * @author lry
 **/
public class DegradeTest {
    public static void main(String[] args) throws Throwable {
        String application = "gateway";
        System.setProperty(Constants.APP_NAME_KEY, application);
        Degrade degrade = Degrade.DEGRADE;

        Identity identity1 = new Identity(application, "order", "sendEms");
        Config config1 = new Config(Level.HINT, Strategy.FALLBACK, Mock.BOOLEAN, null, "true");
        config1.setName("发送短信");
        degrade.addDegrade(new DegradeConfig(identity1, config1));

        Identity identity2 = new Identity(application, "order", "sendEmail");
        Config config2 = new Config(Level.NEED, Strategy.FALLBACK, Mock.ARRAY, null, "zhangsan,lisi");
        config2.setName("发送邮件");
        degrade.addDegrade(new DegradeConfig(identity2, config2));

        NURL nurl = NURL.valueOf("redis://localhost:6379/degrade?minIdle=2");
        degrade.start(nurl);

        for (int i = 0; i < 100000; i++) {
            Object result = degrade.doDegrade(identity1, new OriginalCall() {
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
        }

        Thread.sleep(600000);

        degrade.destroy();
    }
}
