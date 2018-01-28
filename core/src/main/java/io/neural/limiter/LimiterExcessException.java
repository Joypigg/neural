package io.neural.limiter;

/**
 * The Limiter Excess Exception.
 *
 * @author lry
 */
public class LimiterExcessException extends RuntimeException {
    
    public LimiterExcessException(String message) {
        super(message);
    }

}
