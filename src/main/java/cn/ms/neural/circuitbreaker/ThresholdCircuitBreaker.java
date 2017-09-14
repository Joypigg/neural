package cn.ms.neural.circuitbreaker;

import java.util.concurrent.atomic.AtomicLong;

/**
 * A simple implementation of the Circuit Breaker pattern that opens if the
 * requested increment amount is greater than a given threshold.
 * <p>
 * This is thread safe.
 * 
 * @author lry
 */
public class ThresholdCircuitBreaker extends AbstractCircuitBreaker<Long> {
	
	/**
	 * The initial value of the internal counter.
	 */
	private final static long INITIAL_COUNT = 0L;

	/**
	 * The threshold.
	 */
	private final long threshold;

	/**
	 * Controls the amount used.
	 */
	private final AtomicLong used;

	/**
	 * <p>
	 * Creates a new instance of {@code ThresholdCircuitBreaker} and initializes
	 * the threshold.
	 * </p>
	 *
	 * @param threshold the threshold.
	 */
	public ThresholdCircuitBreaker(long threshold) {
		super();
		this.used = new AtomicLong(INITIAL_COUNT);
		this.threshold = threshold;
	}

	/**
	 * Gets the threshold.
	 *
	 * @return the threshold
	 */
	public long getThreshold() {
		return threshold;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean checkState() throws CircuitBreakingException {
		return isOpen();
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * Resets the internal counter back to its initial value (zero).
	 * </p>
	 */
	@Override
	public void close() {
		super.close();
		this.used.set(INITIAL_COUNT);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * If the threshold is zero, the circuit breaker will be in a permanent
	 * <em>open</em> state.
	 * </p>
	 */
	@Override
	public boolean incrementAndCheckState(Long increment)
			throws CircuitBreakingException {
		if (threshold == 0) {
			open();
		}

		long used = this.used.addAndGet(increment);
		if (used > threshold) {
			open();
		}

		return checkState();
	}

}
