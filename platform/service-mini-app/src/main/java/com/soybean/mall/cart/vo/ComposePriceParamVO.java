package com.soybean.mall.cart.vo;

import lombok.Data;

/**
 * @author Liang Jun
 * @date 2022-06-20 18:35:00
 */
@Data
public class ComposePriceParamVO {
    /**
     * 促销id
     */
    private Long marketingId;
    /**
     * 优惠券id
     */
    private String couponId;
}
