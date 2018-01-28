package io.neural.limiter.support;

import io.neural.extension.Extension;
import io.neural.limiter.extension.AdjustableRateLimiter;
import io.neural.limiter.extension.AdjustableSemaphore;
import io.neural.limiter.LimiterConfig;
import io.neural.limiter.LimiterConfig.Config;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * The limiter based on adjustable's semaphore and rateLimiter implementation.
 *
 * @author lry
 */
@Slf4j
@Extension("local")
public class LocalLimiter extends AbstractOriginalCallLimiter {

    private final AdjustableSemaphore semaphore;
    private final AdjustableRateLimiter rateLimiter;

    public LocalLimiter() {
        this.semaphore = new AdjustableSemaphore(1, true);
        this.rateLimiter = AdjustableRateLimiter.create(1);
    }

    @Override
    public synchronized boolean refresh(LimiterConfig limiterConfig) throws Exception {
        if (!super.refresh(limiterConfig)) {
            return false;
        }

        try {
            Config config = super.getLimiterConfig().getConfig();
            // refresh semaphore
            if (0 < config.getConcurrency()) {
                semaphore.setMaxPermits(config.getConcurrency().intValue());
            }
            // refresh rateLimiter
            if (0 < config.getRate()) {
                rateLimiter.setRate(config.getRate());
            }

            return true;
        } catch (Exception e) {
            log.error("The refresh LocalLimiter is exception", e);
            return false;
        }
    }

    @Override
    protected Acquire tryAcquireConcurrency() {
        try {
            Long timeout = super.getLimiterConfig().getConfig().getConcurrencyTimeout();
            if (timeout < 1) {
                return semaphore.tryAcquire() ? Acquire.SUCCESS : Acquire.FAILURE;
            } else {
                return semaphore.tryAcquire(timeout, TimeUnit.MILLISECONDS) ? Acquire.SUCCESS : Acquire.FAILURE;
            }
        } catch (Exception e) {
            return Acquire.EXCEPTION;
        }
    }

    @Override
    protected void releaseAcquireConcurrency() {
        if (null != semaphore) {
            semaphore.release();
        }
    }

    @Override
    protected Acquire tryAcquireRateLimiter() {
        try {
            Long timeout = super.getLimiterConfig().getConfig().getRateTimeout();
            if (timeout < 1) {
                return rateLimiter.tryAcquire() ? Acquire.SUCCESS : Acquire.FAILURE;
            } else {
                return rateLimiter.tryAcquire(timeout, TimeUnit.MILLISECONDS) ? Acquire.SUCCESS : Acquire.FAILURE;
            }
        } catch (Exception e) {
            return Acquire.EXCEPTION;
        }
    }

}
