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
            wxOrderItemResp.setSalePrice(tradeItem.getPrice() == null ? "0" : tradeItem.getPrice().toString());
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

        //价格
        TradePriceVO tradePrice = tradeVO.getTradePrice();
        if (tradePrice != null) {
            WxOrderPriceResp wxOrderPriceResp = new WxOrderPriceResp();
            wxOrderPriceResp.setOriginPrice(tradePrice.getOriginPrice().toString());
            wxOrderPriceResp.setCouponPrice(tradePrice.getCouponPrice().toString());
            wxOrderPriceResp.setFreightPrice(tradePrice.getDeliveryPrice().toString());
            wxOrderPriceResp.setActualPrice(tradePrice.getTotalPrice().toString());
            wxOrderPriceResp.setPointsPrice(tradePrice.getPointsPrice() == null ? "" : tradePrice.getPointsPrice().toString());
            wxOrderResp.setPayPrice(wxOrderPriceResp);
        }

        return wxOrderResp;
    }
}
