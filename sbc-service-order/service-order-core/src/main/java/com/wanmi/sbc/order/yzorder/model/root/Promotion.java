package com.wanmi.sbc.order.yzorder.model.root;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *
 商品级优惠明细结构体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Promotion implements Serializable {

    private static final long serialVersionUID = -8424968415265970157L;

    /**
     * 优惠类型
     */
    private String promotion_type;

    /**
     * 优惠别名
     */
    private String promotion_title;

    /**
     * 会员卡号，如果订单使用了权益卡优惠
     */
    private String card_no;

    /**
     * 优惠券/码编号
     */
    private String coupon_id;

    /**
     * 优惠子类型， card:优惠券 code:优惠码thirdparty:三方券
     */
    private String sub_promotion_type;

    /**
     * 优惠类型描述
     */
    private String promotion_type_name;

    /**
     * 优惠id,即商家端店铺后台该优惠活动id
     */
    private Long promotion_id;

    /**
     * 优惠金额，单位：元
     */
    private String discount_fee;

    /**
     * 优惠类型id
     */
    private Integer promotion_type_id;

    /**
     * 优惠别名
     */
    private String promotion_alias;

    /**
     * 优惠描述
     */
    private String promotion_condition;

    /**
     * 优惠活动别名
     */
    private String promotion_content;
}
