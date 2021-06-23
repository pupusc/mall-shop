package com.wanmi.sbc.order.trade.model.root;

import com.wanmi.sbc.order.trade.model.entity.Discounts;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.entity.value.Supplier;
import com.wanmi.sbc.order.trade.model.entity.value.TradePrice;
import lombok.Data;

import java.util.List;

/**
 * <p>订单确认返回项</p>
 * Created by of628-wenzhi on 2018-03-08-下午6:12.
 */
@Data
public class TradeConfirmItem {
    /**
     * 订单商品sku
     */
    private List<TradeItem> tradeItems;

    /**
     * 赠品列表
     */
    private List<TradeItem> gifts;

    /**
     * 商家与店铺信息
     */
    private Supplier supplier;

    /**
     * 订单项小计
     */
    private TradePrice tradePrice;

    /**
     * 优惠金额
     */
    private List<Discounts> discountsPrice;

}
