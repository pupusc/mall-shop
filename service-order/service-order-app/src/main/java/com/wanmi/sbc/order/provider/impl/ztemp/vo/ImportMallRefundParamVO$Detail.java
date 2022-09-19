package com.wanmi.sbc.order.provider.impl.ztemp.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ImportMallRefundParamVO$Detail {
    /**
     * 支付类型
     */
    @NotNull
    private Integer payType;

    /**
     * 退款金额
     */
    @NotNull
    private Integer amount;

    /**
     * 退款理由
     */
    private String refundReason;
}
