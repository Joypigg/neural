package io.neural.common.config;

import com.alibaba.fastjson.JSON;
import io.neural.common.Constants;
import io.neural.common.Identity;
import io.neural.common.Identity.Config;
import io.neural.common.Identity.GlobalConfig;
import io.neural.common.store.IStore;
import io.neural.common.store.SubscribeListener;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The Store Governor Config Center
 *
 * @author lry
 **/
@Getter
@AllArgsConstructor
public class ConfigCenterStore<C extends Config, G extends GlobalConfig> {

    private String module;
    private IStore store;
    private Class<C> configClass;
    private Class<G> globalClass;

    /**
     * The add(insert or update) GlobalConfig
     *
     * @param globalConfig
     */
    public void addGlobalConfig(G globalConfig) {
        Map<String, String> objectMap = Constants.buildMap(globalConfig);
        String globalConfigKey = Constants.buildGlobalConfigKey(module);
        store.add(globalConfigKey, objectMap);
    }

    /**
     * The query GlobalConfig
     *
     * @return
     */
    public G queryGlobalConfig() {
        String globalConfigKey = Constants.buildGlobalConfigKey(module);
        Map<String, String> remoteGlobalConfigMap = store.query(globalConfigKey);
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
        Map<String, String> localConfigMap = Constants.buildMap(config);
        store.add(Constants.buildConfigKey(module, identity), localConfigMap);
    }

    /**
     * The query Config by Identity
     *
     * @param identity
     * @return
     */
    public C queryConfig(Identity identity) {
        Map<String, String> remoteConfigMap = store.query(
                Constants.buildConfigKey(module, identity));
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
        String allConfigKey = Constants.buildAllConfigKey(module);
        Set<String> remoteConfigKeys = store.searchKeys(allConfigKey);
        Map<Identity, C> map = new HashMap<>(remoteConfigKeys.size());
        remoteConfigKeys.forEach(remoteConfigKey -> {
            Map<String, String> remoteConfigMap = store.query(remoteConfigKey);
            if (null == remoteConfigMap || remoteConfigMap.isEmpty()) {
                map.put(Constants.parseIdentity(remoteConfigKey), null);
            } else {
                Identity identity = Constants.parseIdentity(remoteConfigKey);
                C config = Constants.parseObject(configClass, remoteConfigMap);
                map.put(identity, config);
            }
        });

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
        String message = JSON.toJSONString(object);
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
        //getStore().publish(channel, message);
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

}
