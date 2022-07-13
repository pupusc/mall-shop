package com.soybean.mall.order.response;

import lombok.Data;

import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-06-26 21:35:00
 */
@Data
public class SettlementResultVO$Marketing {
    /**
     * 营销id
     */
    private String marketingId;
    /**
     * 营销类型
     */
    private Integer marketingType;
    /**
     * 描述信息：您已满足100减50
     */
    private String marketingDesc;
    /**
     * 商品信息
     */
    private List<SettlementResultVO$GoodsInfo> goodsInfos;
}
