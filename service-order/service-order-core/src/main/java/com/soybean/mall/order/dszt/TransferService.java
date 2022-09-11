package com.soybean.mall.order.dszt;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.erp.api.constant.DeviceTypeEnum;
import com.wanmi.sbc.erp.api.provider.ShopCenterOrderProvider;
import com.wanmi.sbc.erp.api.provider.ShopCenterProductProvider;
import com.wanmi.sbc.erp.api.req.CreateOrderReq.BuyAddressReq;
import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.Date;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Maps;

import com.wanmi.sbc.erp.api.req.CreateOrderReq;
import com.wanmi.sbc.erp.api.resp.SalePlatformResp;
import com.wanmi.sbc.order.trade.model.entity.TradeState;
import com.wanmi.sbc.order.trade.model.entity.value.Consignee;
import com.wanmi.sbc.order.trade.model.entity.value.TradePrice;
import com.wanmi.sbc.order.trade.model.root.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description: 转换服务
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/9/10 2:21 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class TransferService {


    @Autowired
    private ShopCenterOrderProvider shopCenterOrderProvider;

    @Autowired
    private ShopCenterProductProvider shopCenterProductProvider;


    /**
     * 封装收获地址信息
     */
    private void packageAddress(Consignee consignee) {
        //订单收货地址
        BuyAddressReq buyAddressReq = new BuyAddressReq();
        buyAddressReq.setProvinceId(consignee.getProvinceName());
        buyAddressReq.setCityId("");
        buyAddressReq.setCountryId("");
        buyAddressReq.setFullAddress("");
        buyAddressReq.setAddressType("");
        buyAddressReq.setContactName("");
        buyAddressReq.setContactMobile("");
        buyAddressReq.setContactArea("");
        buyAddressReq.setContactGenders(0);
    }

    /**
     * Trade 转化成 CreateOrderReq
     * @param trade
     * @return
     */
    public CreateOrderReq trade2CreateOrderReq(Trade trade) {
        SalePlatformResp salePlatformResp = shopCenterProductProvider.getSalePlatform(null).getContext();
        if (salePlatformResp == null) {
            throw new SbcRuntimeException("999999", "获取平台渠道为空");
        }
        //汇率转换比率
        BigDecimal exchangeRate = new BigDecimal("100");

        //费用信息
        TradePrice tradePrice = trade.getTradePrice();
        BigDecimal postFee = tradePrice.getDeliveryPrice() == null ? BigDecimal.ZERO : tradePrice.getDeliveryPrice();
        postFee = postFee.multiply(exchangeRate);

        //订单状态
        TradeState tradeState = trade.getTradeState();


        CreateOrderReq createOrderReq = new CreateOrderReq();
        createOrderReq.setPlatformCode(trade.getId());
        createOrderReq.setOrderSource("XX_MALL");
        createOrderReq.setUserId(0L);
        createOrderReq.setBuyerMemo(trade.getBuyerRemark());
        createOrderReq.setDeviceType(DeviceTypeEnum.WEB.getType());
        createOrderReq.setSaleChannelId(salePlatformResp.getSaleChannelId()); //TODO
        createOrderReq.setPostFee(postFee.intValue());
        createOrderReq.setPayTimeOut(trade.getOrderTimeOut());
        createOrderReq.setBuyGoodsBOS(Lists.newArrayList());
        createOrderReq.setBuyAddressBO(new BuyAddressReq());
        createOrderReq.setShopId(salePlatformResp.getShopCode());
        createOrderReq.setSellerMemo(trade.getSellerRemark());
        createOrderReq.setBookModel(1);
        createOrderReq.setBookTime(tradeState.getCreateTime());
        //存S R
        Map<String, String> SandR = new HashMap<>();
        SandR.put("source", trade.getSource());
        SandR.put("promoteUserId", trade.getPromoteUserId());
        createOrderReq.setOrderSnapshot(SandR);


        return createOrderReq;
    }


}
