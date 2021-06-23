package com.wanmi.sbc.message.bean.constant;

import java.util.HashMap;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-12-11
 * \* Time: 19:53
 * \* To change this template use File | Settings | File Templates.
 * \* Description:阿里云的返回码与平台自定义code之间的转化
 * \
 */
public class AliyunSmsResponseCodeMapping {

    public final static HashMap<String,String> CODE_MAPPING = new HashMap<String,String>() {
        {
            put("OK", SmsResponseCode.SUCCESS);
            put("isv.DAY_LIMIT_CONTROL", SmsResponseCode.DAY_LIMIT_CONTROL);
            put("isv.OUT_OF_SERVICE", SmsResponseCode.OUT_OF_SERVICE);
            put("isv.ACCOUNT_NOT_EXISTS", SmsResponseCode.ACCOUNT_NOT_EXISTS);
            put("isv.ACCOUNT_ABNORMAL", SmsResponseCode.ACCOUNT_ABNORMAL);
            put("isv.AMOUNT_NOT_ENOUGH", SmsResponseCode.AMOUNT_NOT_ENOUGH);
            put("isv.MOBILE_COUNT_OVER_LIMIT", SmsResponseCode.MOBILE_COUNT_OVER_LIMIT);
            put("isv.MOBILE_NUMBER_ILLEGAL", SmsResponseCode.MOBILE_NUMBER_ILLEGAL);
            put("isv.SMS_SIGNATURE_ILLEGAL", SmsResponseCode.SMS_SIGNATURE_ILLEGAL);
            put("isv.SMS_TEMPLATE_ILLEGAL", SmsResponseCode.SMS_TEMPLATE_ILLEGAL);

        }
    };
}
