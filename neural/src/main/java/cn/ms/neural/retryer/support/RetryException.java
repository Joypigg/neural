package cn.ms.neural.retryer.support;

import static com.google.common.base.Preconditions.checkNotNull;

public final class RetryException extends Exception {

	private static final long serialVersionUID = 7141592192046324632L;
	
	private final int numberOfFailedAttempts;
    private final Attempt<?> lastFailedAttempt;

    public RetryException(int numberOfFailedAttempts, Attempt<?> lastFailedAttempt) {
        this("Retrying failed to complete successfully after " + numberOfFailedAttempts + " attempts.", numberOfFailedAttempts, lastFailedAttempt);
    }

    public RetryException(String message, int numberOfFailedAttempts, Attempt<?> lastFailedAttempt) {
        super(message, checkNotNull(lastFailedAttempt, "Last attempt was null").hasException() ? lastFailedAttempt.getExceptionCause() : null);
        this.numberOfFailedAttempts = numberOfFailedAttempts;
        this.lastFailedAttempt = lastFailedAttempt;
    }

    public int getNumberOfFailedAttempts() {
        return numberOfFailedAttempts;
    }

    public Attempt<?> getLastFailedAttempt() {
        return lastFailedAttempt;
    }
    
}
