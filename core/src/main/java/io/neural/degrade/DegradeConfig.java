package io.neural.degrade;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import io.neural.common.Constants;
import io.neural.common.Identity;
import io.neural.common.event.EventListener;
import lombok.*;

/**
 * The Degrade Config/GlobalConfig
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DegradeConfig implements Serializable {

    private static final long serialVersionUID = -5445833020633975979L;

    /**
     * The degrade object
     */
    private Identity identity;
    /**
     * The degrade config
     */
    private Config config;

    /**
     * The Degrade Config
     *
     * @author lry
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class Config extends Identity.Config implements Serializable {

        private static final long serialVersionUID = -3446195511245955156L;

        /**
         * The degrade level, default is Level.NEED
         */
        private Level level = Level.NEED;
        /**
         * The degrade strategy, default is Strategy.FALLBACK
         */
        private Strategy strategy = Strategy.FALLBACK;
        /**
         * The mock type of degrade strategy
         */
        private Mock mock = Mock.NULL;
        /**
         * The mock clazz of degrade strategy
         */
        private String clazz;
        /**
         * The mock data of degrade strategy
         */
        private String data;
    }

    /**
     * The Degrade Global Config
     *
     * @author lry
     **/
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class GlobalConfig extends Identity.GlobalConfig implements Serializable {

        private static final long serialVersionUID = 3023172841450527624L;

        /**
         * The degrade level
         */
        private Level level = Level.NON;
    }

    /**
     * The degrade level
     *
     * @author lry
     */
    @Getter
    @AllArgsConstructor
    public enum Level {

        /**
         * The no open downgrade, is non
         */
        NON(0, "The no open downgrade, is non"),
        /**
         * The may need to downgrade, is hint
         */
        HINT(1, "The may need to downgrade, is hint"),
        /**
         * The recommended to downgrade
         */
        RECOMMEND(2, "The recommended to downgrade"),
        /**
         * The need to downgrade
         */
        NEED(3, "The need to downgrade"),
        /**
         * The must to downgrade, is warning
         */
        WARN(4, "The must to downgrade, is warning"),
        /**
         * The must to downgrade, is serious
         */
        SERIOUS(5, "The must to downgrade, is serious");

        Integer order;
        String message;
    }

    /**
     * The degrade strategy
     *
     * @author lry
     */
    @Getter
    @AllArgsConstructor
    public enum Strategy {

        /**
         * The skip request
         */
        NON("The skip of degrade"),
        /**
         * The return mock data
         */
        MOCK("The return mock data of degrade"),
        /**
         * The fallback process
         */
        FALLBACK("The fallback process of degrade");

        String message;
    }

    /**
     * The Degrade Event Type.
     *
     * @author lry
     **/
    @Getter
    @AllArgsConstructor
    public enum EventType implements EventListener.EventType {

        /**
         * The notification mock data exception of degrade
         */
        DEGRADE_NOTIFY_EXCEPTION("The notify config is exception of degrade"),
        /**
         * The collect statistics exception of degrade
         */
        DEGRADE_COLLECT_EXCEPTION("The collect statistics is exception of degrade");

        String message;
    }

    @Getter
    @AllArgsConstructor
    public enum Mock {

        /**
         * The return null type data
         */
        NULL("The return null type data"),
        /**
         * The return String type data
         */
        STRING("The return String type data"),
        /**
         * The return Integer type data
         */
        INTEGER("The return Integer type data"),
        /**
         * The return Float type data
         */
        FLOAT("The return Float type data"),
        /**
         * The return Double type data
         */
        DOUBLE("The return Double type data"),
        /**
         * The return Long type data
         */
        LONG("The return Long type data"),
        /**
         * The return Boolean type data
         */
        BOOLEAN("The return Boolean type data"),
        /**
         * The return T type data with Class
         */
        CLASS("The return T type data with Class"),
        /**
         * The return Array_String type data
         */
        ARRAY("The return Array_String type data"),
        /**
         * The return Map<Object,Object> type data
         */
        MAP("The return Map<Object,Object> type data"),
        /**
         * The return Map<String,String> type data
         */
        MAP_STR("The return Map<String,String> type data"),
        /**
         * The return Map<String,Object> type data
         */
        MAP_OBJ("The return Map<String,Object> type data"),
        /**
         * The return List<Object> type data
         */
        LIST("The return List<Object> type data"),
        /**
         * The return List<String> type data
         */
        LIST_STR("The return List<String> type data"),
        /**
         * The return List<T> type data with Class
         */
        LIST_CLASS("The return List<T> type data with Class");

        String message;
    }

    /**
     * The get mock data
     *
     * @param mock
     * @param clazz
     * @param data
     * @return
     */
    public static Object mockData(Mock mock, String clazz, String data) throws Exception {
        switch (mock) {
            case NULL:
                return null;
            case STRING:
                return String.valueOf(data);
            case INTEGER:
                return Integer.valueOf(data);
            case FLOAT:
                return Float.valueOf(data);
            case DOUBLE:
                return Double.valueOf(data);
            case LONG:
                return Long.valueOf(data);
            case BOOLEAN:
                return Boolean.valueOf(data);
            case ARRAY:
                return data.split(Constants.SEPARATOR);
            case CLASS:
                return JSON.parseObject(data, Class.forName(clazz));
            case MAP:
                return JSONObject.parseObject(data, Map.class);
            case MAP_STR:
                return JSONObject.parseObject(data, new TypeReference<Map<String, String>>() {
                });
            case MAP_OBJ:
                return JSONObject.parseObject(data, new TypeReference<Map<String, Object>>() {
                });
            case LIST:
                return JSON.parseArray(data, List.class);
            case LIST_STR:
                return JSON.parseObject(data, TypeReference.LIST_STRING);
            case LIST_CLASS:
                return JSON.parseArray(data, Class.forName(clazz));
            default:
                throw new IllegalArgumentException("The illegal mock: " + mock);
        }
    }

}
