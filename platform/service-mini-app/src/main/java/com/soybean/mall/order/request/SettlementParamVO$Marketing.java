package com.soybean.mall.order.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-06-26 20:02:00
 */
@Data
public class SettlementParamVO$Marketing {
//    /**
//     * 店铺id
//     */
//    private Long storeId;
    /**
     * 营销活动Id
     */
    @NotNull
    private Long marketingId;
    /**
     * 营销等级id
     */
    private Long marketingLevelId;

    /**
     * 该营销活动关联的订单商品id集合
     */
    @NotEmpty
    private List<SettlementParamVO$TradeItem> goodsInfos = new ArrayList<>();

//    /**
//     * 如果是满赠，则填入用户选择的赠品id集合
//     */
//    //private List<String> giftSkuIds;
//    /**
//     * 如果是加价购，则填入用户选择的换购商品id集合
//     */
//    //private List<String> markupSkuIds;
//
//    /**
//     * 营销活动类型
//     */
//    //private Integer marketingSubType;
//
//    /**
//     * 所需积分
//     */
//    //private Integer pointNeed;
}
