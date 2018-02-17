package io.neural.limiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.neural.NURL;
import io.neural.common.Constants;
import io.neural.common.Identity;
import io.neural.common.Identity.Switch;
import io.neural.common.OriginalCall;
import io.neural.common.config.StoreConfig;
import io.neural.common.config.IStore;
import io.neural.extension.ExtensionLoader;
import io.neural.limiter.LimiterConfig.Config;
import io.neural.limiter.LimiterConfig.GlobalConfig;
import io.neural.limiter.core.ILimiter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * The Limiter.
 *
 * @author lry
 **/
@Slf4j
@Getter
public enum Limiter {

    LIMITER;

    private NURL nurl = null;
    private volatile boolean isStart = false;

    private IStore store = null;
    private StoreConfig<Config, GlobalConfig> storeConfig = null;

    private final ConcurrentMap<Identity, Config> configs = new ConcurrentHashMap<>();
    private final ConcurrentMap<Identity, ILimiter> limiters = new ConcurrentHashMap<>();

    /**
     * The add limiter
     *
     * @param limiterConfig
     * @throws Exception
     */
    public void addLimiter(LimiterConfig limiterConfig) throws Exception {
        Identity identity = limiterConfig.getIdentity();
        identity.setApplication(System.getProperty(Constants.APP_NAME_KEY, identity.getApplication()));
        configs.put(identity, limiterConfig.getConfig());

        ExtensionLoader.getLoader(ILimiter.class).getExtension();
        limiters.put(identity, ExtensionLoader.getLoader(ILimiter.class).getExtension());
    }

    /**
     * The start of limiter
     *
     * @param nurl
     */
    public void start(NURL nurl) {
        if (isStart) {
            log.warn("The limiter already is started");
            return;
        }

        this.nurl = nurl;

        // starting store
        this.store = ExtensionLoader.getLoader(IStore.class).getExtension(nurl.getProtocol());
        this.store.start(nurl);

        // initialize config center and initialize config center store
        this.storeConfig = ExtensionLoader.getLoader(StoreConfig.class).getExtension(nurl.getPath());

        // add limiter global config data to remote
        GlobalConfig globalConfig = storeConfig.queryGlobalConfig();
        if (null == globalConfig) {
            storeConfig.addGlobalConfig(new GlobalConfig());
        }
        // add limiter config data to remote
        configs.forEach(((identity, config) -> {
            storeConfig.putConfig(identity, config);
            if (null == storeConfig.queryConfig(identity)) {
                storeConfig.addConfig(identity, config);
            }
        }));
        storeConfig.start();

        // add shutdown Hook
        Runtime.getRuntime().addShutdownHook(new Thread(this::destroy));
        this.isStart = true;
    }

    /**
     * The process of limiter
     *
     * @param identity
     * @param originalCall
     * @return
     * @throws Throwable
     */
    public Object doLimiter(Identity identity, OriginalCall originalCall) throws Throwable {
        if (!isStart) {
            return originalCall.call();
        }

        GlobalConfig globalConfig = storeConfig.getGlobalConfig();
        // The check global config of limiter
        if (null == globalConfig || null == globalConfig.getEnable() || Switch.OFF == globalConfig.getEnable()) {
            return originalCall.call();
        }

        // The check limiter object
        if (null == identity || !limiters.containsKey(identity)) {
            return originalCall.call();
        }

        return limiters.get(identity).doOriginalCall(originalCall);
    }

    /**
     * The destroy of limiter
     */
    public void destroy() {
        this.isStart = false;
        if (null != storeConfig) {
            storeConfig.destroy();
        }
        limiters.clear();
        if (null != store) {
            this.store.destroy();
        }
    }

}
