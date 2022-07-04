package com.soybean.mall.goods.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SpuSpecsParamVO {
    @NotNull
    private String spuId;
    /**
     * 指定优惠券
     */
    private String couponId;
    /**
     * 指定营销活动
     */
    private Long marketingId;
}
