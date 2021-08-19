package com.wanmi.sbc.order.yzorder.model.root;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 订单级优惠明细
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPromotionOrder implements Serializable {

    private static final long serialVersionUID = -3654876251677316862L;

    /**
     * 优惠别名
     */
    private String promotion_title;

    /**
     * 优惠金额，单位：元
     */
    private String discount_fee;

    /**
     * 优惠类型描述
     */
    private String promotion_type_name;

    /**
     * 优惠子类型， card:优惠券 code:优惠码thirdparty:三方券
     */
    private String sub_promotion_type;

    /**
     * 优惠类型
     */
    private String promotion_type;

    /**
     * 优惠券/码编号
     */
    private String coupon_id;

    /**
     * 优惠活动别名
     */
    private String promotion_content;

    /**
     * 优惠描述
     */
    private String promotion_condition;

    /**
     * 优惠类型id
     */
    private Long promotion_type_id;

    /**
     * 优惠id,即商家端店铺后台该优惠活动id。
     */
    private Long promotion_id;

    private List<OrderPromotionItemInfo> item_info;
}
