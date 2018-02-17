package io.neural.limiter.core;

import io.neural.common.event.EventProcessor;
import io.neural.limiter.Limiter;
import io.neural.limiter.LimiterConfig;
import io.neural.limiter.LimiterConfig.Config;
import io.neural.common.Identity.Switch;
import io.neural.limiter.LimiterConfig.GlobalConfig;
import io.neural.limiter.LimiterConfig.EventType;
import io.neural.limiter.LimiterStatistics;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * The Check Limiter.
 *
 * @author lry
 **/
@Slf4j
@Getter
public abstract class AbstractCheckLimiter implements ILimiter {

    private volatile LimiterConfig limiterConfig = null;
    private final LimiterStatistics statistics = new LimiterStatistics();


    @Override
    public boolean refresh(LimiterConfig limiterConfig) throws Exception {
        GlobalConfig globalConfig = Limiter.LIMITER.getConfigCenter().getGlobalConfig();
        if (null == globalConfig) {
            throw new IllegalStateException("LimiterGlobalConfig = " + globalConfig);
        }

        log.debug("The refresh {}", limiterConfig);
        if (null == this.limiterConfig || !this.limiterConfig.equals(limiterConfig)) {
            return null != (this.limiterConfig = limiterConfig);
        }

        return false;
    }

    @Override
    public void destroy() {
        limiterConfig.getConfig().setEnable(Switch.OFF);
    }

    /**
     * The non need to process
     *
     * @return
     */
    protected boolean isNonProcess() {
        if (null == limiterConfig) {
            return true;
        }

        Config config = limiterConfig.getConfig();
        if (null == config.getEnable() || Switch.OFF == config.getEnable()) {
            return true;
        }

        return false;
    }

    /**
     * Is need concurrency limiter
     *
     * @return
     */
    protected boolean isConcurrencyLimiter() {
        return limiterConfig.getConfig().getConcurrency() > 0L;
    }

    /**
     * Is need rate limiter
     *
     * @return
     */
    protected boolean isRateLimiter() {
        return limiterConfig.getConfig().getRate() > 0L;
    }

    /**
     * Is need print exceed log
     *
     * @return
     */
    protected boolean isPrintExceedLog() {
        GlobalConfig globalConfig = Limiter.LIMITER.getConfigCenter().getGlobalConfig();
        if (null == globalConfig.getPrintExceedLog()) {
            return true;
        }

        return Switch.ON == globalConfig.getPrintExceedLog();
    }

    /**
     * Is need broadcast event
     *
     * @return
     */
    protected boolean isBroadcastEvent() {
        GlobalConfig globalConfig = Limiter.LIMITER.getConfigCenter().getGlobalConfig();
        if (null == globalConfig.getBroadcastEvent()) {
            return true;
        }

        return Switch.ON == globalConfig.getBroadcastEvent();
    }

    /**
     * The check or broadcast event
     *
     * @param eventType
     */
    protected void checkOrBroadcastEvent(EventType eventType) {
        try {
            if (!this.isBroadcastEvent()) {
                return;
            }

            Map<String, Long> dataMap = statistics.getStatisticsData();
            String module = Limiter.LIMITER.getConfigCenter().getStore().getModule();
            EventProcessor.EVENT.notify(module, eventType, limiterConfig, dataMap);
        } catch (Exception e) {
            log.error("The broadcast event is exception", e);
        }
    }

}
