package com.wanmi.sbc.configure;

import com.wanmi.sbc.intercepter.SupplierApiInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 商家 mvc configuration
 * Created by daiyitian on 31/3/2017.
 */
@RefreshScope
@Configuration
public class SupplierMvcConfiguration implements WebMvcConfigurer {

    @Value("${jwt.excluded-urls}")
    String jwtExcludedUrls;

    @Value("${api.excluded-urls}")
    String apiExcludedUrls;

    @Value("${jwt.excluded-rest-urls}")
    String jwtExcludedRestUrls;

    @Value("${api.excluded-rest-urls}")
    String apiExcludedRestUrls;

    @Bean
    SupplierApiInterceptor supplierApiInterceptor() {
        return new SupplierApiInterceptor(jwtExcludedRestUrls, apiExcludedRestUrls);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(supplierApiInterceptor())
                .excludePathPatterns(jwtExcludedUrls.split(",")).excludePathPatterns(apiExcludedUrls.split(","));
        WebMvcConfigurer.super.addInterceptors(registry);
    }

}
