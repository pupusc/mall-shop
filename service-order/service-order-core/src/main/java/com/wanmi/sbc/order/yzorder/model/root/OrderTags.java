package com.wanmi.sbc.order.yzorder.model.root;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 订单打标
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderTags implements Serializable {

    private static final long serialVersionUID = 5620769419092589422L;

    /**
     * 是否分销单
     */
    private Boolean is_fenxiao_order;

    /**
     * 是否支付
     */
    private Boolean is_payed;

    /**
     * 是否线下订单
     */
    private Boolean is_offline_order;

    /**
     * 是否定金预售
     */
    private Boolean is_down_payment_pre;

    /**
     * 是否预订单
     */
    private Boolean is_preorder;

    /**
     * 是否会员订单
     */
    private Boolean is_member;

    /**
     * 是否担保交易
     */
    private Boolean is_secured_transactions;

    /**
     * 是否有退款
     */
    private Boolean is_refund;

    /**
     * 是否虚拟订单
     */
    private Boolean is_virtual;

    /**
     * 是否享受免邮
     */
    private Boolean is_postage_free;

    /**
     * 是否结算
     */
    private Boolean is_settle;

    /**
     * 是否有维权
     */
    private Boolean is_feedback;

    /**
     * 是否采购单
     */
    private Boolean is_purchase_order;

    /**
     * 是否多门店订单
     */
    private Boolean is_multi_store;
}
