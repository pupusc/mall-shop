package com.soybean.mall.wx.mini.order.bean.enums;

import com.wanmi.sbc.common.annotation.ApiEnum;

@ApiEnum
public enum WxAfterSaleReasonType {
    INCORRECT_SELECTION(1,"拍错/多拍"),
    NO_LONGER_WANT(2,"不想要了"),
    OTHERS(12,"其他");

    private Integer id;

    private String description;

    WxAfterSaleReasonType(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

}
