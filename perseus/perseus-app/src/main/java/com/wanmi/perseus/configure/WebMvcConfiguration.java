package com.wanmi.perseus.configure;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanmi.ms.util.jackson.EmptyStringObjectMapper;
import com.wanmi.perseus.filter.XssFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * mvc configuration
 * Created by jinwei on 31/3/2017.
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

   /* @Autowired
    private PrincipalArgumentResolver principalArgumetnResolve;*/


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        WebMvcConfigurer.super.addArgumentResolvers(argumentResolvers);
//        argumentResolvers.add(principalArgumetnResolve);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters){
        converters.add(customJackson2HttpMessageConverter());
    }

    @Bean
    public MappingJackson2HttpMessageConverter customJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new EmptyStringObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        jsonConverter.setObjectMapper(objectMapper);
        return jsonConverter;
    }

    @Bean
    public FilterRegistrationBean xssFilterRegistration(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new CorsFilter(source));

        registrationBean.setFilter(xssFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.addInitParameter("excludeFieldsName", "applyList,QRCodeImgSrc,arrList,skus,ids,skuIds,items,province,user-agent");
        registrationBean.setName("xssFilter");
        registrationBean.setOrder(0);
        return registrationBean;
    }

    @Bean
    public Filter xssFilter(){
        return new XssFilter();
    }


    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor(){
        return new MethodValidationPostProcessor();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
            .addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("*")
            .allowedHeaders("*")
            .allowCredentials(true);

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /*registry.addInterceptor(bossApiIntercepter())
        .excludePathPatterns(excludeUrls.toArray(new String[0]));*/
        WebMvcConfigurer.super.addInterceptors(registry);
    }


}
