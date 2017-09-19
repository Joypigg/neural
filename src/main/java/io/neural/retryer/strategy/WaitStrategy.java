package io.neural.retryer.strategy;

import io.neural.retryer.support.Attempt;

/**
 * A strategy used to decide how long to sleep before retrying after a failed attempt.
 *
 * @author lry
 */
public interface WaitStrategy {

    /**
     * Returns the time, in milliseconds, to sleep before retrying.
     *
     * @param failedAttempt the previous failed {@code Attempt}
     * @return the sleep time before next attempt
     */
	long computeSleepTime(@SuppressWarnings("rawtypes") Attempt failedAttempt);
    
}
