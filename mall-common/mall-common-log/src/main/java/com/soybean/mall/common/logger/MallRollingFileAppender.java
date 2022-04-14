package com.soybean.mall.common.logger;

import ch.qos.logback.core.rolling.RollingFileAppender;

public class MallRollingFileAppender<E> extends RollingFileAppender<E> {

    @Override
    protected void append(E eventObject) {
        if (MallLoggerRob.isOut(eventObject)) {
            super.append(eventObject);
        }
    }
}
