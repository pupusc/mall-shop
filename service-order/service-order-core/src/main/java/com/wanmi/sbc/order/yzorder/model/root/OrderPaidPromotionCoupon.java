package com.wanmi.sbc.order.yzorder.model.root;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPaidPromotionCoupon implements Serializable {

    private static final long serialVersionUID = 4666491644103183307L;

    /**
     * 商家创建券成功生成的优惠券id
     */
    private Long coupon_id;

    /**
     * 买家优惠券id，即买家在店铺个人中心看到的优惠券对应的id
     */
    private Long buyer_coupon_id;
}
