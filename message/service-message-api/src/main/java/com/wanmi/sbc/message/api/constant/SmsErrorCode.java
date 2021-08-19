package com.wanmi.sbc.message.api.constant;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-12-4
 * \* Time: 19:48
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class SmsErrorCode {

    //系统报错
    public static final String UNABLE_BUSINESS_SETTING = "K-300101";
    public static final String NO_BUSINESS_SERVICE = "K-300102";

    //签名报错
    public static final String NO_SMS_SIGN = "K-300201";

    //选择签名
    public static final String USED_SMS_SIGN = "K-300205";

    //模板报错
    public static final String NO_SMS_TEMPLATE = "K-300301";
    public static final String SMS_TEMPLATE_NOT_EXISTS = "K-300305";

    //发送任务报错
    public static final String SEND_TIME_ERROR = "K-300401";
    public static final String SEND_TASK_NOT_MODIFY = "K-300402";
    public static final String SEND_TASK_NO_EXIST = "K-300403";

}
