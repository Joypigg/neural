package io.neural.limiter.ratelimiter;

import static java.lang.Math.min;
import static java.util.concurrent.TimeUnit.SECONDS;

import com.google.common.math.LongMath;

import java.util.concurrent.TimeUnit;

public abstract class SmoothRateLimiter extends AdjustableRateLimiter {

	static final class SmoothWarmingUp extends SmoothRateLimiter {
		
		private final long warmupPeriodMicros;
		private double slope;
		private double thresholdPermits;
		private double coldFactor;

		SmoothWarmingUp(SleepingStopwatch stopwatch, long warmupPeriod, TimeUnit timeUnit, double coldFactor) {
			super(stopwatch);
			this.warmupPeriodMicros = timeUnit.toMicros(warmupPeriod);
			this.coldFactor = coldFactor;
		}

		@Override
		void doSetRate(double permitsPerSecond, double stableIntervalMicros) {
			double oldMaxPermits = maxPermits;
			double coldIntervalMicros = stableIntervalMicros * coldFactor;
			thresholdPermits = 0.5 * warmupPeriodMicros / stableIntervalMicros;
			maxPermits = thresholdPermits + 2.0 * warmupPeriodMicros / (stableIntervalMicros + coldIntervalMicros);
			slope = (coldIntervalMicros - stableIntervalMicros) / (maxPermits - thresholdPermits);
			if (oldMaxPermits == Double.POSITIVE_INFINITY) {
				storedPermits = 0.0;
			} else {
				storedPermits = (oldMaxPermits == 0.0) ? maxPermits : storedPermits * maxPermits / oldMaxPermits;
			}
		}

		@Override
		long storedPermitsToWaitTime(double storedPermits, double permitsToTake) {
			double availablePermitsAboveThreshold = storedPermits - thresholdPermits;
			long micros = 0;
			if (availablePermitsAboveThreshold > 0.0) {
				double permitsAboveThresholdToTake = min(availablePermitsAboveThreshold, permitsToTake);
				double length = permitsToTime(availablePermitsAboveThreshold) + permitsToTime(availablePermitsAboveThreshold - permitsAboveThresholdToTake);
				micros = (long) (permitsAboveThresholdToTake * length / 2.0);
				permitsToTake -= permitsAboveThresholdToTake;
			}
			micros += (stableIntervalMicros * permitsToTake);
			return micros;
		}

		private double permitsToTime(double permits) {
			return stableIntervalMicros + permits * slope;
		}

		@Override
		double coolDownIntervalMicros() {
			return warmupPeriodMicros / maxPermits;
		}
	}

	static final class SmoothBursty extends SmoothRateLimiter {
		final double maxBurstSeconds;

		SmoothBursty(SleepingStopwatch stopwatch, double maxBurstSeconds) {
			super(stopwatch);
			this.maxBurstSeconds = maxBurstSeconds;
		}

		@Override
		void doSetRate(double permitsPerSecond, double stableIntervalMicros) {
			double oldMaxPermits = this.maxPermits;
			maxPermits = maxBurstSeconds * permitsPerSecond;
			if (oldMaxPermits == Double.POSITIVE_INFINITY) {
				storedPermits = maxPermits;
			} else {
				storedPermits = (oldMaxPermits == 0.0) ? 0.0 : storedPermits * maxPermits / oldMaxPermits;
			}
		}

		@Override
		long storedPermitsToWaitTime(double storedPermits, double permitsToTake) {
			return 0L;
		}

		@Override
		double coolDownIntervalMicros() {
			return stableIntervalMicros;
		}
	}

	double storedPermits;
	double maxPermits;
	double stableIntervalMicros;
	private long nextFreeTicketMicros = 0L;

	private SmoothRateLimiter(SleepingStopwatch stopwatch) {
		super(stopwatch);
	}

	@Override
	final void doSetRate(double permitsPerSecond, long nowMicros) {
		resync(nowMicros);
		double stableIntervalMicros = super.getTimeGritSecond() / permitsPerSecond;
		this.stableIntervalMicros = stableIntervalMicros;
		doSetRate(permitsPerSecond, stableIntervalMicros);
	}

	abstract void doSetRate(double permitsPerSecond, double stableIntervalMicros);

	@Override
	final double doGetRate() {
		return SECONDS.toMicros(1L) / stableIntervalMicros;
	}

	@Override
	final long queryEarliestAvailable(long nowMicros) {
		return nextFreeTicketMicros;
	}

	@Override
	final long reserveEarliestAvailable(int requiredPermits, long nowMicros) {
		resync(nowMicros); // 补充令牌
		long returnValue = nextFreeTicketMicros;
		double storedPermitsToSpend = min(requiredPermits, this.storedPermits); // 获取这次请求消耗的令牌数目
		double freshPermits = requiredPermits - storedPermitsToSpend;
		long waitMicros = storedPermitsToWaitTime(this.storedPermits, storedPermitsToSpend) + (long) (freshPermits * stableIntervalMicros);

		this.nextFreeTicketMicros = LongMath.saturatedAdd(nextFreeTicketMicros, waitMicros);
		this.storedPermits -= storedPermitsToSpend; // 减去消耗的令牌
		return returnValue;
	}

	abstract long storedPermitsToWaitTime(double storedPermits, double permitsToTake);
	abstract double coolDownIntervalMicros();

	void resync(long nowMicros) {
		if (nowMicros > nextFreeTicketMicros) {
			double newPermits = (nowMicros - nextFreeTicketMicros) / coolDownIntervalMicros();
			storedPermits = min(maxPermits, storedPermits + newPermits);
			nextFreeTicketMicros = nowMicros;
		}
	}
}
