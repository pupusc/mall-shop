package com.wanmi.sbc.order.provider.impl.ztemp.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ImportMallRefundParamVO$Refund {
    /**
     * 退款单号
     */
    @NotNull
    private String refundNo;
    /**
     * 退款流水号
     */
    private String refundTradeNo;
    /**
     * 退款网关
     */
    private String refundGateway;
    /**
     * 退款金额
     */
    @NotNull
    private Integer amount;

    /**
     * 支付类型
     */
    @NotNull
    private String payType;
    /**
     * 退款时间
     */
    @NotNull
    private LocalDateTime refundTime;
    /**
     * 退款商户号
     */
    @NotNull
    private String refundMchid;
}
