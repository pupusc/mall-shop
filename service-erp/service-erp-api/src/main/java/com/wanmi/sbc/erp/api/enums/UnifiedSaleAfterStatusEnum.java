package com.wanmi.sbc.erp.api.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum UnifiedSaleAfterStatusEnum {
    INIT(10, "新售后"), // 待审核
    SALE_AFTER(1, "审核通过"),
    CONFIRM_DELIVERY(20, "待收货"), // 存在退实物的情况下，等待对方退货并且存在物流信息
    REFUND_MONEY(21, "确认打款"), // 可以给用户打款
    CONFIRM_EXPRESS(22, "待填写物流信息"), // 存在退实物的情况下，等待对方退货但是没有物流信息
    SALE_AFTER_ERROR(2, "售后异常"),
    SALE_AFTER_DONE(3, "售后完成"), // 更新订单表信息
    SALE_AFTER_CLOSE(4, "售后关闭");
    private Integer code;
    private String desc;

    UnifiedSaleAfterStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public static List<Integer> saleAfterDone() {
    	List<Integer> codeList = new ArrayList<>();
    	codeList.add(SALE_AFTER_DONE.getCode());
    	codeList.add(SALE_AFTER_CLOSE.getCode());
    	return codeList;
    }
}
