package com.wanmi.sbc.message.configuration;

import com.wanmi.sbc.common.handler.SbcExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Liang Jun
 * @desc 公共实例初始化（临时）
 * @date 2022-01-08 18:32:00
 */
@Configuration
public class HandlerConfig {
    @Bean
    public SbcExceptionHandler exceptionHandler() {
        return new SbcExceptionHandler();
    }
}
