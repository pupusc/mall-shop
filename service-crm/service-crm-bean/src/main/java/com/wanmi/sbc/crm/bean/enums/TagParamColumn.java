package com.wanmi.sbc.crm.bean.enums;

import com.wanmi.sbc.common.annotation.ApiEnumProperty;
import io.swagger.annotations.ApiModel;

/**
 * tag_param表的column_name枚举，列名枚举
 */
@ApiModel
public enum TagParamColumn {

    @ApiEnumProperty("terminal_source: 终端")
    TERMINAL_SOURCE("terminal_source"),

    @ApiEnumProperty("num:次数")
    NUM("num"),

    @ApiEnumProperty("day_num:天数")
    DAY_NUM("day_num"),

    @ApiEnumProperty("to_date_num:距离当天的天数")
    TO_DATE_NUM("to_date_num"),

    @ApiEnumProperty("money:金额")
    MONEY("money"),

    @ApiEnumProperty("time:时间")
    TIME("time"),

    @ApiEnumProperty("date:日期")
    DATE("date"),

    @ApiEnumProperty("cate_top_id:商品类目")
    CATE_TOP_ID("cate_top_id"),

    @ApiEnumProperty("cate_id:商品品类")
    CATE_ID("cate_id"),

    @ApiEnumProperty("brand_id:商品品牌")
    BRAND_ID("brand_id"),

    @ApiEnumProperty("goods_id:商品")
    GOODS_ID("goods_id"),

    @ApiEnumProperty("store_id:店铺")
    STORE_ID("store_id"),

    @ApiEnumProperty("share_goods_sale_num:分享赚销售额")
    SHARE_GOODS_SALE_NUM("share_goods_sale_num"),

    @ApiEnumProperty("share_goods_commission_num:分享赚佣金收入")
    SHARE_GOODS_COMMISSION_NUM("share_goods_commission_num"),

    @ApiEnumProperty("invite_new_num:邀新人数")
    INVITE_NEW_NUM("invite_new_num"),

    @ApiEnumProperty("effective_invite_new_num:有效邀新人数")
    EFFECTIVE_INVITE_NEW_NUM("effective_invite_new_num"),

    @ApiEnumProperty("invite_new_reward:邀新奖励")
    INVITE_NEW_REWARD("invite_new_reward");

    private final String value;

    TagParamColumn(String value){
        this.value = value;
    }

    public String toValue() {
        return this.value;
    }

}
