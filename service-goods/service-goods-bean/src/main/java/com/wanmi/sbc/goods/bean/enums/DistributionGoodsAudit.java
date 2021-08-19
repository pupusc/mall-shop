package com.wanmi.sbc.goods.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 分销商品审核状态 0:普通商品 1:待审核 2:已审核通过 3:审核不通过 4:禁止分销
 * Created by CHENLI
 */
@ApiEnum
public enum DistributionGoodsAudit {
    @ApiEnumProperty("0：普通商品")
    COMMON_GOODS,

    @ApiEnumProperty("1：待审核")
    WAIT_CHECK,

    @ApiEnumProperty("2：已审核通过")
    CHECKED,

    @ApiEnumProperty("3：审核不通过")
    NOT_PASS,

    @ApiEnumProperty("4：禁止分销")
    FORBID;

    @JsonCreator
    public DistributionGoodsAudit fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
