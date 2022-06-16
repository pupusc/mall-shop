package com.soybean.mall.cart.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-06-16 15:28:00
 */
@Data
public class CartInfoResVO$Group {
    /**
     * 分组类型：0普通；1满减；2满折；满赠；
     */
    private int type;
    /**
     * sku列表
     */
    private List<CartInfoResVO$Sku> goodsInfos;
}
