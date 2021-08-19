package com.wanmi.sbc.message.constant;

import java.util.HashMap;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-12-4
 * \* Time: 13:53
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class SmsBusinessConstant {

    /**
     * 该类型用于动态的切换短信运行商
     * 跟数据库的type字段相对应，对应着实现类的service的beanName
     */
    public static final HashMap<String,String> BUSINESS_MAPPING = new HashMap<String,String>(){
        {
            put("0","aliyunSmsService");
            put("1","huaxinSmsService");
        }
    };
}
