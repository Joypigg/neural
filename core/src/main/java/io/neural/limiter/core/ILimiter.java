package io.neural.limiter.core;

import io.neural.common.OriginalCall;
import io.neural.extension.NPI;
import io.neural.limiter.LimiterConfig;
import io.neural.limiter.LimiterStatistics;

/**
 * The Limiter Interface.
 *
 * @author lry
 */
@NPI("local")
public interface ILimiter {

    /**
     * The get config of limiter.
     *
     * @return
     */
    LimiterConfig getLimiterConfig();

    /**
     * The refresh in-memory data.
     *
     * @param limiterConfig
     * @return
     * @throws Exception
     */
    boolean refresh(LimiterConfig limiterConfig) throws Exception;

    /**
     * The process original call.
     *
     * @param originalCall
     * @return
     * @throws Throwable
     */
    Object doOriginalCall(OriginalCall originalCall) throws Throwable;

    /**
     * The get statistics of limiter.
     *
     * @return
     */
    LimiterStatistics getStatistics();

    /**
     * The destroy of limiter.
     */
    void destroy();

}
