package cn.ms.neural.circuitbreaker;

/**
 * Circuit Breaker Component.
 *
 * @param <T> the type of the value monitored by this circuit breaker
 * @author lry
 */
public interface CircuitBreaker<T> {
	
	/**
	 * Returns the current open state of this circuit breaker. A return value of
	 * <strong>true</strong> means that the circuit breaker is currently open
	 * indicating a problem in the monitored sub system.
	 *
	 * @return the current open state of this circuit breaker
	 */
	boolean isOpen();

	/**
	 * Returns the current closed state of this circuit breaker. A return value
	 * of <strong>true</strong> means that the circuit breaker is currently
	 * closed. This means that everything is okay with the monitored sub system.
	 *
	 * @return the current closed state of this circuit breaker
	 */
	boolean isClosed();

	/**
	 * Checks the state of this circuit breaker and changes it if necessary. The
	 * return value indicates whether the circuit breaker is now in state
	 * {@code CLOSED}; a value of <strong>true</strong> typically means that the
	 * current operation can continue.
	 *
	 * @return <strong>true</strong> if the circuit breaker is now closed;
	 *         <strong>false</strong> otherwise
	 */
	boolean checkState();

	/**
	 * Closes this circuit breaker. Its state is changed to closed. If this
	 * circuit breaker is already closed, this method has no effect.
	 */
	void close();

	/**
	 * Opens this circuit breaker. Its state is changed to open. Depending on a
	 * concrete implementation, it may close itself again if the monitored sub
	 * system becomes available. If this circuit breaker is already open, this
	 * method has no effect.
	 */
	void open();

	/**
	 * Increments the monitored value and performs a check of the current state
	 * of this circuit breaker. This method works like {@link #checkState()},
	 * but the monitored value is incremented before the state check is
	 * performed.
	 *
	 * @param increment
	 *            value to increment in the monitored value of the circuit
	 *            breaker
	 * @return <strong>true</strong> if the circuit breaker is now closed;
	 *         <strong>false</strong> otherwise
	 */
	boolean incrementAndCheckState(T increment);
	
}
