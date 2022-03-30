package com.soybean.mall.order.enums;

import com.wanmi.sbc.common.annotation.ApiEnum;



@ApiEnum
public enum WxAfterSaleOperateType {

    ADD_AFTER_SALE(1,"生成售后单"),

    CANCEL(2,"取消售后单"),

    REJECT(3,"拒绝售后单"),

    REFUND(4,"同意退款"),

    RETURN(5,"同意退货");





    private Integer index;
    private String desc;

    WxAfterSaleOperateType(Integer index,String desc) {
        this.index = index;
        this.desc = desc;
    }


    public Integer getIndex() {
        return index;
    }

    public String getDesc() {
        return desc;
    }
}