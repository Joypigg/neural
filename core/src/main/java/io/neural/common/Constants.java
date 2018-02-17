package io.neural.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.util.Map;

/**
 * The Constants.
 *
 * @author lry
 **/
public final class Constants {

    public static final String SEPARATOR = ",";
    public static final String APP_NAME_KEY = "app.name";
    public static final String SEQ = "-";
    public static final String DELIMITER = ":";
    public static final String SYMBOL = "%s";
    public static final String MATCHING_ALL = "*";

    public static final String STATISTICS = "statistics";
    public static final String CONFIG = "config";
    public static final String GLOBAL_CONFIG = "global_config";
    public static final String SUBSCRIBE_CONFIG_CHANNEL = "subscribe_config";
    public static final String SUBSCRIBE_GLOBAL_CONFIG_CHANNEL = "subscribe_global_config";

    public static final String PULL_CONFIG = "pull-config";
    public static final String SUBSCRIBE_CONFIG = "subscribe-config";
    public static final String PUSH_STATISTICS = "push-statistics";

    public static final String CONFIG_KEY = String.join(DELIMITER, CONFIG, SYMBOL, SYMBOL, SYMBOL);
    public static final String CONFIG_ALL_KEY = String.join(DELIMITER, CONFIG, MATCHING_ALL);
    public static final String CONFIG_APP_ALL_KEY = String.join(DELIMITER, CONFIG, SYMBOL, MATCHING_ALL);
    public static final String SUBSCRIBE_CONFIG_CHANNEL_KEY = String.join(DELIMITER, SUBSCRIBE_CONFIG_CHANNEL, SYMBOL, SYMBOL, SYMBOL);
    public static final String IDENTITY = String.join(DELIMITER, SYMBOL, SYMBOL, SYMBOL, SYMBOL);

    public static final String TOTAL_REQUEST = String.join(DELIMITER, STATISTICS, "request", IDENTITY);
    public static final String TOTAL_SUCCESS = String.join(DELIMITER, STATISTICS, "success", IDENTITY);
    public static final String TOTAL_FAILURE = String.join(DELIMITER, STATISTICS, "failure", IDENTITY);
    public static final String TOTAL_TIMEOUT = String.join(DELIMITER, STATISTICS, "timeout", IDENTITY);
    public static final String TOTAL_REJECTION = String.join(DELIMITER, STATISTICS, "rejection", IDENTITY);
    public static final String TOTAL_ELAPSED = String.join(DELIMITER, STATISTICS, "elapsed", IDENTITY);
    public static final String MAX_ELAPSED = String.join(DELIMITER, STATISTICS, "max-elapsed", IDENTITY);
    public static final String TOTAL_CONCURRENCY = String.join(DELIMITER, STATISTICS, "concurrency", IDENTITY);
    public static final String MAX_CONCURRENCY = String.join(DELIMITER, STATISTICS, "max-concurrency", IDENTITY);
    public static final String TOTAL_C_EXCEED = String.join(DELIMITER, STATISTICS, "c-exceed", IDENTITY);
    public static final String TOTAL_R_EXCEED = String.join(DELIMITER, STATISTICS, "r-exceed", IDENTITY);
    public static final String TOTAL_DEGRADE = String.join(DELIMITER, STATISTICS, "degrade", IDENTITY);


    /**
     * The build global config key
     *
     * @param module
     * @return
     */
    public static String buildGlobalConfigKey(String module) {
        return String.join(DELIMITER, module, GLOBAL_CONFIG);
    }

    /**
     * The build matching all config key
     *
     * @param module
     * @return
     */
    public static String buildAllConfigKey(String module) {
        return String.join(DELIMITER, module, CONFIG_ALL_KEY);
    }


    /**
     * The build Config key by Identity
     *
     * @param id
     * @return
     */
    public static String buildConfigKey(String module, Identity id) {
        return String.format(String.join(DELIMITER, module, CONFIG_KEY),
                id.getApplication(), id.getGroup(), id.getResource());
    }

    /**
     * The parse Identity by config key
     *
     * @param configKey
     * @return
     */
    public static Identity parseIdentity(String configKey) {
        if (null == configKey) {
            return null;
        }

        String[] keyArray = configKey.split(Constants.DELIMITER);
        if (null == keyArray || keyArray.length != 5) {
            return null;
        }

        return new Identity(keyArray[2], keyArray[3], keyArray[4]);
    }

    /**
     * The parse Config or GlobalConfig
     *
     * @param map
     * @return
     */
    public static <T> T parseObject(Class<T> clz, Map<String, String> map) {
        return JSONObject.parseObject(JSON.toJSONString(map), clz);
    }

    public static <T> T parseObject(Class<T> clz, String json) {
        return JSONObject.parseObject(json, clz);
    }

    /**
     * The build JSON string
     *
     * @param object
     * @return
     */
    public static String buildJSONString(Object object) {
        return JSONObject.toJSONString(object);
    }

    /**
     * The build Map
     *
     * @param object
     * @return
     */
    public static Map<String, String> buildMap(Object object) {
        return JSONObject.parseObject(JSON.toJSONString(object), new TypeReference<Map<String, String>>() {
        });
    }

}
