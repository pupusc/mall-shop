package com.wanmi.sbc.configure;

import brave.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * \* Author: zgl
 * \* Date: 2020-2-7
 * \* Time: 15:20
 * \* Description:
 * \
 */
//@ControllerAdvice
public class HeaderModifierAdvice implements ResponseBodyAdvice<Object> {

    @Autowired
    Tracer tracer;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                                  ServerHttpResponse response) {
        response.getHeaders().add("TraceId",tracer.currentSpan().context().traceIdString());
        return body;
    }
}
