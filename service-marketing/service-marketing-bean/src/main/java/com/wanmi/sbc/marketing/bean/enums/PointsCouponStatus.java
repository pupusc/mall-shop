package com.wanmi.sbc.marketing.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 积分兑换券活动状态 1: 进行中, 2: 暂停中,3: 未开始,4: 已结束
 *
 * @author yang
 * @since 2019/6/11
 */
@ApiEnum
public enum PointsCouponStatus {
    @ApiEnumProperty("0：全部")
    ALL,

    @ApiEnumProperty("1：进行中")
    STARTED,

    @ApiEnumProperty("2：暂停中")
    PAUSED,

    @ApiEnumProperty("3：未开始")
    NOT_START,

    @ApiEnumProperty("4：已结束")
    ENDED;

    @JsonCreator
    public PointsCouponStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
