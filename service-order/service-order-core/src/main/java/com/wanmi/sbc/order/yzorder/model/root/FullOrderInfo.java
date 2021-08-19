package com.wanmi.sbc.order.yzorder.model.root;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 交易基础信息结构体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullOrderInfo implements Serializable {

    private static final long serialVersionUID = -8553418039951048946L;

    /**
     * 收货地址信息
     */
    private AddressInfo address_info;

    /**
     * 交易明细结
     */
    private List<Order> orders;

    /**
     * 交易基础信息
     */
    private OrderInfo order_info;

    /**
     * 订单买家信息
     */
    private BuyerInfo buyer_info;

    /**
     * 交易来源信息
     */
    private SourceInfo source_info;

    /**
     * 订单支付信息
     */
    private PayInfo pay_info;

    /**
     * 送礼订单子单
     */
    private ChildInfo child_info;

    /**
     * 标记信息
     */
    private RemarkInfo remark_info;

    /**
     * 电子发票信息
     */
    private InvoiceInfo invoice_info;

    /**
     * 周期购配置信息
     */
    private MultiPeriodDetail multiPeriodDetail;

    /**
     * 周期购订单最新一期的发货计划
     */
    private MultiPeriodLatestPlan multiPeriodLatestPlan;
}
