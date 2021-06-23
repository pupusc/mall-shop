package com.wanmi.sbc.order.bean.dto.yzorder;

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
public class FullOrderInfoDTO implements Serializable {

    private static final long serialVersionUID = -8553418039951048946L;

    /**
     * 收货地址信息
     */
    private AddressInfoDTO address_info;

    /**
     * 交易明细结
     */
    private List<OrderDTO> orders;

    /**
     * 交易基础信息
     */
    private OrderInfoDTO order_info;

    /**
     * 订单买家信息
     */
    private BuyerInfoDTO buyer_info;

    /**
     * 交易来源信息
     */
    private SourceInfoDTO source_info;

    /**
     * 订单支付信息
     */
    private PayInfoDTO pay_info;

    /**
     * 送礼订单子单
     */
    private ChildInfoDTO child_info;

    /**
     * 标记信息
     */
    private RemarkInfoDTO remark_info;

    /**
     * 电子发票信息
     */
    private InvoiceInfoDTO invoice_info;

    /**
     * 周期购配置信息
     */
    private MultiPeriodDetailDTO multiPeriodDetail;

    /**
     * 周期购订单最新一期的发货计划
     */
    private MultiPeriodLatestPlanDTO multiPeriodLatestPlan;
}
