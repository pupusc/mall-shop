package com.soybean.mall.common.spring.manager;

import feign.RequestTemplate;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Liang Jun
 * @date 2022-04-20 18:50:00
 */
public class TraceIdManager {
    private static final String TRACE_ID = "trace_id";

    public static void entrySpan(HttpServletRequest request, HttpServletResponse response) {
        String traceId = request.getHeader(TRACE_ID);
        if (StringUtils.isEmpty(traceId)) {
            traceId = generate();
        }
        response.setHeader(TRACE_ID, traceId);
        MDC.put(TRACE_ID, traceId);
    }

    public static void exitSpan(RequestTemplate template) {
        if (Objects.nonNull(template)) {
            template.header(TRACE_ID, MDC.get(TRACE_ID));
        }
    }

    public static void finishSpan(HttpServletRequest request, HttpServletResponse response) {
        MDC.remove(TRACE_ID);
    }

    private static String generate() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
