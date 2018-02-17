package io.neural.degrade;

import io.neural.common.Identity;
import io.neural.common.config.StoreConfig;
import io.neural.common.event.EventProcessor;
import io.neural.extension.Extension;
import io.neural.degrade.DegradeConfig.Config;
import io.neural.degrade.DegradeConfig.GlobalConfig;
import io.neural.degrade.DegradeConfig.EventType;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * The Degrade Config Center.
 *
 * @author lry
 **/
@Slf4j
@Extension("degrade")
public class DegradeConfigCenter extends StoreConfig<Config, GlobalConfig> {

    @Override
    public Map<String, Long> collectStatistics() {
        Map<String, Long> dataMap = new HashMap<>();
        try {
            Degrade.DEGRADE.getDegradeStatistics().forEach((identity, statistics) -> {
                Map<String, Long> tempDataMap = statistics.getStatisticsAndReset(identity);
                if (null == tempDataMap || tempDataMap.isEmpty()) {
                    return;
                }

                dataMap.putAll(tempDataMap);
            });
        } catch (Exception e) {
            EventProcessor.EVENT.notify(Degrade.MODULE, EventType.DEGRADE_COLLECT_EXCEPTION);
            log.error("The notify config is exception of degrade", e);
        }

        return dataMap;
    }

    @Override
    public synchronized void notify(Identity identity, Config config) {
        try {
            Object mockData = DegradeConfig.mockData(config.getMock(), config.getClazz(), config.getData());
            Degrade.DEGRADE.getMockDataMap().put(identity, mockData);
        } catch (Exception e) {
            EventProcessor.EVENT.notify(Degrade.MODULE, EventType.DEGRADE_NOTIFY_EXCEPTION);
            log.error("The collect statistics is exception of degrade", e);
        }
    }

}
