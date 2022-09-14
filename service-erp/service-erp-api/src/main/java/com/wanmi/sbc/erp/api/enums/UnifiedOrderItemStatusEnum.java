package com.wanmi.sbc.erp.api.enums;

import lombok.Getter;

@Getter
public enum UnifiedOrderItemStatusEnum {
    INIT(0, "初始"),
    AUDITING(11, "审核中"),
    APPLY_FOR_DELIVERY(12, "发货申请中"),
    WAIT_FOR_DELIVERY(13, "待发货"),
    PART_OF_DELIVERY(14, "部分发货"),
    WAIT_FOR_SIGN(15, "待签收"),
    DEPT_FOR_SIGN(16, "部份签收"),
    HAS_SIGN(17, "已签收"),
    COMPLETE_ORDER(18, "交易完成"),
    CLOSE_ORDER(19, "交易关闭");

    private Integer code;
    private String msg;

    UnifiedOrderItemStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


}
