package io.neural.limiter;

import io.neural.common.config.AbstractConfigCenter;
import io.neural.common.Identity;
import io.neural.common.event.EventProcessor;
import io.neural.extension.Extension;
import io.neural.limiter.LimiterConfig.Config;
import io.neural.limiter.LimiterConfig.GlobalConfig;
import io.neural.limiter.LimiterConfig.EventType;
import io.neural.limiter.core.ILimiter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * The Limiter Config Center.
 *
 * @author lry
 **/
@Slf4j
@Extension("limiter")
public class LimiterConfigCenter extends AbstractConfigCenter<Config, GlobalConfig> {

    @Override
    protected Map<String, Long> collectStatistics() {
        Map<String, Long> dataMap = new HashMap<>();
        try {
            Limiter.LIMITER.getLimiters().forEach((identity, limiter) -> {
                Map<String, Long> tempDataMap = limiter.getStatistics().getStatisticsAndReset(identity);
                if (null == tempDataMap || tempDataMap.isEmpty()) {
                    return;
                }

                dataMap.putAll(tempDataMap);
            });
        } catch (Exception e) {
            EventProcessor.EVENT.notify(module, EventType.LIMITER_COLLECT_EXCEPTION);
            log.error(EventType.LIMITER_COLLECT_EXCEPTION.getMessage(), e);
        }

        return dataMap;
    }

    @Override
    protected synchronized void notify(Identity identity, Config config) {
        try {
            ILimiter limiter = Limiter.LIMITER.getLimiters().get(identity);
            if (null == limiter) {
                log.warn("The limiter config is notify is exception, " +
                        "not found limiter:[identity={}]", identity);
                return;
            }

            limiter.refresh(new LimiterConfig(identity, config));
        } catch (Exception e) {
            EventProcessor.EVENT.notify(module, EventType.LIMITER_NOTIFY_EXCEPTION);
            log.error(EventType.LIMITER_NOTIFY_EXCEPTION.getMessage(), e);
        }
    }

}
