package com.wanmi.sbc.order.bean.dto.yzorder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPaidPromotionDTO implements Serializable {

    private static final long serialVersionUID = 4883711058573670558L;

    /**
     * 商家创建支付有礼或满减送成功生成的活动id
     */
    private Long promotion_id;

    /**
     * 优惠券
     */
    private List<OrderPaidPromotionCouponDTO> coupons;

    private List<OidDTO> oids;
}
