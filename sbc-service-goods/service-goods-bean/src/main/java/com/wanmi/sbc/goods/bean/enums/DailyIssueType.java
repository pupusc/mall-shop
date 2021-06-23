package com.wanmi.sbc.goods.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

public enum DailyIssueType {

    @ApiEnumProperty("1：每日送达")
    EVERY_DAY("1", "每日送达"),
    @ApiEnumProperty("2：工作日每天送达")
    WORKDAY("2", "工作日每天送达"),
    @ApiEnumProperty("3：周末每天送达")
    WEEKEND("3", "周末每天送达");

    private String dailyIssueTypeId;

    private String description;

    DailyIssueType(String dailyIssueTypeId, String description) {
        this.dailyIssueTypeId = dailyIssueTypeId;
        this.description = description;
    }

    public String getDailyIssueTypeId() {
        return dailyIssueTypeId;
    }

    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static DailyIssueType fromValue(int value) {
        return values()[value - 1];
    }
}
