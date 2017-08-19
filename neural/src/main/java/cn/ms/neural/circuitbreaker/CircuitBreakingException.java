package cn.ms.neural.circuitbreaker;

/**
 * An exception class used for reporting runtime error conditions related to
 * circuit breakers.
 * 
 * @author lry
 */
public class CircuitBreakingException extends RuntimeException {

	private static final long serialVersionUID = 1408176654686913340L;

	public CircuitBreakingException() {
		super();
	}

	public CircuitBreakingException(String message, Throwable cause) {
		super(message, cause);
	}

	public CircuitBreakingException(String message) {
		super(message);
	}

	public CircuitBreakingException(Throwable cause) {
		super(cause);
	}

}
