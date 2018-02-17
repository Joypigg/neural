package io.neural.common.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.neural.common.Constants;
import io.neural.common.Identity;
import io.neural.common.event.EventProcessor;
import io.neural.common.SubscribeListener;
import io.neural.common.Identity.EventType;
import io.neural.extension.Extension;
import io.neural.extension.NPI;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/**
 * The Store Config
 *
 * @param <C> extends {@link Identity.Config}
 * @param <G> extends {@link Identity.GlobalConfig}
 * @author lry
 **/
@NPI
@Slf4j
public class StoreConfig<C extends Identity.Config, G extends Identity.GlobalConfig> {

    protected final String module;
    private final Class<C> configClass;
    private final Class<G> globalClass;

    @Setter
    private IStore store;
    @Getter
    private volatile G globalConfig;
    private volatile ConcurrentMap<Identity, C> configs = new ConcurrentHashMap<>();

    private SubscribeListener listener = null;
    private ExecutorService subscribeConfigExecutor = null;
    private ScheduledExecutorService pullConfigExecutor = null;
    private ScheduledExecutorService pushStatisticsExecutor = null;


    public StoreConfig() {
        this.module = this.getClass().getAnnotation(Extension.class).value();

        Type type = this.getClass().getGenericSuperclass();
        Type[] args = ((ParameterizedType) type).getActualTypeArguments();
        this.configClass = (Class<C>) args[0];
        this.globalClass = (Class<G>) args[1];
    }

    public void putConfig(Identity identity, C config) {
        this.configs.put(identity, config);
    }

    /**
     * The start store config
     */
    public void start() {
        // initialize pull all configs
        this.pullConfigs();
        // start cycle pull configs
        this.cyclePullConfigs();
        // start subscribe configs
        this.subscribeConfigs();
        // start cycle push statistics
        this.cyclePushStatistics();

        // add shutdown Hook
        Runtime.getRuntime().addShutdownHook(new Thread(this::destroy));
    }

    /**
     * The destroy store config
     */
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
        this.unSubscribe(listener);
    }

    /**
     * The pull all configs
     */
    private void pullConfigs() {
        // cycle pull global config
        try {
            G remoteGlobalConfig = this.queryGlobalConfig();
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
                C remoteConfig = this.queryConfig(identity);
                if (null != remoteConfig && !remoteConfig.equals(config)) {
                    log.debug("The {} config pull changed: {}, {}", module, identity, remoteConfig);
                    configs.put(identity, remoteConfig);
                    this.notify(identity, remoteConfig);
                }

                // the first excuse
                this.notify(identity, remoteConfig);
            } catch (Exception e) {
                EventProcessor.EVENT.notify(module, EventType.PULL_CONFIG_EXCEPTION);
                log.error("The " + module + " cycle pull config[" + identity + "] is exception", e);
            }
        });
    }

    /**
     * The cycle pull configs
     */
    private synchronized void cyclePullConfigs() {
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
        this.pullConfigExecutor.scheduleAtFixedRate(this::pullConfigs,
                globalConfig.getPullConfigCycle(), globalConfig.getPullConfigCycle(), TimeUnit.MILLISECONDS);
    }

    /**
     * The subscribe configs
     */
    private synchronized void subscribeConfigs() {
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
                this.subscribe(module, configs.keySet(), listener = new SubscribeListener() {
                    @Override
                    public void notify(String channel, String message) {
                        log.debug("The {} global config subscribed changed: {}, {}", module, channel, message);
                        if (null == channel || null == message) {
                            return;
                        }

                        if (channel.endsWith(Constants.SUBSCRIBE_GLOBAL_CONFIG_CHANNEL)) {
                            // the global config notify
                            globalConfig = Constants.parseObject(globalClass, message);
                        } else {
                            // the config notify
                            configs.put(Constants.parseIdentity(channel), Constants.parseObject(configClass, message));
                        }
                    }
                });
            } catch (Exception e) {
                EventProcessor.EVENT.notify(module, EventType.SUBSCRIBE_CONFIG_EXCEPTION);
                log.error("The " + module + " subscribed configs or global config is exception", e);
            }
        });
    }

    /**
     * The cycle push statistics
     */
    private synchronized void cyclePushStatistics() {
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
                this.batchUpOrAdd(globalConfig.getStatisticDataExpire(), sendData);
            } catch (Exception e) {
                EventProcessor.EVENT.notify(module, EventType.PUSH_STATISTICS_EXCEPTION);
                log.error("The " + module + " cycle push statistics is exception", e);
            }
        }, globalConfig.getReportStatisticCycle(), globalConfig.getReportStatisticCycle(), TimeUnit.MILLISECONDS);
    }


    /**
     * The add(insert or update) GlobalConfig
     *
     * @param globalConfig
     */
    public void addGlobalConfig(G globalConfig) {
        store.add(Constants.buildGlobalConfigKey(module), Constants.buildMap(globalConfig));
    }

    /**
     * The query GlobalConfig
     *
     * @return
     */
    public G queryGlobalConfig() {
        Map<String, String> remoteGlobalConfigMap = store.query(Constants.buildGlobalConfigKey(module));
        if (null == remoteGlobalConfigMap || remoteGlobalConfigMap.isEmpty()) {
            return null;
        }

        return Constants.parseObject(globalClass, remoteGlobalConfigMap);
    }

    /**
     * The add(insert or update) Config
     *
     * @param identity
     * @param config
     */
    public void addConfig(Identity identity, C config) {
        store.add(Constants.buildConfigKey(module, identity), Constants.buildMap(config));
    }

    /**
     * The query Config by Identity
     *
     * @param identity
     * @return
     */
    public C queryConfig(Identity identity) {
        Map<String, String> remoteConfigMap = store.query(Constants.buildConfigKey(module, identity));
        if (null == remoteConfigMap || remoteConfigMap.isEmpty()) {
            return null;
        }

        return Constants.parseObject(configClass, remoteConfigMap);
    }

    /**
     * The query all configs
     *
     * @return
     */
    public Map<Identity, C> queryConfigs() {
        Set<String> remoteConfigKeys = store.searchKeys(Constants.buildAllConfigKey(module));
        Map<Identity, C> map = new HashMap<>(remoteConfigKeys.size());
        for (String remoteConfigKey : remoteConfigKeys) {
            Map<String, String> remoteConfigMap = store.query(remoteConfigKey);
            C config = Constants.parseObject(configClass, remoteConfigMap);
            if (null == remoteConfigMap || remoteConfigMap.isEmpty()) {
                config = null;
            }
            map.put(Constants.parseIdentity(remoteConfigKey), config);
        }

        return map;
    }

    /**
     * The batch update or add
     *
     * @param expire
     * @param data
     */
    public void batchUpOrAdd(long expire, Map<String, Long> data) {
        store.batchUpOrAdd(expire, data);
    }

    /**
     * The subscribe
     *
     * @param identity
     * @param object
     */
    public void publish(Identity identity, Object object) {
        String channel = null;
        if (object instanceof Identity.GlobalConfig) {
            channel = String.join(Constants.DELIMITER, module, Constants.SUBSCRIBE_GLOBAL_CONFIG_CHANNEL);
        } else if (object instanceof Identity.Config) {
            channel = String.format(String.join(
                    Constants.DELIMITER, module, Constants.SUBSCRIBE_CONFIG_CHANNEL_KEY),
                    identity.getApplication(), identity.getGroup(), identity.getResource());
        }
        if (null == channel) {
            return;
        }
        //getStore().publish(channel, Constants.toJSONString(object));
    }

    /**
     * The subscribe
     *
     * @param module
     * @param identities
     * @param listener
     */
    public void subscribe(String module, Set<Identity> identities, SubscribeListener listener) {
        // the build subscribe keys
        Set<String> subscribeKeys = new HashSet<>(identities.size() + 1);
        subscribeKeys.add(String.join(
                Constants.DELIMITER, module, Constants.SUBSCRIBE_GLOBAL_CONFIG_CHANNEL));
        String configKey = String.join(
                Constants.DELIMITER, module, Constants.SUBSCRIBE_CONFIG_CHANNEL_KEY);
        for (Identity identity : identities) {
            subscribeKeys.add(String.format(configKey,
                    identity.getApplication(), identity.getGroup(), identity.getResource()));
        }

        // the execute subscribe
        //String[] subscribeKeyArray = subscribeKeys.toArray(new String[subscribeKeys.size()]);
        //getStore().subscribe(subscribeKeyArray, listener);
    }

    /**
     * The un subscribe
     *
     * @param listener
     */
    public void unSubscribe(SubscribeListener listener) {
        //getStore().unSubscribe(listener);
    }


    /**
     * The notify changed config
     *
     * @param identity the config identity
     * @param config   the config
     */
    public void notify(Identity identity, C config) {

    }

    /**
     * The collect statistics data
     *
     * @return statistics data
     */
    public Map<String, Long> collectStatistics() {
        return null;
    }

}
