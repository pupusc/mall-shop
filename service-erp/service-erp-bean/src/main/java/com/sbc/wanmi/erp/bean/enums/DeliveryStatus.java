package com.sbc.wanmi.erp.bean.enums;

import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 商品状态 -1:发货失败
 * 0:未发货
 * 1:发货中
 * 2:发货成功
 * Created by wugongjiang on 2021/2/0.
 */
public enum DeliveryStatus {

    @ApiEnumProperty("未发货")
    UN_DELIVERY(0, "未发货"),

    @ApiEnumProperty("已发货")
    DELIVERY_COMPLETE(1, "已发货"),

    @ApiEnumProperty("部分发货")
    PART_DELIVERY(2, "部分发货"),

    @ApiEnumProperty("取消发货")
    CANCELED(3, "取消发货");

    private Integer key;

    private String value;

    DeliveryStatus( Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public Integer getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

}
