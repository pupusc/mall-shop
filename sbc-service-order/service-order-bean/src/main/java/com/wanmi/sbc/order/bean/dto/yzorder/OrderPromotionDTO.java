package com.wanmi.sbc.order.bean.dto.yzorder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 订单优惠详情结构体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPromotionDTO implements Serializable {

    private static final long serialVersionUID = -866889742416420277L;

    /**
     * 订单级优惠总金额，单位：元
     */
    private String order_discount_fee;

    /**
     * 商品级优惠总金额，单位：元
     */
    private String item_discount_fee;

    /**
     * 订单改价金额，单位：元。带“-”负数表示涨价金额，不带“-”表示减价金额。例如：返回值：-0.01-表示涨价0.01元，0.01-表示减价0.01元。
     */
    private String adjust_fee;

    /**
     * 邮费优惠总额，单位：分，单位：分
     */
    private Long postage_decrease;

    /**
     * 优惠金额,单位：分
     */
    private Long goods_decrease;

    /**
     * 商品改价金额，单位：分
     */
    private Long edit_goods_price;

    /**
     *
     订单级货款优惠总额，单位：分
     */
    private Long order_decrease;

    /**
     * 商品级优惠明细
     */
    private List<OrderPromotionItemDTO> item;

    /**
     * 订单级优惠明细
     */
    private List<OrderPromotionOrderDTO> order;

    private List<OrderPaidPromotionDTO> order_paid_promotion;

    /**
     * 邮费优惠信息
     */
    private List<PostFeeDTO> post_fee;
}
