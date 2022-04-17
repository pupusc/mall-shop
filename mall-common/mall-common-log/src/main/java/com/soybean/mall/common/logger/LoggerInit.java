package com.soybean.mall.common.logger;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class LoggerInit implements ApplicationListener<ContextRefreshedEvent> {

    private static AtomicBoolean started = new AtomicBoolean(false);

    public void init(ApplicationContext applicationContext) {
        NacosConfigService.initConfig(applicationContext);
        MallLoggerRob.initLevel();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (started.compareAndSet(false, true)) {
            init(event.getApplicationContext());
        }
    }
}