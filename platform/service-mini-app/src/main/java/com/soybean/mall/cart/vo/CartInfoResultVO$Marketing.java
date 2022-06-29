package com.soybean.mall.cart.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-06-16 15:28:00
 */
@Data
public class CartInfoResultVO$Marketing {
    /**
     * 营销id：0表示没有营销
     */
    private Long marketingId;
    /**
     * 营销类型 0：满减 1:满折 2:满赠
     */
    private int type;
    /**
     * 营销类型文案
     */
    private String typeText;
    /**
     * 营销子类型 0：满金额减 1：满数量减 2:满金额折 3:满数量折 4:满金额赠 5:满数量赠
     */
    private int subType;
    /**
     * 促销文案：每满300减150，还差32
     */
    private String typeDesc;
    /**
     * sku列表
     */
    private List<CartInfoResultVO$Sku> goodsInfos;
}
