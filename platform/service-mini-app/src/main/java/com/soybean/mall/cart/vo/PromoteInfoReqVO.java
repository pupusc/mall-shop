package com.soybean.mall.cart.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Liang Jun
 * @desc 促销信息
 * @date 2022-06-16 19:07:00
 */
@Data
public class PromoteInfoReqVO {
    @NotNull
    private Integer goodsId;
}