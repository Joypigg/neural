package io.neural.common.store;

import io.neural.NURL;
import io.neural.extension.NPI;

import java.util.Map;
import java.util.Set;

/**
 * The Store With Config.
 *
 * @author lry
 **/
@NPI(single = true)
public interface IStore {

    /**
     * The start
     *
     * @param nurl
     */
    void start(NURL nurl);

    /**
     * The batch update or add
     *
     * @param expire
     * @param data
     */
    void batchUpOrAdd(long expire, Map<String, Long> data);

    /**
     * The add Map with key
     *
     * @param key
     * @param data
     */
    void add(String key, Map<String, String> data);

    /**
     * The search keys with keyword
     *
     * @param keyword
     * @return
     */
    Set<String> searchKeys(String keyword);

    /**
     * The query Map with key
     *
     * @param key
     * @return
     */
    Map<String, String> query(String key);

    /**
     * The publish
     *
     * @param channel
     * @param message
     */
    void publish(String channel, String message);

    /**
     * The subscribe by subscribeKey
     *
     * @param subscribeKeys
     * @param listener
     */
    void subscribe(String[] subscribeKeys, SubscribeListener listener);

    /**
     * The un subscribe by SubscribeListener
     *
     * @param listener
     */
    void unSubscribe(SubscribeListener listener);

    /**
     * The destroy
     */
    void destroy();

}
