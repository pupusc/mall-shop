package com.wanmi.sbc.erp.api.resp;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PaymentResp implements Serializable {
    /**
     * 支付流水号
     */
    private Long tid;

    /**
     * 支付流水-网关
     */
    private String payTradeNo;

    /**
     * 支付类型，1 现金，2 知豆，3 积分，4 智慧币
     */
    private String payType;

    /**
     * 支付网关
     *
     * @see com.soybean.unified.order.api.enums.PaymentGatewayEnum
     */
    private Integer payGateway;

    /**
     * 支付金额
     */
    private Integer amount;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 支付商户号
     */
    private String payMchid;

    /**
     * 本地订单号
     */
    private Long orderNumber;

    /**
     * 订单ID
     */
    private Long orderId;

}
