package com.soybean.mall.cart.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Liang Jun
 * @desc 促销信息
 * @date 2022-06-16 19:07:00
 */
@Data
public class PromoteCouponParamVO {
    @NotNull
    private List<Integer> goodsInfoIds;
}