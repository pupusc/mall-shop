package com.wanmi.sbc.marketing.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * @Author: songhanlin
 * @Date: Created In 10:43 AM 2018/9/12
 * @Description: 优惠券作用范围类型(0, 1, 2, 3)
 * 0 全部商品，        1 品牌，
 * 2 平台类目/店铺分类，3 自定义货品（店铺可用）
 */
@ApiEnum
public enum ScopeType {

    /**
     * 全部商品
     */
    @ApiEnumProperty("0：全部商品")
    ALL(0),

    /**
     * 品牌
     */
    @ApiEnumProperty("1：品牌")
    BRAND(1),

    /**
     * 平台(boss)类目
     */
    @ApiEnumProperty("2：平台(boss)类目")
    BOSS_CATE(2),

    /**
     * 店铺分类
     */
    @ApiEnumProperty("3：店铺分类")
    STORE_CATE(3),

    /**
     * 自定义货品（店铺可用）
     */
    @ApiEnumProperty("4：自定义货品（店铺可用）")
    SKU(4);

    private int type;

    ScopeType(int type) {
        this.type = type;
    }

    @JsonCreator
    public static ScopeType fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.type;
    }

}
