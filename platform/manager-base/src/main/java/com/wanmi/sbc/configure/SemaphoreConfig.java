package com.wanmi.sbc.configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Semaphore;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-12-5
 * \* Time: 17:27
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@Configuration
@EnableAsync
@EnableConfigurationProperties(SemaphoreProperties.class)
public class SemaphoreConfig {

    @Autowired
    private SemaphoreProperties semaphoreProperties;

    @Bean
    public Semaphore payCallbackSemaphore() {
        int permits = semaphoreProperties.getPermits() == 0 ? 8: semaphoreProperties.getPermits();
        return new Semaphore(permits, semaphoreProperties.isFair());
    }
}