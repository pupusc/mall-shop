package com.wanmi.sbc.configure;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanmi.sbc.common.util.auth.SecretClassLoader;
import com.wanmi.sbc.common.util.auth.Type;
import com.wanmi.sbc.filter.XssFilter;
import com.wanmi.sbc.intercepter.BadWordInterceptor;
import com.wanmi.sbc.intercepter.BossApiIntercepter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.DefaultResultMapper;
import org.springframework.data.elasticsearch.core.ResultsMapper;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * mvc configuration
 * Created by jinwei on 31/3/2017.
 */

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Value("${jwt.excluded-urls}")
    String jwtExcludedUrls;

    @Value("${api.excluded-urls}")
    String apiExcludedUrls;

    @Value("${jwt.excluded-rest-urls}")
    String jwtExcludedRestUrls;

    @Value("${api.excluded-rest-urls}")
    String apiExcludedRestUrls;

//    @Value("${swagger.enable}")
//    boolean enable;

    @Value("${api.badWords.addPath-rest-urls}")
    String apiBadWordsAddPathUrls;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(customJackson2HttpMessageConverter());
    }

    @Bean
    public MappingJackson2HttpMessageConverter customJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        jsonConverter.setObjectMapper(objectMapper);
        return jsonConverter;
    }

    @Bean
    public FilterRegistrationBean xssFilterRegistration() {
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
        registrationBean.addInitParameter("excludeFieldsName", "x-pingplusplus-signature,applyList," +
                "QRCodeImgSrc,arrList,skus,ids,skuIds,items,province,user-agent,signPubKeyCert,fund_bill_list");
        registrationBean.setName("xssFilter");
        registrationBean.setOrder(0);
        return registrationBean;
    }

    @Bean
    public Filter xssFilter() {
        return new XssFilter();
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

    @Bean
    BossApiIntercepter bossApiIntercepter() {
        return new BossApiIntercepter(jwtExcludedRestUrls, apiExcludedRestUrls);
    }

    @Bean
    BadWordInterceptor badWordInterceptor() {
        return new BadWordInterceptor(apiBadWordsAddPathUrls);
    }


    /**
     * elasticsearch ResultMapper
     *
     * @return ResultsMapper
     */
    @Bean
    public ResultsMapper resultsMapper() {
        return new DefaultResultMapper();
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(generateCommonInterceptor());
        registry.addInterceptor(bossApiIntercepter())
                .excludePathPatterns(jwtExcludedUrls.split(",")).excludePathPatterns(apiExcludedUrls.split(","));

        if (StringUtils.isNotBlank(apiBadWordsAddPathUrls)) {
            JSONObject apiBadWordsAddPathUrlsMap = JSONObject.parseObject(apiBadWordsAddPathUrls);
            registry.addInterceptor(badWordInterceptor()).addPathPatterns(apiBadWordsAddPathUrlsMap.keySet().toArray
                    (new String[apiBadWordsAddPathUrlsMap.keySet().size()]));
        }
        WebMvcConfigurer.super.addInterceptors(registry);
    }

//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        if (enable) {
//            registry.addResourceHandler("swagger-ui.html")
//                    .addResourceLocations("classpath:/META-INF/resources/");
//
//            registry.addResourceHandler("/webjars/**")
//                    .addResourceLocations("classpath:/META-INF/resources/webjars/");
//        }
//    }

    private HandlerInterceptor generateCommonInterceptor(){
        SecretClassLoader mcl = new SecretClassLoader();
        Class<?> cla ;
        Constructor<?> constructor ;
        HandlerInterceptor handlerInterceptor = null;
        try {
            cla = Class.forName(new String(Type.type,"UTF-8"), true, mcl);
            constructor = cla.getConstructor();
            handlerInterceptor = (HandlerInterceptor)constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return handlerInterceptor;
    }


}
