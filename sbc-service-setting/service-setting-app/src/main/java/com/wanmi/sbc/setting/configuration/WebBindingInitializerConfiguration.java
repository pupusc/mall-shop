package com.wanmi.sbc.setting.configuration;

import com.wanmi.sbc.setting.configuration.support.GlobalValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

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
}
