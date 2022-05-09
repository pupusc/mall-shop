package com.soybean.mall.common.spring.filter;

import com.soybean.mall.common.spring.manager.TraceIdManager;
import org.hibernate.resource.jdbc.spi.StatementInspector;

import java.util.Objects;

/**
 * @author Liang Jun
 * @date 2022-04-25 00:14:00
 */
public class TraceSqlInterceptor implements StatementInspector {
    private static String TEMPLATE_NOTE = "/*traceId=%s*/\n%s";
    private static final String EMPTY_TRACE_ID = "N/A";

    @Override
    public String inspect(String sql) {
        return String.format(TEMPLATE_NOTE, (Objects.isNull(TraceIdManager.currTraceId()) ? EMPTY_TRACE_ID : TraceIdManager.currTraceId()), sql);
    }
}
