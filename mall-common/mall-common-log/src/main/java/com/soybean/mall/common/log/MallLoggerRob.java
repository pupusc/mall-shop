package com.soybean.mall.common.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;

public final class MallLoggerRob {
    private static final String LOG_FILTER = "log.filter";
    private static final String LOG_LEVEL = "log.level";
    private static final String ROOT_LOGGER = "root";

    private static final String FORMAT_LOCATION_CLASS = "%s";
    private static final String FORMAT_LOCATION_CLASS_LINE = "%s:%s";
    private static final String FORMAT_LOCATION_CLASS_LEVEL = "%s:%s";

    private static final String FORMAT_LOCATION_METHOD = "%s.%s";
    private static final String FORMAT_LOCATION_METHOD_LINE = "%s.%s:%s";
    private static final String FORMAT_LOCATION_METHOD_LEVEL = "%s.%s:%s";
    private static final String INNER_CLASS_SYMBOL = "$";

    private static NacosConfigService configService;

    private MallLoggerRob() {
    }

    public static void initLevel() {
        try {
            configService = NacosConfigService.getInstance();
            if (setLevel()) {
                configService.addChangeListener(LOG_LEVEL, () -> setLevel());
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * 全局日志输出级别，默认INFO
     * 规则：level = INFO
     * 示例：log.level = DEBUG
     */
    private static boolean setLevel() {
        ILoggerFactory loggerFactory = LoggerFactory.getILoggerFactory();

        if (loggerFactory instanceof LoggerContext) {
            LoggerContext loggerContext = (LoggerContext) loggerFactory;

            String configLevelStr = configService.getStringProperty(LOG_LEVEL, Level.INFO.levelStr);
            Level configLevel = Level.toLevel(configLevelStr);

            // 设置全局日志级别
            ch.qos.logback.classic.Logger logger = loggerContext.getLogger(ROOT_LOGGER);
            logger.setLevel(configLevel);

            return true;
        }
        return false;
    }

    public static boolean isOut(Object eventObject) {
        try {
            if (eventObject instanceof ILoggingEvent) {
                ILoggingEvent loggingEvent = (ILoggingEvent) eventObject;

                StackTraceElement[] stackTraceElements = loggingEvent.getCallerData();
                if (stackTraceElements != null && stackTraceElements.length > 0) {
                    StackTraceElement element = stackTraceElements[0];

                    /**
                     * 处理内部类，lambda表达式等特殊情况
                     */
                    String className = element.getClassName();
                    if (StringUtils.contains(className, INNER_CLASS_SYMBOL)) {
                        className = StringUtils.substringBefore(className, INNER_CLASS_SYMBOL);
                    }

                    String methodName = element.getMethodName();
                    if (StringUtils.contains(methodName, INNER_CLASS_SYMBOL)) {
                        methodName = StringUtils.substringBefore(methodName, INNER_CLASS_SYMBOL);
                    }

                    /**
                     * 类日志开关
                     * 规则：class = boolean
                     * 示例：org.soybean.Application = false  //关闭类日志
                     */
                    String keyClass = String.format(FORMAT_LOCATION_CLASS, className);
                    boolean isOutClass = configService.getBooleanProperty(keyClass, Boolean.TRUE);
                    if (!isOutClass) {
                        return false;
                    }
                    /**
                     * 类中指定行的日志开关
                     * 规则：class:line = boolean
                     * 示例：org.soybean.Application:16 = false  //关闭Application类第16行日志
                     */
                    String keyClassLine = String.format(FORMAT_LOCATION_CLASS_LINE, className, element.getLineNumber());
                    boolean isOutClassLine = configService.getBooleanProperty(keyClassLine, Boolean.TRUE);
                    if (!isOutClassLine) {
                        return false;
                    }
                    /**
                     * 类中指定级别的日志开关
                     * 规则：class:level = boolean
                     * 示例：org.soybean.Application:INFO = false  //关闭Application类中所有INFO级别的日志
                     */
                    String keyClassLevel = String.format(FORMAT_LOCATION_CLASS_LEVEL, className, loggingEvent.getLevel().levelStr);
                    boolean isOutClassLevel = configService.getBooleanProperty(keyClassLevel, Boolean.TRUE);
                    if (!isOutClassLevel) {
                        return false;
                    }
                    /**
                     * 类中指方法的日志开关
                     * 规则：class.method = boolean
                     * 示例：com.soybean.common.utils.exception.GlobalExceptionHandle.Exception = false  //关闭GlobalExceptionHandle类Exception方法的日志
                     */
                    String keyMethod = String.format(FORMAT_LOCATION_METHOD, className, methodName);
                    boolean isOutMethod = configService.getBooleanProperty(keyMethod, Boolean.TRUE);
                    if (!isOutMethod) {
                        return false;
                    }
                    /**
                     * 类中指方法指定行的日志开关
                     * 规则：class.method:line = boolean
                     * 示例：com.soybean.common.utils.exception.GlobalExceptionHandle.Exception:73 = false  //关闭GlobalExceptionHandle类Exception方法中第73行的日志
                     */
                    String keyMethodLine = String.format(FORMAT_LOCATION_METHOD_LINE, className, methodName, element.getLineNumber());
                    boolean isOutMethodLine = configService.getBooleanProperty(keyMethodLine, Boolean.TRUE);
                    if (!isOutMethodLine) {
                        return false;
                    }
                    /**
                     * 类中指方法指定级别的日志开关
                     * 规则：class.method:level = boolean
                     * 示例：com.soybean.kafka.consumer.BehaviorConsumerMessageProcessor.onMessage:INFO = false //关闭BehaviorConsumerMessageProcessor类onMessage方法中所有INFO级别的日志
                     */
                    String keyMethodLevel = String.format(FORMAT_LOCATION_METHOD_LEVEL, className, methodName, loggingEvent.getLevel().levelStr);
                    boolean isOutMethodLevel = configService.getBooleanProperty(keyMethodLevel, Boolean.TRUE);
                    if (!isOutMethodLevel) {
                        return false;
                    }
                }
            }
        } catch (Throwable e) {
            return true;
        }
        return true;
    }
}
