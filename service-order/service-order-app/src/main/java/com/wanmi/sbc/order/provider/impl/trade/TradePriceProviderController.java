package com.wanmi.sbc.order.provider.impl.trade;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.provider.trade.TradePriceProvider;
import com.wanmi.sbc.order.api.request.trade.TradePriceParamBO;
import com.wanmi.sbc.order.api.response.trade.TradePriceResultBO;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.entity.value.DiscountsPriceDetail;
import com.wanmi.sbc.order.trade.model.entity.value.TradePrice;
import com.wanmi.sbc.order.trade.service.CalcTradePriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Arrays;
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
        resultBO.setTotalPrice(price.getPropPrice());
        resultBO.setCutPrice(price.getDiscountsPrice());
        resultBO.setPayPrice(price.getTotalPrice());
        //加价明细
        TradePriceResultBO.PriceItem priceItem = new TradePriceResultBO.PriceItem();
        priceItem.setType(TradePriceResultBO.PriceItemType.ADD_GOODS.getCode());
        priceItem.setDesc(TradePriceResultBO.PriceItemType.ADD_GOODS.getDesc());
        priceItem.setAmount(resultBO.getTotalPrice());
        resultBO.setTotalPriceItems(Arrays.asList(priceItem));
        //减价明细
        //1.商品优惠
        if (price.getPropPrice().compareTo(price.getSalePrice()) > 0) {
            TradePriceResultBO.PriceItem item = new TradePriceResultBO.PriceItem();
            item.setAmount(price.getPropPrice().subtract(price.getSalePrice()));
            item.setType(TradePriceResultBO.PriceItemType.SUB_GOODS.getCode());
            item.setDesc(TradePriceResultBO.PriceItemType.SUB_GOODS.getDesc());
            resultBO.getCutPriceItems().add(item);
        }
        //2.会员优惠
        if (price.getSalePrice().compareTo(price.getTotalPrice()) > 0) {
            TradePriceResultBO.PriceItem item = new TradePriceResultBO.PriceItem();
            item.setAmount(price.getSalePrice().subtract(price.getTotalPrice()));
            item.setType(TradePriceResultBO.PriceItemType.SUB_VIP_RATE.getCode());
            item.setDesc(TradePriceResultBO.PriceItemType.SUB_VIP_RATE.getDesc());
            resultBO.getCutPriceItems().add(item);
        }
        //3.活动折扣明细
        if (CollectionUtils.isNotEmpty(price.getDiscountsPriceDetails())) {
            for (DiscountsPriceDetail detail : price.getDiscountsPriceDetails()) {
                TradePriceResultBO.PriceItem item = new TradePriceResultBO.PriceItem();
                priceItem.setAmount(detail.getDiscounts());
                priceItem.setType(TradePriceResultBO.PriceItemType.SUB_PROMOTE_MKT.getCode());
                priceItem.setDesc(detail.getMarketingType().getDesc());
                resultBO.getCutPriceItems().add(item);
            }
        }
        //4.优惠券优惠
        if (price.getCouponPrice() != null && price.getCouponPrice().compareTo(BigDecimal.ZERO) > 0) {
            TradePriceResultBO.PriceItem item = new TradePriceResultBO.PriceItem();
            priceItem.setAmount(price.getCouponPrice());
            priceItem.setType(TradePriceResultBO.PriceItemType.SUB_COUPON_COST.getCode());
            priceItem.setDesc(TradePriceResultBO.PriceItemType.SUB_COUPON_COST.getDesc());
            resultBO.getCutPriceItems().add(item);
        }
        return BaseResponse.success(resultBO);
    }
}
