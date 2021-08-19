package com.wanmi.sbc.customer.api.constant;

/**
 * 会员财务邮箱异常码定义
 */
public final class CustomerEmailErrorCode {
    private CustomerEmailErrorCode() {
    }

    /**
     * 邮箱数量已达到上限
     */
    public final static String CUSTOMER_EMAIL_ALREADY_MAX = "K-040101";

    /**
     * 该邮箱地址已存在
     */
    public final static String CUSTOMER_EMAIL_ADDRESS_EXIST = "K-040102";

    /**
     * 该邮箱地址不存在
     */
    public final static String CUSTOMER_EMAIL_NOT_EXIST = "K-040103";

}
