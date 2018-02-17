package io.neural.limiter.core;

import io.neural.common.event.EventProcessor;
import io.neural.extension.Extension;
import io.neural.limiter.LimiterConfig;
import io.neural.limiter.LimiterConfig.Config;
import io.neural.common.Identity.Switch;
import io.neural.limiter.LimiterConfig.EventType;
import io.neural.limiter.LimiterStatistics;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * The Abstract Check Limiter.
 *
 * @author lry
 * @apiNote The main implementation of check limiting
 **/
@Slf4j
@Getter
public abstract class AbstractCheckLimiter implements ILimiter {

    private volatile LimiterConfig limiterConfig = null;
    private volatile LimiterStatistics statistics = new LimiterStatistics();

    private final String model;
    private final String module = "limiter";

    AbstractCheckLimiter() {
        Extension extension = this.getClass().getAnnotation(Extension.class);
        if (null == extension) {
            throw new IllegalStateException("The " + this.getClass().getName() + " must has @Extension");
        }

        this.model = extension.value();
    }

    @Override
    public boolean refresh(LimiterConfig limiterConfig) throws Exception {
        log.debug("The refresh {}", limiterConfig);
        if (null != limiterConfig && !this.limiterConfig.equals(limiterConfig)) {
            this.limiterConfig = limiterConfig;
        }

        return true;
    }

    @Override
    public void destroy() {
        limiterConfig.getConfig().setEnable(Switch.OFF);
    }

    /**
     * Whether the check does not need process
     *
     * @return true indicates that it does not need to be handled
     */
    boolean isNonProcess() {
        if (null == limiterConfig) {
            return true;
        }

        Config config = limiterConfig.getConfig();
        return null == config || null == config.getEnable() || Switch.OFF == config.getEnable();
    }

    /**
     * The check the need for concurrency limiting
     *
     * @return true indicates that it need to be concurrency handled
     */
    boolean isConcurrencyLimiter() {
        return limiterConfig.getConfig().getConcurrency() > 0L;
    }

    /**
     * The check the need for rate limiting
     *
     * @return true indicates that it need to be rate handled
     */
    boolean isRateLimiter() {
        return limiterConfig.getConfig().getRate() > 0L;
    }

    /**
     * The notify for broadcast event
     *
     * @param eventType The notify Event Type
     */
    void notifyBroadcastEvent(EventType eventType) {
        try {
            EventProcessor.EVENT.notify(module, eventType, model, limiterConfig, statistics.getStatisticsData());
        } catch (Exception e) {
            log.error("The notify broadcast event is exception", e);
        }
    }

}
