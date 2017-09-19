package io.neural.circuitbreaker;

/**
 * Circuit Breaker Component.
 * <br>
 * 1.Closed：熔断器关闭状态，调用失败次数积累，到了阈值（或一定比例）则启动熔断机制<br>
 * 2.Open：熔断器打开状态，此时对下游的调用都内部直接返回错误，不走网络，但设计了一个时钟选项，默认的时钟达到了一定时间（这个时间一般设置成平均故障处理时间，也就是MTTR），到了这个时间，进入半熔断状态<br>
 * 3.Half-Open：半熔断状态，允许定量的服务请求，如果调用都成功（或一定比例）则认为恢复了，关闭熔断器，否则认为还没好，又回到熔断器打开状态<br>
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
