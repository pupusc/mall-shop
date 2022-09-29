package com.wanmi.sbc.erp.api.resp;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class OrdOrderResp implements Serializable {
    
    private Long tid;

    private Integer bookModel;

    private Long orderNumber;

    private String platformCode;

    /**
     * 订单来源
     */
    private String orderSource;

    private String platformOrderId;

    private Integer orderStatus;

    private Integer totalFee;

    private Integer oughtFee;

    private Integer actualFee;

    private Integer refundFee;

    private Integer postFee;

    private Integer discountFee;

    private Integer userBelong;

    private Integer userId;

    private Date bookTime;

    private Date payTimeout;

    private Date payTime;

    private Date completeTime;

    private Date cancelTime;

    private String buyerMemo;

    private String sellerMemo;

    private Integer paymentStatus;

    private String errorReason;

    private Integer promoType;

    private Integer promoUserId;

    private Integer saleChannelId;

    private Integer payGateway;

    private String shopId;

    private Integer deviceType;

    List<OrderItemRsp> orderItemBOS;
}
