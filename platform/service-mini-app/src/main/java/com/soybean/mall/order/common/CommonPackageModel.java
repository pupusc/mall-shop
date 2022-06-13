package com.soybean.mall.order.common;

import com.soybean.mall.order.response.order.WxOrderItemResp;
import com.soybean.mall.order.response.order.WxOrderPayInfoResp;
import com.soybean.mall.order.response.order.WxOrderPriceResp;
import com.soybean.mall.order.response.order.WxOrderResp;
import com.wanmi.sbc.order.bean.vo.PayInfoVO;
import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import com.wanmi.sbc.order.bean.vo.TradePriceVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/7 5:50 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class CommonPackageModel {

    /**
     * 封装 微信订单对象
     * @param tradeVO
     * @return
     */
    public static WxOrderResp packTradeVo2WxOrderResp(TradeVO tradeVO) {
        WxOrderResp wxOrderResp = new WxOrderResp();
        wxOrderResp.setTid(tradeVO.getId());
        wxOrderResp.setCreateTime(tradeVO.getTradeState().getCreateTime());
        wxOrderResp.setOrderTimeOut(tradeVO.getOrderTimeOut());
        //商品
        List<WxOrderItemResp> wxOrderItemRespList = new ArrayList<>();
        for (TradeItemVO tradeItem : tradeVO.getTradeItems()) {
            WxOrderItemResp wxOrderItemResp = new WxOrderItemResp();
            wxOrderItemResp.setSpuId(tradeItem.getSpuId());
            wxOrderItemResp.setSkuId(tradeItem.getSkuId());
            wxOrderItemResp.setTitle(tradeItem.getSpuName());
            wxOrderItemResp.setSpecs(tradeItem.getSpecDetails());
            wxOrderItemResp.setSalePrice(tradeItem.getPrice() == null ? new BigDecimal("0") : tradeItem.getPrice());
            wxOrderItemResp.setPoints(tradeItem.getPoints() == null ? 0 : tradeItem.getPoints().intValue());
            wxOrderItemResp.setNum(tradeItem.getNum().intValue());
            wxOrderItemResp.setPic(tradeItem.getPic());
            wxOrderItemRespList.add(wxOrderItemResp);
        }
        wxOrderResp.setItems(wxOrderItemRespList);
        
        //支付
        PayInfoVO payInfo = tradeVO.getPayInfo();
        if (payInfo != null) {
            WxOrderPayInfoResp wxOrderPayInfoResp = new WxOrderPayInfoResp();
            wxOrderPayInfoResp.setPayTypeId(payInfo.getPayTypeId());
            wxOrderPayInfoResp.setPayTypeName(payInfo.getPayTypeName());
            wxOrderResp.setPayInfo(wxOrderPayInfoResp);
        }

        // originPrice 原始价格信息
        BigDecimal sumOriginPriceTmp = BigDecimal.ZERO;
        //原始售价
        BigDecimal sumMarketPriceTmp = BigDecimal.ZERO;
        for (TradeItemVO tradeItem : tradeVO.getTradeItems()) {
            //如果是打包商品；
            if (StringUtils.isNotBlank(tradeItem.getPackId())) {
                if (!tradeItem.getSpuId().equals(tradeItem.getPackId())) {
                    continue;
                }
            }
            //获取总定价
            BigDecimal marketPrice = tradeItem.getMarketPrice() == null ? BigDecimal.ZERO : tradeItem.getMarketPrice();
            sumMarketPriceTmp = sumMarketPriceTmp.add(marketPrice);
            sumOriginPriceTmp = sumOriginPriceTmp.add(tradeItem.getPropPrice() == null ? marketPrice : new BigDecimal(tradeItem.getPropPrice()+""));

        }

        //价格
        TradePriceVO tradePrice = tradeVO.getTradePrice();
        if (tradePrice != null) {
            WxOrderPriceResp wxOrderPriceResp = new WxOrderPriceResp();
            //定价
            BigDecimal originPrice = sumOriginPriceTmp.compareTo(BigDecimal.ZERO) > 0 ? sumOriginPriceTmp : tradePrice.getOriginPrice(); //定价金额
            wxOrderPriceResp.setOriginPrice(originPrice); //定价
            // 优惠金额 = 定价 - 售价
            BigDecimal discountsPrice = sumMarketPriceTmp.compareTo(BigDecimal.ZERO) > 0 ? sumMarketPriceTmp : tradePrice.getDiscountsPrice();
            wxOrderPriceResp.setDiscountsPrice(originPrice.subtract(discountsPrice));
            BigDecimal actualPrice = tradePrice.getTotalPrice() == null ? BigDecimal.ZERO : tradePrice.getTotalPrice();
            wxOrderPriceResp.setActualPrice(actualPrice); //折扣价
            wxOrderPriceResp.setVipDiscountPrice(discountsPrice.subtract(actualPrice)); //优惠金额 售价 - 折扣价
            wxOrderPriceResp.setCouponPrice(tradePrice.getCouponPrice() == null ? BigDecimal.ZERO : tradePrice.getCouponPrice());
            wxOrderPriceResp.setFreightPrice(tradePrice.getDeliveryPrice() == null ? BigDecimal.ZERO : tradePrice.getDeliveryPrice());
            wxOrderPriceResp.setPointsPrice(tradePrice.getPointsPrice() == null ? BigDecimal.ZERO : tradePrice.getPointsPrice());

            wxOrderResp.setPayPrice(wxOrderPriceResp);
        }

        return wxOrderResp;
    }
}
