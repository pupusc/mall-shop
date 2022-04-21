package com.soybean.mall.common.log.layout;

import ch.qos.logback.classic.PatternLayout;

/**
 * @author Liang Jun
 * @date 2022-04-21 12:56:00
 */
public class MallPatternLayout extends PatternLayout {
    static {
        defaultConverterMap.put(TraceIdConverter.TRACE_ID, TraceIdConverter.class.getName());
    }
}
