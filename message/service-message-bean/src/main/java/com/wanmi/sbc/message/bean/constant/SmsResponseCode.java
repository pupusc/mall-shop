package com.wanmi.sbc.message.bean.constant;

import com.wanmi.sbc.message.bean.enums.ResendType;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-12-11
 * \* Time: 19:23
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \ sms平台自定义code用于统一管理，与其他短信平台之间做转化
 */
public class SmsResponseCode {

    //成功
    public final static String SUCCESS = "SUCCESS";

    //默认错误码（返回为空或找不到对应的code）
    public final static String DEFAULT_ERROR = "DEFAULT_ERROR";

    //配置不存在
    public final static String DEFAULT_CONFIG_ERROR = "DEFAULT_CONFIG_ERROR";

    //触发日发送限额
    public final static String DAY_LIMIT_CONTROL = "DAY_LIMIT_CONTROL";


    //业务停机
    public final static String OUT_OF_SERVICE = "OUT_OF_SERVICE";

    //账户不存在
    public final static String ACCOUNT_NOT_EXISTS = "ACCOUNT_NOT_EXISTS";

    //账户异常
    public final static String ACCOUNT_ABNORMAL = "ACCOUNT_ABNORMAL";

    //账户余额不足
    public final static String AMOUNT_NOT_ENOUGH = "AMOUNT_NOT_ENOUGH";

    //手机号码数量超过限制
    public final static String MOBILE_COUNT_OVER_LIMIT = "MOBILE_COUNT_OVER_LIMIT";

    //非法手机号
    public final static String MOBILE_NUMBER_ILLEGAL = "MOBILE_NUMBER_ILLEGAL";

    public final static String SMS_SIGNATURE_ILLEGAL = "SMS_SIGNATURE_ILLEGAL";

    public final static String SMS_TEMPLATE_ILLEGAL = "SMS_TEMPLATE_ILLEGAL";

    public static ResendType resendType(String code){
        if(code.equals(OUT_OF_SERVICE)||code.equals(AMOUNT_NOT_ENOUGH)){
            return ResendType.YES;
        }
        return ResendType.NO;
    }

}
