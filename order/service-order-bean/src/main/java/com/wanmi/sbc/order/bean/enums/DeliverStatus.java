package com.wanmi.sbc.order.bean.enums;

import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

@ApiEnum(dataType = "java.lang.String")
public enum DeliverStatus {

    @ApiEnumProperty("0: 未发货")
    NOT_YET_SHIPPED("NOT_YET_SHIPPED","未发货"),

    @ApiEnumProperty("1: 已发货")
    SHIPPED("SHIPPED","已发货"),

    @ApiEnumProperty("2: 部分发货")
    PART_SHIPPED("PART_SHIPPED","部分发货"),

    @ApiEnumProperty("3: 作废")
    VOID("VOID","作废");

    private String statusId;

    private String description;

    DeliverStatus(String statusId, String description) {
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
