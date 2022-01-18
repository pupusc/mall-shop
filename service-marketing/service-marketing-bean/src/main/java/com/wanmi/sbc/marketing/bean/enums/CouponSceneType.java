package com.wanmi.sbc.marketing.bean.enums;

import com.wanmi.sbc.common.annotation.ApiEnum;

@ApiEnum
public enum CouponSceneType {



    DETAIL_CART_CENTER(1,"商详页、购物车、领券中心"),

    TOPIC(2,"专题页");

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
