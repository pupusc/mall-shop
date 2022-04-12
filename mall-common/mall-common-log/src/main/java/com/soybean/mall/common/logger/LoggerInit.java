package com.soybean.mall.common.logger;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class LoggerInit implements ApplicationListener<ContextStartedEvent> {

    private static AtomicBoolean started = new AtomicBoolean(false);

    public void init() {
        MallLoggerRob.initLevel();
    }

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        if (started.compareAndSet(false, true)) {
            init();
        }
    }
}
