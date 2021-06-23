package com.wanmi.sbc.customer.api.constant;

/**
 * <p>会员级别异常码定义</p>
 * Created by of628-wenzhi on 2018-06-21-下午3:51.
 */
public final class CustomerLevelErrorCode {
    private CustomerLevelErrorCode() {
    }

    /**
     * 会员等级不存在
     */
    public final static String NOT_EXIST = "K-010006";

    /**
     * 默认等级不允许编辑
     */
    public final static String NOT_EDIT_DEFAULT = "K-010007";

    /**
     * 默认等级不允许编辑
     */
    public final static String NOT_DELETE_DEFAULT = "K-010008";
}
