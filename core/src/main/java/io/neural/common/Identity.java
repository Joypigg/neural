package io.neural.common;

import java.io.Serializable;

import io.neural.common.event.EventListener;
import lombok.*;

/**
 * The Identity of Config.
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Identity implements Serializable {

    private static final long serialVersionUID = 5564143662571971030L;

    /**
     * The application name or id
     **/
    private String application = "micro";
    /**
     * The group of service resource
     **/
    private String group = "neural";
    /**
     * The service or resource id
     **/
    private String resource;

    /**
     * The Config.
     *
     * @author lry
     **/
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Config implements Serializable {

        private static final long serialVersionUID = 1831995874813865374L;

        /**
         * The switch of, default is Switch.ON
         **/
        private Switch enable = Switch.ON;
        /**
         * The resource name
         **/
        private String name;
        /**
         * The remarks
         **/
        private String remarks;
    }

    /**
     * The Global Config.
     *
     * @author lry
     **/
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GlobalConfig implements Serializable {

        private static final long serialVersionUID = 6793855595768022387L;

        /**
         * The switch, default is Switch.ON
         **/
        protected Switch enable = Switch.ON;
        /**
         * The broadcast event, default is Switch.ON
         */
        protected Switch broadcastEvent = Switch.ON;

        /**
         * The cycle pull config(ms)
         */
        protected Long pullConfigCycle = 10 * 1000L;
        /**
         * The report cycle of monitor statistics(ms)
         */
        protected Long reportStatisticCycle = 10 * 1000L;
        /**
         * The statistic data expire(ms)
         */
        protected Long statisticDataExpire = 3 * 60 * 1000L;
    }

    /**
     * The Switch.
     *
     * @author lry
     */
    @Getter
    @AllArgsConstructor
    public enum Switch {

        /**
         * The switch is OFF
         */
        OFF("The switch is OFF"),
        /**
         * The switch is ON
         */
        ON("The switch is ON");

        String message;
    }

    /**
     * The Config Center Event Type.
     *
     * @author lry
     **/
    @Getter
    @AllArgsConstructor
    public enum EventType implements EventListener.EventType {

        /**
         * The pull global config is exception
         */
        PULL_GLOBAL_CONFIG_EXCEPTION("The pull global config is exception"),
        /**
         * The pull config is exception
         */
        PULL_CONFIG_EXCEPTION("The pull config is exception"),
        /**
         * The subscribe config or global config is exception
         */
        SUBSCRIBE_CONFIG_EXCEPTION("The subscribe config or global config is exception"),
        /**
         * The push statistics is exception
         */
        PUSH_STATISTICS_EXCEPTION("The subscribe config is exception");

        String message;
    }

}
