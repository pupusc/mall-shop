package com.wanmi.sbc.marketing.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 拼团操作状态
 */

public enum GrouponDetailOptStatus {

    @ApiEnumProperty("0：开团-可以开团")
    LEADER_OPEN_ABLE,

    @ApiEnumProperty("1：开团-查看团详情")
    LEADER_OPEN_EXIT,

    @ApiEnumProperty("2：活动已结束")
    ACTIVITY_END,

    @ApiEnumProperty("3：达到上限")
    GROUPON_LIMIT,

    @ApiEnumProperty("4：参团-未参团-我要参团")
    JOIN_GROUPON_JOIN_ABLE,

    @ApiEnumProperty("5：参团-未参团-来晚了-我也开个团")
    JOIN_GROUPON_FINISH_OPEN_OTHER,

    @ApiEnumProperty("6：参团-团结束-团失败")
    JOIN_GROUPON_FAILED_OPEN_OTHER,

    @ApiEnumProperty("7：参团-已参团-等待收货看其他团购")
    JOIN_GROUPON_JOINED_AND_SUCCESS,

    @ApiEnumProperty("8：参团-已参团-邀请好友")
    JOIN_GROUPON_JOINED_AND_SHARE,

    @ApiEnumProperty("9:业务数据异常")
    GROUPON_PARAMS_ERROR;

    @JsonCreator
    public GrouponDetailOptStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

}
