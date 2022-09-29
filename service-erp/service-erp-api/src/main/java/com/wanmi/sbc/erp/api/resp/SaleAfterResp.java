package com.wanmi.sbc.erp.api.resp;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SaleAfterResp implements Serializable {

    /**
     * 订单Id
     */
    private Long orderId;

    /**
     * 订单号
     */
    private Long orderNumber;

    /**
     * 订单创建时间
     */
    private Date orderCreateTime;

    /**
     * 下单时间
     */
    private Date bookTime;

    /**
     * 商户ID
     */
    private String shopId;

    /**
     * 渠道订单号
     */
    private String platformOrderId;

    /**
     * 子订单号
     */
    private Long orderItemId;

    /**
     * 售后主单号
     */
    private Long saId;

    /**
     * 回收入库状态，1.0版本，暂时没有值
     */
    private Integer stockStatus;

    /**
     * 售后原因
     */
    private String saMemo;

    /**
     * 售后状态
     *
     * @see com.soybean.unified.order.api.enums.UnifiedSaleAfterStatusEnum
     */
    private int saStatus;

    /**
     * 售后创建时间
     */
    private Date saCreateTime;

    /**
     * 售后业务类型
     *
     * @see com.soybean.unified.order.api.enums.SaleAfterRefundTypeEnum
     */
    private Integer refundType;

    /**
     * 售后子单号
     */
    private Long saItemId;

    /**
     * 退货数量
     */
    private Integer refundNum;

    /**
     * 退款状态
     */
    private Integer refundStatus;

    /**
     * 货品规格Id
     */
    private Long metaSkuId;
    /**
     * 货品规则名称
     */
    private String metaSkuName;

    /**
     * 收货手机号
     */
    private String mobile;

    /**
     * 发货状态-待补充
     */
    private Integer deliveryStatus;


}
