package com.wanmi.sbc.order.provider.impl.trade;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.provider.trade.TradePriceProvider;
import com.wanmi.sbc.order.api.request.trade.TradePriceParamBO;
import com.wanmi.sbc.order.api.response.trade.TradePriceResultBO;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.entity.value.DiscountsPriceDetail;
import com.wanmi.sbc.order.trade.model.entity.value.TradePrice;
import com.wanmi.sbc.order.trade.reponse.CalcPriceResult;
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
        List<TradeItem> tradeItems = paramBO.getGoodsInfos().stream()
                .map(item -> TradeItem.builder().skuId(item.getGoodsInfoId()).num(item.getBuyCount()).marketingId(item.getMarketingId()).build())
                .collect(Collectors.toList());

        //综合算价，propPrice：定价；salePrice：售价；originPrice：原价（会员价）；totalPrice：支付价（活动价）
        CalcPriceResult priceResult = calcTradePriceService.calc(tradeItems, paramBO.getCouponId(), paramBO.getCustomerId());
        TradePrice price = priceResult.getTradePrice();
        BigDecimal propPriceCut = price.getPropPrice().compareTo(price.getSalePrice()) > 0 ? price.getPropPrice().subtract(price.getSalePrice()) : BigDecimal.ZERO;
        BigDecimal vipPriceCut = price.getSalePrice().compareTo(price.getOriginPrice()) > 0 ? price.getSalePrice().subtract(price.getOriginPrice()) : BigDecimal.ZERO;
        BigDecimal mktPriceCut = price.getDiscountsPrice() != null ? price.getDiscountsPrice() : BigDecimal.ZERO;

        TradePriceResultBO resultBO = new TradePriceResultBO();
        resultBO.setTotalPrice(price.getTotalPrice().add(propPriceCut).add(vipPriceCut).add(mktPriceCut));
        resultBO.setCutPrice(propPriceCut.add(vipPriceCut).add(mktPriceCut));
        resultBO.setPayPrice(price.getTotalPrice());
        //正价明细
        TradePriceResultBO.PriceItem priceItem = new TradePriceResultBO.PriceItem();
        priceItem.setType(TradePriceResultBO.PriceItemType.ADD_GOODS.getCode());
        priceItem.setDesc(TradePriceResultBO.PriceItemType.ADD_GOODS.getDesc());
        priceItem.setAmount(resultBO.getTotalPrice());
        resultBO.setTotalPriceItems(Arrays.asList(priceItem));

        //负价明细
        //1.商品优惠
        if (propPriceCut.compareTo(BigDecimal.ZERO) > 0) {
            TradePriceResultBO.PriceItem item = new TradePriceResultBO.PriceItem();
            item.setAmount(propPriceCut);
            item.setType(TradePriceResultBO.PriceItemType.SUB_GOODS.getCode());
            item.setDesc(TradePriceResultBO.PriceItemType.SUB_GOODS.getDesc());
            resultBO.getCutPriceItems().add(item);
        }
        //2.会员优惠
        if (vipPriceCut.compareTo(BigDecimal.ZERO) > 0) {
            TradePriceResultBO.PriceItem item = new TradePriceResultBO.PriceItem();
            item.setAmount(vipPriceCut);
            item.setType(TradePriceResultBO.PriceItemType.SUB_VIP_RATE.getCode());
            item.setDesc(TradePriceResultBO.PriceItemType.SUB_VIP_RATE.getDesc());
            resultBO.getCutPriceItems().add(item);
        }
        //3.活动折扣明细：mktPriceCut
        if (CollectionUtils.isNotEmpty(price.getDiscountsPriceDetails())) {
            for (DiscountsPriceDetail detail : price.getDiscountsPriceDetails()) {
                TradePriceResultBO.PriceItem item = new TradePriceResultBO.PriceItem();
                item.setAmount(detail.getDiscounts());
                item.setType(TradePriceResultBO.PriceItemType.SUB_PROMOTE_MKT.getCode());
                item.setDesc(detail.getMarketingType().getDesc());
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
        //参与算价的营销活动
        resultBO.setTradeMkts(priceResult.getTradeMkts().stream().map(i -> {
            TradePriceResultBO.TradeMkt tradeMkt = new TradePriceResultBO.TradeMkt();
            tradeMkt.setMktId(i.getMktId());
            tradeMkt.setMktLevelId(i.getMktLevelId());
            return tradeMkt;
        }).collect(Collectors.toList()));
        return BaseResponse.success(resultBO);
    }
}
