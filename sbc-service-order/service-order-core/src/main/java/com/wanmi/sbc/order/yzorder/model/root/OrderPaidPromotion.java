package com.wanmi.sbc.order.yzorder.model.root;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPaidPromotion implements Serializable {

    private static final long serialVersionUID = 4883711058573670558L;

    /**
     * 商家创建支付有礼或满减送成功生成的活动id
     */
    private Long promotion_id;

    /**
     * 优惠券
     */
    private List<OrderPaidPromotionCoupon> coupons;

    private List<Oid> oids;
}
