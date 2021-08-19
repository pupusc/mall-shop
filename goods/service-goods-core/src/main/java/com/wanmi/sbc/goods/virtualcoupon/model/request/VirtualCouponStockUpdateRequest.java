package com.wanmi.sbc.goods.virtualcoupon.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VirtualCouponStockUpdateRequest {

    /**
     * 增加的总库存
     */
    private int addCount;

    /**
     * 增加的可用库存
     */
    private int addAbleCount;

    /**
     * 增加的过期库存
     */
    private int expireCount;

    /**
     * 增加的已售数量
     */
    private int saledCount;

    /**
     * 卡券ID
     */
    private Long couponId;

    /**
     * 更新人
     */
    private String updatePerson;

    private boolean orderFlag = false;

}
