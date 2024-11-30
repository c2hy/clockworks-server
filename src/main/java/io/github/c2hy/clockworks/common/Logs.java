package io.github.c2hy.clockworks.common;

import org.slf4j.MDC;

public class Logs {
    private static final String CONTEXT_KEY = "logId";

    public static void trace(Object traceId) {
        MDC.put(CONTEXT_KEY, String.valueOf(traceId));
    }

    public static void trace(String traceId) {
        MDC.put(CONTEXT_KEY, traceId);
    }
}
