package com.wanmi.ms.autoconfigure;

import com.wanmi.ms.log.RequestLogFilter;
import com.wanmi.ms.log.aop.RequestLogAspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * RequestLog自动配置类
 * Created by aqlu on 15/11/20.
 */
@Configuration
@ConditionalOnClass(Filter.class)
@EnableConfigurationProperties(RequestLogProperties.class)
public class RequestLogAutoConfiguration {

    @Autowired
    private RequestLogProperties requestLogProperties;

    @Bean
    @ConditionalOnProperty(prefix = "request.log", name = "enable", havingValue = "true")
    public FilterRegistrationBean requestLogFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new RequestLogFilter());
        registrationBean.addInitParameter("NEED_RESULT", String.valueOf(requestLogProperties.isNeedParam()));
        registrationBean.addInitParameter("NEED_PARAM", String.valueOf(requestLogProperties.isNeedParam()));
        registrationBean.addInitParameter("MAX_RESULT_LENGTH", String.valueOf(requestLogProperties.getMaxResultLength()));
        registrationBean.addInitParameter("MAX_BODY_LENGTH", String.valueOf(requestLogProperties.getMaxBodyLength()));
        registrationBean.addInitParameter("EXCLUDE_PATTERNS", requestLogProperties.getExcludePatterns());
        registrationBean.addInitParameter("EXCLUDE_METHODS", requestLogProperties.getExcludeMethods());
        registrationBean.addInitParameter("LOG_TYPE", String.valueOf(requestLogProperties.getLogType()));
        registrationBean.addInitParameter("EXCLUDE_HEADERS", requestLogProperties.getExcludeHeaders());
        registrationBean.addInitParameter("INCLUDE_HEADERS", requestLogProperties.getIncludeHeaders());
        registrationBean.addInitParameter("NEED_BLUR_PASSWORD_FOR_PARAM", String.valueOf(requestLogProperties.isNeedBlurPasswordForParam()));
        registrationBean.addInitParameter("PASSWORD_FIELD_REGEX", requestLogProperties.getPasswordFieldRegex());
        registrationBean.addInitParameter("SITE_OWNER_KEY", requestLogProperties.getSiteOwnerKey());
        registrationBean.addInitParameter("REQUEST_ID_KEY", requestLogProperties.getRequestIdKey());
        registrationBean.addInitParameter("SESSION_ID_KEY", requestLogProperties.getSessionIdKey());

        registrationBean.addUrlPatterns(requestLogProperties.getUrlPatterns());
        registrationBean.setOrder(requestLogProperties.getOrder());

        return registrationBean;
    }

    @Bean
    @ConditionalOnProperty(prefix = "request.log", name = "enable", havingValue = "true")
    public RequestLogAspect requestLogAspect(){
        return new RequestLogAspect();
    }
}
