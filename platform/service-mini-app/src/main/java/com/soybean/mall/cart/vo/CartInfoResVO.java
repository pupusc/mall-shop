package com.soybean.mall.cart.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Liang Jun
 * @desc 购物车信息
 * @date 2022-06-15 11:20:00
 */
@Data
public class CartInfoResVO {
    /**
     * 店铺信息
     */
    private CartInfoResVO$Store store;
    /**
     * 活动分组
     */
    private List<CartInfoResVO$Group> goodsGroups;
}
