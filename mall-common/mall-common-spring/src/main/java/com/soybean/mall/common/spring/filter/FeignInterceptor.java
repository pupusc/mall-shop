package com.soybean.mall.common.spring.filter;

import com.soybean.mall.common.spring.manager.TraceIdManager;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Liang Jun
 * @date 2022-04-20 23:25:00
 */
@Component
public class FeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        TraceIdManager.exitSpan(template);
    }
}
