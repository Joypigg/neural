package io.neural.limiter;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

import io.neural.common.Constants;
import io.neural.common.Identity;
import io.neural.common.Statistics;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * The LimiterStatistics of Limiter.
 *
 * @author lry
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LimiterStatistics extends Statistics implements Serializable {

    private static final long serialVersionUID = 5685475274387172658L;

    /**
     * The total concurrency exceed counter in the current time window
     */
    private LongAdder totalCExceedCounter = new LongAdder();
    /**
     * The total rate exceed counter in the current time window
     */
    private LongAdder totalRExceedCounter = new LongAdder();

    /**
     * The total exceed of statistical traffic
     */
    public void exceedTraffic(LimiterConfig.EventType eventType) {
        try {
            switch (eventType) {
                case LIMITER_CONCURRENT_EXCEED:
                    // increment exceed times
                    totalCExceedCounter.increment();
                    return;
                case LIMITER_RATE_EXCEED:
                    // increment exceed times
                    totalRExceedCounter.increment();
                    return;
                default:
                    log.error("The illegal EventType: {}", eventType);
            }
        } catch (Exception e) {
            log.error("The total request traffic is exception", e);
        }
    }

    /**
     * The get statistics data
     *
     * @return
     */
    @Override
    public Map<String, Long> getStatisticsData() {
        Map<String, Long> map = super.getStatisticsData();
        // statistics trade
        map.put("totalCExceed", totalCExceedCounter.longValue());
        map.put("totalRExceed", totalRExceedCounter.longValue());

        return map;
    }

    /**
     * The query statistics and reset
     *
     * @return
     */
    public synchronized Map<String, Long> getStatisticsAndReset(Identity identity) {
        Long time = super.buildStatisticsTime(
                Limiter.LIMITER.getStoreConfig().getGlobalConfig().getReportStatisticCycle().intValue());
        Map<String, Long> map = super.getBaseStatisticsAndReset(identity, time);

        // statistics exceed
        long totalCExceed = totalCExceedCounter.sumThenReset();
        long totalRExceed = totalRExceedCounter.sumThenReset();
        if (map.isEmpty()) {
            return map;
        }

        // statistics exceed
        map.put(super.buildStatisticsKey(Constants.TOTAL_C_EXCEED, identity, time), totalCExceed);
        map.put(super.buildStatisticsKey(Constants.TOTAL_R_EXCEED, identity, time), totalRExceed);

        return map;
    }

}
