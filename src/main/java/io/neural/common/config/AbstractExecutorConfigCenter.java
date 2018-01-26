package io.neural.common.config;

import java.util.*;
import java.util.concurrent.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.neural.common.Constants;
import io.neural.common.Identity;
import io.neural.common.Identity.Config;
import io.neural.common.Identity.GlobalConfig;
import io.neural.common.Identity.EventType;
import io.neural.common.event.EventProcessor;
import io.neural.common.store.SubscribeListener;
import lombok.extern.slf4j.Slf4j;


/**
 * The Default Config Center
 *
 * @param <C> extends {@link Config}
 * @param <G> extends {@link GlobalConfig}
 * @author lry
 **/
@Slf4j
public abstract class AbstractExecutorConfigCenter<C extends Config, G extends GlobalConfig>
        extends AbstractConfigCenter<C, G> {

    private ExecutorService subscribeConfigExecutor = null;
    private ScheduledExecutorService pullConfigExecutor = null;
    private ScheduledExecutorService pushStatisticsExecutor = null;
    private SubscribeListener listener = null;

    @Override
    protected void pullConfigs() {
        // cycle pull global config
        try {
            G remoteGlobalConfig = this.governor.queryGlobalConfig();
            if (null != remoteGlobalConfig && !remoteGlobalConfig.equals(globalConfig)) {
                log.debug("The {} global config pull changed: {}", module, remoteGlobalConfig);
                this.globalConfig = remoteGlobalConfig;
            }
        } catch (Exception e) {
            EventProcessor.EVENT.notify(module, EventType.PULL_GLOBAL_CONFIG_EXCEPTION);
            log.error("The " + module + " cycle pull global config is exception", e);
        }

        // cycle pull resource config
        configs.forEach((identity, config) ->
        {
            try {
                C remoteConfig = this.governor.queryConfig(identity);
                if (null != remoteConfig && !remoteConfig.equals(config)) {
                    log.debug("The {} config pull changed: {}, {}", module, identity, remoteConfig);
                    configs.put(identity, remoteConfig);
                    this.notify(identity, remoteConfig);
                }

                // the first excuse
                if (!isStarted) {
                    this.notify(identity, remoteConfig);
                }
            } catch (Exception e) {
                EventProcessor.EVENT.notify(module, EventType.PULL_CONFIG_EXCEPTION);
                log.error("The " + module + " cycle pull config[" + identity + "] is exception", e);
            }
        });
    }

    @Override
    protected synchronized void cyclePullConfigs() {
        if (null != pullConfigExecutor) {
            log.warn("The {} cyclePullConfigs is executed", module);
            return;
        }

        // start pull config data executor
        log.debug("The {} executing pull config data executor……", module);
        // build Task Name
        String pullConfigName = String.join(Constants.SEQ, module, Constants.PULL_CONFIG);
        ThreadFactoryBuilder pullBuilder = new ThreadFactoryBuilder();
        ThreadFactory pullThreadFactory = pullBuilder.setDaemon(true).setNameFormat(pullConfigName).build();
        this.pullConfigExecutor = Executors.newScheduledThreadPool(1, pullThreadFactory);

        // execute schedule pull config by fixed rate
        this.pullConfigExecutor.scheduleAtFixedRate(() ->
        {
            this.pullConfigs();
        }, globalConfig.getPullConfigCycle(), globalConfig.getPullConfigCycle(), TimeUnit.MILLISECONDS);
    }

    @Override
    protected synchronized void subscribeConfigs() {
        if (null != subscribeConfigExecutor) {
            log.warn("The {} subscribeConfigs is executed", module);
            return;
        }

        // start subscribe config data executor
        log.debug("The {} executing subscribe config data executor……", module);
        String subscribeConfigName = String.join(Constants.SEQ, module, Constants.SUBSCRIBE_CONFIG);
        ThreadFactoryBuilder subscribeBuilder = new ThreadFactoryBuilder();
        ThreadFactory subscribeThreadFactory =
                subscribeBuilder.setDaemon(true).setNameFormat(subscribeConfigName).build();
        this.subscribeConfigExecutor = Executors.newFixedThreadPool(1, subscribeThreadFactory);

        // execute subscribe global config
        this.subscribeConfigExecutor.execute(() ->
        {
            try {
                // the execute subscribe
                this.governor.subscribe(module, configs.keySet(), listener = new SubscribeListener() {
                    @Override
                    public void notify(String channel, String message) {
                        log.debug("The {} global config subscribed changed: {}, {}", module, channel, message);
                        if (null == channel || null == message) {
                            return;
                        }

                        if (channel.endsWith(Constants.SUBSCRIBE_GLOBAL_CONFIG_CHANNEL)) {
                            // the global config notify
                            Map<String, String> map = JSON.parseObject(
                                    message, new TypeReference<Map<String, String>>() {
                                    });
                            Class<G> globalClass = governor.getGlobalClass();
                            globalConfig = Constants.parseObject(globalClass, map);
                        } else {
                            // the config notify
                            Identity identity = Constants.parseIdentity(channel);
                            Map<String, String> map = JSON.parseObject(
                                    message, new TypeReference<Map<String, String>>() {
                                    });
                            Class<C> configClass = governor.getConfigClass();
                            configs.put(identity, Constants.parseObject(configClass, map));
                        }
                    }
                });
            } catch (Exception e) {
                EventProcessor.EVENT.notify(module, EventType.SUBSCRIBE_CONFIG_EXCEPTION);
                log.error("The " + module + " subscribed configs or global config is exception", e);
            }
        });
    }

    @Override
    protected synchronized void cyclePushStatistics() {
        if (null != pushStatisticsExecutor) {
            log.warn("The {} cyclePushStatistics is executed", module);
            return;
        }

        // start push statistics data executor
        log.debug("The {} executing push statistics data executor……", module);
        String pushStatisticsName = String.join(Constants.SEQ, module, Constants.PUSH_STATISTICS);
        ThreadFactoryBuilder pushBuilder = new ThreadFactoryBuilder();
        ThreadFactory pushTreadFactory = pushBuilder.setDaemon(true).setNameFormat(pushStatisticsName).build();
        this.pushStatisticsExecutor = Executors.newScheduledThreadPool(1, pushTreadFactory);

        // execute schedule push statistics by fixed rate
        this.pushStatisticsExecutor.scheduleAtFixedRate(() ->
        {
            try {
                // query memory statistics data
                Map<String, Long> statisticsData = this.collectStatistics();
                log.debug("The {} cycle push statistics: {}", module, statisticsData);
                if (null == statisticsData || statisticsData.isEmpty()) {
                    return;
                }

                Map<String, Long> sendData = new HashMap<>();
                for (Map.Entry<String, Long> entry : statisticsData.entrySet()) {
                    sendData.put(String.join(Constants.DELIMITER, module, entry.getKey()), entry.getValue());
                }

                // push statistics data to remote
                this.governor.batchUpOrAdd(globalConfig.getStatisticDataExpire(), sendData);
            } catch (Exception e) {
                EventProcessor.EVENT.notify(module, EventType.PUSH_STATISTICS_EXCEPTION);
                log.error("The " + module + " cycle push statistics is exception", e);
            }
        }, globalConfig.getReportStatisticCycle(), globalConfig.getReportStatisticCycle(), TimeUnit.MILLISECONDS);
    }

    @Override
    protected void notify(Identity identity, C config) {

    }

    @Override
    public void destroy() {
        log.debug("The {} is executing destroy……", module);
        if (null != pullConfigExecutor) {
            pullConfigExecutor.shutdown();
        }
        if (null != subscribeConfigExecutor) {
            subscribeConfigExecutor.shutdown();
        }
        if (null != pushStatisticsExecutor) {
            pushStatisticsExecutor.shutdown();
        }
        this.governor.unSubscribe(listener);
    }

}
