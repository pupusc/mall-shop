package com.soybean.mall.order.response;

import com.soybean.mall.cart.vo.CalcPriceSum;
import com.soybean.mall.cart.vo.PromoteInfoResultVO$Coupon;
import lombok.Data;

import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-06-20 13:58:00
 */
@Data
public class StmtResultVO {
    /**
     * 优惠券
     */
    private List<PromoteInfoResultVO$Coupon> coupons;
    /**
     * 价格汇总
     */
    private CalcPriceSum calcPrice;
    /**
     * 营销活动
     */
    private List<Marketing> marketings;

    @Data
    private static class Marketing {
        /**
         * 促销活动
         */
        private SettlementResultVO$Marketing marketing;
        /**
         * 数量
         */
        private List<SettlementResultVO$GoodsInfo> goodsInfos;
    }
}
