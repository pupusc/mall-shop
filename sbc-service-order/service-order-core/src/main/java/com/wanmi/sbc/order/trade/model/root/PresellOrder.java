package com.wanmi.sbc.order.trade.model.root;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 预售活动订单实体
 */
@Data
public class PresellOrder {


    /**
     * 预售活动id
     */
    private String presellSaleId;

    /**
     * 活动支付类型  0： 定金  ，1：全款
     */
    private Integer presellType;

    /**
     * 预售活动定金支付号
     */
    private String handselOrderNum;

    /**
     * 预售活动尾款支付号
     */
    private String finalPaymentOrderNum;

    /**
     * 预售活动全款支付号
     */
    private String presellOrderNum;

    /**
     * 预售活动支付类型的阶段   0 定金  1尾款 2全款
     */
    private Integer orderStatus;

    /**
     * 预售活动定金价格
     */
    private BigDecimal handselPrice;

    /**
     * 预售活动尾款价格（已优惠后的价格）
     */
    private BigDecimal finalPaymentPrice;

    /**
     * 预售活动全款价格
     */
    private BigDecimal presellPrice;

    /**
     * 预售支付定金使用的订单
     */
    private String handSelTid;
}