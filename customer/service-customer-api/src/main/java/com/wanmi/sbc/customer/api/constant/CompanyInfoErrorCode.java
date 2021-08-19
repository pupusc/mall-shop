package com.wanmi.sbc.customer.api.constant;

/**
 * <p>商家异常码定义</p>
 * Created by of628-wenzhi on 2018-06-21-下午3:43.
 */
public final class CompanyInfoErrorCode {
    /**
     * 不存在
     */
    public final static String NOT_EXIST = "K-110101";

    /**
     * 名称已存在
     */
    public final static String NAME_ALREADY_EXISTS = "K-110102";

    /**
     * 统一社会授权代码重复
     */
    public final static String SOCIAL_CREDIT_REPEAT = "K-110104";


    private CompanyInfoErrorCode() {
    }
}
