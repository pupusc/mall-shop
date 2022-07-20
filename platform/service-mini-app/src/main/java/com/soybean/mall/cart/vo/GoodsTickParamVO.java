package com.soybean.mall.cart.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-07-18 15:06:00
 */
@Data
public class GoodsTickParamVO {
    /**
     * 需要打勾的商品，为空时则全部取消
     */
    private List<String> skuIds = new ArrayList<>();
}
