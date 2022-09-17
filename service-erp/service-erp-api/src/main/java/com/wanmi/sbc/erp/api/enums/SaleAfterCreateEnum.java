package com.wanmi.sbc.erp.api.enums;

/**
 * 售后发起的业务方
 */
public enum SaleAfterCreateEnum {

    /**
     * 第三方平台,包含抖音、快手
     */
    THIRD_PLATFORM(1),
    /**
     * 商城，由用户端发起
     */
    MALL(2),
    /**
     * app主站，也是用户端发起
     */
    FD_APP(3),
    /**
     * cms后台，由业务人员发起
     */
    BACK_END(4),
    /**
     * 商城历史已完成数据
     */
    MALL_HISTORY(5);

    SaleAfterCreateEnum(int code) {
        this.code = code;
    }

    private int code;

    public int getCode() {
        return code;
    }
}
