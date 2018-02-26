package io.neural.trace;

import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

/**
 * The Trace Buried Point
 *
 * @author echo
 */
public class Trace {

    public final static String TRACE_ID = "trace-id";

    public static void trace(String traceId) {
        Map<String, String> contextMap = new HashMap<>();
        // setter trace id
        contextMap.put(TRACE_ID, String.valueOf(TraceID.getTraceID(traceId)));
        
        MDC.setContextMap(contextMap);
    }


}
