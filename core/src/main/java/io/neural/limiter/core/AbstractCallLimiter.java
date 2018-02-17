package io.neural.limiter.core;

import io.neural.common.OriginalCall;
import io.neural.limiter.LimiterConfig.EventType;
import io.neural.limiter.LimiterExcessException;
import io.neural.limiter.LimiterConfig.Config;
import lombok.extern.slf4j.Slf4j;

/**
 * The Abstract Limiter.
 *
 * @author lry
 */
@Slf4j
public abstract class AbstractCallLimiter extends AbstractCheckLimiter {

    @Override
    public Object doOriginalCall(OriginalCall originalCall) throws Throwable {
        if (super.isNonProcess()) {
            // the don't need limiting
            return originalCall.call();
        }

        // the total request of statistical traffic
        super.getStatistics().totalRequestTraffic();

        // the concurrency limiter and original call
        return this.doConcurrencyOriginalCall(originalCall);
    }

    /**
     * The concurrency limiter and original call
     *
     * @param originalCall
     * @return
     * @throws Throwable
     */
    private Object doConcurrencyOriginalCall(OriginalCall originalCall) throws Throwable {
        if (super.isConcurrencyLimiter()) {
            switch (this.tryAcquireConcurrency()) {
                // the concurrent exceed
                case FAILURE:
                    return this.doStrategyProcess(EventType.LIMITER_CONCURRENT_EXCEED, originalCall);
                // the concurrent success must be released
                case SUCCESS:
                    try {
                        return this.doRateOriginalCall(originalCall);
                    } finally {
                        this.releaseAcquireConcurrency();
                    }
                case EXCEPTION:
                default:
            }
        }

        // if NON or other
        return this.doRateOriginalCall(originalCall);
    }

    /**
     * The rate limiter and original call
     *
     * @param originalCall
     * @return
     * @throws Throwable
     */
    private Object doRateOriginalCall(OriginalCall originalCall) throws Throwable {
        if (super.isRateLimiter()) {
            switch (this.tryAcquireRateLimiter()) {
                // the rate exceed
                case FAILURE:
                    return this.doStrategyProcess(EventType.LIMITER_RATE_EXCEED, originalCall);
                case SUCCESS:
                case EXCEPTION:
                default:
            }
        }

        // if NO or NON or other
        return this.doOriginalCallWrapper(originalCall);
    }

    /**
     * The execute strategy process of limiting exceed
     *
     * @param eventType
     * @param originalCall
     * @return
     * @throws Throwable
     */
    private Object doStrategyProcess(EventType eventType, OriginalCall originalCall) throws Throwable {
        // the total exceed of statistical traffic
        super.getStatistics().exceedTraffic(eventType);

        // print exceed log
        Config config = super.getLimiterConfig().getConfig();
        log.warn("The {} exceed, [{}]-[{}]", eventType, config, super.getStatistics());

        // the broadcast event of traffic exceed
        super.notifyBroadcastEvent(eventType);

        // the execute strategy with traffic exceed
        if (null != config.getStrategy()) {
            switch (config.getStrategy()) {
                case FALLBACK:
                    return originalCall.fallback();
                case EXCEPTION:
                    throw new LimiterExcessException(eventType.name());
                case NON:
                default:
            }
        }

        return this.doOriginalCallWrapper(originalCall);
    }

    /**
     * The wrapper of original call
     *
     * @param originalCall
     * @return
     * @throws Throwable
     */
    private Object doOriginalCallWrapper(OriginalCall originalCall) throws Throwable {
        // increment traffic
        super.getStatistics().incrementTraffic();
        long startTime = System.currentTimeMillis();

        try {
            return originalCall.call();
        } catch (Throwable t) {
            // total exception traffic
            super.getStatistics().exceptionTraffic(t);
            throw t;
        } finally {
            // decrement traffic
            super.getStatistics().decrementTraffic(startTime);
        }
    }

    /**
     * The acquire of concurrency limiter.
     *
     * @return
     */
    protected abstract Acquire tryAcquireConcurrency();

    /**
     * The release of concurrency limiter.
     */
    protected abstract void releaseAcquireConcurrency();

    /**
     * The acquire of rate limiter.
     *
     * @return
     */
    protected abstract Acquire tryAcquireRateLimiter();

    /**
     * The Excess of Limiter.
     *
     * @author lry
     */
    public enum Acquire {
        /**
         * The exception of limiter
         */
        EXCEPTION,
        /**
         * The success of limiter
         */
        SUCCESS,
        /**
         * The failure of limiter
         */
        FAILURE;
    }

}
