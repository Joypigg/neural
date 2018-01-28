package io.neural.degrade;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

import io.neural.common.Constants;
import io.neural.common.Identity;
import io.neural.common.Statistics;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The Degrade Statistics.
 *
 * @author lry
 **/
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DegradeStatistics extends Statistics implements Serializable {

    private static final long serialVersionUID = -2928427414009752116L;
    
    /**
     * The total times degrade counter（ms）
     */
    private LongAdder totalDegradeCounter = new LongAdder();

    /**
     * The total degrade of statistical traffic
     */
    public void degradeTraffic() {
        try {
            // increment exceed times
            totalDegradeCounter.increment();
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
        map.put("totalDegrade", totalDegradeCounter.longValue());

        return map;
    }

    /**
     * The query statistics and reset
     *
     * @return
     */
    public synchronized Map<String, Long> getStatisticsAndReset(Identity identity) {
        Long time = super.buildStatisticsTime(
                Degrade.DEGRADE.getConfigCenter().getGlobalConfig().getReportStatisticCycle().intValue());
        Map<String, Long> map = super.getBaseStatisticsAndReset(identity, time);

        // statistics exceed
        long totalDegrade = totalDegradeCounter.sumThenReset();
        if (map.isEmpty()) {
            return map;
        }

        // statistics exceed
        map.put(super.buildStatisticsKey(Constants.TOTAL_DEGRADE, identity, time), totalDegrade);

        return map;
    }

}
