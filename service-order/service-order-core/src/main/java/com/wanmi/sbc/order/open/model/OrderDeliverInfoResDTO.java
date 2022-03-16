package com.wanmi.sbc.order.open.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-02-16 10:03:00
 */
@Data
public class OrderDeliverInfoResDTO {

    /**
     * 商城订单编号
     */
    private String tradeNo;

    /**
     * 外部订单编号
     */
    private String outTradeNo;

    /**
     * 订单状态：
     * INIT：创建订单
     * REMEDY：修改订单
     * REFUND：已退款
     * AUDIT：已审核
     * DELIVERED_PART：部分发货
     * DELIVERED：已发货
     * CONFIRMED：已确认
     * COMPLETED：已完成
     * VOID：已作废
     */
    private String orderStatus;

    /**
     * 发货状态：
     * NOT_YET_SHIPPED：未发货
     * SHIPPED：已发货
     * PART_SHIPPED：部分发货
     * VOID：作废
     */
    private String deliverStatus;

    /**
     * 发货单
     */
    private List<DeliverResDTO> delivers = new ArrayList<>();
}