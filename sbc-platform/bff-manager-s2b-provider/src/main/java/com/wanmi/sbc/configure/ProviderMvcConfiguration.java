package com.wanmi.sbc.configure;

import com.wanmi.sbc.intercepter.ProviderApiInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 供应商 mvc configuration
 * Created by daiyitian on 31/3/2017.
 */
@RefreshScope
@Configuration
public class ProviderMvcConfiguration implements WebMvcConfigurer {

    @Value("${jwt.excluded-urls}")
    String jwtExcludedUrls;

    @Value("${api.excluded-urls}")
    String apiExcludedUrls;

    @Value("${jwt.excluded-rest-urls}")
    String jwtExcludedRestUrls;

    @Value("${api.excluded-rest-urls}")
    String apiExcludedRestUrls;

    @Bean
    ProviderApiInterceptor providerApiInterceptor() {
        return new ProviderApiInterceptor(jwtExcludedRestUrls, apiExcludedRestUrls);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(providerApiInterceptor())
                .excludePathPatterns(jwtExcludedUrls.split(",")).excludePathPatterns(apiExcludedUrls.split(","));
        WebMvcConfigurer.super.addInterceptors(registry);
    }

}
