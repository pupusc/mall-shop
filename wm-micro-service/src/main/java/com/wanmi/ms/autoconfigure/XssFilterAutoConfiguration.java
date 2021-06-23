package com.wanmi.ms.autoconfigure;

import com.ofpay.rex.security.XssFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RequestLog自动配置类
 * Created by aqlu on 15/11/20.
 */
@Configuration
@EnableConfigurationProperties(XssFilterProperties.class)
@ConditionalOnClass(XssFilter.class)
public class XssFilterAutoConfiguration {

    private final XssFilterProperties xssFilterProperties;

    @Autowired
    public XssFilterAutoConfiguration(XssFilterProperties xssFilterProperties) {
        this.xssFilterProperties = xssFilterProperties;
    }

    @Bean
    @ConditionalOnProperty(prefix = "xss-filter", name = "enable", havingValue = "true")
    public FilterRegistrationBean xssFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();

        registrationBean.setFilter(new XssFilter());
        registrationBean.addInitParameter("excludeFieldsName", String.valueOf(xssFilterProperties.getExcludeFieldsName()));
        registrationBean.addInitParameter("paramNameSize", String.valueOf(xssFilterProperties.getParamNameSize()));
        registrationBean.addInitParameter("paramValueSize", String.valueOf(xssFilterProperties.getParamValueSize()));
        registrationBean.addInitParameter("RTFName", String.valueOf(xssFilterProperties.getRTFName()));
        registrationBean.addInitParameter("stripPath", String.valueOf(xssFilterProperties.isStripPath()));
        registrationBean.addInitParameter("stripJsonStream", String.valueOf(xssFilterProperties.isStripJsonStream()));
        registrationBean.addInitParameter("streamCharset", String.valueOf(xssFilterProperties.getStreamCharset()));

        registrationBean.addUrlPatterns(xssFilterProperties.getUrlPatterns());
        registrationBean.setOrder(xssFilterProperties.getOrder());

        return registrationBean;
    }
}
