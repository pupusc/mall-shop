package com.sbc.wanmi.erp.bean.enums;

import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * ERP退单类型
 * 1：退货
 * 2：换货
 * 3：补发
 * 4：其他
 * 5：维护
 */
public enum ReturnTradeType {

    @ApiEnumProperty("退货")
    RETURN(1, "退货");

    private int code;

    private String reason;

    ReturnTradeType(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    public int getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }
}
