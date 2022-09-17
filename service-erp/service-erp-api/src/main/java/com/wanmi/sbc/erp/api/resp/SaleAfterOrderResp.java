package com.wanmi.sbc.erp.api.resp;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class SaleAfterOrderResp implements Serializable {
	
	private static final long serialVersionUID = 227363795363317628L;
	
	/**
	 * 用户id
	 */
	private Integer userId;
    /**
     * 售后主单号
     */
    private Long saId;

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
     * 销售渠道ID
     */
    private Integer saleChannelId;

    /**
     * 快照销售渠道ID
     */
    private Integer fkSaleChannelId;

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
     * 退款状态
     */
    private Integer refundStatus;

    /**
     * 收货手机号
     */
    private String mobile;

    /**
     * 发货状态-待补充
     */
    private Integer deliveryStatus;
    /**
     * 收货地址信息
     */
//    private OrderAddressBO orderAddressBO;
    /**
     * 售后子单
     */
    List<SaleAfterItemResp> items;

}
