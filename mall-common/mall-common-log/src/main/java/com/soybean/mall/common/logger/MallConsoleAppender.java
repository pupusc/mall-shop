package com.soybean.mall.common.logger;

import ch.qos.logback.core.ConsoleAppender;

public class MallConsoleAppender<E> extends ConsoleAppender<E> {

    @Override
    protected void append(E eventObject) {
        if (MallLoggerRob.isOut(eventObject)) {
            super.append(eventObject);
        }
    }
}
