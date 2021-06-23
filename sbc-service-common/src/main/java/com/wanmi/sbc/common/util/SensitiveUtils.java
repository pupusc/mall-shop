package com.wanmi.sbc.common.util;


import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * 处理敏感信息工具类
 * @author: Geek Wang
 * @createDate: 2019/2/26 14:33
 * @version: 1.0
 */
public class SensitiveUtils{

    /**
     * 判断手机号正则
     */
    private static Pattern pattern = Pattern.compile("^134[0-8]\\d{7}$|^13[^4]\\d{8}$|^14[5-9]\\d{8}$|^15[^4]\\d{8}$|^16[6]\\d{8}$|^17[0-8]\\d{8}$|^18[\\d]{9}$|^19[1,5,8,9]\\d{8}$");

    /**
     * 正则表达式：验证邮箱
     */
    public static String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * 判断是否为手机号
     * @param mobile    手机号码
     * @return
     */
    public static boolean isMobilePhone(String mobile) {
        if(StringUtils.isEmpty(mobile)){
            return false;
        }
        if (pattern.matcher(mobile).matches()) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(isMobilePhone("13512756510"));
    }

    /**
     * 处理手机号码
     * @param mobilePhone 手机号码
     * @return  返回处理后的手机号码,如:132****8856
     */
    public static String handlerMobilePhone(String mobilePhone) {
        if (StringUtils.isEmpty(mobilePhone)) {
            return mobilePhone;
        }
        if(isMobilePhone(mobilePhone)){
            mobilePhone = mobilePhone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        }
         return mobilePhone;
    }

    /**
     * 处理手机号码
     * @param mobilePhone 手机号码
     * @return  返回处理后的手机号码,如:*8856
     */
    public static String handlerMobilePhoneWithHideSeven(String mobilePhone) {
        if (StringUtils.isEmpty(mobilePhone)) {
            return mobilePhone;
        }
        if(isMobilePhone(mobilePhone)){
            mobilePhone = mobilePhone.replaceAll("\\d{7}(\\d{4})", "*$1");
        }
        return mobilePhone;
    }

    /**
     * 判断是否为邮箱
     * @param email    邮箱
     * @return
     */
    public static boolean isEmail(String email) {
        if(StringUtils.isEmpty(email)){
            return false;
        }
        Pattern pattern = Pattern.compile(REGEX_EMAIL);
        if (pattern.matcher(email).matches()) {
            return true;
        }
        return false;
    }
}


