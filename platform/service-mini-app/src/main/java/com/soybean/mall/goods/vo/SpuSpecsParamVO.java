package com.soybean.mall.goods.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SpuSpecsParamVO {
    @NotNull
    private String spuId;
    private Long marketingId;
}
