package io.neural.common.config;

import io.neural.common.Identity;
import io.neural.common.Identity.Config;
import io.neural.common.Identity.GlobalConfig;
import io.neural.common.store.IStore;
import io.neural.extension.NPI;

/**
 * The Config Center
 *
 * @param <C> extends {@link Config}
 * @param <G> extends {@link GlobalConfig}
 * @author lry
 */
@NPI
public interface IConfigCenter<C extends Config, G extends GlobalConfig> {

    /**
     * The initialize
     *
     * @param store
     * @param configClass
     * @param globalClass
     */
    void initialize(IStore store, Class<C> configClass, Class<G> globalClass);

    /**
     * The start
     */
    void start();

    /**
     * The get store
     *
     * @return
     */
    ConfigCenterStore<C, G> getStore();

    /**
     * The query global config
     *
     * @return
     */
    G getGlobalConfig();

    /**
     * The query config
     *
     * @param identity
     * @return
     */
    C getConfig(Identity identity);

    /**
     * The add config to memory
     *
     * @param identity
     * @param config
     */
    void putConfig(Identity identity, C config);

    /**
     * The destroy all task
     */
    void destroy();

}
