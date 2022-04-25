package com.soybean.mall.common.spring.filter;

import com.soybean.mall.common.spring.manager.TraceIdManager;
import org.hibernate.resource.jdbc.spi.StatementInspector;

/**
 * @author Liang Jun
 * @date 2022-04-25 00:14:00
 */
public class TraceSqlInterceptor implements StatementInspector {
    private static String TEMPLATE_NOTE = "/*traceId=%s*/\n%s";

    @Override
    public String inspect(String sql) {
        return String.format(TEMPLATE_NOTE, TraceIdManager.currTraceId(), sql);
    }
}
