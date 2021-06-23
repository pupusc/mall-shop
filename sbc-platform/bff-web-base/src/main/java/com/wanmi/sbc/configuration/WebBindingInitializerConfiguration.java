package com.wanmi.sbc.configuration;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.wanmi.sbc.configuration.support.GlobalValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.Properties;

/**
 * <p>
 *      提供一个带有“全局”初始化的WebBindingInitializer，主要配置了一个全局的Validator
 *      适用于每个DataBinder实例。
 * </p>
 * Created by of628-wenzhi on 2017-07-18-下午4:18.
 */
@Configuration
public class WebBindingInitializerConfiguration {

    @Autowired
    public void setWebBindingInitializer(RequestMappingHandlerAdapter requestMappingHandlerAdapter, WebMvcConfigurationSupport mvcConfigurationSupport) {
        requestMappingHandlerAdapter.setWebBindingInitializer(getConfigurableWebBindingInitializer(mvcConfigurationSupport));
    }

    private ConfigurableWebBindingInitializer getConfigurableWebBindingInitializer(WebMvcConfigurationSupport mvcConfigurationSupport) {
        CustomWebBindingInitializer initializer = new CustomWebBindingInitializer();
        initializer.setConversionService(mvcConfigurationSupport.mvcConversionService());
        initializer.setValidator(mvcConfigurationSupport.mvcValidator());
        return initializer;
    }

    class CustomWebBindingInitializer extends ConfigurableWebBindingInitializer {
        GlobalValidator globalValidator = new GlobalValidator();

        @Override
        public void initBinder(WebDataBinder binder) {
            super.initBinder(binder);
            if (binder.getTarget() != null && globalValidator.supports(binder.getTarget().getClass())) {
                binder.addValidators(globalValidator);
            }

        }
    }

    @Bean
    public DefaultKaptcha captchaProducer(){
        DefaultKaptcha captchaProducer =new DefaultKaptcha();
        Properties properties =new Properties();
        properties.setProperty("kaptcha.border","no");
//        properties.setProperty("kaptcha.border.color","105,179,90");
        properties.setProperty("kaptcha.textproducer.font.color","blue");
        properties.setProperty("kaptcha.image.width","125");
        properties.setProperty("kaptcha.image.height","45");
        properties.setProperty("kaptcha.textproducer.font.size","42");
        properties.setProperty("kaptcha.textproducer.char.length","4");
        properties.setProperty("kaptcha.textproducer.font.names","Arial");
        captchaProducer.setConfig(new Config(properties));
        return  captchaProducer;
    }
}
