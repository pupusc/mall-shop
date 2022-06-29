package com.wanmi.sbc.order.provider.impl.trade;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.provider.trade.TradePriceProvider;
import com.wanmi.sbc.order.api.request.trade.TradePriceParamBO;
import com.wanmi.sbc.order.api.response.trade.TradePriceResultBO;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.entity.value.TradePrice;
import com.wanmi.sbc.order.trade.service.CalcTradePriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Liang Jun
 * @date 2022-06-28 13:49:00
 */
@RestController
public class TradePriceProviderController implements TradePriceProvider {
    @Autowired
    private CalcTradePriceService calcTradePriceService;

    @Override
    public BaseResponse<TradePriceResultBO> calcPrice(TradePriceParamBO paramBO) {
        //skus
        List<TradeItem> tradeItems = paramBO.getGoodsInfos().stream().map(item -> TradeItem.builder().skuId(item.getGoodsInfoId()).num(item.getBuyCount())
                .marketingId(item.getMarketingId()).build()).collect(Collectors.toList());
        //算价
        TradePrice price = calcTradePriceService.calc(tradeItems, paramBO.getCouponId(), paramBO.getCustomerId());
        TradePriceResultBO resultBO = new TradePriceResultBO();
        resultBO.setTotalPrice(price.getOriginPrice());
        resultBO.setCutPrice(price.getDiscountsPrice());
        resultBO.setPayPrice(price.getTotalPrice());
        return BaseResponse.success(resultBO);
    }
}
