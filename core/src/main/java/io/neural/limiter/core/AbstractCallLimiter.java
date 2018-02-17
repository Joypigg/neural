package io.neural.limiter.core;

import io.neural.common.OriginalCall;
import io.neural.limiter.LimiterConfig.EventType;
import io.neural.limiter.LimiterExcessException;
import io.neural.limiter.LimiterConfig.Config;
import lombok.extern.slf4j.Slf4j;

/**
 * The Abstract Call Limiter.
 *
 * @author lry
 * @apiNote The main implementation of original call limiting
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
     * @param originalCall The original call interface
     * @return The original call result
     * @throws Throwable throw original call exception
     */
    private Object doConcurrencyOriginalCall(OriginalCall originalCall) throws Throwable {
        // the check concurrency limiting
        if (super.isConcurrencyLimiter()) {
            // try acquire concurrency
            switch (this.tryAcquireConcurrency()) {
                case FAILURE:
                    // the concurrent exceed
                    return this.doStrategyProcess(EventType.LIMITER_CONCURRENT_EXCEED, originalCall);
                case SUCCESS:
                    // the concurrent success must be released
                    try {
                        return this.doRateOriginalCall(originalCall);
                    } finally {
                        this.releaseAcquireConcurrency();
                    }
                case EXCEPTION:
                    // the skip exception case
                default:
                    // the skip other case
            }
        }

        // the skip non check ConcurrencyLimiter or exception or other
        return this.doRateOriginalCall(originalCall);
    }

    /**
     * The rate limiter and original call
     *
     * @param originalCall The original call interface
     * @return The original call result
     * @throws Throwable throw original call exception
     */
    private Object doRateOriginalCall(OriginalCall originalCall) throws Throwable {
        if (super.isRateLimiter()) {
            switch (this.tryAcquireRateLimiter()) {
                case FAILURE:
                    // the rate exceed
                    return this.doStrategyProcess(EventType.LIMITER_RATE_EXCEED, originalCall);
                case SUCCESS:
                    // the pass success case
                case EXCEPTION:
                    // the skip exception case
                default:
                    // the skip other case
            }
        }

        // the skip non check RateLimiter or success or exception or other
        return this.doOriginalCallWrapper(originalCall);
    }

    /**
     * The execute strategy process of limiting exceed
     *
     * @param eventType    The event type
     * @param originalCall The original call interface
     * @return The original call result
     * @throws Throwable throw original call exception
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
                    // the skip non case
                default:
                    // the skip other case
            }
        }

        // the wrapper of original call
        return this.doOriginalCallWrapper(originalCall);
    }

    /**
     * The wrapper of original call
     *
     * @param originalCall The original call interface
     * @return The original call result
     * @throws Throwable throw original call exception
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
     * @return The excess of limiting
     */
    protected abstract Acquire tryAcquireConcurrency();

    /**
     * The release of concurrency limiter.
     */
    protected abstract void releaseAcquireConcurrency();

    /**
     * The acquire of rate limiter.
     *
     * @return The excess of limiting
     */
    protected abstract Acquire tryAcquireRateLimiter();


    /**
     * The Excess of Limiter.
     *
     * @author lry
     */
    public enum Acquire {

        /**
         * The success of limiter
         */
        SUCCESS,

        /**
         * The failure of limiter
         */
        FAILURE,

        /**
         * The exception of limiter
         */
        EXCEPTION;

    }

}
