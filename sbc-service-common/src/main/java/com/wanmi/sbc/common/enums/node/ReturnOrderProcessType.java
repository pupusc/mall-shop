package com.wanmi.sbc.common.enums.node;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * 退单进度通知
 */
@ApiEnum
public enum ReturnOrderProcessType {

    @ApiEnumProperty(" 0：售后单提交成功通知")
    AFTER_SALE_ORDER_COMMIT_SUCCESS("AFTER_SALE_ORDER_COMMIT_SUCCESS","售后单提交成功通知"),

    @ApiEnumProperty("1：售后审核通过通知")
    AFTER_SALE_ORDER_CHECK_PASS("AFTER_SALE_ORDER_CHECK_PASS","售后审核通过通知"),

    @ApiModelProperty("2: 售后审核未通过通知")
    AFTER_SALE_ORDER_CHECK_NOT_PASS("AFTER_SALE_ORDER_CHECK_NOT_PASS","售后审核未通过通知"),

    @ApiModelProperty("3: 退货物品拒收通知")
    RETURN_ORDER_GOODS_REJECT("RETURN_ORDER_GOODS_REJECT","退货物品拒收通知"),

    @ApiModelProperty("4: 退款审核通过通知")
    REFUND_CHECK_PASS("REFUND_CHECK_PASS","退款审核通过通知"),

    @ApiModelProperty("5: 退款审核未通过通知")
    REFUND_CHECK_NOT_PASS("REFUND_CHECK_NOT_PASS","退款审核未通过通知");

    private String type;

    private String description;

    ReturnOrderProcessType(String type, String description) {
        this.type = type;
        this.description = description;
    }

    @JsonCreator
    public ReturnOrderProcessType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
