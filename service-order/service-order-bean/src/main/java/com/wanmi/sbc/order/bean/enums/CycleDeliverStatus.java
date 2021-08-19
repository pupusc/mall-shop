package com.wanmi.sbc.order.bean.enums;

import com.wanmi.sbc.common.annotation.ApiEnumProperty;

public enum  CycleDeliverStatus {

    @ApiEnumProperty("0: 待配送")
    NOT_SHIPPED("NOT_SHIPPED", "待配送"),

    @ApiEnumProperty("1: 已配送")
    SHIPPED("SHIPPED", "已配送"),

    @ApiEnumProperty("2: 已顺延")
    POSTPONE("POSTPONE", "已顺延"),

    @ApiEnumProperty("3: 已推送")
    PUSHED("PUSHED", "已推送"),

    @ApiEnumProperty("4:推送失败")
    PUSHED_FAIL("PUSHED_FAIL", "推送失败");


    private String statusId;

    private String description;

    CycleDeliverStatus(String statusId, String description) {
        this.statusId = statusId;
        this.description = description;
    }

    public String getStatusId() {
        return statusId;
    }

    public String getDescription() {
        return description;
    }
}
