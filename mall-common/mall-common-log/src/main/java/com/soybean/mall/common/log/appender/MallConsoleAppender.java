package com.soybean.mall.common.log.appender;

import ch.qos.logback.core.ConsoleAppender;
import com.soybean.mall.common.log.MallLoggerRob;

public class MallConsoleAppender<E> extends ConsoleAppender<E> {

    @Override
    protected void append(E eventObject) {
        if (MallLoggerRob.isOut(eventObject)) {
            super.append(eventObject);
        }
    }
}
