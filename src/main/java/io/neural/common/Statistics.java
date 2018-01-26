package io.neural.common;

import io.neural.degrade.Degrade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

/**
 * The Statistics.
 *
 * @author lry
 **/
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Statistics implements Serializable {

    /**
     * The total request counter in the current time window: Calculation QPS
     */
    protected LongAdder totalRequestCounter = new LongAdder();


    /**
     * The total success counter in the current time window: Calculation TPS
     */
    protected LongAdder totalSuccessCounter = new LongAdder();
    /**
     * The total failure counter in the current time window
     */
    protected LongAdder totalFailureCounter = new LongAdder();
    /**
     * The total timeout counter in the current time window
     */
    protected LongAdder totalTimeoutCounter = new LongAdder();
    /**
     * The total rejection counter in the current time window
     */
    protected LongAdder totalRejectionCounter = new LongAdder();


    /**
     * The total elapsed counter in the current time window
     */
    protected LongAdder totalElapsedCounter = new LongAdder();
    /**
     * The max elapsed counter in the current time window
     */
    protected LongAccumulator maxElapsedCounter = new LongAccumulator(Long::max, 0);


    /**
     * The now counter number
     */
    protected LongAdder nowConcurrencyCounter = new LongAdder();
    /**
     * The total counter in the current time window
     */
    protected LongAdder totalConcurrencyCounter = new LongAdder();
    /**
     * The max concurrency counter in the current time window
     */
    protected LongAccumulator maxConcurrencyCounter = new LongAccumulator(Long::max, 0);


    /**
     * The total request of statistical traffic
     */
    public void totalRequestTraffic() {
        try {
            // increment request times
            totalRequestCounter.increment();
        } catch (Exception e) {
            log.error("The total request traffic is exception", e);
        }
    }

    /**
     * The total increment of statistical traffic
     */
    public void incrementTraffic() {
        try {
            // increment concurrency times
            nowConcurrencyCounter.increment();

            long tempNowConcurrencyCounter = nowConcurrencyCounter.longValue();
            // total all concurrency times
            totalConcurrencyCounter.add(tempNowConcurrencyCounter);
            // total max concurrency
            maxConcurrencyCounter.accumulate(tempNowConcurrencyCounter);
        } catch (Exception e) {
            log.error("The increment traffic is exception", e);
        }
    }

    /**
     * The total decrement of statistical traffic
     *
     * @param startTime
     */
    public void decrementTraffic(long startTime) {
        try {
            long elapsed = System.currentTimeMillis() - startTime;
            // total all elapsed
            totalElapsedCounter.add(elapsed);
            // total max elapsed
            maxElapsedCounter.accumulate(elapsed);

            // total all success times
            totalSuccessCounter.increment();
            // decrement concurrency times
            nowConcurrencyCounter.decrement();
        } catch (Exception e) {
            log.error("The decrement traffic is exception", e);
        }
    }

    /**
     * The total exception of statistical traffic
     *
     * @param t
     */
    public void exceptionTraffic(Throwable t) {
        try {
            // total all failure times
            totalFailureCounter.increment();
            if (t instanceof TimeoutException) {
                // total all timeout times
                totalTimeoutCounter.increment();
            } else if (t instanceof RejectedExecutionException) {
                // total all rejection times
                totalRejectionCounter.increment();
            }
        } catch (Exception e) {
            log.error("The exception traffic is exception", e);
        }
    }

    /**
     * The build limiter statistics key
     *
     * @param KEY
     * @param id
     * @param time
     * @return
     */
    public String buildStatisticsKey(String KEY, Identity id, long time) {
        return String.format(KEY, id.getApplication(), id.getGroup(), id.getResource(), time);
    }

    /**
     * The build statistics time
     *
     * @param statisticReportCycle
     * @return
     */
    public Long buildStatisticsTime(Integer statisticReportCycle) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        Integer second = statisticReportCycle / 1000;
        Integer tempSecond = calendar.get(Calendar.SECOND) % second;
        second = tempSecond >= second / 2 ? second : 0;
        calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) + second - tempSecond);

        return calendar.getTimeInMillis();
    }

    /**
     * The query statistics and reset
     *
     * @return
     */
    public Map<String, Long> getBaseStatisticsAndReset(Identity identity, Long time) {
        Map<String, Long> map = new HashMap<>();
        // statistics trade
        long totalRequest = totalRequestCounter.sumThenReset();
        long totalSuccess = totalSuccessCounter.sumThenReset();
        long totalFailure = totalFailureCounter.sumThenReset();
        long totalTimeout = totalTimeoutCounter.sumThenReset();
        long totalRejection = totalRejectionCounter.sumThenReset();
        // statistics elapsed
        long totalElapsed = totalElapsedCounter.sumThenReset();
        long maxElapsed = maxElapsedCounter.getThenReset();
        // statistics concurrency
        long totalConcurrency = totalConcurrencyCounter.sumThenReset();
        long maxConcurrency = maxConcurrencyCounter.getThenReset();

        if (totalRequest < 1) {
            return map;
        }

        // statistics trade
        map.put(this.buildStatisticsKey(Constants.TOTAL_REQUEST, identity, time), totalRequest);
        map.put(this.buildStatisticsKey(Constants.TOTAL_SUCCESS, identity, time), totalSuccess);
        map.put(this.buildStatisticsKey(Constants.TOTAL_FAILURE, identity, time), totalFailure);
        map.put(this.buildStatisticsKey(Constants.TOTAL_TIMEOUT, identity, time), totalTimeout);
        map.put(this.buildStatisticsKey(Constants.TOTAL_REJECTION, identity, time), totalRejection);
        // statistics elapsed
        map.put(this.buildStatisticsKey(Constants.TOTAL_ELAPSED, identity, time), totalElapsed);
        map.put(this.buildStatisticsKey(Constants.MAX_ELAPSED, identity, time), maxElapsed);
        // statistics concurrency
        map.put(this.buildStatisticsKey(Constants.TOTAL_CONCURRENCY, identity, time), totalConcurrency);
        map.put(this.buildStatisticsKey(Constants.MAX_CONCURRENCY, identity, time), maxConcurrency);

        return map;
    }

    /**
     * The get statistics data
     *
     * @return
     */
    public Map<String, Long> getStatisticsData() {
        Map<String, Long> map = new HashMap<>();
        // statistics trade
        map.put("totalRequest", totalRequestCounter.longValue());
        map.put("totalSuccess", totalSuccessCounter.longValue());
        map.put("totalFailure", totalFailureCounter.longValue());
        map.put("totalTimeout", totalTimeoutCounter.longValue());
        map.put("totalRejection", totalRejectionCounter.longValue());
        // statistics elapsed
        map.put("totalElapsed", totalElapsedCounter.longValue());
        map.put("maxElapsed", maxElapsedCounter.longValue());
        // statistics concurrency
        map.put("totalConcurrency", totalConcurrencyCounter.longValue());
        map.put("maxConcurrency", maxConcurrencyCounter.longValue());

        return map;
    }

}
