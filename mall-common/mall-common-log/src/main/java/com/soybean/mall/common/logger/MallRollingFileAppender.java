package com.soybean.mall.common.logger;

import ch.qos.logback.core.rolling.RollingFileAppender;

/**
 * @author:troy
 * @date:13:21 2019-12-26
 * @email:kouhongyu@163.com
 */
public class MallRollingFileAppender<E> extends RollingFileAppender<E> {

    @Override
    protected void append(E eventObject) {
        if (com.soybean.common.logger.appender.SoybeanLogRob.isOut(eventObject)) {
            super.append(eventObject);
        }
    }
}
