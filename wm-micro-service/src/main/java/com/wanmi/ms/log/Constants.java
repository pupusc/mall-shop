package com.wanmi.ms.log;

/**
 * 常量
 * Created by aqlu on 14-5-26.
 */
public final class Constants {

    /**
     * 短信服务器地址
     */
    public static final String SMS_PROVIDER_URL = System.getProperty("Sms.provider.url", "http://192.168.65.222:90/sms/sendSms.do").trim();

    /**
     * 生产环境
     */
    public static final String ENV_PRODUCTION = "product";

    /**
     * 需要告警的环境，默认为生产环境
     */
    public static final String ALARM_ENV = System.getProperty("Alarm.environment", ENV_PRODUCTION).trim();

    /**
     * 允许的每分钟最大失败次数，默认为5次；
     */
    public static final long ALLOWED_MAX_FAILED_COUNT = Long.parseLong(System.getProperty("Allowed.maxFailedCount", "5").trim());

    /**
     * 接口日志入参最大长度，超过该长度后会进行截取；默认为：2048
     */
    public static final int MAX_PARAM_VALUES_LENGTH = Integer.parseInt(System.getProperty("Intflog.maxInputParamLength", "2048").trim());

    /**
     * 接口日志出参最大长度，超过该长度后会进行截取；默认为：512
     */
    public static final int MAX_RESULT_VALUE_LENGTH = Integer.parseInt(System.getProperty("Intflog.maxOutputParamLength", "512").trim());

    /**
     * 截取后的字符串后缀
     */
    public static final String DEFAULT_SUFFIX = "......content truncated! real length is ";

    /**
     * 是否需要截取入参，默认false
     */
    public static final boolean NEED_SUB_INPUT_PARAMS = Boolean.parseBoolean(System.getProperty("Intflog.needSubInputParam", "true").trim());

    /**
     * 是否需要截取出参，默认true
     */
    public static final boolean NEED_SUB_OUTPUT_PARAMS = Boolean.parseBoolean(System.getProperty("Intflog.needSubOutputParam", "true").trim());

    /**
     * 截取参数时是否需要保留第一个数组元素，默认true
     */
    public static final boolean RESERVE_FIRST_ELEMENT_IN_ARRAY = Boolean.parseBoolean(System.getProperty("Intflog.reserveFirstElementInArray", "true").trim());

    /**
     * 是否开启logstash格式，默认false
     */
    public static final boolean OPEN_LOGSTASH_FORMAT = Boolean.parseBoolean(System.getProperty("Intflog.openLogstashFormat", "true").trim());

    /**
     * 是否总是记录consumer日志，默认false, 只记录consumer的异常日志; 为true时记录consumer的所有日志
     */
    public static final boolean ALWAYS_RECORD_CONSUMER_LOG = Boolean.parseBoolean(System.getProperty("Intflog.alwaysRecordConsumerLog", "false").trim());
}
