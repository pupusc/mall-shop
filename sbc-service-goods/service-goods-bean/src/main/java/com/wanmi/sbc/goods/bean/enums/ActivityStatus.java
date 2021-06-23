package com.wanmi.sbc.goods.bean.enums;

import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 活动状态 0：即将开场，1：进行中，2：已结束
 */
@ApiEnum(dataType = "java.lang.String")
public enum ActivityStatus {
    @ApiEnumProperty("0：即将开场")
    ABOUT_TO_START("ABOUT_TO_START", "即将开场"),
    @ApiEnumProperty("1：进行中")
    SALE("SALE", "进行中"),
    @ApiEnumProperty("2：已结束")
    COMPLETED("COMPLETED", "已结束");

    private String activityStatusId;

    private String description;

    ActivityStatus(String activityStatusId, String description) {
        this.activityStatusId = activityStatusId;
        this.description = description;
    }

    public String getActivityStatusId() {
        return activityStatusId;
    }

    public String getDescription() {
        return description;
    }

}
