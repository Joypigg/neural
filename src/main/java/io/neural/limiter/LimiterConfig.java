package io.neural.limiter;

import java.io.Serializable;

import io.neural.common.Identity;
import io.neural.common.Identity.Switch;
import io.neural.common.event.EventProcessor;
import lombok.*;

/**
 * The Limiter Config and GlobalConfig.
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LimiterConfig implements Serializable {

    private static final long serialVersionUID = -6531850175462577742L;

    /**
     * The limiter object
     */
    private Identity identity;
    /**
     * The limiter config
     */
    private Config config;

    /**
     * The Limiter Config
     *
     * @author lry
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class Config extends Identity.Config implements Serializable {

        private static final long serialVersionUID = -7452222651352158855L;

        /**
         * The rate of limiter
         */
        private Long rate = 0L;
        /**
         * The timeout of rate limiter
         */
        private Long rateTimeout = 0L;
        /**
         * The granularity of limiter, default is 1
         */
        private Long granularity = 1L;
        /**
         * The unit of limiter granularity, default is Unit.SEC
         */
        private Unit unit = Unit.SEC;

        /**
         * The concurrency of limiter
         */
        private Long concurrency = 0L;
        /**
         * The timeout of concurrency limiter
         */
        private Long concurrencyTimeout = 0L;

        /**
         * The strategy of limiter, default is Strategy.NON
         */
        private Strategy strategy = Strategy.NON;
    }

    /**
     * The Limiter Global Config.
     *
     * @author lry
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class GlobalConfig extends Identity.GlobalConfig {

        private static final long serialVersionUID = -4103412814609503453L;

        /**
         * The print warn log when limiter exceeds, default is Switch.ON
         */
        private Switch printExceedLog = Switch.ON;
    }

    /**
     * The Limiter Type.
     *
     * @author lry
     **/
    @Getter
    @AllArgsConstructor
    public enum EventType implements EventProcessor.EventType {

        /**
         * The rate exceed event of limiter
         */
        LIMITER_RATE_EXCEED("The rate exceed event of limiter"),
        /**
         * The concurrent exceed event of limiter
         */
        LIMITER_CONCURRENT_EXCEED("The concurrent exceed event of limiter"),
        /**
         * The notify config exception of limiter
         */
        LIMITER_NOTIFY_EXCEPTION("The notify config is exception of limiter"),
        /**
         * The collect statistics exception of limiter
         */
        LIMITER_COLLECT_EXCEPTION("The collect statistics is exception of limiter");

        String message;
    }

    /**
     * The Strategy of Limiter Overflow.
     *
     * @author lry
     */
    @Getter
    @AllArgsConstructor
    public enum Strategy {

        /**
         * The skip of limiter, when over flow
         */
        NON("The skip of limiter, when over flow"),
        /**
         * The fallback of limiter, when over flow
         */
        FALLBACK("The fallback of limiter, when over flow"),
        /**
         * The throw 'LimiterExcessException' exception of limiter, when over flow
         */
        EXCEPTION("The throw 'LimiterExcessException' exception of limiter, when over flow");

        String message;
    }

    /**
     * The unit of limiter granularity
     *
     * @author lry
     */
    @Getter
    @AllArgsConstructor
    public enum Unit {

        /**
         * The second of limiter granularity unit, abbreviation 'SEC'
         */
        SEC("The second of limiter granularity unit, abbreviation 'SEC'"),
        /**
         * The minute of limiter granularity unit, abbreviation 'MIN'
         */
        MIN("The minute of limiter granularity unit, abbreviation 'MIN'"),
        /**
         * The hour of limiter granularity unit, abbreviation 'HOU'
         */
        HOU("The hour of limiter granularity unit, abbreviation 'HOU'"),
        /**
         * The day of limiter granularity unit, abbreviation 'DAY'
         */
        DAY("The day of limiter granularity unit, abbreviation 'DAY'");

        String message;
    }

}
