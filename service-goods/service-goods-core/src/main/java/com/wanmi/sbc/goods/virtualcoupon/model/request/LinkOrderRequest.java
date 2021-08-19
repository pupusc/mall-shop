package com.wanmi.sbc.goods.virtualcoupon.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkOrderRequest {

    /**
     * 订单号
     */
    private String tid;

    /**
     * 卡券ID
     */
    private Long couponId;

    /**
     * 要更新的ID
     */
    private List<Long> ids;

    /**
     * 更新人
     */
    private String updatePerson;
    /**
     * 更新时间
     */
    private LocalDateTime now;
}
