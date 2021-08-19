package com.wanmi.ms.autoconfigure;

import com.wanmi.ms.log.aop.LoggingAspect;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 日志自动配置类
 */
@Configuration
@EnableAspectJAutoProxy
@EnableConfigurationProperties(LoggingAspectConfiguration.LoggingAspectProperties.class)
@ConditionalOnProperty(prefix = "wanmi.log.aspect", name="enable", havingValue = "true")
public class LoggingAspectConfiguration {

    @ConfigurationProperties(prefix = "wanmi.log.aspect")
    @Data
    class LoggingAspectProperties{
        /**
         * 是否启用日志拦截器；启用后，拦截器将自动记录所有com.wanmi..*Service与com.wanmi..*Repository类的进出方法日志；
         */
        private boolean enable;
    }

    @Bean
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}
