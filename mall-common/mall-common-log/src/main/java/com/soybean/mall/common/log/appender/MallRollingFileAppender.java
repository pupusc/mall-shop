package com.soybean.mall.common.log.appender;

import ch.qos.logback.core.rolling.RollingFileAppender;
import com.soybean.mall.common.log.MallLoggerRob;

public class MallRollingFileAppender<E> extends RollingFileAppender<E> {

    @Override
    protected void append(E eventObject) {
        if (MallLoggerRob.isOut(eventObject)) {
            super.append(eventObject);
        }
    }
}
