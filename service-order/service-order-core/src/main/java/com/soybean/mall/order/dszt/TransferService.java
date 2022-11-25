package com.soybean.mall.order.dszt;

import com.alibaba.fastjson.JSON;
import com.soybean.mall.order.enums.OrderSourceEnum;
import com.soybean.mall.order.enums.PaymentPayTypeEnum;
import com.soybean.mall.order.enums.UnifiedOrderChangeTypeEnum;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.ChannelType;
import com.wanmi.sbc.common.enums.GenderType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.detail.CustomerDetailQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.request.detail.CustomerDetailByCustomerIdRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.api.response.detail.CustomerDetailGetCustomerIdResponse;
import com.wanmi.sbc.erp.api.constant.DeviceTypeEnum;
import com.wanmi.sbc.erp.api.provider.ShopCenterOrderProvider;
import com.wanmi.sbc.erp.api.provider.ShopCenterProductProvider;
import com.wanmi.sbc.erp.api.req.CreateOrderReq;
import com.wanmi.sbc.erp.api.req.SaleAfterCreateNewReq;
import com.wanmi.sbc.erp.api.req.SaleAfterCreateNewReq.SaleAfterOrderReq;
import com.wanmi.sbc.erp.api.req.SaleAfterCreateNewReq.SaleAfterPostFeeReq;
import com.wanmi.sbc.erp.api.req.SalePlatformQueryReq;
import com.wanmi.sbc.erp.api.resp.OrderDetailResp;
import com.wanmi.sbc.erp.api.resp.SalePlatformResp;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.order.api.enums.MiniProgramSceneType;
import com.wanmi.sbc.order.bean.enums.OutTradePlatEnum;
import com.wanmi.sbc.order.bean.enums.PayState;
import com.wanmi.sbc.order.bean.enums.ReturnReason;
import com.wanmi.sbc.order.returnorder.model.entity.ReturnItem;
import com.wanmi.sbc.order.returnorder.model.root.ReturnOrder;
import com.wanmi.sbc.order.returnorder.model.value.ReturnDeliveryDetailPrice;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.entity.TradeState;
import com.wanmi.sbc.order.trade.model.entity.value.Buyer;
import com.wanmi.sbc.order.trade.model.entity.value.Consignee;
import com.wanmi.sbc.order.trade.model.entity.value.TradePrice;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.pay.api.provider.PayQueryProvider;
import com.wanmi.sbc.pay.api.request.TradeRecordByOrderCodeRequest;
import com.wanmi.sbc.pay.api.response.PayTradeRecordResponse;
import com.wanmi.sbc.setting.api.provider.platformaddress.PlatformAddressQueryProvider;
import com.wanmi.sbc.setting.api.request.platformaddress.PlatformAddressListRequest;
import com.wanmi.sbc.setting.api.response.platformaddress.PlatformAddressListResponse;
import com.wanmi.sbc.setting.bean.vo.PlatformAddressVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Description: 转换服务
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/9/10 2:21 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Slf4j
@Service
public class TransferService {



    @Autowired
    private ShopCenterProductProvider shopCenterProductProvider;

    @Autowired
    private CustomerDetailQueryProvider customerDetailQueryProvider;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private PayQueryProvider payQueryProvider;

    @Autowired
    private PlatformAddressQueryProvider platformAddressQueryProvider;

    @Autowired
    private ShopCenterOrderProvider shopCenterOrderProvider;

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    //转换率
    private static BigDecimal exchangeRate = new BigDecimal("100");



    /**
     * 封装收获地址信息
     */
    private CreateOrderReq.BuyAddressReq packageAddress(Trade trade, Consignee consignee, CustomerDetailGetCustomerIdResponse customerDetail) {

        //获取省市信息
        String provinceName = consignee.getProvinceName();
        String cityName = consignee.getCityName();
        String areaName = consignee.getAreaName();
        PlatformAddressListRequest platformAddressListRequest = new PlatformAddressListRequest();
        platformAddressListRequest.setIdList(
                Arrays.asList(consignee.getProvinceId().toString(), consignee.getCityId().toString(), consignee.getAreaId().toString()));
        platformAddressListRequest.setLeafFlag(false);
        PlatformAddressListResponse platformAddressListResponse =
                platformAddressQueryProvider.list(platformAddressListRequest).getContext();
        List<PlatformAddressVO> platformAddressVOList = platformAddressListResponse.getPlatformAddressVOList();
        if (CollectionUtils.isNotEmpty(platformAddressVOList)) {
            for (PlatformAddressVO platformAddressVO : platformAddressVOList) {
                if (Objects.equals(platformAddressVO.getId(), consignee.getProvinceId().toString())) {
                    provinceName = platformAddressVO.getAddrName();
                }
                if (Objects.equals(platformAddressVO.getId(), consignee.getCityId().toString())) {
                    cityName = platformAddressVO.getAddrName();
                }
                if (Objects.equals(platformAddressVO.getId(), consignee.getAreaId().toString())) {
                    areaName = platformAddressVO.getAddrName();
                }
            }
        }

        //订单收货地址
        CreateOrderReq.BuyAddressReq buyAddressReq = new CreateOrderReq.BuyAddressReq();
        buyAddressReq.setProvinceId(provinceName);
        buyAddressReq.setCityId(cityName);
        buyAddressReq.setCountryId(areaName);
        buyAddressReq.setFullAddress(consignee.getDetailAddress());
        buyAddressReq.setAddressType("ORDER");
        buyAddressReq.setContactName(consignee.getName());
        if (!StringUtils.isEmpty(trade.getDirectChargeMobile())) {
            buyAddressReq.setContactMobile(trade.getDirectChargeMobile());
        } else {
            buyAddressReq.setContactMobile(consignee.getPhone());
        }
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
    private List<CreateOrderReq.BuyGoodsReq> packageSku(List<TradeItem> tradeItems, List<TradeItem> giftsTradeItems, OrderSourceEnum orderSourceEnum, TradePrice tradePrice) {
        List<CreateOrderReq.BuyGoodsReq> result = new ArrayList<>();

        //打包商品
        Map<String, List<TradeItem>> spuId2ModelMap = new HashMap<>();
        for (TradeItem tradeItem : tradeItems) {
            if (StringUtils.isEmpty(tradeItem.getPackId())) {
                continue;
            }
            List<TradeItem> tradeItemList = spuId2ModelMap.get(tradeItem.getPackId());
            if (tradeItemList == null) {
                tradeItemList = new ArrayList<>();
                spuId2ModelMap.put(tradeItem.getPackId(), tradeItemList);
            }
            tradeItemList.add(tradeItem);
        }

//        Map<String, List<GoodsPackDetailResponse>> packageId2ModelMap = new HashMap<>();
//        if (spuId2ModelMap.size() > 0) {
//            BaseResponse<List<GoodsPackDetailResponse>> packResponse =
//                    goodsQueryProvider.listPackDetailByPackIds(new PackDetailByPackIdsRequest(new ArrayList<>(spuId2ModelMap.keySet())));
//            List<GoodsPackDetailResponse> goodsPackDetailResponses = packResponse.getContext();
//            for (GoodsPackDetailResponse goodsPackDetailRespon : goodsPackDetailResponses) {
//                List<GoodsPackDetailResponse> goodsPackDetailResponseList = packageId2ModelMap.get(goodsPackDetailRespon.getPackId());
//                if (goodsPackDetailResponseList == null) {
//                    goodsPackDetailResponseList = new ArrayList<>();
//                    packageId2ModelMap.put(goodsPackDetailRespon.getPackId(), goodsPackDetailResponseList);
//                }
//                goodsPackDetailResponseList.add(goodsPackDetailRespon);
//            }
//        }


        for (Map.Entry<String, List<TradeItem>> entry : spuId2ModelMap.entrySet()) {
            TradeItem packageTradeItem = null;
            for (TradeItem tradeItem : entry.getValue()) {
                if (Objects.equals(entry.getKey(), tradeItem.getSpuId())) {
                    packageTradeItem = tradeItem;
                    break;
                }
            }

            if (packageTradeItem == null) {
                throw new SbcRuntimeException("999999", "组合商品的主商品没有找到");
            }
            if (packageTradeItem.getOriginalPrice() == null) {
                throw new SbcRuntimeException("999999", "组合商品的主商品售价为空");
            }

            //是否有会员价
            boolean hasVipPrice = packageTradeItem.getLevelPrice() != null && packageTradeItem.getLevelPrice().compareTo(BigDecimal.ZERO) > 0;
            //商品支付价
            BigDecimal allPayPrice = entry.getValue().stream().map(item -> {
                BigDecimal price = BigDecimal.ZERO;
                if (item.getSplitPrice() != null) {
                    price = price.add(item.getSplitPrice());
                }
                if (item.getPoints() != null) {
                    price = price.add(new BigDecimal(item.getPoints().toString()).divide(exchangeRate));
                }
                if (item.getKnowledge() != null) {
                    price = price.add(new BigDecimal(item.getKnowledge().toString()).divide(exchangeRate));
                }
                return price;
            }).reduce(BigDecimal.ZERO, BigDecimal::add);

            for (TradeItem tradeItem : entry.getValue()) {
//                if (Objects.equals(entry.getKey(), tradeItem.getSpuId())) {
//                    continue;
//                }

                //获取打包对应的折扣信息
//                List<GoodsPackDetailResponse> goodsPackDetailResponses = packageId2ModelMap.get(tradeItem.getPackId());
//                Map<String, GoodsPackDetailResponse> collect =
//                        goodsPackDetailResponses.stream().collect(Collectors.toMap(GoodsPackDetailResponse::getGoodsId, Function.identity(), (k1, k2) -> k1));
//                GoodsPackDetailResponse goodsPackDetailResponse = collect.get(tradeItem.getSpuId());

                CreateOrderReq.BuyGoodsReq buyGoodsReq = new CreateOrderReq.BuyGoodsReq();
                buyGoodsReq.setPlatformItemId(tradeItem.getOid());
                buyGoodsReq.setGoodsCode(StringUtils.isEmpty(tradeItem.getErpSkuNo()) || tradeItem.getErpSkuNo().startsWith("zh") ? tradeItem.getErpSpuNo() : tradeItem.getErpSkuNo());
                buyGoodsReq.setNum(tradeItem.getNum().intValue());
                buyGoodsReq.setPlatformGoodsId(tradeItem.getSpuId());
                buyGoodsReq.setPlatformGoodsName(tradeItem.getSpuName());
                buyGoodsReq.setPlatformSkuId(tradeItem.getSkuId());
                buyGoodsReq.setPlatformSkuName(tradeItem.getSkuName());
//                BigDecimal  price = tradeItem.getSplitPrice() == null ? BigDecimal.ZERO : tradeItem.getSplitPrice();
//                BigDecimal newPrice = price.multiply(exchangeRate);
//                buyGoodsReq.setPrice(newPrice.intValue());
//                buyGoodsReq.setGiftFlag(0); //非赠送
//                result.add(buyGoodsReq);

                buyGoodsReq.setGiftFlag(0); //非赠送
                buyGoodsReq.setGoodsDiscounts(new ArrayList<>()); //优惠信息
                //计算价格
                BigDecimal payPrice = BigDecimal.ZERO;
                if (tradeItem.getSplitPrice() != null) {
                    payPrice = payPrice.add(tradeItem.getSplitPrice());
                }
                if (tradeItem.getPoints() != null) {
                    payPrice = payPrice.add(new BigDecimal(tradeItem.getPoints().toString()).divide(exchangeRate));
                }
                if (tradeItem.getKnowledge() != null) {
                    payPrice = payPrice.add(new BigDecimal(tradeItem.getKnowledge().toString()).divide(exchangeRate));
                }
                //支付比例
                BigDecimal ratio = payPrice.divide(allPayPrice, 100, RoundingMode.HALF_DOWN);
                //反推原始价格
                BigDecimal originPrice = payPrice;

                //如果分摊是0元，则跳过优惠计算，根据分摊计算优惠也是0
                if (payPrice.compareTo(BigDecimal.ZERO) < 1) {
                    buyGoodsReq.setPrice(originPrice.divide(new BigDecimal(tradeItem.getNum().toString())).multiply(exchangeRate).intValue());
                    result.add(buyGoodsReq);
                    continue;
                }

                //会员价减免金额
                if (hasVipPrice) {
                    //减免金额
                    BigDecimal allDiscount = packageTradeItem.getOriginalPrice().subtract(packageTradeItem.getLevelPrice()).multiply(new BigDecimal(packageTradeItem.getNum().toString()));
                    BigDecimal discount = ratio.multiply(allDiscount);
                    originPrice = originPrice.add(discount); //反推原始价格

                    CreateOrderReq.BuyDiscountReq buyDiscountReq = new CreateOrderReq.BuyDiscountReq();
                    buyDiscountReq.setAmount(discount.multiply(exchangeRate).intValue());
                    buyDiscountReq.setCouponId("");
                    buyDiscountReq.setDiscountNo("");
                    buyDiscountReq.setDiscountName(UnifiedOrderChangeTypeEnum.VIP_PRICE_DIFF.getMsg());
                    buyDiscountReq.setChangeType(UnifiedOrderChangeTypeEnum.VIP_PRICE_DIFF.getType()); //优惠
                    buyDiscountReq.setMemo("会员价");
                    buyDiscountReq.setCostAssume("CHANNEL");
                    buyGoodsReq.getGoodsDiscounts().add(buyDiscountReq);
                }
                //优惠券减免金额
                if (!CollectionUtils.isEmpty(packageTradeItem.getCouponSettlements())) {
                    for (TradeItem.CouponSettlement couponSettlement : packageTradeItem.getCouponSettlements()) {
                        //减免金额
                        BigDecimal allDiscount = couponSettlement.getReducePrice();
                        BigDecimal discount = ratio.multiply(allDiscount);
                        originPrice = originPrice.add(discount); //反推原始价格

                        CreateOrderReq.BuyDiscountReq buyDiscountReq = new CreateOrderReq.BuyDiscountReq();
                        buyDiscountReq.setAmount(discount.multiply(exchangeRate).intValue());
                        buyDiscountReq.setCouponId(couponSettlement.getCouponCodeId());
                        buyDiscountReq.setDiscountNo(couponSettlement.getCouponCode());
                        buyDiscountReq.setDiscountName(UnifiedOrderChangeTypeEnum.DISCOUNT.getMsg()); //优惠券别称
                        buyDiscountReq.setChangeType(UnifiedOrderChangeTypeEnum.DISCOUNT.getType()); //优惠
                        buyDiscountReq.setMemo("优惠券");
                        buyDiscountReq.setCostAssume("CHANNEL");
                        buyGoodsReq.getGoodsDiscounts().add(buyDiscountReq);
                    }
                }
                //活动减免金额
                if (CollectionUtils.isNotEmpty(packageTradeItem.getMarketingSettlements())) {
                    for (TradeItem.MarketingSettlement marketingSettlement : packageTradeItem.getMarketingSettlements()) {
                        //基础价
                        BigDecimal allBasePrice = (hasVipPrice ? packageTradeItem.getLevelPrice() : packageTradeItem.getOriginalPrice()).multiply(new BigDecimal(packageTradeItem.getNum().toString()));
                        BigDecimal allDiscountPrice = allBasePrice.subtract(marketingSettlement.getSplitPrice() == null ? BigDecimal.ZERO : marketingSettlement.getSplitPrice());
                        BigDecimal discount = ratio.multiply(allDiscountPrice);
                        originPrice = originPrice.add(discount); //反推原始价格

                        CreateOrderReq.BuyDiscountReq buyDiscountReq = new CreateOrderReq.BuyDiscountReq();
                        buyDiscountReq.setAmount(discount.multiply(exchangeRate).intValue());
                        buyDiscountReq.setCouponId("");
                        buyDiscountReq.setDiscountNo("");
                        buyDiscountReq.setDiscountName(marketingSettlement.getMarketingType().toString());
                        buyDiscountReq.setChangeType(UnifiedOrderChangeTypeEnum.MARKETING.getType()); //优惠
                        buyDiscountReq.setMemo("营销活动");
                        buyDiscountReq.setCostAssume("CHANNEL");
                        buyGoodsReq.getGoodsDiscounts().add(buyDiscountReq);
                    }
                }

                buyGoodsReq.setPrice(originPrice.divide(new BigDecimal(tradeItem.getNum().toString())).multiply(exchangeRate).intValue());
                result.add(buyGoodsReq);
            }

//            //打包的商品
//            //获取打包对应的折扣信息
//            CreateOrderReq.BuyGoodsReq buyGoodsReq = new CreateOrderReq.BuyGoodsReq();
//            buyGoodsReq.setPlatformItemId(packageTradeItem.getOid());
//            buyGoodsReq.setGoodsCode(StringUtils.isEmpty(packageTradeItem.getErpSkuNo()) || packageTradeItem.getErpSkuNo().startsWith("zh") ? packageTradeItem.getErpSpuNo() : packageTradeItem.getErpSkuNo());
//            buyGoodsReq.setNum(packageTradeItem.getNum().intValue());
//            buyGoodsReq.setPlatformGoodsId(packageTradeItem.getSpuId());
//            buyGoodsReq.setPlatformGoodsName(packageTradeItem.getSpuName());
//            buyGoodsReq.setPlatformSkuId(packageTradeItem.getSkuId());
//            buyGoodsReq.setPlatformSkuName(packageTradeItem.getSkuName());
//            BigDecimal  price = packageTradeItem.getSplitPrice() == null ? BigDecimal.ZERO : packageTradeItem.getSplitPrice();
//            BigDecimal newPrice = price.multiply(exchangeRate);
//            buyGoodsReq.setPrice(newPrice.intValue());
//            buyGoodsReq.setGiftFlag(0); //非赠送
//            result.add(buyGoodsReq);
        }

        //非打包商品
        for (TradeItem tradeItem : tradeItems) {

            //判断是否为打包商品
            if (!StringUtils.isEmpty(tradeItem.getPackId())) {
                continue;
            }
            result.add(this.packageBuyGoodsReq(tradeItem, orderSourceEnum, 0, tradePrice));
        }

        //赠品
        for (TradeItem giftsTradeItem : giftsTradeItems) {
            result.add(this.packageBuyGoodsReq(giftsTradeItem, orderSourceEnum, 1, tradePrice));
        }
        return result;
    }


    /**
     * 获取BuyGoodsReq对象信息
     * @param tradeItem
     * @param
     * @param giftFlag  是否赠送, 1 赠送，0 非赠送，2 补偿 【0元非赠送】
     * @return
     */
    private CreateOrderReq.BuyGoodsReq packageBuyGoodsReq(TradeItem tradeItem, OrderSourceEnum orderSourceEnum, Integer giftFlag, TradePrice tradePrice) {
        CreateOrderReq.BuyGoodsReq buyGoodsReq = new CreateOrderReq.BuyGoodsReq();
        buyGoodsReq.setPlatformItemId(tradeItem.getOid());
        buyGoodsReq.setGoodsCode(StringUtils.isEmpty(tradeItem.getErpSkuNo()) || tradeItem.getErpSkuNo().startsWith("zh") ? tradeItem.getErpSpuNo() : tradeItem.getErpSkuNo());
        buyGoodsReq.setNum(tradeItem.getNum().intValue());
        buyGoodsReq.setPlatformGoodsId(tradeItem.getSpuId());
        buyGoodsReq.setPlatformGoodsName(tradeItem.getSpuName());
        buyGoodsReq.setPlatformSkuId(tradeItem.getSkuId());
        buyGoodsReq.setPlatformSkuName(tradeItem.getSkuName());
        BigDecimal  originalPrice = tradeItem.getOriginalPrice() == null ? BigDecimal.ZERO : tradeItem.getOriginalPrice();
        BigDecimal newOriginalPrice = originalPrice.multiply(exchangeRate);
        buyGoodsReq.setPrice(newOriginalPrice.intValue());
        buyGoodsReq.setGiftFlag(giftFlag); //非赠送

        //优惠
        List<CreateOrderReq.BuyDiscountReq> buyDiscountReqList = new ArrayList<>();
        if (Objects.equals(orderSourceEnum, OrderSourceEnum.PLATFORM_MALL)) {
            //履约订单价格优惠没有设置，这里强制给设置成会员优惠
            if (newOriginalPrice.compareTo(BigDecimal.ZERO) > 0) {
                CreateOrderReq.BuyDiscountReq buyDiscountReq = new CreateOrderReq.BuyDiscountReq();
                buyDiscountReq.setAmount(newOriginalPrice.intValue());
                buyDiscountReq.setCouponId("");
                buyDiscountReq.setDiscountNo("");
                buyDiscountReq.setDiscountName(UnifiedOrderChangeTypeEnum.VIP_PRICE_DIFF.getMsg());
                buyDiscountReq.setChangeType(UnifiedOrderChangeTypeEnum.VIP_PRICE_DIFF.getType()); //优惠
                buyDiscountReq.setMemo("会员价");
                buyDiscountReq.setCostAssume("CHANNEL");
                buyDiscountReqList.add(buyDiscountReq);
            }
        } else {
            //会员价格
            if (tradeItem.getOriginalPrice().compareTo(tradeItem.getPrice()) > 0) {
                CreateOrderReq.BuyDiscountReq buyDiscountReq = new CreateOrderReq.BuyDiscountReq();
                BigDecimal tmpPrice = tradeItem.getOriginalPrice().subtract(tradeItem.getPrice());
                tmpPrice = tmpPrice.multiply(exchangeRate).multiply(new BigDecimal(tradeItem.getNum().toString()));
                buyDiscountReq.setAmount(tmpPrice.intValue());
                buyDiscountReq.setCouponId("");
                buyDiscountReq.setDiscountNo("");
                buyDiscountReq.setDiscountName(UnifiedOrderChangeTypeEnum.VIP_PRICE_DIFF.getMsg());
                buyDiscountReq.setChangeType(UnifiedOrderChangeTypeEnum.VIP_PRICE_DIFF.getType()); //优惠
                buyDiscountReq.setMemo("会员价");
                buyDiscountReq.setCostAssume("CHANNEL");
                buyDiscountReqList.add(buyDiscountReq);
            }
        }


        //优惠券
        if (!CollectionUtils.isEmpty(tradeItem.getCouponSettlements())) {
            for (TradeItem.CouponSettlement couponSettlement : tradeItem.getCouponSettlements()) {
                CreateOrderReq.BuyDiscountReq buyDiscountReq = new CreateOrderReq.BuyDiscountReq();
                BigDecimal discount = couponSettlement.getReducePrice().multiply(exchangeRate);
                buyDiscountReq.setAmount(discount.intValue());
                buyDiscountReq.setCouponId(couponSettlement.getCouponCodeId());
                buyDiscountReq.setDiscountNo(couponSettlement.getCouponCode());
                buyDiscountReq.setDiscountName(UnifiedOrderChangeTypeEnum.DISCOUNT.getMsg()); //优惠券别称
                buyDiscountReq.setChangeType(UnifiedOrderChangeTypeEnum.DISCOUNT.getType()); //优惠
                buyDiscountReq.setMemo("优惠券");
                buyDiscountReq.setCostAssume("CHANNEL");
                buyDiscountReqList.add(buyDiscountReq);
            }
        }

        //营销价格
        if (CollectionUtils.isNotEmpty(tradeItem.getMarketingSettlements())) {
            for (TradeItem.MarketingSettlement marketingSettlement : tradeItem.getMarketingSettlements()) {
                CreateOrderReq.BuyDiscountReq buyDiscountReq = new CreateOrderReq.BuyDiscountReq();
                BigDecimal tmpPrice = tradeItem.getPrice().multiply(new BigDecimal(tradeItem.getNum()+""));
                BigDecimal tmpMarketingPrice = tmpPrice.subtract(marketingSettlement.getSplitPrice() == null ? BigDecimal.ZERO : marketingSettlement.getSplitPrice());
                tmpMarketingPrice = tmpMarketingPrice.multiply(exchangeRate);
                buyDiscountReq.setAmount(tmpMarketingPrice.intValue());
                buyDiscountReq.setCouponId("");
                buyDiscountReq.setDiscountNo("");
                buyDiscountReq.setDiscountName(marketingSettlement.getMarketingType().toString());
                buyDiscountReq.setChangeType(UnifiedOrderChangeTypeEnum.MARKETING.getType()); //优惠
                buyDiscountReq.setMemo("营销活动");
                buyDiscountReq.setCostAssume("CHANNEL");
                buyDiscountReqList.add(buyDiscountReq);
            }
        }

        /**
         * 非赠品
         */
        if (Objects.equals(giftFlag, 0) && tradePrice.getPrivilegePrice() != null && tradePrice.getPrivilegePrice().compareTo(BigDecimal.ZERO) > 0) {

            BigDecimal discountAmount = BigDecimal.ZERO;
            for (CreateOrderReq.BuyDiscountReq buyDiscountReq : buyDiscountReqList) {
                discountAmount = discountAmount.add(new BigDecimal(buyDiscountReq.getAmount() + ""));
            }
            //改价
            long points = tradeItem.getPoints() == null ? 0 : tradeItem.getPoints();
            long knowledge = tradeItem.getKnowledge() == null ? 0 : tradeItem.getKnowledge();

            BigDecimal diffPrice = newOriginalPrice.subtract(discountAmount)
                    .subtract(new BigDecimal(points + ""))
                    .subtract(new BigDecimal(knowledge + "")).subtract(tradeItem.getSplitPrice());
            if (diffPrice.compareTo(BigDecimal.ZERO) > 0) {
                CreateOrderReq.BuyDiscountReq buyDiscountReq = new CreateOrderReq.BuyDiscountReq();
                buyDiscountReq.setAmount(diffPrice.intValue());
                buyDiscountReq.setCouponId("");
                buyDiscountReq.setDiscountNo("");
                buyDiscountReq.setDiscountName(UnifiedOrderChangeTypeEnum.CHANGE_PRICE.getMsg());
                buyDiscountReq.setChangeType(UnifiedOrderChangeTypeEnum.CHANGE_PRICE.getType()); //改价
                buyDiscountReq.setMemo("差价");
                buyDiscountReq.setCostAssume("CHANNEL");
                buyDiscountReqList.add(buyDiscountReq);
            }
        }

        buyGoodsReq.setGoodsDiscounts(buyDiscountReqList);
        return buyGoodsReq;
    }


    /**
     * 打包支付信息
     * @param trade
     * @return
     */
    private List<CreateOrderReq.BuyPaymentReq> packageBuyPayment(Trade trade, OrderSourceEnum orderSourceEnum) {

        List<CreateOrderReq.BuyPaymentReq> paymentReqList = new ArrayList<>();


        //支付金额
        BigDecimal amount = trade.getTradePrice().getTotalPrice().multiply(exchangeRate);
        if (Objects.equals(OrderSourceEnum.PLATFORM_MALL, orderSourceEnum)) {
            CreateOrderReq.BuyPaymentReq buyPaymentReq = new CreateOrderReq.BuyPaymentReq();
            buyPaymentReq.setPayType(PaymentPayTypeEnum.XIAN_JIN.getPayTypeCode().toString());

            buyPaymentReq.setAmount(BigDecimal.ZERO.intValue());
            buyPaymentReq.setMemo("支付");
            buyPaymentReq.setPayGateway(108);
            buyPaymentReq.setPayTradeNo("");
            buyPaymentReq.setPayMchid("");
            buyPaymentReq.setPayTime(trade.getTradeState().getPayTime());
            paymentReqList.add(buyPaymentReq);
        } else {

            if (amount.compareTo(BigDecimal.ZERO) > 0) {
                //获取支付流水和 商户号
                TradeRecordByOrderCodeRequest request = new TradeRecordByOrderCodeRequest();
                request.setOrderId(trade.getId());
                log.info("TransferService packageBuyPayment getTradeRecordByOrderCode param {}", JSON.toJSONString(request));
                BaseResponse<PayTradeRecordResponse> tradeRecordByOrderCode = payQueryProvider.getTradeRecordByOrderCode(request);
                log.info("TransferService packageBuyPayment result {}", JSON.toJSONString(tradeRecordByOrderCode));
                PayTradeRecordResponse payTradeRecordResponse = tradeRecordByOrderCode.getContext();

                CreateOrderReq.BuyPaymentReq buyPaymentReq = new CreateOrderReq.BuyPaymentReq();
                buyPaymentReq.setPayType(PaymentPayTypeEnum.XIAN_JIN.getPayTypeCode().toString());

                buyPaymentReq.setAmount(amount.intValue());
                buyPaymentReq.setMemo("支付");
                buyPaymentReq.setPayGateway(108);
                buyPaymentReq.setPayTradeNo(payTradeRecordResponse.getTradeNo());
                String appId = payTradeRecordResponse.getAppId();
                if (!StringUtils.isEmpty(payTradeRecordResponse.getAppId()) && payTradeRecordResponse.getAppId().contains("$")) {
                    appId = payTradeRecordResponse.getAppId().split("\\$")[1];
                }
                buyPaymentReq.setPayMchid(appId);
                buyPaymentReq.setPayTime(trade.getTradeState().getPayTime());
                paymentReqList.add(buyPaymentReq);
            }

            Long knowledge = trade.getTradePrice().getKnowledge();
            if (knowledge != null && knowledge > 0) {
                BigDecimal knowledgeDecimal = new BigDecimal(trade.getTradePrice().getKnowledge().toString());
                CreateOrderReq.BuyPaymentReq buyPaymentReq = new CreateOrderReq.BuyPaymentReq();
                buyPaymentReq.setPayType(PaymentPayTypeEnum.ZHI_DOU.getPayTypeCode().toString());

                buyPaymentReq.setAmount(knowledgeDecimal.intValue());
                buyPaymentReq.setMemo("知豆");
                buyPaymentReq.setPayGateway(108);
                buyPaymentReq.setPayTime(trade.getTradeState().getPayTime());
                paymentReqList.add(buyPaymentReq);
            }

            Long point = trade.getTradePrice().getPoints();
            if (point != null && point > 0) {
                BigDecimal pointDecimal = new BigDecimal(trade.getTradePrice().getPoints().toString());
                CreateOrderReq.BuyPaymentReq buyPaymentReq = new CreateOrderReq.BuyPaymentReq();
                buyPaymentReq.setPayType(PaymentPayTypeEnum.JI_FEN.getPayTypeCode().toString());

                buyPaymentReq.setAmount(pointDecimal.intValue());
                buyPaymentReq.setMemo("积分");
                buyPaymentReq.setPayGateway(108);
                buyPaymentReq.setPayTime(trade.getTradeState().getPayTime());
                paymentReqList.add(buyPaymentReq);
            }
        }
        return paymentReqList;
    }

    /**
     * Trade 转化成 CreateOrderReq
     * @param trade
     * @return
     */
    public CreateOrderReq trade2CreateOrderReq(Trade trade) {
        if (!Objects.equals(trade.getTradeState().getPayState(), PayState.PAID)) {
            log.info("TransferService trade2CreateOrderReq tradeId {} 当前支付状态为：{} 不推送", trade.getId(), trade.getTradeState().getPayState());
            return null;
        }

    	SalePlatformQueryReq salePlatformQueryReq = new SalePlatformQueryReq();
    	salePlatformQueryReq.setTid(21L);
        SalePlatformResp salePlatformResp = shopCenterProductProvider.getSalePlatform(salePlatformQueryReq).getContext();
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

        //订单状态
        TradeState tradeState = trade.getTradeState();

        //商品列表
        List<TradeItem> tradeItems = trade.getTradeItems();
        if (CollectionUtils.isEmpty(tradeItems)) {
            throw new SbcRuntimeException("999999", "购买的商品列表");
        }

        CreateOrderReq createOrderReq = new CreateOrderReq();

        createOrderReq.setPlatformOrderId(trade.getId());
        createOrderReq.setPlatformCode("WAN_MI");
        //订单来源
        OrderSourceEnum orderSourceEnum = OrderSourceEnum.H5_MALL;
        if (Objects.equals(trade.getChannelType(), ChannelType.MINIAPP)) {
            orderSourceEnum = OrderSourceEnum.MINIAPP_MALL;
            if (Objects.equals(trade.getMiniProgramScene(), MiniProgramSceneType.WECHAT_VIDEO.getIndex())) {
                orderSourceEnum = OrderSourceEnum.VIDEO_MALL;
            }
        }
        if (Objects.equals(trade.getOutTradePlat(), OutTradePlatEnum.FDDS_PERFORM.getCode())) {
            orderSourceEnum = OrderSourceEnum.PLATFORM_MALL;
        }

        createOrderReq.setOrderSource(orderSourceEnum.getCode());
//        createOrderReq.setUserId(Long.valueOf(customer.getFanDengUserNo()));
        //收货地址
        Consignee consignee = trade.getConsignee();

        CreateOrderReq.OrderUserInfoReq orderUserInfoReq = new CreateOrderReq.OrderUserInfoReq();

        if (!StringUtils.isEmpty(trade.getDirectChargeMobile())) {
            orderUserInfoReq.setArea("+86");
            orderUserInfoReq.setMobile(trade.getDirectChargeMobile());
        } else if (!StringUtils.isEmpty(consignee.getPhone())) {
            orderUserInfoReq.setArea("+86");
            orderUserInfoReq.setMobile(consignee.getPhone());
        } else {
            orderUserInfoReq.setUserId(getFddsUserNo(buyer.getId(), trade.isImportFlag()));
        }
        createOrderReq.setOrderUserInfoBO(orderUserInfoReq);
        createOrderReq.setBuyerMemo(trade.getBuyerRemark());
        createOrderReq.setDeviceType(DeviceTypeEnum.WEB.getType());
        createOrderReq.setSaleChannelId(salePlatformResp.getSaleChannelId());
        //费用信息
        TradePrice tradePrice = trade.getTradePrice();
        BigDecimal postFee = tradePrice.getDeliveryPrice() == null ? BigDecimal.ZERO : tradePrice.getDeliveryPrice();
        postFee = postFee.multiply(exchangeRate);
        createOrderReq.setPostFee(postFee.intValue());
        //支付信息
        createOrderReq.setBuyPaymentBO(this.packageBuyPayment(trade, orderSourceEnum));
        createOrderReq.setPayTimeOut(trade.getOrderTimeOut());
        //商品信息
        createOrderReq.setBuyGoodsBOS(this.packageSku(tradeItems, trade.getGifts(), orderSourceEnum, tradePrice));

        createOrderReq.setBuyAddressBO(this.packageAddress(trade, consignee, customerDetail));
        if (salePlatformResp.getTid() != null) {
        	createOrderReq.setShopId(salePlatformResp.getTid().toString());
        }
        createOrderReq.setSellerMemo(trade.getSellerRemark());
        createOrderReq.setBookModel(2); //支付后再下单
        //下单时间
        createOrderReq.setBookTime(tradeState.getCreateTime());
        //存S R
        Map<String, String> SandR = new HashMap<>();
        SandR.put("source", trade.getSource());
        SandR.put("promoteUserId", trade.getPromoteUserId());
        SandR.put("deductDoce", trade.getDeductCode());
        createOrderReq.setOrderSnapshot(SandR);
        return createOrderReq;
    }

    private Integer getFddsUserNo(String customerId, boolean importFlag) {
        CustomerGetByIdRequest customerGetByIdRequest = new CustomerGetByIdRequest();
        customerGetByIdRequest.setCustomerId(customerId);

        CustomerGetByIdResponse customer = null;
        try {
            customer = customerQueryProvider.getCustomerById(customerGetByIdRequest).getContext();
        } catch (Exception e) {
            if (importFlag) {
                log.warn("查询用户信息发生异常", e);
                return null;
            }
            throw e;
        }
        if (customer == null) {
            throw new SbcRuntimeException("999999", "当前获取客户信息为null");
        }
        return Integer.valueOf(customer.getFanDengUserNo());
    }


    /**
     * 转化售后订单对象
     * @param returnOrder
     * @return
     */
    public SaleAfterCreateNewReq changeSaleAfterCreateReq(ReturnOrder returnOrder) {
        SaleAfterCreateNewReq saleAfterCreateNewReq = new SaleAfterCreateNewReq();
        //根据订单id获取订单的详细信息
        OrderDetailResp context = shopCenterOrderProvider.detailByPlatformOrderId(returnOrder.getTid()).getContext();
        log.info("TransferService detailByPlatformOrderId context {}", JSON.toJSONString(context));
        if (context == null) {
            log.error("TransferService detailByPlatformOrderId context is null");
            return null;
        }
        //计算map，skuId2自订单信息
        Map<String, List<OrderDetailResp.OrderItemResp>> skuId2OrderItemMap = new HashMap<>();
        for (OrderDetailResp.OrderItemResp orderItemBO : context.getOrderItemBOS()) {
            List<OrderDetailResp.OrderItemResp> orderItemRespList = skuId2OrderItemMap.get(orderItemBO.getPlatformSkuId());
            if (CollectionUtils.isEmpty(orderItemRespList)) {
                orderItemRespList = new ArrayList<>();
                skuId2OrderItemMap.put(orderItemBO.getPlatformSkuId(), orderItemRespList);
            }
            orderItemRespList.add(orderItemBO);
        }
        //获取订单支付信息

        //商品
        Integer refundType = returnOrder.getReturnType().ordinal() == 0 ? 1 : 2;

        saleAfterCreateNewReq.setOrderNumber(context.getOrderNumber().toString());
        saleAfterCreateNewReq.setRefundTypeList(Arrays.asList(refundType)); //退货退款

        SaleAfterOrderReq saleAfterCreateReq = new SaleAfterOrderReq();
        saleAfterCreateReq.setPlatformRefundId(returnOrder.getId());
        saleAfterCreateReq.setApplyTime(returnOrder.getCreateTime());
        saleAfterCreateReq.setCloseTime(returnOrder.getFinishTime());
        saleAfterCreateReq.setMemo(returnOrder.getDescription());
        saleAfterCreateNewReq.setSaleAfterOrderBO(saleAfterCreateReq);

        SaleAfterPostFeeReq saleAfterPostFeeReq = new SaleAfterPostFeeReq();

        //运费
        List<SaleAfterCreateNewReq.SaleAfterRefundDetailReq> saleAfterFreeList = new ArrayList<>();
        if (Objects.equals(returnOrder.getReturnReason(), ReturnReason.PRICE_DELIVERY)) {
            saleAfterCreateNewReq.setRefundTypeList(Arrays.asList(5)); //主订单仅退款
            ReturnDeliveryDetailPrice returnDeliveryDetailPrice = returnOrder.getReturnPrice().getReturnDeliveryDetailPrice();
            if (returnDeliveryDetailPrice == null) {
                BigDecimal freightAmount = returnOrder.getReturnPrice().getApplyPrice().multiply(exchangeRate);
                SaleAfterCreateNewReq.SaleAfterRefundDetailReq saleAfterRefundDetailReq =
                        new SaleAfterCreateNewReq.SaleAfterRefundDetailReq();
                saleAfterRefundDetailReq.setPayType(PaymentPayTypeEnum.XIAN_JIN.getPayTypeCode());
                saleAfterRefundDetailReq.setAmount(freightAmount.intValue());
                saleAfterRefundDetailReq.setRefundReason(returnOrder.getReturnReason().getDesc());
                saleAfterFreeList.add(saleAfterRefundDetailReq);
            } else {
                if (returnDeliveryDetailPrice.getDeliveryPayPrice() != null && returnDeliveryDetailPrice.getDeliveryPayPrice().compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal freightAmount = returnDeliveryDetailPrice.getDeliveryPayPrice().multiply(exchangeRate);
                    SaleAfterCreateNewReq.SaleAfterRefundDetailReq saleAfterRefundDetailReq =
                            new SaleAfterCreateNewReq.SaleAfterRefundDetailReq();
                    saleAfterRefundDetailReq.setPayType(PaymentPayTypeEnum.XIAN_JIN.getPayTypeCode());
                    saleAfterRefundDetailReq.setAmount(freightAmount.intValue());
                    saleAfterRefundDetailReq.setRefundReason(returnOrder.getReturnReason().getDesc());
                    saleAfterFreeList.add(saleAfterRefundDetailReq);
                }
                if (returnDeliveryDetailPrice.getDeliveryPoint() != null && returnDeliveryDetailPrice.getDeliveryPoint() > 0) {
                    SaleAfterCreateNewReq.SaleAfterRefundDetailReq saleAfterRefundDetailReq =
                            new SaleAfterCreateNewReq.SaleAfterRefundDetailReq();
                    saleAfterRefundDetailReq.setPayType(PaymentPayTypeEnum.JI_FEN.getPayTypeCode());
                    saleAfterRefundDetailReq.setAmount(returnDeliveryDetailPrice.getDeliveryPoint().intValue());
                    saleAfterRefundDetailReq.setRefundReason(returnOrder.getReturnReason().getDesc());
                    saleAfterFreeList.add(saleAfterRefundDetailReq);
                }
            }

        } else if (returnOrder.getReturnPrice().getDeliverPrice() != null && returnOrder.getReturnPrice().getDeliverPrice().compareTo(BigDecimal.ZERO) > 0) {
//            SaleAfterCreateNewReq.SaleAfterRefundDetailReq saleAfterRefundDetailReq =
//                    new SaleAfterCreateNewReq.SaleAfterRefundDetailReq();
//            saleAfterRefundDetailReq.setPayType(PaymentPayTypeEnum.XIAN_JIN.getPayTypeCode());
//            saleAfterRefundDetailReq.setAmount(returnOrder.getReturnPrice().getDeliverPrice().multiply(exchangeRate).intValue());
//            saleAfterRefundDetailReq.setRefundReason(returnOrder.getRejectReason());
//            saleAfterFreeList.add(saleAfterRefundDetailReq);

            ReturnDeliveryDetailPrice returnDeliveryDetailPrice = returnOrder.getReturnPrice().getReturnDeliveryDetailPrice();
            if (returnDeliveryDetailPrice == null) {
                SaleAfterCreateNewReq.SaleAfterRefundDetailReq saleAfterRefundDetailReq =
                    new SaleAfterCreateNewReq.SaleAfterRefundDetailReq();
                saleAfterRefundDetailReq.setPayType(PaymentPayTypeEnum.XIAN_JIN.getPayTypeCode());
                saleAfterRefundDetailReq.setAmount(returnOrder.getReturnPrice().getDeliverPrice().multiply(exchangeRate).intValue());
                saleAfterRefundDetailReq.setRefundReason(returnOrder.getRejectReason());
                saleAfterFreeList.add(saleAfterRefundDetailReq);
            } else {
                if (returnDeliveryDetailPrice.getDeliveryPayPrice() != null && returnDeliveryDetailPrice.getDeliveryPayPrice().compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal freightAmount = returnDeliveryDetailPrice.getDeliveryPayPrice().multiply(exchangeRate);
                    SaleAfterCreateNewReq.SaleAfterRefundDetailReq saleAfterRefundDetailReq =
                            new SaleAfterCreateNewReq.SaleAfterRefundDetailReq();
                    saleAfterRefundDetailReq.setPayType(PaymentPayTypeEnum.XIAN_JIN.getPayTypeCode());
                    saleAfterRefundDetailReq.setAmount(freightAmount.intValue());
                    saleAfterRefundDetailReq.setRefundReason(returnOrder.getReturnReason().getDesc());
                    saleAfterFreeList.add(saleAfterRefundDetailReq);
                }
                if (returnDeliveryDetailPrice.getDeliveryPoint() != null && returnDeliveryDetailPrice.getDeliveryPoint() > 0) {
                    SaleAfterCreateNewReq.SaleAfterRefundDetailReq saleAfterRefundDetailReq =
                            new SaleAfterCreateNewReq.SaleAfterRefundDetailReq();
                    saleAfterRefundDetailReq.setPayType(PaymentPayTypeEnum.JI_FEN.getPayTypeCode());
                    saleAfterRefundDetailReq.setAmount(returnDeliveryDetailPrice.getDeliveryPoint().intValue());
                    saleAfterRefundDetailReq.setRefundReason(returnOrder.getReturnReason().getDesc());
                    saleAfterFreeList.add(saleAfterRefundDetailReq);
                }
            }
        }
        saleAfterPostFeeReq.setSaleAfterRefundDetailBOList(saleAfterFreeList);
        saleAfterCreateNewReq.setSaleAfterPostFee(saleAfterPostFeeReq);


        List<SaleAfterCreateNewReq.SaleAfterItemReq> saleAfterItemReqList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(returnOrder.getReturnItems())) {
            Map<ReturnItem, List<Pair<SaleAfterCreateNewReq.SaleAfterItemReq, OrderDetailResp.OrderItemResp>>> returnItems = new HashMap<>();
            for (ReturnItem returnItem : returnOrder.getReturnItems()) {

                List<OrderDetailResp.OrderItemResp> orderItemRespList = skuId2OrderItemMap.get(returnItem.getSkuId());
                if (CollectionUtils.isEmpty(orderItemRespList)) {
                    log.error("TransferService detailByPlatformOrderId skuId:{} 在电商中台中不存在", returnItem.getSkuId());
                    throw new SbcRuntimeException("999999", "skuId" + returnItem.getSkuId() + " 电商中台中不存在");
                }

                BigDecimal sum = BigDecimal.ZERO;
                for (OrderDetailResp.OrderItemResp orderItemResp : orderItemRespList) {
                    sum = sum.add(new BigDecimal(orderItemResp.getOughtFee().toString()));
                }

                BigDecimal surplusPrice =
                        (returnItem.getApplyRealPrice() != null && returnItem.getApplyRealPrice().compareTo(BigDecimal.ZERO) > 0)
                        ? returnItem.getApplyRealPrice().multiply(exchangeRate) : BigDecimal.ZERO;
                BigDecimal surplusPoint =
                        returnItem.getApplyPoint() != null && returnItem.getApplyPoint() > 0 ? new BigDecimal(returnItem.getApplyPoint().toString())
                        : BigDecimal.ZERO;
                BigDecimal surplusKnowledge =
                        returnItem.getApplyKnowledge() != null && returnItem.getApplyKnowledge() > 0 ? new BigDecimal(returnItem.getApplyKnowledge().toString())
                                : BigDecimal.ZERO;

                for (int i = 0; i < orderItemRespList.size(); i++) {
                    SaleAfterCreateNewReq.SaleAfterItemReq saleAfterItemReq = new SaleAfterCreateNewReq.SaleAfterItemReq();
                    saleAfterItemReq.setRefundType(refundType); // todo

                    if (returnOrder.getReturnLogistics() != null) {
                        saleAfterItemReq.setExpressCode(returnOrder.getReturnLogistics().getCode());
                        saleAfterItemReq.setExpressNo(returnOrder.getReturnLogistics().getNo());
                    }
                    OrderDetailResp.OrderItemResp orderItemResp = orderItemRespList.get(i);
                    if (orderItemResp.getOughtFee() <= defaultIfNull(orderItemResp.getRefundFee(), 0)) {
                        continue;
                    }
                    saleAfterItemReq.setRefundNum(orderItemResp.getNum());
                    saleAfterItemReq.setObjectId(orderItemResp.getTid().toString());
                    saleAfterItemReq.setObjectType("ORD_ITEM");

                    if (i == orderItemRespList.size() - 1) {
                        List<SaleAfterCreateNewReq.SaleAfterRefundDetailReq> saleAfterRefundDetailReqList = new ArrayList<>();
                        //金额
                        if (returnItem.getApplyRealPrice() != null && returnItem.getApplyRealPrice().compareTo(BigDecimal.ZERO) > 0) {
                            SaleAfterCreateNewReq.SaleAfterRefundDetailReq saleAfterRefundDetailReq = new SaleAfterCreateNewReq.SaleAfterRefundDetailReq();
                            saleAfterRefundDetailReq.setPayType(PaymentPayTypeEnum.XIAN_JIN.getPayTypeCode());
                            saleAfterRefundDetailReq.setAmount(surplusPrice.intValue());
                            saleAfterRefundDetailReq.setRefundReason(returnOrder.getDescription());
                            saleAfterRefundDetailReqList.add(saleAfterRefundDetailReq);
                        }
                        //积分
                        if (returnItem.getApplyPoint() != null && returnItem.getApplyPoint() > 0) {
                            SaleAfterCreateNewReq.SaleAfterRefundDetailReq saleAfterRefundDetailReq = new SaleAfterCreateNewReq.SaleAfterRefundDetailReq();
                            saleAfterRefundDetailReq.setPayType(PaymentPayTypeEnum.JI_FEN.getPayTypeCode());
                            saleAfterRefundDetailReq.setAmount(surplusPoint.intValue());
                            saleAfterRefundDetailReq.setRefundReason(returnOrder.getDescription());
                            saleAfterRefundDetailReqList.add(saleAfterRefundDetailReq);
                        }
                        //知豆
                        if (returnItem.getApplyKnowledge() != null && returnItem.getApplyKnowledge() > 0) {
                            SaleAfterCreateNewReq.SaleAfterRefundDetailReq saleAfterRefundDetailReq = new SaleAfterCreateNewReq.SaleAfterRefundDetailReq();
                            saleAfterRefundDetailReq.setPayType(PaymentPayTypeEnum.ZHI_DOU.getPayTypeCode());
                            saleAfterRefundDetailReq.setAmount(surplusKnowledge.intValue());
                            saleAfterRefundDetailReq.setRefundReason(returnOrder.getDescription());
                            saleAfterRefundDetailReqList.add(saleAfterRefundDetailReq);
                        }
                        saleAfterItemReq.setSaleAfterRefundDetailBOList(saleAfterRefundDetailReqList);
                    } else {
                        BigDecimal divide = new BigDecimal(orderItemResp.getOughtFee().toString()).divide(sum, 100, RoundingMode.HALF_DOWN);
                        List<SaleAfterCreateNewReq.SaleAfterRefundDetailReq> saleAfterRefundDetailReqList = new ArrayList<>();
                        //金额
                        if (returnItem.getApplyRealPrice() != null && returnItem.getApplyRealPrice().compareTo(BigDecimal.ZERO) > 0) {
                            BigDecimal tmpPrice = returnItem.getApplyRealPrice().multiply(exchangeRate).multiply(divide);
                            int intValue = tmpPrice.intValue();
                            SaleAfterCreateNewReq.SaleAfterRefundDetailReq saleAfterRefundDetailReq = new SaleAfterCreateNewReq.SaleAfterRefundDetailReq();
                            saleAfterRefundDetailReq.setPayType(PaymentPayTypeEnum.XIAN_JIN.getPayTypeCode());
                            saleAfterRefundDetailReq.setAmount(intValue);
                            saleAfterRefundDetailReq.setRefundReason(returnOrder.getDescription());
                            saleAfterRefundDetailReqList.add(saleAfterRefundDetailReq);
                            surplusPrice = surplusPrice.subtract(new BigDecimal(intValue + ""));
                        }
                        //积分
                        if (returnItem.getApplyPoint() != null && returnItem.getApplyPoint() > 0) {
                            BigDecimal tmpPoint = new BigDecimal(returnItem.getApplyPoint().toString()).multiply(divide);
                            int intValue = tmpPoint.intValue();
                            SaleAfterCreateNewReq.SaleAfterRefundDetailReq saleAfterRefundDetailReq = new SaleAfterCreateNewReq.SaleAfterRefundDetailReq();
                            saleAfterRefundDetailReq.setPayType(PaymentPayTypeEnum.JI_FEN.getPayTypeCode());
                            saleAfterRefundDetailReq.setAmount(intValue);
                            saleAfterRefundDetailReq.setRefundReason(returnOrder.getDescription());
                            saleAfterRefundDetailReqList.add(saleAfterRefundDetailReq);
                            surplusPoint = surplusPoint.subtract(new BigDecimal(intValue + ""));
                        }
                        //金额
                        if (returnItem.getApplyKnowledge() != null && returnItem.getApplyKnowledge() > 0) {
                            BigDecimal tmpKnowledge = new BigDecimal(returnItem.getApplyKnowledge().toString()).multiply(divide);
                            int intValue = tmpKnowledge.intValue();
                            SaleAfterCreateNewReq.SaleAfterRefundDetailReq saleAfterRefundDetailReq = new SaleAfterCreateNewReq.SaleAfterRefundDetailReq();
                            saleAfterRefundDetailReq.setPayType(PaymentPayTypeEnum.ZHI_DOU.getPayTypeCode());
                            saleAfterRefundDetailReq.setAmount(intValue);
                            saleAfterRefundDetailReq.setRefundReason(returnOrder.getDescription());
                            saleAfterRefundDetailReqList.add(saleAfterRefundDetailReq);
                            surplusKnowledge = surplusKnowledge.subtract(new BigDecimal(intValue + ""));
                        }
                        saleAfterItemReq.setSaleAfterRefundDetailBOList(saleAfterRefundDetailReqList);
                    }
                    saleAfterItemReqList.add(saleAfterItemReq);
                    returnItems.computeIfAbsent(returnItem, key -> new ArrayList<>()).add(Pair.of(saleAfterItemReq, orderItemResp));
                }
            }
            //验证组合商品的分摊金额
            validatePrice(returnOrder, returnItems);
        }

        //赠品，不参与分摊
        if (!CollectionUtils.isEmpty(returnOrder.getReturnGifts())) {
            for (ReturnItem returnGiftItem : returnOrder.getReturnGifts()) {
                List<OrderDetailResp.OrderItemResp> orderItemRespList = skuId2OrderItemMap.get(returnGiftItem.getSkuId());
                if (CollectionUtils.isEmpty(orderItemRespList)) {
                    log.error("TransferService detailByPlatformOrderId skuId:{} 在电商中台中不存在", returnGiftItem.getSkuId());
                    throw new SbcRuntimeException("999999", "skuId" + returnGiftItem.getSkuId() + " 电商中台中不存在");
                }

                for (OrderDetailResp.OrderItemResp orderItemResp : orderItemRespList) {
                    SaleAfterCreateNewReq.SaleAfterItemReq saleAfterItemReq = new SaleAfterCreateNewReq.SaleAfterItemReq();
                    saleAfterItemReq.setRefundType(refundType); // todo赠品
                    if (returnOrder.getReturnLogistics() != null) {
                        saleAfterItemReq.setExpressCode(returnOrder.getReturnLogistics().getCode());
                        saleAfterItemReq.setExpressNo(returnOrder.getReturnLogistics().getNo());
                    }
                    saleAfterItemReq.setRefundNum(orderItemResp.getNum());
                    saleAfterItemReq.setObjectId(orderItemResp.getTid().toString());
                    saleAfterItemReq.setObjectType("ORD_ITEM");
                    saleAfterItemReq.setSaleAfterRefundDetailBOList(new ArrayList<>());

                    List<SaleAfterCreateNewReq.SaleAfterRefundDetailReq> saleAfterRefundDetailReqList = new ArrayList<>();
                    SaleAfterCreateNewReq.SaleAfterRefundDetailReq saleAfterRefundDetailReq = new SaleAfterCreateNewReq.SaleAfterRefundDetailReq();
                    saleAfterRefundDetailReq.setPayType(PaymentPayTypeEnum.XIAN_JIN.getPayTypeCode());
                    saleAfterRefundDetailReq.setAmount(0); //赠品金额为0
                    saleAfterRefundDetailReq.setRefundReason(returnOrder.getDescription());
                    saleAfterRefundDetailReqList.add(saleAfterRefundDetailReq);
                    saleAfterItemReq.setSaleAfterRefundDetailBOList(saleAfterRefundDetailReqList);
                    saleAfterItemReqList.add(saleAfterItemReq);
                }
            }
        }
        saleAfterCreateNewReq.setSaleAfterItemBOList(saleAfterItemReqList);

        //退款流水
        //获取流水信息

        List<SaleAfterCreateNewReq.SaleAfterRefundReq> saleAfterRefundReqList =  new ArrayList<>();
        if (Objects.nonNull(returnOrder.getReturnPrice().getApplyPrice())) {
            // 获取订单流水
            //获取支付流水和 商户号
            TradeRecordByOrderCodeRequest request = new TradeRecordByOrderCodeRequest();
            request.setOrderId(returnOrder.getTid());
            log.info("TransferService changeSaleAfterCreateReq getTradeRecordByOrderCode param {}", JSON.toJSONString(request));
            BaseResponse<PayTradeRecordResponse> tradeRecordByOrderCode = payQueryProvider.getTradeRecordByOrderCode(request);
            log.info("TransferService changeSaleAfterCreateReq result {}", JSON.toJSONString(tradeRecordByOrderCode));
            PayTradeRecordResponse payTradeRecordResponse = tradeRecordByOrderCode.getContext();
            if (payTradeRecordResponse != null) {
                SaleAfterCreateNewReq.SaleAfterRefundReq saleAfterRefundReq = new SaleAfterCreateNewReq.SaleAfterRefundReq();
                saleAfterRefundReq.setRefundTradeNo(payTradeRecordResponse.getTradeNo());
                saleAfterRefundReq.setRefundGateway("108");
                saleAfterRefundReq.setAmount(returnOrder.getReturnPrice().getApplyPrice().multiply(exchangeRate).intValue());
                saleAfterRefundReq.setPayType(PaymentPayTypeEnum.XIAN_JIN.getPayTypeCode().toString());
                saleAfterRefundReq.setRefundTime(returnOrder.getFinishTime());
                saleAfterRefundReq.setRefundMchid(payTradeRecordResponse.getAppId());
                saleAfterRefundReqList.add(saleAfterRefundReq);
            }
        }

        if (Objects.nonNull(returnOrder.getReturnPoints())
                && Objects.nonNull(returnOrder.getReturnPoints().getApplyPoints())
                && returnOrder.getReturnPoints().getApplyPoints() > 0L) {
            SaleAfterCreateNewReq.SaleAfterRefundReq saleAfterRefundReq = new SaleAfterCreateNewReq.SaleAfterRefundReq();
//            saleAfterRefundReq.setRefundTradeNo("");
            saleAfterRefundReq.setRefundGateway("108");
            saleAfterRefundReq.setAmount(returnOrder.getReturnPoints().getApplyPoints().intValue());
            saleAfterRefundReq.setPayType(PaymentPayTypeEnum.JI_FEN.getPayTypeCode().toString());
            saleAfterRefundReq.setRefundTime(returnOrder.getFinishTime());
//            saleAfterRefundReq.setRefundMchid("");
            saleAfterRefundReqList.add(saleAfterRefundReq);
        }

        if (Objects.nonNull(returnOrder.getReturnKnowledge())
                && Objects.nonNull(returnOrder.getReturnKnowledge().getApplyKnowledge())
                && returnOrder.getReturnKnowledge().getApplyKnowledge() > 0L) {
            SaleAfterCreateNewReq.SaleAfterRefundReq saleAfterRefundReq = new SaleAfterCreateNewReq.SaleAfterRefundReq();
//            saleAfterRefundReq.setRefundTradeNo("");
            saleAfterRefundReq.setRefundGateway("108");
            saleAfterRefundReq.setAmount(returnOrder.getReturnKnowledge().getApplyKnowledge().intValue());
            saleAfterRefundReq.setPayType(PaymentPayTypeEnum.ZHI_DOU.getPayTypeCode().toString());
            saleAfterRefundReq.setRefundTime(returnOrder.getFinishTime());
//            saleAfterRefundReq.setRefundMchid("");
            saleAfterRefundReqList.add(saleAfterRefundReq);
        }
        saleAfterCreateNewReq.setSaleAfterRefundBOList(saleAfterRefundReqList);
        return saleAfterCreateNewReq;
    }

    /**
     * 补偿拆分导致的金额误差
     */
    private void validatePrice(ReturnOrder returnOrder, Map<ReturnItem, List<Pair<SaleAfterCreateNewReq.SaleAfterItemReq, OrderDetailResp.OrderItemResp>>> returnItems) {
        if (MapUtils.isEmpty(returnItems)) {
            return;
        }

        for (Map.Entry<ReturnItem, List<Pair<SaleAfterCreateNewReq.SaleAfterItemReq, OrderDetailResp.OrderItemResp>>> entry : returnItems.entrySet()) {
            int totalSpillCash = 0;
            int totalSpillPoint = 0;
            int totalSpillKnow = 0;

            ReturnItem returnItem = entry.getKey();
            //打印拆分补偿前的价格信息
            printPriceInfo(entry.getValue());

            //检查是否超出金额
            for (Pair<SaleAfterCreateNewReq.SaleAfterItemReq, OrderDetailResp.OrderItemResp> pair : entry.getValue()) {
                SaleAfterCreateNewReq.SaleAfterItemReq  salAfterItem = pair.getKey();
                OrderDetailResp.OrderItemResp orderItem = pair.getValue();

                int maxRefundFee = orderItem.getOughtFee() - defaultIfNull(orderItem.getRefundFee(), 0); //剩余最多能退
                int nowRefundFee = salAfterItem.getSaleAfterRefundDetailBOList().stream().mapToInt(SaleAfterCreateNewReq.SaleAfterRefundDetailReq::getAmount).sum();
                if (nowRefundFee <= maxRefundFee) {
                    continue;
                }
                //超出的金额
                int spillFee = nowRefundFee - maxRefundFee;
                Iterator<SaleAfterCreateNewReq.SaleAfterRefundDetailReq> iterator = salAfterItem.getSaleAfterRefundDetailBOList().iterator();
                while (iterator.hasNext() && spillFee > 0) {
                    SaleAfterCreateNewReq.SaleAfterRefundDetailReq refund = iterator.next();
                    int cutFee = refund.getAmount() > spillFee ? spillFee : refund.getAmount();
                    spillFee -= cutFee;
                    refund.setAmount(refund.getAmount() - cutFee);
                    if (refund.getAmount() <= 0) {
                        iterator.remove();
                    }
                    if (PaymentPayTypeEnum.XIAN_JIN.getPayTypeCode().equals(refund.getPayType())) {
                        totalSpillCash +=  cutFee;
                    } else if (PaymentPayTypeEnum.JI_FEN.getPayTypeCode().equals(refund.getPayType())) {
                        totalSpillPoint += cutFee;
                    } else if (PaymentPayTypeEnum.ZHI_DOU.getPayTypeCode().equals(refund.getPayType())) {
                        totalSpillKnow += cutFee;
                    } else {
                        throw new SbcRuntimeException(CommonErrorCode.FAILED, "错误的支付类型，商城只支持现金/积分/知豆三种支付类型");
                    }
                }
            }

            if (totalSpillCash == 0 && totalSpillPoint == 0 && totalSpillKnow == 0) {
                continue;
            }

            log.info("补偿拆分差价：溢出现金:{}，溢出积分：{}，溢出知豆：{}", totalSpillCash, totalSpillPoint, totalSpillKnow);

            //分摊超出部分金额
            for (Pair<SaleAfterCreateNewReq.SaleAfterItemReq, OrderDetailResp.OrderItemResp> pair : entry.getValue()) {
                SaleAfterCreateNewReq.SaleAfterItemReq  salAfterItem = pair.getKey();
                OrderDetailResp.OrderItemResp orderItem = pair.getValue();

                int maxRefundFee = orderItem.getOughtFee() - defaultIfNull(orderItem.getRefundFee(), 0); //剩余最多能退
                int nowRefundFee = salAfterItem.getSaleAfterRefundDetailBOList().stream().mapToInt(SaleAfterCreateNewReq.SaleAfterRefundDetailReq::getAmount).sum();
                if (nowRefundFee >= maxRefundFee) {
                    continue;
                }
                //可以分摊的金额
                int consumeFee = maxRefundFee - nowRefundFee;
                //现金
                if (totalSpillCash > 0 && consumeFee > 0) {
                    int tempFee = totalSpillCash > consumeFee ? consumeFee : totalSpillCash;
                    consumeFee -= tempFee;
                    totalSpillCash -= tempFee;

                    Optional<SaleAfterCreateNewReq.SaleAfterRefundDetailReq> refundOpt =
                            salAfterItem.getSaleAfterRefundDetailBOList().stream().filter(i -> i.getPayType().equals(PaymentPayTypeEnum.XIAN_JIN.getPayTypeCode())).findFirst();
                    SaleAfterCreateNewReq.SaleAfterRefundDetailReq refund;
                    if (refundOpt.isPresent()) {
                        refund = refundOpt.get();
                        refund.setAmount(refund.getAmount().intValue() + tempFee);
                    } else {
                        refund = new SaleAfterCreateNewReq.SaleAfterRefundDetailReq();
                        refund.setPayType(PaymentPayTypeEnum.XIAN_JIN.getPayTypeCode());
                        refund.setAmount(tempFee);
                        refund.setRefundReason(returnOrder.getDescription());
                        salAfterItem.getSaleAfterRefundDetailBOList().add(refund);
                    }
                }
                //积分
                if (totalSpillPoint > 0 && consumeFee > 0) {
                    int tempFee = totalSpillPoint > consumeFee ? consumeFee : totalSpillPoint;
                    consumeFee -= tempFee;
                    totalSpillPoint -= tempFee;

                    Optional<SaleAfterCreateNewReq.SaleAfterRefundDetailReq> refundOpt =
                            salAfterItem.getSaleAfterRefundDetailBOList().stream().filter(i -> i.getPayType().equals(PaymentPayTypeEnum.JI_FEN.getPayTypeCode())).findFirst();
                    SaleAfterCreateNewReq.SaleAfterRefundDetailReq refund;
                    if (refundOpt.isPresent()) {
                        refund = refundOpt.get();
                        refund.setAmount(refund.getAmount().intValue() + tempFee);
                    } else {
                        refund = new SaleAfterCreateNewReq.SaleAfterRefundDetailReq();
                        refund.setPayType(PaymentPayTypeEnum.JI_FEN.getPayTypeCode());
                        refund.setAmount(tempFee);
                        refund.setRefundReason(returnOrder.getDescription());
                        salAfterItem.getSaleAfterRefundDetailBOList().add(refund);
                    }
                }
                //知豆
                if (totalSpillKnow > 0 && consumeFee > 0) {
                    int tempFee = totalSpillKnow > consumeFee ? consumeFee : totalSpillKnow;
                    consumeFee -= tempFee;
                    totalSpillKnow -= tempFee;

                    Optional<SaleAfterCreateNewReq.SaleAfterRefundDetailReq> refundOpt =
                            salAfterItem.getSaleAfterRefundDetailBOList().stream().filter(i -> i.getPayType().equals(PaymentPayTypeEnum.ZHI_DOU.getPayTypeCode())).findFirst();
                    SaleAfterCreateNewReq.SaleAfterRefundDetailReq refund;
                    if (refundOpt.isPresent()) {
                        refund = refundOpt.get();
                        refund.setAmount(refund.getAmount().intValue() + tempFee);
                    } else {
                        refund = new SaleAfterCreateNewReq.SaleAfterRefundDetailReq();
                        refund.setPayType(PaymentPayTypeEnum.ZHI_DOU.getPayTypeCode());
                        refund.setAmount(tempFee);
                        refund.setRefundReason(returnOrder.getDescription());
                        salAfterItem.getSaleAfterRefundDetailBOList().add(refund);
                    }
                }
            }

            //打印拆分补偿后的价格信息
            printPriceInfo(entry.getValue());

            //验证金额是否一致
            int totalRefundCash = 0;
            int totalRefundPoint = 0;
            int totalRefundKnow = 0;

            for (Pair<SaleAfterCreateNewReq.SaleAfterItemReq, OrderDetailResp.OrderItemResp> pair : entry.getValue()) {
                SaleAfterCreateNewReq.SaleAfterItemReq  salAfterItem = pair.getKey();
                OrderDetailResp.OrderItemResp orderItem = pair.getValue();

                int maxRefundFee = orderItem.getOughtFee() - defaultIfNull(orderItem.getRefundFee(), 0); //剩余最多能退
                int nowRefundFee = salAfterItem.getSaleAfterRefundDetailBOList().stream().mapToInt(SaleAfterCreateNewReq.SaleAfterRefundDetailReq::getAmount).sum();
                if (nowRefundFee > maxRefundFee) {
                    log.error("订单分摊金额计算有误，itemId{}，订单最多能退金额={}，本次计算金额={}", orderItem.getTid(), maxRefundFee, nowRefundFee);
                    throw new SbcRuntimeException("999999", "分摊金额计算错误");
                }
                for (SaleAfterCreateNewReq.SaleAfterRefundDetailReq refund : salAfterItem.getSaleAfterRefundDetailBOList()) {
                    if (PaymentPayTypeEnum.XIAN_JIN.getPayTypeCode().equals(refund.getPayType())) {
                        totalRefundCash += refund.getAmount().intValue();
                    } else if (PaymentPayTypeEnum.JI_FEN.getPayTypeCode().equals(refund.getPayType())) {
                        totalRefundPoint += refund.getAmount().intValue();
                    } else if (PaymentPayTypeEnum.ZHI_DOU.getPayTypeCode().equals(refund.getPayType())) {
                        totalRefundKnow += refund.getAmount().intValue();
                    } else {
                        throw new SbcRuntimeException("999999", "错误的支付类型, payType=" + refund.getPayType());
                    }
                }
            }

            int applyRefundCash = returnItem.getApplyRealPrice() == null ? 0 : returnItem.getApplyRealPrice().multiply(exchangeRate).intValue(); //申请退现金
            int applyRefundPoint = returnItem.getApplyPoint() == null ? 0 : returnItem.getApplyPoint().intValue(); //申请退积分
            int applyRefundKnow = returnItem.getApplyKnowledge() == null ? 0 : returnItem.getApplyKnowledge().intValue(); //申请退知豆

            if (totalRefundCash != applyRefundCash || totalRefundPoint != applyRefundPoint || totalRefundKnow != applyRefundKnow) {
                log.error("申请退款的金额与分摊计算的金额不一致，申请金额:{}/{}/{}，分摊金额:{}/{}/{}",
                        applyRefundCash, applyRefundPoint, applyRefundKnow, totalRefundCash, totalRefundPoint, totalRefundKnow);
                throw new SbcRuntimeException("999999", "分摊金额计算错误");
            }
        }
    }

    private void printPriceInfo(List<Pair<SaleAfterCreateNewReq.SaleAfterItemReq, OrderDetailResp.OrderItemResp>> pairs) {
        for (Pair<SaleAfterCreateNewReq.SaleAfterItemReq, OrderDetailResp.OrderItemResp> pair : pairs) {
            SaleAfterCreateNewReq.SaleAfterItemReq salAfterItem = pair.getKey();
            OrderDetailResp.OrderItemResp orderItem = pair.getValue();

            StringBuilder msg = new StringBuilder();
            for (SaleAfterCreateNewReq.SaleAfterRefundDetailReq item : salAfterItem.getSaleAfterRefundDetailBOList()) {
                msg.append(PaymentPayTypeEnum.byCode(item.getPayType()).getPayTypeName() + "=" + item.getAmount() + " ");
            }
            log.info("金额信息，商品:{}，可退金额:{}，拆分金额:{}", orderItem.getPlatformSkuName(), orderItem.getOughtFee() - defaultIfNull(orderItem.getRefundFee(), 0), msg);
        }
    }

    private <T> T defaultIfNull(T target, T defaultVal) {
        return Objects.isNull(target) ? defaultVal : target;
    }
}
