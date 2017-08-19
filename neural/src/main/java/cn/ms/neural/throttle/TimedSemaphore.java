package cn.ms.neural.throttle;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A specialized <em>semaphore</em> implementation that provides a number of
 * permits in a given time frame.
 * 
 * @author lry
 */
public class TimedSemaphore {

	/**
	 * Constant for a value representing no limit. If the limit is set to a
	 * value less or equal this constant, the {@code TimedSemaphore} will be
	 * effectively switched off.
	 */
	public static final int NO_LIMIT = 0;

	/** Constant for the thread pool size for the executor. */
	private static final int THREAD_POOL_SIZE = 1;

	/** The executor service for managing the timer thread. */
	private final ScheduledExecutorService executorService;

	/** Stores the period for this timed semaphore. */
	private final long period;

	/** The time unit for the period. */
	private final TimeUnit unit;

	/** A flag whether the executor service was created by this object. */
	private final boolean ownExecutor;

	/** A future object representing the timer task. */
	private ScheduledFuture<?> task;

	/** Stores the total number of invocations of the acquire() method. */
	private long totalAcquireCount;

	/**
	 * The counter for the periods. This counter is increased every time a
	 * period ends.
	 */
	private long periodCount;

	/** The limit. */
	private int limit;

	/** The current counter. */
	private int acquireCount;

	/** The number of invocations of acquire() in the last period. */
	private int lastCallsPerPeriod;

	/** A flag whether shutdown() was called. */
	private boolean shutdown;

	/**
	 * Creates a new instance of {@link TimedSemaphore} and initializes it with
	 * the given time period and the limit.
	 *
	 * @param timePeriod
	 *            the time period
	 * @param timeUnit
	 *            the unit for the period
	 * @param limit
	 *            the limit for the semaphore
	 * @throws IllegalArgumentException
	 *             if the period is less or equals 0
	 */
	public TimedSemaphore(final long timePeriod, final TimeUnit timeUnit,
			final int limit) {
		this(null, timePeriod, timeUnit, limit);
	}

	/**
	 * Creates a new instance of {@link TimedSemaphore} and initializes it with
	 * an executor service, the given time period, and the limit. The executor
	 * service will be used for creating a periodic task for monitoring the time
	 * period. It can be <b>null</b>, then a default service will be created.
	 *
	 * @param service
	 *            the executor service
	 * @param timePeriod
	 *            the time period
	 * @param timeUnit
	 *            the unit for the period
	 * @param limit
	 *            the limit for the semaphore
	 * @throws IllegalArgumentException
	 *             if the period is less or equals 0
	 */
	public TimedSemaphore(final ScheduledExecutorService service,
			final long timePeriod, final TimeUnit timeUnit, final int limit) {
		if (timePeriod < 1 || timePeriod > Long.MAX_VALUE) {
            throw new IllegalArgumentException(String.format("Time period must be greater than 0!"));
        }

		period = timePeriod;
		unit = timeUnit;

		if (service != null) {
			executorService = service;
			ownExecutor = false;
		} else {
			final ScheduledThreadPoolExecutor s = new ScheduledThreadPoolExecutor(
					THREAD_POOL_SIZE);
			s.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
			s.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
			executorService = s;
			ownExecutor = true;
		}

		setLimit(limit);
	}

	/**
	 * Returns the limit enforced by this semaphore. The limit determines how
	 * many invocations of {@link #acquire()} are allowed within the monitored
	 * period.
	 *
	 * @return the limit
	 */
	public final synchronized int getLimit() {
		return limit;
	}

	/**
	 * Sets the limit. This is the number of times the {@link #acquire()} method
	 * can be called within the time period specified. If this limit is reached,
	 * further invocations of {@link #acquire()} will block. Setting the limit
	 * to a value &lt;= {@link #NO_LIMIT} will cause the limit to be disabled,
	 * i.e. an arbitrary number of{@link #acquire()} invocations is allowed in
	 * the time period.
	 *
	 * @param limit
	 *            the limit
	 */
	public final synchronized void setLimit(final int limit) {
		this.limit = limit;
	}

	/**
	 * Initializes a shutdown. After that the object cannot be used any more.
	 * This method can be invoked an arbitrary number of times. All invocations
	 * after the first one do not have any effect.
	 */
	public synchronized void shutdown() {
		if (!shutdown) {

			if (ownExecutor) {
				// if the executor was created by this instance, it has
				// to be shutdown
				getExecutorService().shutdownNow();
			}
			if (task != null) {
				task.cancel(false);
			}

			shutdown = true;
		}
	}

	/**
	 * Tests whether the {@link #shutdown()} method has been called on this
	 * object. If this method returns <b>true</b>, this instance cannot be used
	 * any longer.
	 *
	 * @return a flag whether a shutdown has been performed
	 */
	public synchronized boolean isShutdown() {
		return shutdown;
	}

	/**
	 * Acquires a permit from this semaphore. This method will block if the
	 * limit for the current period has already been reached. If
	 * {@link #shutdown()} has already been invoked, calling this method will
	 * cause an exception. The very first call of this method starts the timer
	 * task which monitors the time period set for this {@code TimedSemaphore}.
	 * From now on the semaphore is active.
	 *
	 * @throws InterruptedException
	 *             if the thread gets interrupted
	 * @throws IllegalStateException
	 *             if this semaphore is already shut down
	 */
	public synchronized void acquire() throws InterruptedException {
		prepareAcquire();

		boolean canPass;
		do {
			canPass = acquirePermit();
			if (!canPass) {
				wait();
			}
		} while (!canPass);
	}

	/**
	 * Tries to acquire a permit from this semaphore. If the limit of this
	 * semaphore has not yet been reached, a permit is acquired, and this method
	 * returns <strong>true</strong>. Otherwise, this method returns immediately
	 * with the result <strong>false</strong>.
	 *
	 * @return <strong>true</strong> if a permit could be acquired;
	 *         <strong>false</strong> otherwise
	 * @throws IllegalStateException
	 *             if this semaphore is already shut down
	 * @since 3.5
	 */
	public synchronized boolean tryAcquire() {
		prepareAcquire();
		return acquirePermit();
	}

	/**
	 * Returns the number of (successful) acquire invocations during the last
	 * period. This is the number of times the {@link #acquire()} method was
	 * called without blocking. This can be useful for testing or debugging
	 * purposes or to determine a meaningful threshold value. If a limit is set,
	 * the value returned by this method won't be greater than this limit.
	 *
	 * @return the number of non-blocking invocations of the {@link #acquire()}
	 *         method
	 */
	public synchronized int getLastAcquiresPerPeriod() {
		return lastCallsPerPeriod;
	}

	/**
	 * Returns the number of invocations of the {@link #acquire()} method for
	 * the current period. This may be useful for testing or debugging purposes.
	 *
	 * @return the current number of {@link #acquire()} invocations
	 */
	public synchronized int getAcquireCount() {
		return acquireCount;
	}

	/**
	 * Returns the number of calls to the {@link #acquire()} method that can
	 * still be performed in the current period without blocking. This method
	 * can give an indication whether it is safe to call the {@link #acquire()}
	 * method without risking to be suspended. However, there is no guarantee
	 * that a subsequent call to {@link #acquire()} actually is not-blocking
	 * because in the mean time other threads may have invoked the semaphore.
	 *
	 * @return the current number of available {@link #acquire()} calls in the
	 *         current period
	 */
	public synchronized int getAvailablePermits() {
		return getLimit() - getAcquireCount();
	}

	/**
	 * Returns the average number of successful (i.e. non-blocking)
	 * {@link #acquire()} invocations for the entire life-time of this
	 * {@code TimedSemaphore}. This method can be used for instance for
	 * statistical calculations.
	 *
	 * @return the average number of {@link #acquire()} invocations per time
	 *         unit
	 */
	public synchronized double getAverageCallsPerPeriod() {
		return periodCount == 0 ? 0 : (double) totalAcquireCount
				/ (double) periodCount;
	}

	/**
	 * Returns the time period. This is the time monitored by this semaphore.
	 * Only a given number of invocations of the {@link #acquire()} method is
	 * possible in this period.
	 *
	 * @return the time period
	 */
	public long getPeriod() {
		return period;
	}

	/**
	 * Returns the time unit. This is the unit used by {@link #getPeriod()}.
	 *
	 * @return the time unit
	 */
	public TimeUnit getUnit() {
		return unit;
	}

	/**
	 * Returns the executor service used by this instance.
	 *
	 * @return the executor service
	 */
	protected ScheduledExecutorService getExecutorService() {
		return executorService;
	}

	/**
	 * Starts the timer. This method is called when {@link #acquire()} is called
	 * for the first time. It schedules a task to be executed at fixed rate to
	 * monitor the time period specified.
	 *
	 * @return a future object representing the task scheduled
	 */
	protected ScheduledFuture<?> startTimer() {
		return getExecutorService().scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				endOfPeriod();
			}
		}, getPeriod(), getPeriod(), getUnit());
	}

	/**
	 * The current time period is finished. This method is called by the timer
	 * used internally to monitor the time period. It resets the counter and
	 * releases the threads waiting for this barrier.
	 */
	synchronized void endOfPeriod() {
		lastCallsPerPeriod = acquireCount;
		totalAcquireCount += acquireCount;
		periodCount++;
		acquireCount = 0;
		notifyAll();
	}

	/**
	 * Prepares an acquire operation. Checks for the current state and starts
	 * the internal timer if necessary. This method must be called with the lock
	 * of this object held.
	 */
	private void prepareAcquire() {
		if (isShutdown()) {
			throw new IllegalStateException("TimedSemaphore is shut down!");
		}

		if (task == null) {
			task = startTimer();
		}
	}

	/**
	 * Internal helper method for acquiring a permit. This method checks whether
	 * currently a permit can be acquired and - if so - increases the internal
	 * counter. The return value indicates whether a permit could be acquired.
	 * This method must be called with the lock of this object held.
	 *
	 * @return a flag whether a permit could be acquired
	 */
	private boolean acquirePermit() {
		if (getLimit() <= NO_LIMIT || acquireCount < getLimit()) {
			acquireCount++;
			return true;
		}
		return false;
	}
}
