package com.wanmi.sbc.order.yzorder.model.root;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 订单支付信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayInfo implements Serializable {

    private static final long serialVersionUID = 6189064354169515167L;

    /**
     * 最终支付价格 payment=orders.payment的总和
     */
    private String payment;

    /**
     * 邮费
     */
    private String post_fee;

    /**
     * 优惠前商品总价
     */
    private String total_fee;

    /**
     * 外部支付单号
     */
    private List<String> outer_transactions;

    /**
     * 有赞支付流水号
     */
    private List<String> transaction;

    /**
     * 订单整单实付价格 单位：元
     */
    private String real_payment;

    /**
     * 礼品卡/储值卡抵扣之后的订单实付金额 单位：分
     */
    private Long deduction_real_pay;

    /**
     * 多阶段支付信息
     */
    private List<PhasePayments> phase_payments;
}
