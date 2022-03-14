package com.wanmi.ms.autoconfigure;

import com.wanmi.ms.jwt.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * JWT认证自动配置类
 * Created by aqlu on 15/11/20.
 */
@Configuration
@EnableConfigurationProperties(JwtProperties.class)
@ConditionalOnProperty(prefix = "jwt", name = "secret-key")
@Order(Ordered.LOWEST_PRECEDENCE - 100) // 优先级空100个位置出来, 满足业务在JWT认证之后添加拦截器处理
public class JwtAutoConfiguration implements WebMvcConfigurer {

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
        String secretKey = jwtProperties.getSecretKey();
        String jwtHeaderKey = jwtProperties.getJwtHeaderKey();
        String jwtHeaderPrefix = jwtProperties.getJwtHeaderPrefix();
        String excludedRestUrls = jwtProperties.getExcludedRestUrls();

        registry.addWebRequestInterceptor(new JwtInterceptor(secretKey, jwtHeaderKey, jwtHeaderPrefix,
                excludedRestUrls))
                .addPathPatterns(jwtProperties.getUrlPatterns())
                .excludePathPatterns(jwtProperties.getExcludedUrls());
    }

 /*   @Bean
    public FilterRegistrationBean jwtFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new JwtFilter());
        registrationBean.addInitParameter(JwtFilter.JWT_SECRET_KEY, jwtProperties.getSecretKey());
        registrationBean.addInitParameter(JwtFilter.EXCLUDED_URLS_KEY, join(jwtProperties.getExcludedURLs(),
        JwtFilter.SEPARATOR));
        registrationBean.addUrlPatterns(jwtProperties.getUrlPatterns());
        registrationBean.setOrder(jwtProperties.getOrder());

        return registrationBean;
    }

    public static String join(final String[] target, final String separator) {
        if (target == null) {
            return "";
        }

        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < target.length; i++) {
            sb.append(target[i]);
            if (i < target.length - 1) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }
    */
}
