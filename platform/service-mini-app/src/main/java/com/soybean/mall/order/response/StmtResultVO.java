package com.soybean.mall.order.response;

import com.soybean.mall.cart.vo.PromoteInfoResultVO$Coupon;
import com.wanmi.sbc.order.api.response.trade.TradePriceResultBO;
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
    //private CalcPriceSum calcPrice;
    private TradePriceResultBO calcPrice;

    /**
     * 商品列表
     */
    private List<SettlementResultVO$GoodsInfo> goodsInfos;
}
