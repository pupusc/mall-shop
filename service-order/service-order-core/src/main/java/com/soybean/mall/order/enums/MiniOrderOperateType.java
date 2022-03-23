package com.soybean.mall.order.enums;

import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

@ApiEnum
public enum MiniOrderOperateType {

    @ApiEnumProperty("创建订单")
    ADD_ORDER(1,"创建订单"),

    SYNC_PAY_RESULT(2,"同步支付结果"),

    RECEIVE(3,"确认收获");





    private Integer index;
    private String desc;

    MiniOrderOperateType(Integer index,String desc) {
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


