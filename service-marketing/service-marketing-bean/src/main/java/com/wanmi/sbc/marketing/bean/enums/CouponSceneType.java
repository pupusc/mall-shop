package com.wanmi.sbc.marketing.bean.enums;

import com.wanmi.sbc.common.annotation.ApiEnum;

@ApiEnum
public enum CouponSceneType {



    GOODS_DETAIL(1,"商详页"),

    COUPON_CENTER(2,"领券中心"),

    CART(4,"购物车"),

    TOPIC(4,"专题页");

    private Integer type;

    private String desc;

    CouponSceneType(int type,String desc) {

        this.type = type;
        this.desc =desc;
    }

    public Integer getType() {
        return type;
    }

    public String getValue() {
        return desc;
    }
}
