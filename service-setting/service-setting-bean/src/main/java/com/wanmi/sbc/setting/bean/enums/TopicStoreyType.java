package com.wanmi.sbc.setting.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;


@ApiEnum
public enum TopicStoreyType {

    ONEIMAGEONEGOODS(1, "一行一个图片+商品"),

    TWOIMAGEONEGOODS(2, "一行两个图片+商品"),

    TWOGOODS(3, "一行两个商品"),

    THREEIMAGE(4, "一行三个图片"),

    WATERFALL(5, "瀑布流"),

    SCROLLIMAGE(6, "轮播"),

    HETERSCROLLIMAGE(7, "异形轮播"),

    NAVIGATION(8,"导航"),

    COUPON(9,"优惠券");

    private Integer id;

    private String desc;

    TopicStoreyType(Integer id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public Integer getId() {
        return id;
    }

    public String getValue() {
        return desc;
    }

}