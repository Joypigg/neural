package io.neural.common.config;

import io.neural.common.Identity;
import io.neural.common.Identity.Config;
import io.neural.common.Identity.GlobalConfig;
import io.neural.common.store.IStore;
import io.neural.extension.Extension;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * The Abstract Config Center
 *
 * @param <C> extends {@link Config}
 * @param <G> extends {@link GlobalConfig}
 * @author lry
 **/
public abstract class BaseConfigCenter<C extends Config, G extends GlobalConfig>
        implements IConfigCenter<C, G> {

    protected String module;
    protected ConfigCenterStore<C, G> governor;

    protected volatile G globalConfig;
    protected volatile boolean isStarted = false;
    protected final ConcurrentMap<Identity, C> configs = new ConcurrentHashMap<>();

    @Override
    public void initialize(IStore store, Class<C> configClass, Class<G> globalClass) {
        this.module = this.getClass().getAnnotation(Extension.class).value();
        this.governor = new ConfigCenterStore<>(module, store, configClass, globalClass);
    }

    @Override
    public ConfigCenterStore<C, G> getStore() {
        return governor;
    }

    /**
     * The start
     * <p>
     * 1.cyclePullConfigs
     * 2.subscribeConfigs
     * 3.cyclePushStatistics
     */
    @Override
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
        // set isStarted is true
        this.isStarted = true;
    }

    @Override
    public G getGlobalConfig() {
        return globalConfig;
    }

    @Override
    public C getConfig(Identity identity) {
        return configs.get(identity);
    }

    @Override
    public void putConfig(Identity identity, C config) {
        configs.put(identity, config);
    }

    /**
     * The notify changed config
     *
     * @param identity
     * @param config
     */
    protected abstract void notify(Identity identity, C config);

    /**
     * The pull all configs
     */
    protected abstract void pullConfigs();

    /**
     * The cycle pull configs
     */
    protected abstract void cyclePullConfigs();

    /**
     * The subscribe configs
     */
    protected abstract void subscribeConfigs();

    /**
     * The cycle push statistics
     */
    protected abstract void cyclePushStatistics();

    /**
     * The collect statistics data
     *
     * @return
     */
    protected abstract Map<String, Long> collectStatistics();

}
