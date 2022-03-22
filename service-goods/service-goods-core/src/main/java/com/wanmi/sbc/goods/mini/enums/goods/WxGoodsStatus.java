package com.wanmi.sbc.goods.mini.enums.goods;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum WxGoodsStatus {

    //上传中
    ON_UPLOAD,
    //已上传未上架
    UPLOAD,
    //上架
    ON_SHELF,
    //下架
    OFF_SHELF;

    @JsonCreator
    public static WxGoodsStatus fromValue(int value) {
        return values()[value];
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }
}
