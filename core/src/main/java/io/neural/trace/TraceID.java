package io.neural.trace;

import io.neural.micro.NUUID;
import io.neural.micro.Snowflake;

/**
 * The Trace ID
 *
 * @author lry
 */
public enum TraceID {

    /**
     * The Generating 15 bit UUID
     */
    UUID15 {
        public Object getId() {
            return NUUID.randomUUID15();
        }
    },

    /**
     * The Generating 15 bit Long UUID
     */
    UUID15_LONG {
        public Object getId() {
            return NUUID.randomUUID15Long();
        }
    },

    /**
     * The Generating 19 bit UUID
     */
    UUID19 {
        public Object getId() {
            return NUUID.randomUUID19();
        }
    },

    /**
     * The Generating 32 bit UUID
     */
    UUID32 {
        public Object getId() {
            return NUUID.randomUUID32();
        }
    },

    /**
     * The Generating 36 bit UUID
     */
    UUID {
        public Object getId() {
            return NUUID.randomUUID();
        }
    },

    /**
     * The Generating 19 bit Long Snowflake ID
     */
    SNOWFLAKE {
        Snowflake snowflake = new Snowflake();

        public Object getId() {
            return snowflake.nextId();
        }
    };

    /**
     * The Get Trace ID
     *
     * @param traceId
     * @return
     */
    public static TraceID getTraceID(String traceId) {
        if (traceId == null) {
            return TraceID.UUID;
        }

        try {
            return TraceID.valueOf(traceId);
        } catch (Exception e) {
            return TraceID.UUID;
        }
    }

}