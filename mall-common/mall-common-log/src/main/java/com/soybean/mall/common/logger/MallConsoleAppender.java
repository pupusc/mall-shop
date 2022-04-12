package com.soybean.mall.common.logger;

import ch.qos.logback.core.ConsoleAppender;

/**
 * @author:troy
 * @date:15:03 2019-12-25
 * @email:kouhongyu@163.com
 */
public class MallConsoleAppender<E> extends ConsoleAppender<E> {

    @Override
    protected void append(E eventObject) {
        if (com.soybean.common.logger.appender.SoybeanLogRob.isOut(eventObject)) {
            super.append(eventObject);
        }
    }
}
