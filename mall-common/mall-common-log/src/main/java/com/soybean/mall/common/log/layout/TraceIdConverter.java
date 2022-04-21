package com.soybean.mall.common.log.layout;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.slf4j.MDC;

/**
 * @author Liang Jun
 * @date 2022-04-21 13:04:00
 */
public class TraceIdConverter extends ClassicConverter {
    public static final String TRACE_ID = "trace_id";

    @Override
    public String convert(ILoggingEvent event) {
        return MDC.get(TRACE_ID);
    }
}
