package com.soybean.mall.order.dszt;

import com.wanmi.sbc.erp.api.provider.ShopCenterOrderProvider;
import com.wanmi.sbc.erp.api.req.SaleAfterCreateNewReq.SaleAfterPostFeeReq;
import com.wanmi.sbc.erp.api.req.SaleAfterCreateNewReq.SaleAfterOrderReq;
import com.wanmi.sbc.erp.api.req.SaleAfterCreateNewReq;
import com.google.common.collect.Lists;


import com.alibaba.fastjson.JSON;
import com.soybean.mall.order.enums.OrderSourceEnum;
import com.soybean.mall.order.enums.PaymentPayTypeEnum;
import com.soybean.mall.order.enums.UnifiedOrderChangeTypeEnum;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.ChannelType;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.common.enums.GenderType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.customer.api.provider.detail.CustomerDetailQueryProvider;
import com.wanmi.sbc.customer.api.request.detail.CustomerDetailByCustomerIdRequest;
import com.wanmi.sbc.customer.api.response.detail.CustomerDetailGetCustomerIdResponse;
import com.wanmi.sbc.erp.api.constant.DeviceTypeEnum;
import com.wanmi.sbc.erp.api.provider.ShopCenterProductProvider;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;


import com.wanmi.sbc.erp.api.req.CreateOrderReq;
import com.wanmi.sbc.erp.api.req.SalePlatformQueryReq;
import com.wanmi.sbc.erp.api.resp.OrderDetailResp;
import com.wanmi.sbc.erp.api.resp.SalePlatformResp;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.request.goods.PackDetailByPackIdsRequest;
import com.wanmi.sbc.goods.api.response.goods.GoodsPackDetailResponse;
import com.wanmi.sbc.order.api.enums.MiniProgramSceneType;
import com.wanmi.sbc.order.bean.enums.OutTradePlatEnum;
import com.wanmi.sbc.order.bean.enums.PayState;
import com.wanmi.sbc.order.bean.enums.ReturnReason;
import com.wanmi.sbc.order.returnorder.model.entity.ReturnItem;
import com.wanmi.sbc.order.returnorder.model.root.ReturnOrder;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
        platformAddressListRequest.setIdList(Arrays.asList(provinceName, cityName, areaName));
        platformAddressListRequest.setLeafFlag(false);
        PlatformAddressListResponse platformAddressListResponse =
                platformAddressQueryProvider.list(platformAddressListRequest).getContext();
        List<PlatformAddressVO> platformAddressVOList = platformAddressListResponse.getPlatformAddressVOList();
        if (CollectionUtils.isNotEmpty(platformAddressVOList)) {
            for (PlatformAddressVO platformAddressVO : platformAddressVOList) {
                if (Objects.equals(platformAddressVO.getId(), provinceName)) {
                    provinceName = platformAddressVO.getAddrName();
                }
                if (Objects.equals(platformAddressVO.getId(), cityName)) {
                    cityName = platformAddressVO.getAddrName();
                }
                if (Objects.equals(platformAddressVO.getId(), areaName)) {
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

        Map<String, List<GoodsPackDetailResponse>> packageId2ModelMap = new HashMap<>();
        if (spuId2ModelMap.size() > 0) {
            BaseResponse<List<GoodsPackDetailResponse>> packResponse =
                    goodsQueryProvider.listPackDetailByPackIds(new PackDetailByPackIdsRequest(new ArrayList<>(spuId2ModelMap.keySet())));
            List<GoodsPackDetailResponse> goodsPackDetailResponses = packResponse.getContext();
            for (GoodsPackDetailResponse goodsPackDetailRespon : goodsPackDetailResponses) {
                List<GoodsPackDetailResponse> goodsPackDetailResponseList = packageId2ModelMap.get(goodsPackDetailRespon.getPackId());
                if (goodsPackDetailResponseList == null) {
                    goodsPackDetailResponseList = new ArrayList<>();
                    packageId2ModelMap.put(goodsPackDetailRespon.getPackId(), goodsPackDetailResponseList);
                }
                goodsPackDetailResponseList.add(goodsPackDetailRespon);
            }
        }




        for (Map.Entry<String, List<TradeItem>> entry : spuId2ModelMap.entrySet()) {
            TradeItem packageTradeItem = null;
            for (TradeItem tradeItem : entry.getValue()) {
                if (Objects.equals(entry.getKey(), tradeItem.getSpuId())) {
                    packageTradeItem = tradeItem;
                    break;
                }
            }

            if (packageTradeItem == null) {
                continue;
            }

            BigDecimal sumOther = BigDecimal.ZERO;
            for (TradeItem tradeItem : entry.getValue()) {
                if (Objects.equals(entry.getKey(), tradeItem.getSpuId())) {
                    continue;
                }

                //获取打包对应的折扣信息
                List<GoodsPackDetailResponse> goodsPackDetailResponses = packageId2ModelMap.get(tradeItem.getPackId());
                Map<String, GoodsPackDetailResponse> collect =
                        goodsPackDetailResponses.stream().collect(Collectors.toMap(GoodsPackDetailResponse::getGoodsId, Function.identity(), (k1, k2) -> k1));
                GoodsPackDetailResponse goodsPackDetailResponse = collect.get(tradeItem.getSpuId());

                CreateOrderReq.BuyGoodsReq buyGoodsReq = new CreateOrderReq.BuyGoodsReq();
                buyGoodsReq.setPlatformItemId(tradeItem.getOid());
                buyGoodsReq.setGoodsCode(tradeItem.getErpSkuNo());
                buyGoodsReq.setNum(tradeItem.getNum().intValue());
                buyGoodsReq.setPlatformGoodsId(tradeItem.getSpuId());
                buyGoodsReq.setPlatformGoodsName(tradeItem.getSpuName());
                buyGoodsReq.setPlatformSkuId(tradeItem.getSkuId());
                buyGoodsReq.setPlatformSkuName(tradeItem.getSkuName());
                BigDecimal  price = packageTradeItem.getPrice() == null ? BigDecimal.ZERO : packageTradeItem.getPrice();
                BigDecimal newPrice = price.multiply(goodsPackDetailResponse.getShareRate());
                buyGoodsReq.setPrice(newPrice.intValue());
                buyGoodsReq.setGiftFlag(0); //非赠送
                sumOther = sumOther.add(newPrice);
                result.add(buyGoodsReq);
            }

            //打包的商品
            //获取打包对应的折扣信息
            CreateOrderReq.BuyGoodsReq buyGoodsReq = new CreateOrderReq.BuyGoodsReq();
            buyGoodsReq.setPlatformItemId(packageTradeItem.getOid());
            buyGoodsReq.setGoodsCode(packageTradeItem.getErpSkuNo());
            buyGoodsReq.setNum(packageTradeItem.getNum().intValue());
            buyGoodsReq.setPlatformGoodsId(packageTradeItem.getSpuId());
            buyGoodsReq.setPlatformGoodsName(packageTradeItem.getSpuName());
            buyGoodsReq.setPlatformSkuId(packageTradeItem.getSkuId());
            buyGoodsReq.setPlatformSkuName(packageTradeItem.getSkuName());
            BigDecimal  price = packageTradeItem.getPrice() == null ? BigDecimal.ZERO : packageTradeItem.getPrice();
            BigDecimal newPrice = price.multiply(exchangeRate).subtract(sumOther);
            buyGoodsReq.setPrice(newPrice.intValue());
            buyGoodsReq.setGiftFlag(0); //非赠送
            result.add(buyGoodsReq);
        }

        //非打包商品
        for (TradeItem tradeItem : tradeItems) {

            //判断是否为打包商品
            if (!StringUtils.isEmpty(tradeItem.getPackId())) {
                continue;
            }
            CreateOrderReq.BuyGoodsReq buyGoodsReq = new CreateOrderReq.BuyGoodsReq();
            buyGoodsReq.setPlatformItemId(tradeItem.getOid());
            buyGoodsReq.setGoodsCode(tradeItem.getErpSkuNo());
            buyGoodsReq.setNum(tradeItem.getNum().intValue());
            buyGoodsReq.setPlatformGoodsId(tradeItem.getSpuId());
            buyGoodsReq.setPlatformGoodsName(tradeItem.getSpuName());
            buyGoodsReq.setPlatformSkuId(tradeItem.getSkuId());
            buyGoodsReq.setPlatformSkuName(tradeItem.getSkuName());
            BigDecimal  originalPrice = tradeItem.getOriginalPrice() == null ? BigDecimal.ZERO : tradeItem.getOriginalPrice();
            BigDecimal newOriginalPrice = originalPrice.multiply(exchangeRate);
            buyGoodsReq.setPrice(newOriginalPrice.intValue());
            buyGoodsReq.setGiftFlag(0); //非赠送

            //优惠
            List<CreateOrderReq.BuyDiscountReq> buyDiscountReqList = new ArrayList<>();

            //会员价格
            if (tradeItem.getOriginalPrice().compareTo(tradeItem.getPrice()) > 0) {
                CreateOrderReq.BuyDiscountReq buyDiscountReq = new CreateOrderReq.BuyDiscountReq();
                BigDecimal tmpPrice = tradeItem.getOriginalPrice().subtract(tradeItem.getPrice());
                tmpPrice = tmpPrice.multiply(exchangeRate);
                buyDiscountReq.setAmount(tmpPrice.intValue());
                buyDiscountReq.setCouponId("");
                buyDiscountReq.setDiscountNo("");
                buyDiscountReq.setDiscountName(UnifiedOrderChangeTypeEnum.VIP_PRICE_DIFF.getMsg());
                buyDiscountReq.setChangeType(UnifiedOrderChangeTypeEnum.VIP_PRICE_DIFF.getType()); //优惠
                buyDiscountReq.setMemo("会员价");
                buyDiscountReq.setCostAssume("CHANNEL");
                buyDiscountReqList.add(buyDiscountReq);
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



            buyGoodsReq.setGoodsDiscounts(buyDiscountReqList);
            result.add(buyGoodsReq);
        }
        return result;
    }


    /**
     * 打包支付信息
     * @param trade
     * @return
     */
    private List<CreateOrderReq.BuyPaymentReq> packageBuyPayment(Trade trade) {

        List<CreateOrderReq.BuyPaymentReq> paymentReqList = new ArrayList<>();
        //支付金额
        BigDecimal amount = trade.getTradePrice().getTotalPrice().multiply(exchangeRate);
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
            buyPaymentReq.setPayMchid(payTradeRecordResponse.getAppId());
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

        CustomerGetByIdRequest customerGetByIdRequest = new CustomerGetByIdRequest();
        customerGetByIdRequest.setCustomerId(buyer.getId());
        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(customerGetByIdRequest).getContext();
        if (customer == null) {
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
        CreateOrderReq.OrderUserInfoReq orderUserInfoReq = new CreateOrderReq.OrderUserInfoReq();
        if (!StringUtils.isEmpty(trade.getDirectChargeMobile())) {
            orderUserInfoReq.setArea("+86");
            orderUserInfoReq.setMobile(trade.getDirectChargeMobile());
        } else {
            orderUserInfoReq.setUserId(Integer.valueOf(customer.getFanDengUserNo()));
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
        createOrderReq.setBuyPaymentBO(this.packageBuyPayment(trade));
        createOrderReq.setPayTimeOut(trade.getOrderTimeOut());
        //商品信息
        createOrderReq.setBuyGoodsBOS(this.packageSku(tradeItems));
        //收货地址
        Consignee consignee = trade.getConsignee();
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


//    /**
//     * 封装购买信息
//     * @param customerGetByAccountResponse
//     * @return
//     */
//    private Buyer packageBuyer(NoDeleteCustomerGetByAccountResponse customerGetByAccountResponse) {
//        Buyer buyer = new Buyer();
//        buyer.setId(customerGetByAccountResponse.getCustomerId());
//        buyer.setName(customerGetByAccountResponse.getCustomerDetail().getCustomerName());
//        buyer.setAccount(customerGetByAccountResponse.getCustomerAccount());
//        buyer.setLevelId(customerGetByAccountResponse.getCustomerLevelId());
//        buyer.setLevelName(customerGetByAccountResponse.getCustomerLevelId().toString());
//        buyer.setCustomerFlag(true);
//        buyer.setPhone(customerGetByAccountResponse.getCustomerAccount());
////        buyer.setEmployeeId(customerGetByAccountResponse.getem);
//        buyer.setIepCustomer(false);
//        buyer.setOpenId(customerGetByAccountResponse.getWxMiniOpenId());
//        return buyer;
//    }


//    /**
//     * 打包订单状态
//     * @param orderDetailResp
//     * @return
//     */
//    private TradeState packageTradeState(OrderDetailResp orderDetailResp) {
//        TradeState tradeState = new TradeState();
//        tradeState.setAuditState(AuditState.CHECKED);
//
//        /**
//         *     AUDITING(1, "审核中"),
//         *     WAIT_FOR_DELIVERY(3, "待发货"),
//         *     PART_OF_DELIVERY(4, "部分发货"),
//         *     WAIT_FOR_SIGN(6, "待签收"),
//         *     HAS_SIGN(7, "已签收"),
//         *     COMPLETE_ORDER(8, "交易完成"),
//         *     CLOSE_ORDER(9, "交易关闭"),
//         *     PART_OF_SIGN(10, "部分签收"),
//         *     // 以下状态迁移自主站订单
//         *     NEW(0, "新订单"),
//         *     PREPARED(11, "待支付"),
//         *     ERROR(13, "已失败"),
//         *     CANCELLED(14, "已取消"),
//         *     REFUNDED(15, "已退款"),
//         *     REFUNDING(16, "退款中"),
//         *     PAID(17, "已付款"),
//         *     PARTEDREFUNDED(19, "部分已退款"),
//         *     RISK_REJECT(20, "风控拒绝");
//         */
//        Integer status = orderDetailResp.getStatus();
//        PayState payState = orderDetailResp.getPaymentStatus() == 1 ? PayState.PAID : PayState.NOT_PAID;
//        tradeState.setPayState(payState);
//
//        FlowState flowState = null;
//
//        DeliverStatusEnum deliverStatus = null;
//        if (status == 11) {
//            flowState = FlowState.INIT;
//            deliverStatus = DeliverStatusEnum.NOT_YET_SHIPPED;
//        } else if (status == 3) {
//            flowState = FlowState.INIT;
//            deliverStatus = DeliverStatusEnum.NOT_YET_SHIPPED;
//        } else if (status == 8) {
//            flowState = FlowState.COMPLETED;
//            deliverStatus = DeliverStatusEnum.SHIPPED;
//        } else if (status == 14) {
//            flowState = FlowState.VOID;
//            deliverStatus = DeliverStatusEnum.SHIPPED;
//        }
//        tradeState.setFlowState(flowState); //TODO
//        tradeState.setDeliverStatus(deliverStatus);
////        tradeState.setOrderSource(OrderSource.SUPPLIER);
//
//        tradeState.setCreateTime(orderDetailResp.getCreateTime());
////        tradeState.setModifyTime(LocalDateTime.now());
//        tradeState.setEndTime(orderDetailResp.getCompleteTime());
//
//        tradeState.setPayTime(orderDetailResp.getPayTime());
////        tradeState.setDeliverTime(orderDetailResp);
////        tradeState.setAutoConfirmTime(LocalDateTime.now());
////        tradeState.setStartPayTime(LocalDateTime.now());
////        tradeState.setFinalTime(LocalDateTime.now());
////        tradeState.setObsoleteReason("");
////        tradeState.setHandSelStartTime(LocalDateTime.now());
////        tradeState.setHandSelEndTime(LocalDateTime.now());
////        tradeState.setTailStartTime(LocalDateTime.now());
////        tradeState.setTailEndTime(LocalDateTime.now());
////        tradeState.setErpTradeState("");
////        tradeState.setPushCount(0);
////        tradeState.setScanCount(0);
////        tradeState.setPushTime(LocalDateTime.now());
////        tradeState.setPushResponse("");
////        tradeState.setVirtualAllDelivery(0);
//        return tradeState;
//    }


//    /**
//     *
//     * @param orderDetailResp
//     * @return
//     */
//    private TradePrice packageTradePrice(OrderDetailResp orderDetailResp) {
//        BigDecimal goodsPrice = BigDecimal.ZERO;
//        BigDecimal points = BigDecimal.ZERO; //设置积分 TODO 记录积分 [记录在商城]
//        BigDecimal knowledge = BigDecimal.ZERO; //设置知豆 TODO [记录在商城]
//        BigDecimal discountPrice = BigDecimal.ZERO;
//        BigDecimal marketingPrice = BigDecimal.ZERO;
//        for (OrderDetailResp.OrderItemResp orderItemBO : orderDetailResp.getOrderItemBOS()) {
//            goodsPrice = new BigDecimal(orderItemBO.getPrice().toString());
//
//            OrderDetailResp.BuyDiscountResp buyDiscountBo = orderItemBO.getBuyDiscountBo();
//            if (Objects.equals(buyDiscountBo.getChangeType(), UnifiedOrderChangeTypeEnum.DISCOUNT.getType())) {
//                discountPrice = discountPrice.add(new BigDecimal(buyDiscountBo.getAmount().toString()).multiply(exchangeRate));
//            } else if (Objects.equals(buyDiscountBo.getChangeType(), UnifiedOrderChangeTypeEnum.MARKETING.getType())) {
//                marketingPrice = marketingPrice.add(new BigDecimal(buyDiscountBo.getAmount().toString()).multiply(exchangeRate));
//            }
//        }
//
//
//        TradePrice tradePrice = new TradePrice();
//        tradePrice.setGoodsPrice(goodsPrice.divide(exchangeRate, 2, RoundingMode.HALF_UP));
//        tradePrice.setDeliveryPrice(new BigDecimal(orderDetailResp.getPostFee()));
////        tradePrice.setPrivilegePrice(new BigDecimal("0"));
////        tradePrice.setDiscountsPrice(discountPrice);
////        tradePrice.setMarkupPrice(new BigDecimal("0"));
////        tradePrice.setIntegral(0);
////        tradePrice.setIntegralPrice(new BigDecimal("0"));
//        tradePrice.setPoints(points.longValue());
//        tradePrice.setKnowledge(knowledge.longValue());
////        tradePrice.setBuyPoints(0L);
////        tradePrice.setBuyKnowledge(0L);
//        tradePrice.setPointsPrice(points.divide(exchangeRate, 2, RoundingMode.HALF_UP));
//        tradePrice.setKnowledgePrice(knowledge.divide(exchangeRate, 2, RoundingMode.HALF_UP));
////        tradePrice.setPointWorth(exchangeRate.longValue());
////        tradePrice.setKnowledgeWorth(exchangeRate.longValue());
////        tradePrice.setSpecial(false);
////        tradePrice.setEnableDeliveryPrice(false);
////        tradePrice.setOriginPrice(new BigDecimal("0"));
//        tradePrice.setTotalPrice(new BigDecimal(orderDetailResp.getTotalFee().toString()).divide(exchangeRate, 2, RoundingMode.HALF_UP));
////        tradePrice.setTotalPayCash(new BigDecimal("0"));
////        tradePrice.setRate(new BigDecimal("0"));
////        tradePrice.setCmCommission(new BigDecimal("0"));
////        tradePrice.setInvoiceFee(new BigDecimal("0"));
////        tradePrice.setDiscountsPriceDetails(Lists.newArrayList());
//        tradePrice.setCouponPrice(discountPrice);
////        tradePrice.setOrderSupplyPrice(new BigDecimal("0"));
////        tradePrice.setEarnestPrice(new BigDecimal("0"));
////        tradePrice.setSwellPrice(new BigDecimal("0"));
////        tradePrice.setTailPrice(new BigDecimal("0"));
////        tradePrice.setCanBackEarnestPrice(new BigDecimal("0"));
////        tradePrice.setCanBackTailPrice(new BigDecimal("0"));
//        tradePrice.setMarketingDiscountPrice(marketingPrice);
////        tradePrice.setSplitDeliveryPrice(Maps.newHashMap());
////        tradePrice.setActualPrice(new BigDecimal("0"));
////        tradePrice.setActualPoints(new BigDecimal("0"));
////        tradePrice.setActualKnowledge(0L);
////        tradePrice.setPropPrice(new BigDecimal(orderDetailResp.getPostFee()));
////        tradePrice.setSalePrice(new BigDecimal("0"));
//        return tradePrice;
//    }

//    /**
//     * 1、支付列表接口
//     * 2、区分视频号订单。
//     * @return
//     */
//    public Trade orderDetailResp2Trade(String orderNumber) {
//        OrderDetailResp orderDetailResp = shopCenterOrderProvider.orderDetailByOrderNumber(orderNumber).getContext();
//        if (orderDetailResp == null) {
//            throw new SbcRuntimeException("999999", "订单不存在");
//        }
//
//        //获取客户信息
//        NoDeleteCustomerGetByFanDengRequest fanDengRequest = new NoDeleteCustomerGetByFanDengRequest();
//        fanDengRequest.setFanDengId(orderDetailResp.getUserId().toString());
//        NoDeleteCustomerGetByAccountResponse customerGetByAccountResponse = customerQueryProvider.getNoDeleteCustomerByFanDengId(fanDengRequest).getContext();
//        if (customerGetByAccountResponse == null) {
//            throw new SbcRuntimeException("999999", "用户订单用户信息" + orderDetailResp.getUserId() + "不存在");
//        }
//
//
////        //查询商品
////        List<String> skuIdList =
////                orderDetailResp.getOrderItemBOS().stream().map(OrderDetailResp.OrderItemResp::getPlatformSkuId).collect(Collectors.toList());
////        GoodsInfoViewByIdsRequest goodsInfoViewByIdsRequest = new GoodsInfoViewByIdsRequest();
////        goodsInfoViewByIdsRequest.setGoodsInfoIds(skuIdList);
////        GoodsInfoViewByIdsResponse goodsInfoResponse = goodsInfoQueryProvider.listSimpleView(goodsInfoViewByIdsRequest).getContext();
//
//
//        Trade trade = new Trade();
////        trade.setId(orderDetailResp.);
////        trade.setYzTid("");
////        trade.setParentId("");
////        trade.setGroupId("");
//        //封装购买用户
//        trade.setBuyer(this.packageBuyer(customerGetByAccountResponse));
//
////        trade.setSeller(new Seller());
//        //封装商户信息
//        Supplier supplier = new Supplier();
//        supplier.setSupplierCode("S01183");
//        supplier.setSupplierName("上海信选网络技术有限公司");
//        supplier.setSupplierId(1183L);
//        supplier.setStoreId(123458039L);
//        supplier.setFreightTemplateType(DefaultFlag.YES);
//        supplier.setStoreName("樊登读书自营旗舰店");
////        supplier.setEmployeeId("");
////        supplier.setEmployeeName("");
//        supplier.setIsSelf(true);
//        trade.setSupplier(supplier);
//
//        trade.setBuyerRemark(orderDetailResp.getBuyerMemo());
//        trade.setSellerRemark(orderDetailResp.getSellerMemo());
////        trade.setEncloses("");
////        trade.setRequestIp("");
////        trade.setInvoice(new Invoice());
//        TradeState tradeState = this.packageTradeState(orderDetailResp);
//        //设置发货时间
//        for (OrderDetailResp.OrderItemResp orderItemBO : orderDetailResp.getOrderItemBOS()) {
//            if (orderItemBO.getDeliveryTime() == null) {
//                continue;
//            }
//            if (tradeState.getDeliverTime() == null) {
//                tradeState.setDeliverTime(orderItemBO.getDeliveryTime());
//            } else {
//                if (orderItemBO.getDeliveryTime().isBefore(tradeState.getDeliverTime())) {
//                    tradeState.setDeliverTime(orderItemBO.getDeliveryTime());
//                }
//            }
//        }
//        trade.setTradeState(tradeState);
//
//        //收件人
//        OrderDetailResp.OrderAddressResp orderAddressBO = orderDetailResp.getOrderAddressBO();
//        if (orderAddressBO != null) {
//            Consignee consignee = new Consignee();
//
//
////            consignee.setId(orderAddressBO.getTid());
//            consignee.setProvinceName(orderAddressBO.getProvinceId());
//            consignee.setCityName(orderAddressBO.getCityId());
//            consignee.setAreaName(orderAddressBO.getCountryId());
////            consignee.setStreetId(0L);
////            consignee.setAddress(orderAddressB);
//            consignee.setDetailAddress(orderAddressBO.getFullAddress());
//            consignee.setName(orderAddressBO.getContactName());
//            consignee.setPhone(orderAddressBO.getContactMobile());
////            consignee.setExpectTime(LocalDateTime.now());
////            consignee.setUpdateTime("");
////            consignee.setProvinceName(orderAddressBO.get);
////            consignee.setCityName("");
////            consignee.setAreaName("");
//            trade.setConsignee(consignee);
//        }
//
//        //价格信息
//        trade.setTradePrice(this.packageTradePrice(orderDetailResp));
//        trade.setTradeItems(Lists.newArrayList());
////        trade.setTradeCouponItem(new TradePointsCouponItem());
////        List<TradeDeliver> tradeDelivers = new ArrayList<>();
////        TradeDeliver tradeDeliver = new TradeDeliver();
////        tradeDeliver.setLogisticCode();
////        trade.setTradeDelivers(tradeDelivers); //TODO 发货信息 只是给物流号和发货状态 发货时间；
////        trade.setDeliverWay(DeliverWay.OTHER); //TODO 配送方式 删除掉
//
//        //支付信息
//        PayInfo payInfo = new PayInfo();
//        payInfo.setPayTypeId(String.format("%d", PayType.ONLINE.toValue()));
//        payInfo.setPayTypeName(PayType.ONLINE.name());
//        payInfo.setDesc(PayType.ONLINE.getDesc());
//        trade.setPayInfo(payInfo);
//
////        trade.setPayOrderId("");
////        trade.setTailPayOrderId("");
////        trade.setPlatform(Platform.BOSS); // TODO source
////        trade.setThirdPlatformType(ThirdPlatformType.LINKED_MALL);
////        trade.setThirdPlatformPayErrorFlag(false);
////        trade.setIsAuditOpen(false);
////        trade.setPaymentOrder(PaymentOrder.NO_LIMIT);
//        trade.setOrderTimeOut(orderDetailResp.getCancelTime());
////        trade.setTradeEventLogs(Lists.newArrayList());
////        trade.setChannelType(ChannelType.PC_MALL);
////        trade.setDistributionShareCustomerId("");
////        trade.setDistributorId("");
//        trade.setDirectChargeMobile("");
////        trade.setInviteeId("");
////        trade.setShopName("");
////        trade.setDistributorName("");
////        trade.setStoreBagsFlag(DefaultFlag.NO);
////        trade.setSuitMarketingFlag(false);
////        trade.setStoreBagsInviteeId("");
////        trade.setDistributeItems(Lists.newArrayList());
////        trade.setCommission(new BigDecimal("0"));
////        trade.setTotalCommission(new BigDecimal("0"));
////        trade.setCommissions(Lists.newArrayList());
////        trade.setCommissionFlag(false);
////        trade.setReturnOrderNum(0);
////        trade.setHasBeanSettled(false);
////        trade.setCanReturnFlag(false);
////        trade.setRefundFlag(false);
////        trade.setTradeMarketings(Lists.newArrayList());
////        trade.setTradeCoupon(new TradeCouponVO());
////        trade.setGifts(Lists.newArrayList());
//        trade.setOrderSource(OrderSourceEnum.get(orderDetailResp.getOrderSource()));
//        trade.setOrderEvaluateStatus(EvaluateStatus.NO_EVALUATE);
//        trade.setStoreEvaluate(EvaluateStatus.NO_EVALUATE);
//        Integer paymentStatus = orderDetailResp.getPaymentStatus();
//        PayWay payWay = PayWay.OTHER;
//        if (paymentStatus == 1) {
//            payWay = PayWay.WECHAT;
//        } else if (paymentStatus == 2) {
//            payWay = PayWay.ALIPAY;
//        }
//        trade.setPayWay(payWay);
////        trade.setCanReturnPoints(0L);
////        trade.setCanReturnKnowledge(0L);
////        trade.setCanReturnPrice(new BigDecimal("0"));
//        trade.setOrderType(OrderType.NORMAL_ORDER);
////        trade.setPointsOrderType(PointsOrderType.POINTS_GOODS);
////        trade.setShareUserId("");
////        trade.setIsFlashSaleGoods(false);
////        trade.setIsVirtualCouponGoods(false);
////        trade.setIsVirtualCouponGiveawayGoods(false);
////        trade.setIsBookingSaleGoods(false); //TODO 预售
////        trade.setBookingType(BookingType.FULL_MONEY);
////        trade.setTailOrderNo("");
////        trade.setTailNoticeMobile("");
////        trade.setGrouponFlag(false);
////        trade.setTradeGroupon(new TradeGroupon());
////        trade.setCycleBuyFlag(false);
////        trade.setYzOrderFlag(false);
////        trade.setTradeCycleBuyInfo(new TradeCycleBuyInfo());
//        Map<String, String> SandR = new HashMap<>();
//        SandR.put("source", trade.getSource());
//        SandR.put("promoteUserId", trade.getPromoteUserId());
//        SandR.put("deductDoce", trade.getDeductCode());
//        Map<String, String> orderSnapshot = orderDetailResp.getOrderSnapshot();
//        if (orderSnapshot.get(source) != null) {
//            trade.setSource(source);
//        }
//        if (orderSnapshot.get(promoteUserId) != null) {
//            trade.setPromoteUserId(promoteUserId);
//        }
//        if (orderSnapshot.get(deductDoce) != null) {
//            trade.setDeductCode(deductDoce);
//        }
//
////        trade.setEmallSessionId("");
////        trade.setSuitScene(0);
////        trade.setMiniProgram(new MiniProgram());
////        trade.setUpdateTime(LocalDateTime.now());
////        trade.setOutTradeNo("");
////        trade.setOutTradePlat("");
////        trade.setTags(Lists.newArrayList());
////        trade.setMiniProgramScene(0);
//        return trade;
//    }

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
        Map<String, OrderDetailResp.OrderItemResp> skuId2OrderItemMap = new HashMap<>();
        for (OrderDetailResp.OrderItemResp orderItemBO : context.getOrderItemBOS()) {
            skuId2OrderItemMap.put(orderItemBO.getPlatformSkuId(), orderItemBO);
        }
        //获取订单支付信息


        saleAfterCreateNewReq.setOrderNumber(context.getOrderNumber().toString());
        saleAfterCreateNewReq.setRefundTypeList(Arrays.asList(1)); //退货退款

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
            BigDecimal freightAmount = returnOrder.getReturnPrice().getApplyPrice().multiply(exchangeRate);
            SaleAfterCreateNewReq.SaleAfterRefundDetailReq saleAfterRefundDetailReq =
                    new SaleAfterCreateNewReq.SaleAfterRefundDetailReq();
            saleAfterRefundDetailReq.setPayType(1);
            saleAfterRefundDetailReq.setAmount(freightAmount.intValue());
            saleAfterRefundDetailReq.setRefundReason(returnOrder.getRejectReason());
            saleAfterFreeList.add(saleAfterRefundDetailReq);
        }

        if (returnOrder.getReturnPrice().getDeliverPrice() != null && returnOrder.getReturnPrice().getDeliverPrice().compareTo(BigDecimal.ZERO) > 0) {
            SaleAfterCreateNewReq.SaleAfterRefundDetailReq saleAfterRefundDetailReq =
                    new SaleAfterCreateNewReq.SaleAfterRefundDetailReq();
            saleAfterRefundDetailReq.setPayType(1);
            saleAfterRefundDetailReq.setAmount(returnOrder.getReturnPrice().getDeliverPrice().multiply(exchangeRate).intValue());
            saleAfterRefundDetailReq.setRefundReason(returnOrder.getRejectReason());
            saleAfterFreeList.add(saleAfterRefundDetailReq);
        }
        saleAfterPostFeeReq.setSaleAfterRefundDetailBOList(saleAfterFreeList);
        saleAfterCreateNewReq.setSaleAfterPostFee(saleAfterPostFeeReq);

        //商品
        Integer refundType = 1;
        List<SaleAfterCreateNewReq.SaleAfterItemReq> saleAfterItemReqList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(returnOrder.getReturnItems())) {

            for (ReturnItem returnItem : returnOrder.getReturnItems()) {
                SaleAfterCreateNewReq.SaleAfterItemReq saleAfterItemReq = new SaleAfterCreateNewReq.SaleAfterItemReq();
                saleAfterItemReq.setRefundType(refundType); // todo

                if (returnOrder.getReturnLogistics() != null) {
                    saleAfterItemReq.setExpressCode(returnOrder.getReturnLogistics().getCode());
                    saleAfterItemReq.setExpressNo(returnOrder.getReturnLogistics().getNo());
                }

                OrderDetailResp.OrderItemResp orderItemResp = skuId2OrderItemMap.get(returnItem.getSkuId());
                if (orderItemResp == null) {
                    log.error("TransferService detailByPlatformOrderId skuId:{} 在电商中台中不存在", returnItem.getSkuId());
                    throw new SbcRuntimeException("999999", "skuId" + returnItem.getSkuId() + " 电商中台中不存在");
                }

                saleAfterItemReq.setRefundNum(returnItem.getNum());
                saleAfterItemReq.setObjectId(orderItemResp.getTid());
                saleAfterItemReq.setObjectType("ORDER_ITEM");
//                saleAfterItemReq.setDeliveryStatus(0);
//                saleAfterItemReq.setSignatureTime(LocalDateTime.now());

                List<SaleAfterCreateNewReq.SaleAfterRefundDetailReq> saleAfterRefundDetailReqList = new ArrayList<>();
                //金额
                if (returnItem.getApplyRealPrice() != null && returnItem.getApplyRealPrice().compareTo(BigDecimal.ZERO) > 0) {
                    SaleAfterCreateNewReq.SaleAfterRefundDetailReq saleAfterRefundDetailReq = new SaleAfterCreateNewReq.SaleAfterRefundDetailReq();
                    saleAfterRefundDetailReq.setPayType(1);
                    saleAfterRefundDetailReq.setAmount(returnItem.getApplyRealPrice().multiply(exchangeRate).intValue());
                    saleAfterRefundDetailReq.setRefundReason(returnOrder.getDescription());
                    saleAfterRefundDetailReqList.add(saleAfterRefundDetailReq);
                }
                //积分
                if (returnItem.getApplyPoint() != null && returnItem.getApplyPoint() > 0) {
                    SaleAfterCreateNewReq.SaleAfterRefundDetailReq saleAfterRefundDetailReq = new SaleAfterCreateNewReq.SaleAfterRefundDetailReq();
                    saleAfterRefundDetailReq.setPayType(3);
                    saleAfterRefundDetailReq.setAmount(returnItem.getApplyPoint().intValue());
                    saleAfterRefundDetailReq.setRefundReason(returnOrder.getDescription());
                    saleAfterRefundDetailReqList.add(saleAfterRefundDetailReq);
                }
                //金额
                if (returnItem.getApplyKnowledge() != null && returnItem.getApplyKnowledge() > 0) {
                    SaleAfterCreateNewReq.SaleAfterRefundDetailReq saleAfterRefundDetailReq = new SaleAfterCreateNewReq.SaleAfterRefundDetailReq();
                    saleAfterRefundDetailReq.setPayType(2);
                    saleAfterRefundDetailReq.setAmount(returnItem.getApplyKnowledge().intValue());
                    saleAfterRefundDetailReq.setRefundReason(returnOrder.getDescription());
                    saleAfterRefundDetailReqList.add(saleAfterRefundDetailReq);
                }
                saleAfterItemReq.setSaleAfterRefundDetailBOList(saleAfterRefundDetailReqList);
                saleAfterItemReqList.add(saleAfterItemReq);
            }
        }
        saleAfterCreateNewReq.setSaleAfterItemBOList(saleAfterItemReqList);

        return saleAfterCreateNewReq;
    }

}
