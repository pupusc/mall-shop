package com.soybean.mall.order.dszt;
import com.wanmi.sbc.common.enums.GenderType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.customer.api.provider.detail.CustomerDetailQueryProvider;
import com.wanmi.sbc.customer.api.request.detail.CustomerDetailByCustomerIdRequest;
import com.wanmi.sbc.customer.api.response.detail.CustomerDetailGetCustomerIdResponse;
import com.wanmi.sbc.erp.api.constant.DeviceTypeEnum;
import com.wanmi.sbc.erp.api.provider.ShopCenterOrderProvider;
import com.wanmi.sbc.erp.api.provider.ShopCenterProductProvider;
import com.wanmi.sbc.erp.api.req.CreateOrderReq.BuyAddressReq;
import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import com.wanmi.sbc.erp.api.req.CreateOrderReq;
import com.wanmi.sbc.erp.api.resp.SalePlatformResp;
import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.entity.TradeState;
import com.wanmi.sbc.order.trade.model.entity.value.Buyer;
import com.wanmi.sbc.order.trade.model.entity.value.Consignee;
import com.wanmi.sbc.order.trade.model.entity.value.TradePrice;
import com.wanmi.sbc.order.trade.model.root.Trade;
import org.apache.commons.collections.CollectionUtils;
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

    @Autowired
    private CustomerDetailQueryProvider customerDetailQueryProvider;

    //转换率
    private static BigDecimal exchangeRate = new BigDecimal("100");


    /**
     * 封装收获地址信息
     */
    private CreateOrderReq.BuyAddressReq packageAddress(Consignee consignee, CustomerDetailGetCustomerIdResponse customerDetail) {

        //订单收货地址
        CreateOrderReq.BuyAddressReq buyAddressReq = new CreateOrderReq.BuyAddressReq();
        buyAddressReq.setProvinceId(consignee.getProvinceName());
        buyAddressReq.setCityId(consignee.getCityName());
        buyAddressReq.setCountryId(consignee.getAreaName());
        buyAddressReq.setFullAddress(consignee.getDetailAddress());
        buyAddressReq.setAddressType("ORDER");
        buyAddressReq.setContactName(consignee.getName());
        buyAddressReq.setContactMobile(consignee.getPhone());
        buyAddressReq.setContactArea("+86");
        // 1女 2 男 3 未知
        Integer gender = 3;
        if (customerDetail.getGender() != null && customerDetail.getGender() == GenderType.FEMALE) {
            gender = 1;
        }
        if (customerDetail.getGender() != null && customerDetail.getGender() == GenderType.MALE) {
            gender = 2;
        }
        buyAddressReq.setContactGenders(gender);
        return buyAddressReq;
    }


    /**
     * 打包商品信息
     * @param tradeItems
     * @return
     */
    private List<CreateOrderReq.BuyGoodsReq> packageSku(List<TradeItem> tradeItems) {
        List<CreateOrderReq.BuyGoodsReq> result = new ArrayList<>();
        for (TradeItem tradeItem : tradeItems) {
            CreateOrderReq.BuyGoodsReq buyGoodsReq = new CreateOrderReq.BuyGoodsReq();
            buyGoodsReq.setGoodsCode(tradeItem.getErpSkuNo());
            buyGoodsReq.setNum(tradeItem.getNum().intValue());
            BigDecimal marketPrice;
            if (tradeItem.getMarketPrice() == null && tradeItem.getOriginalPrice() != null) {
                //兼容购物车
                marketPrice = tradeItem.getOriginalPrice();
            } else {
                marketPrice = tradeItem.getMarketPrice() == null ? BigDecimal.ZERO : tradeItem.getMarketPrice();
            }
            BigDecimal newMarketPrice = marketPrice.multiply(exchangeRate);
            buyGoodsReq.setPrice(newMarketPrice.intValue());
            buyGoodsReq.setGiftFlag(0); //非赠送

            //营销价格
            if (CollectionUtils.isNotEmpty(tradeItem.getMarketingSettlements())) {
                List<CreateOrderReq.BuyDiscountReq> buyDiscountReqList = new ArrayList<>();
                //优惠券
                if (!CollectionUtils.isEmpty(tradeItem.getCouponSettlements())) {
                    for (TradeItem.CouponSettlement couponSettlement : tradeItem.getCouponSettlements()) {
                        CreateOrderReq.BuyDiscountReq buyDiscountReq = new CreateOrderReq.BuyDiscountReq();
                        BigDecimal discount = couponSettlement.getReducePrice().multiply(exchangeRate);
                        buyDiscountReq.setAmount(discount.intValue());
                        buyDiscountReq.setCouponId(couponSettlement.getCouponCodeId());
                        buyDiscountReq.setDiscountNo(couponSettlement.getCouponCode());
                        buyDiscountReq.setDiscountName("");
                        buyDiscountReq.setChangeType("DISCOUNT"); //优惠
                        buyDiscountReq.setMemo("优惠券");
                        buyDiscountReq.setCostAssume("CHANNEL");
                        buyDiscountReqList.add(buyDiscountReq);
                    }
                }

            for (TradeItem.MarketingSettlement marketingSettlement : tradeItem.getMarketingSettlements()) {
                CreateOrderReq.BuyDiscountReq buyDiscountReq = new CreateOrderReq.BuyDiscountReq();
                BigDecimal tmpPrice = tradeItem.getPrice().multiply(new BigDecimal(tradeItem.getNum()+""));
                BigDecimal tmpMarketingPrice = tmpPrice.subtract(marketingSettlement.getSplitPrice() == null ? BigDecimal.ZERO : marketingSettlement.getSplitPrice());
                tmpMarketingPrice = tmpMarketingPrice.multiply(exchangeRate);
                buyDiscountReq.setAmount(tmpMarketingPrice.intValue());
                buyDiscountReq.setCouponId("");
                buyDiscountReq.setDiscountNo("");
                buyDiscountReq.setDiscountName("");
                buyDiscountReq.setChangeType("DISCOUNT"); //优惠
                buyDiscountReq.setMemo("营销");
                buyDiscountReq.setCostAssume("CHANNEL");
                buyDiscountReqList.add(buyDiscountReq);                }
            }
            buyGoodsReq.setGoodsDiscounts(Lists.newArrayList());
            result.add(buyGoodsReq);
        }
        return result;
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

        //获取用户信息
        Buyer buyer = trade.getBuyer();

        CustomerDetailByCustomerIdRequest customerDetailReq = new CustomerDetailByCustomerIdRequest();
        customerDetailReq.setCustomerId(buyer.getId());
        CustomerDetailGetCustomerIdResponse customerDetail =
                customerDetailQueryProvider.getCustomerDetailByCustomerId(customerDetailReq).getContext();
        if (customerDetail == null) {
            throw new SbcRuntimeException("999999", "当前获取客户信息为null");
        }



        //费用信息
        TradePrice tradePrice = trade.getTradePrice();
        BigDecimal postFee = tradePrice.getDeliveryPrice() == null ? BigDecimal.ZERO : tradePrice.getDeliveryPrice();
        postFee = postFee.multiply(exchangeRate);

        //订单状态
        TradeState tradeState = trade.getTradeState();

        //收货
        Consignee consignee = trade.getConsignee();

        //商品列表
        List<TradeItem> tradeItems = trade.getTradeItems();
        if (CollectionUtils.isEmpty(tradeItems)) {
            throw new SbcRuntimeException("999999", "购买的商品列表");
        }

        CreateOrderReq createOrderReq = new CreateOrderReq();
        createOrderReq.setPlatformCode(trade.getId());
        createOrderReq.setOrderSource("XX_MALL");
        createOrderReq.setUserId(0L);
        createOrderReq.setBuyerMemo(trade.getBuyerRemark());
        createOrderReq.setDeviceType(DeviceTypeEnum.WEB.getType());
        createOrderReq.setSaleChannelId(salePlatformResp.getSaleChannelId()); //TODO
        createOrderReq.setPostFee(postFee.intValue());
        createOrderReq.setPayTimeOut(trade.getOrderTimeOut());
        createOrderReq.setBuyGoodsBOS(this.packageSku(tradeItems));
        createOrderReq.setBuyAddressBO(this.packageAddress(consignee, customerDetail));
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
