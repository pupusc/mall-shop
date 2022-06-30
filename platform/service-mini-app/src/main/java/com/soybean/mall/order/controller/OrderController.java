package com.soybean.mall.order.controller;

import com.alibaba.fastjson.JSON;
import com.soybean.mall.common.CommonUtil;
import com.soybean.mall.order.api.provider.order.MiniAppOrderProvider;
import com.soybean.mall.order.api.request.order.GetPaymentParamsRequest;
import com.soybean.mall.order.api.response.OrderCommitResponse;
import com.soybean.mall.order.bean.vo.OrderCommitResultVO;
import com.soybean.mall.order.bean.vo.WxOrderPaymentParamsVO;
import com.soybean.mall.order.common.DefaultPayBatchRequest;
import com.soybean.mall.order.common.PayServiceHelper;
import com.soybean.mall.order.request.StmtParamVO;
import com.soybean.mall.order.request.TradeItemConfirmRequest;
import com.soybean.mall.order.response.OrderConfirmResponse;
import com.soybean.mall.order.response.StmtResultVO;
import com.soybean.mall.service.CommonService;
import com.soybean.mall.vo.WxAddressInfoVO;
import com.soybean.mall.vo.WxOrderCommitResultVO;
import com.soybean.mall.vo.WxOrderPaymentVO;
import com.soybean.mall.vo.WxProductInfoVO;
import com.soybean.mall.wx.mini.goods.bean.request.WxUpdateProductWithoutAuditRequest;
import com.soybean.mall.wx.mini.goods.bean.response.WxGetProductDetailResponse;
import com.soybean.mall.wx.mini.goods.bean.response.WxResponseBase;
import com.soybean.mall.wx.mini.goods.controller.WxGoodsApiController;
import com.wanmi.sbc.account.bean.enums.PayWay;
import com.wanmi.sbc.common.annotation.MultiSubmitWithToken;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.DistributeChannel;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.ChannelType;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.address.CustomerDeliveryAddressQueryProvider;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.address.CustomerDeliveryAddressByIdRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerByCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.store.ListNoDeleteStoreByIdsRequest;
import com.wanmi.sbc.customer.api.request.store.NoDeleteStoreByIdRequest;
import com.wanmi.sbc.customer.api.response.address.CustomerDeliveryAddressByIdResponse;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.api.enums.GoodsBlackListCategoryEnum;
import com.wanmi.sbc.goods.api.provider.blacklist.GoodsBlackListProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.VideoChannelSetFilterControllerProvider;
import com.wanmi.sbc.goods.api.provider.price.GoodsIntervalPriceProvider;
import com.wanmi.sbc.goods.api.request.blacklist.GoodsBlackListPageProviderRequest;
import com.wanmi.sbc.goods.api.request.enterprise.goods.EnterprisePriceGetRequest;
import com.wanmi.sbc.goods.api.request.flashsalegoods.FlashSaleGoodsListRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsListByIdsRequest;
import com.wanmi.sbc.goods.api.request.goods.PackDetailByPackIdsRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdsRequest;
import com.wanmi.sbc.goods.api.response.blacklist.GoodsBlackListPageProviderResponse;
import com.wanmi.sbc.goods.api.response.enterprise.EnterprisePriceResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsListByIdsResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsPackDetailResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdsResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import com.wanmi.sbc.goods.bean.vo.FlashSaleGoodsVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsLevelPriceVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingLevelPluginProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCodeListForUseByCustomerIdRequest;
import com.wanmi.sbc.marketing.api.request.markup.MarkupListRequest;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingLevelGoodsListFilterRequest;
import com.wanmi.sbc.marketing.bean.dto.TradeItemInfoDTO;
import com.wanmi.sbc.marketing.bean.dto.TradeMarketingDTO;
import com.wanmi.sbc.marketing.bean.vo.MarkupLevelVO;
import com.wanmi.sbc.order.api.provider.trade.TradeItemProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeItemQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeProvider;
import com.wanmi.sbc.order.api.provider.trade.VerifyQueryProvider;
import com.wanmi.sbc.order.api.request.trade.MergeGoodsInfoRequest;
import com.wanmi.sbc.order.api.request.trade.TradeCommitRequest;
import com.wanmi.sbc.order.api.request.trade.TradeDefaultPayBatchRequest;
import com.wanmi.sbc.order.api.request.trade.TradeGetGoodsRequest;
import com.wanmi.sbc.order.api.request.trade.TradeItemByCustomerIdRequest;
import com.wanmi.sbc.order.api.request.trade.TradeItemConfirmSettlementRequest;
import com.wanmi.sbc.order.api.request.trade.TradeQueryPurchaseInfoRequest;
import com.wanmi.sbc.order.api.request.trade.VerifyGoodsRequest;
import com.wanmi.sbc.order.api.response.trade.TradeGetGoodsResponse;
import com.wanmi.sbc.order.bean.dto.TradeGoodsInfoPageDTO;
import com.wanmi.sbc.order.bean.dto.TradeGoodsListDTO;
import com.wanmi.sbc.order.bean.dto.TradeItemDTO;
import com.wanmi.sbc.order.bean.dto.TradeItemGroupDTO;
import com.wanmi.sbc.order.bean.enums.BookingType;
import com.wanmi.sbc.order.bean.vo.SupplierVO;
import com.wanmi.sbc.order.bean.vo.TradeConfirmItemVO;
import com.wanmi.sbc.order.bean.vo.TradeItemGroupVO;
import com.wanmi.sbc.order.bean.vo.TradeItemMarketingVO;
import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.pay.api.provider.WxPayProvider;
import com.wanmi.sbc.pay.api.request.WxPayForJSApiRequest;
import com.wanmi.sbc.pay.bean.enums.WxPayTradeType;
import com.wanmi.sbc.setting.api.provider.platformaddress.PlatformAddressQueryProvider;
import com.wanmi.sbc.setting.api.request.platformaddress.PlatformAddressVerifyRequest;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @menu 小程序
 */
@Slf4j
@RestController
@RequestMapping("/wx/order")
public class OrderController {

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private MarketingLevelPluginProvider marketingLevelPluginProvider;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private VerifyQueryProvider verifyQueryProvider;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private CustomerDeliveryAddressQueryProvider customerDeliveryAddressQueryProvider;

    @Autowired
    private PlatformAddressQueryProvider platformAddressQueryProvider;

    @Autowired
    private TradeProvider tradeProvider;

    @Autowired
    private MiniAppOrderProvider miniAppOrderProvider;

    @Autowired
    private GoodsIntervalPriceProvider goodsIntervalPriceProvider;

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @Autowired
    private GoodsBlackListProvider goodsBlackListProvider;

    @Autowired
    private CommonService commonService;


    @Value("${mini.program.appid}")
    private String appId;

    @Autowired
    private PayServiceHelper payServiceHelper;

    @Autowired
    private WxPayProvider wxPayProvider;

    @Autowired
    private VideoChannelSetFilterControllerProvider videoChannelSetFilterControllerProvider;

    @Autowired
    private WxGoodsApiController wxGoodsApiController;
    @Autowired
    private TradeItemProvider tradeItemProvider;
    @Autowired
    private TradeItemQueryProvider tradeItemQueryProvider;

    @Value("${wx.default.image.url}")
    private String defaultImageUrl;

    @Value("${wx.goods.detail.url}")
    private String goodsDetailUrl;
    @Value("${wx.order.detail.url}")
    private String orderDetailUrl;


    /**
     * @description 创建订单
     * @param tradeCommitRequest
     * @menu 小程序
     * @status done
     */
    @ApiOperation(value = "提交订单，用于生成订单操作")
    @RequestMapping(value = "/commit", method = RequestMethod.POST)
    @MultiSubmitWithToken
    //@GlobalTransactional
    public BaseResponse<WxOrderPaymentVO> commit(@RequestBody @Valid TradeCommitRequest tradeCommitRequest) {
        //校验是否需要完善地址信息
        CustomerDeliveryAddressByIdResponse address = customerDeliveryAddressQueryProvider.getById(new CustomerDeliveryAddressByIdRequest(tradeCommitRequest.getConsigneeId())).getContext();
        if (address != null) {
            PlatformAddressVerifyRequest platformAddressVerifyRequest = new PlatformAddressVerifyRequest();
            if (Objects.nonNull(address.getProvinceId())) {
                platformAddressVerifyRequest.setProvinceId(String.valueOf(address.getProvinceId()));
            }
            if (Objects.nonNull(address.getCityId())) {
                platformAddressVerifyRequest.setCityId(String.valueOf(address.getCityId()));
            }
            if (Objects.nonNull(address.getAreaId())) {
                platformAddressVerifyRequest.setAreaId(String.valueOf(address.getAreaId()));
            }
            if (Objects.nonNull(address.getStreetId())) {
                platformAddressVerifyRequest.setStreetId(String.valueOf(address.getStreetId()));
            }
            if (Boolean.TRUE.equals(platformAddressQueryProvider.verifyAddress(platformAddressVerifyRequest).getContext())) {
                throw new SbcRuntimeException("K-220001");
            }
        }


        RLock rLock = redissonClient.getFairLock(commonUtil.getOperatorId());

        List<OrderCommitResultVO> successResults;
        try {
//            rLock.lock();
            if (rLock.tryLock(3, 3, TimeUnit.SECONDS)) {
                Operator operator = commonUtil.getOperator();
                tradeCommitRequest.setOperator(operator);
                DistributeChannel channel = new DistributeChannel();
                channel.setChannelType(ChannelType.MINIAPP);
                tradeCommitRequest.setDistributeChannel(channel);
                tradeCommitRequest.setGoodsChannelTypeSet(Collections.singletonList(commonUtil.getTerminal().getCode()));
                BaseResponse<OrderCommitResponse> orderCommitResponseBaseResponse = tradeProvider.commitTrade(tradeCommitRequest);
                log.info("OrderController commit result: {}", JSON.toJSONString(orderCommitResponseBaseResponse));
                if (!CommonErrorCode.SUCCESSFUL.equals(orderCommitResponseBaseResponse.getCode())) {
                    throw new SbcRuntimeException(orderCommitResponseBaseResponse.getCode(), orderCommitResponseBaseResponse.getMessage());
                }
                successResults = orderCommitResponseBaseResponse.getContext().getOrderCommitResults();
            } else {
                throw new SbcRuntimeException("K-000001", "操作频繁");
            }
            return BaseResponse.success(getOrderPaymentResult(successResults,tradeCommitRequest.getOpenId(),tradeCommitRequest.getMiniProgramScene()));
        } catch (InterruptedException ex) {
            log.error("OrderController commit InterruptedException", ex);
            throw new SbcRuntimeException("K-000001",  "操作频繁");
        } catch (Exception e) {
            log.error("OrderController commit Exception ", e);
            throw e;
        } finally {
            if (rLock.isLocked()) {
                rLock.unlock();
            }
        }
    }

    /**
     * 处理下单
     * @param trades
     * @param openId
     * @param miniProgramScene
     * @return
     */
    private WxOrderPaymentVO getOrderPaymentResult(List<OrderCommitResultVO> trades,String openId,Integer miniProgramScene){
        WxOrderPaymentVO wxOrderPaymentVO = new WxOrderPaymentVO();
        wxOrderPaymentVO.setCouponFlag(trades.get(0).getCouponFlag());

        //1、获取商品的库存 此处应该有一个锁机制的，但是当前没有比较好的锁key
        Map<String, Long> wxOutSkuId2StockMap = new HashMap<>();
        for (TradeItemVO tradeItemParam : trades.get(0).getTradeItems()) {
            BaseResponse<WxGetProductDetailResponse.Spu> productDetail =
                    wxGoodsApiController.getProductDetail(tradeItemParam.getSpuId());
            if (productDetail.getContext() == null) {
                continue;
            }
            for (WxGetProductDetailResponse.Sku sku : productDetail.getContext().getSkus()) {
                wxOutSkuId2StockMap.put(sku.getOutSkuId(), sku.getStockNum());
            }
        }

        //0元支付不需要生成预支付单
        if(trades.get(0).getTradePrice().getTotalPrice().compareTo(new BigDecimal(0))==0){
            wxOrderPaymentVO.setOrderInfo(convertResult(trades,openId));
        } else if (trades.get(0).getTradePrice().getTotalPrice().compareTo(new BigDecimal(0)) < 0) {
            throw new SbcRuntimeException("K-000001", "下单金额有误请重新下单");
        } else {
            //2 下单
            if(Objects.equals(miniProgramScene,1) || miniProgramScene ==null){
                //生成预支付订单
                WxPayForJSApiRequest req = wxPayCommon(openId,trades.get(0).getId());
                req.setAppid(appId);
                BaseResponse<Map<String,String>> prepayResult= wxPayProvider.wxPayForLittleProgram(req);
                if(prepayResult == null || prepayResult.getContext().isEmpty()){
                    return wxOrderPaymentVO;
                }
                wxOrderPaymentVO.setTimeStamp(prepayResult.getContext().get("timeStamp"));
                wxOrderPaymentVO.setNonceStr(prepayResult.getContext().get("nonceStr"));
                wxOrderPaymentVO.setPrepayId(prepayResult.getContext().get("package"));
                wxOrderPaymentVO.setPaySign(prepayResult.getContext().get("paySign"));
                wxOrderPaymentVO.setSignType("MD5");
            }else{
                //视频号订单
                GetPaymentParamsRequest getPaymentParamsRequest = new GetPaymentParamsRequest();
                getPaymentParamsRequest.setTid(trades.get(0).getId());
                BaseResponse<WxOrderPaymentParamsVO> response = miniAppOrderProvider.getWxOrderPaymentParams(getPaymentParamsRequest);
                if(response == null || response.getContext() ==null){
                    throw new SbcRuntimeException("K-000001", "网络异常请重新下单！！");
                }
                wxOrderPaymentVO.setPrepayId(response.getContext().getPrepayId());
                wxOrderPaymentVO.setPaySign(response.getContext().getPaySign());
                wxOrderPaymentVO.setNonceStr(response.getContext().getNonceStr());
                wxOrderPaymentVO.setTimeStamp(response.getContext().getTimeStamp());
                wxOrderPaymentVO.setSignType(response.getContext().getSignType());
            }

            wxOrderPaymentVO.setOrderInfo(convertResult(trades,openId));
            String prepayId = wxOrderPaymentVO.getPrepayId();
            String ppid = "";
            if(StringUtils.isNotEmpty(prepayId) && prepayId.length() > 10){
                ppid = prepayId.substring(10,prepayId.length());
            }
            wxOrderPaymentVO.getOrderInfo().getOrderDetail().getPayInfo().setPrepayId(ppid);
            wxOrderPaymentVO.setOrderInfoStr(JSON.toJSONString(wxOrderPaymentVO.getOrderInfo()));
        }

        //3、扣减商品库存[此处异常不做处理，只做记录]
        try {
            for (TradeItemVO tradeItemParam : trades.get(0).getTradeItems()) {
                if (wxOutSkuId2StockMap.get(tradeItemParam.getSkuId()) == null) {
                    continue;
                }
                WxUpdateProductWithoutAuditRequest wxUpdateProductWithoutAuditRequest = new WxUpdateProductWithoutAuditRequest();
                wxUpdateProductWithoutAuditRequest.setOutProductId(tradeItemParam.getSpuId());
                Long wxStockNum = wxOutSkuId2StockMap.get(tradeItemParam.getSkuId());

                List<WxUpdateProductWithoutAuditRequest.Sku> skus = new ArrayList<>();
                WxUpdateProductWithoutAuditRequest.Sku sku = new WxUpdateProductWithoutAuditRequest.Sku();
                sku.setOutSkuId(tradeItemParam.getSkuId());
                sku.setStockNum(wxStockNum - tradeItemParam.getNum().intValue());
                skus.add(sku);
                wxUpdateProductWithoutAuditRequest.setSkus(skus);
                BaseResponse<WxResponseBase> wxResponseBaseBaseResponse = wxGoodsApiController.updateGoodsWithoutAudit(wxUpdateProductWithoutAuditRequest);
                log.error("微信小程序创建订单 {} 扣减库存返回的结果为 {}", trades.get(0).getId(), JSON.toJSONString(wxResponseBaseBaseResponse));
            }
        } catch (Exception ex) {
            log.error("OrderController getOrderPaymentResult execute exception", ex);
        }

        return wxOrderPaymentVO;
    }


    private WxOrderCommitResultVO convertResult(List<OrderCommitResultVO> trades,String openId) {
        WxOrderCommitResultVO result = new WxOrderCommitResultVO();
        result.setOutOrderId(trades.get(0).getId());
        result.setCreateTime(DateUtil.format(LocalDateTime.now(),DateUtil.FMT_TIME_1));
        result.setOpenid(openId);
        result.setPath(orderDetailUrl);
        OrderCommitResultVO trade = trades.get(0);
        WxOrderCommitResultVO.WxOrderDetail detail = new WxOrderCommitResultVO.WxOrderDetail();
        List<WxProductInfoVO> productInfoDTOS = new ArrayList<>();
        trade.getTradeItems().forEach(tradeItem -> {
            productInfoDTOS.add(WxProductInfoVO.builder()
                    .outProductId(tradeItem.getSpuId())
                    .outSkuId(tradeItem.getSkuId())
                    .productNum(tradeItem.getNum())
                    .salePrice(tradeItem.getOriginalPrice().multiply(new BigDecimal(100)).intValue())
                    .realPrice(tradeItem.getSplitPrice().multiply(new BigDecimal(100)).intValue())
                    .title(tradeItem.getSkuName())
                    .path(goodsDetailUrl+tradeItem.getSpuId())
                    .headImg(StringUtils.isEmpty(tradeItem.getPic())?defaultImageUrl:tradeItem.getPic()).build());
        });
        detail.setProductInfos(productInfoDTOS);

        detail.setPayInfo(WxOrderCommitResultVO.WxPayInfo.builder().payMethodType(0)
                .prepayId(trades.get(0).getId())
                .prepayTime(DateUtil.format(LocalDateTime.now(),DateUtil.FMT_TIME_1)).build());

        WxOrderCommitResultVO.WxPriceInfo priceInfo = new WxOrderCommitResultVO.WxPriceInfo();
        if(trade.getTradePrice().getTotalPrice()!=null) {
            priceInfo.setOrderPrice(trade.getTradePrice().getTotalPrice().multiply(new BigDecimal(100)).intValue());
        }
        if(trade.getTradePrice().getDeliveryPrice()!=null) {
            priceInfo.setFreight(trade.getTradePrice().getDeliveryPrice().multiply(new BigDecimal(100)).intValue());
        }
        detail.setPriceInfo(priceInfo);

        WxAddressInfoVO addressInfo = new WxAddressInfoVO();
        addressInfo.setCity(trade.getConsignee().getCityName());
        addressInfo.setReceiverName(trade.getConsignee().getName());
        addressInfo.setDetailedAddress(trade.getConsignee().getDetailAddress());
        addressInfo.setProvince(trade.getConsignee().getProvinceName());
        addressInfo.setTown(trade.getConsignee().getAreaName());
        addressInfo.setTelNumber(trade.getConsignee().getPhone());
        result.setAddressInfo(addressInfo);
        result.setOrderDetail(detail);
        if(trades.get(0).getTradePrice().getTotalPrice().compareTo(new BigDecimal(0)) ==0){
            result.setPrePay(false);
        }
        return result;
    }


    /**
     * 微信内浏览器,小程序支付公用逻辑
     *
     * @param
     * @return
     */
    private WxPayForJSApiRequest wxPayCommon(String openId,String tid) {
        WxPayForJSApiRequest jsApiRequest = new WxPayForJSApiRequest();
        String id = payServiceHelper.getPayBusinessId(tid, null);
        List<TradeVO> trades = payServiceHelper.checkTrades(id);
        //订单总金额
        String totalPrice = payServiceHelper.calcTotalPriceByPenny(trades).toString();
        String body = payServiceHelper.buildBody(trades);
        jsApiRequest.setBody(body + "订单");
        jsApiRequest.setOut_trade_no(id);
        jsApiRequest.setTotal_fee(totalPrice);
        jsApiRequest.setSpbill_create_ip(HttpUtil.getIpAddr());
        jsApiRequest.setTrade_type(WxPayTradeType.JSAPI.toString());
        jsApiRequest.setOpenid(openId);
        jsApiRequest.setStoreId(-2L);
        return jsApiRequest;
    }


    /**
     * @description 用于确认订单后，创建订单前的获取订单商品信息
     * @param request
     * @menu 小程序
     * @status done
     */
    @ApiOperation(value = "用于确认订单后，创建订单前的获取订单商品信息")
    @RequestMapping(value = "/purchase", method = RequestMethod.POST)
   // @GlobalTransactional
    public BaseResponse<OrderConfirmResponse> getPurchaseItems(@RequestBody TradeItemConfirmRequest request) {

        OrderConfirmResponse confirmResponse = new OrderConfirmResponse();
        Operator operator = commonUtil.getOperator();
        CustomerGetByIdResponse customer =null;
        if(operator!=null && StringUtils.isNotEmpty(operator.getUserId())){
            String customerId = commonUtil.getOperatorId();
            //验证用户
            customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest
                    (customerId)).getContext();
        }


        //入参是商品sku和num，返回商品信息和价格信息
        List<TradeItemDTO> tradeItems = KsBeanUtil.convertList(request.getTradeItems(), TradeItemDTO.class);
        List<String> skuIds = tradeItems.stream().map(TradeItemDTO::getSkuId).collect(Collectors.toList());

        //获取订单商品详情和会员价salePrice
        GoodsInfoResponse skuResp = getGoodsResponse(skuIds, customer);

        List<String> spuIds = skuResp.getGoodses().stream().map(GoodsVO::getGoodsId).collect(Collectors.toList());
        //设置是否展示输入电话输入框
        //获取商品下的打包信息 TODO 修改此处的时候，同时修改 h5的 purchase 接口
        Map<String, Boolean> mainGoodsId2HasVirtualMap = this.getGoodsIdHasVirtual(spuIds);

        List<TradeConfirmItemVO> items = new ArrayList<>(1);
        //一期只能购买一个商品，只有一个商家
        StoreVO store = storeQueryProvider.getNoDeleteStoreById(NoDeleteStoreByIdRequest.builder().storeId(skuResp.getGoodsInfos().get(0).getStoreId())
                .build())
                .getContext().getStoreVO();

        TradeConfirmItemVO tradeConfirmItemVO = new TradeConfirmItemVO();
        //商品验证并填充商品价格
        List<TradeItemVO> tradeItemVOList =
                verifyQueryProvider.verifyGoods(new VerifyGoodsRequest(tradeItems, Collections.emptyList(),
                        KsBeanUtil.convert(skuResp, TradeGoodsInfoPageDTO.class),
                        store.getStoreId(), true)).getContext().getTradeItems();

        //视频号黑名单
        List<String> skuIdList = tradeItemVOList.stream().map(TradeItemVO::getSkuId).collect(Collectors.toList());
        Map<String, Boolean> goodsId2VideoChannelMap = videoChannelSetFilterControllerProvider.filterGoodsIdHasVideoChannelMap(skuIdList).getContext();

        List<String> blackListGoodsIdList = new ArrayList<>();
        // 积分和名单商品不能使用积分，也不参与分摊
        GoodsBlackListPageProviderRequest goodsBlackListPageProviderRequest = new GoodsBlackListPageProviderRequest();
        goodsBlackListPageProviderRequest.setBusinessCategoryColl(Collections.singletonList(GoodsBlackListCategoryEnum.POINT_NOT_SPLIT.getCode()));
        GoodsBlackListPageProviderResponse context = goodsBlackListProvider.listNoPage(goodsBlackListPageProviderRequest).getContext();
        if (context.getPointNotSplitBlackListModel() != null && !CollectionUtils.isEmpty(context.getPointNotSplitBlackListModel().getGoodsIdList())) {
            blackListGoodsIdList.addAll(context.getPointNotSplitBlackListModel().getGoodsIdList());
        }

        //定价
        for (TradeItemVO tradeItem : tradeItemVOList) {
            BaseResponse<String> priceByGoodsId = goodsIntervalPriceProvider.findPriceByGoodsId(tradeItem.getSkuId());
            if(priceByGoodsId.getContext() != null){
                tradeItem.setPropPrice(Double.valueOf(priceByGoodsId.getContext()));
            }
            if(blackListGoodsIdList.contains(tradeItem.getSpuId()) || (goodsId2VideoChannelMap.get(tradeItem.getSpuId()) != null && goodsId2VideoChannelMap.get(tradeItem.getSpuId()))){
                tradeItem.setInPointBlackList(true);
            }
            //设置是否显示输入框
            tradeItem.setShowPhoneNum(mainGoodsId2HasVirtualMap.get(tradeItem.getSpuId()) != null && mainGoodsId2HasVirtualMap.get(tradeItem.getSpuId()));
        }


        tradeConfirmItemVO.setTradeItems(tradeItemVOList);
        tradeConfirmItemVO.setTradePrice(commonService.calPrice(tradeItemVOList));

        DefaultFlag freightTemplateType = store.getFreightTemplateType();
        SupplierVO supplier = SupplierVO.builder()
                .storeId(store.getStoreId())
                .storeName(store.getStoreName())
                .isSelf(store.getCompanyType() == BoolFlag.NO)
                .supplierCode(store.getCompanyInfo().getCompanyCode())
                .supplierId(store.getCompanyInfo().getCompanyInfoId())
                .supplierName(store.getCompanyInfo().getSupplierName())
                .freightTemplateType(freightTemplateType)
                .build();
        tradeConfirmItemVO.setSupplier(supplier);
        items.add(tradeConfirmItemVO);
        confirmResponse.setTradeConfirmItems(items);

//        //设置优惠券
//        List<TradeItemInfoDTO> tradeDtos = items.stream().flatMap(confirmItem ->
//                confirmItem.getTradeItems().stream().map(tradeItem -> {
//                    TradeItemInfoDTO dto = new TradeItemInfoDTO();
//                    dto.setBrandId(tradeItem.getBrand());
//                    dto.setCateId(tradeItem.getCateId());
//                    dto.setSpuId(tradeItem.getSpuId());
//                    dto.setSkuId(tradeItem.getSkuId());
//                    dto.setStoreId(confirmItem.getSupplier().getStoreId());
//                    dto.setPrice(tradeItem.getSplitPrice());
//                    return dto;
//                })).collect(Collectors.toList());
//
//        CouponCodeListForUseByCustomerIdRequest couponCodeListForUseByCustomerIdRequest = new CouponCodeListForUseByCustomerIdRequest();
//        couponCodeListForUseByCustomerIdRequest.setCustomerId(customer.getCustomerId());
//        couponCodeListForUseByCustomerIdRequest.setTradeItems(tradeDtos);
//        BigDecimal totalPriceSum = items.stream().map(item -> item.getTradePrice().getTotalPrice()).reduce(BigDecimal.ZERO, BigDecimal::add);
//        couponCodeListForUseByCustomerIdRequest.setPrice(totalPriceSum);
//        confirmResponse.setCouponCodes(couponCodeQueryProvider.listForUseByCustomerId(couponCodeListForUseByCustomerIdRequest).getContext()
//                .getCouponCodeList());

        return BaseResponse.success(confirmResponse);
    }


    /**
     * 查看商品下是否有虚拟商品，是否显示 电话输入框
     * @param spuIdList
     * @return
     */
    private Map<String, Boolean> getGoodsIdHasVirtual(List<String> spuIdList) {


        //获取商品下的打包信息
        Map<String, Boolean> mainGoodsId2HasVirtualMap = new HashMap<>();
        BaseResponse<List<GoodsPackDetailResponse>> packResponse = goodsQueryProvider.listPackDetailByPackIds(new PackDetailByPackIdsRequest(spuIdList));
        List<GoodsPackDetailResponse> goodsPackDetailList = packResponse.getContext();

        if (!CollectionUtils.isEmpty(goodsPackDetailList)) {
            GoodsListByIdsRequest request = new GoodsListByIdsRequest();
            request.setGoodsIds(goodsPackDetailList.stream().map(GoodsPackDetailResponse::getGoodsId).collect(Collectors.toList()));
            BaseResponse<GoodsListByIdsResponse> childGoodsList = goodsQueryProvider.listByIds(request);
            GoodsListByIdsResponse childGoodsResponse = childGoodsList.getContext();

            Map<String, Boolean> childGoodsId2HasVirtualMap = new HashMap<>();
            for (GoodsVO goodsVOParam : childGoodsResponse.getGoodsVOList()) {
                Boolean hasGoodsType = childGoodsId2HasVirtualMap.get(goodsVOParam.getGoodsId());
                if (hasGoodsType != null && hasGoodsType) {
                    continue;
                }
                childGoodsId2HasVirtualMap.put(goodsVOParam.getGoodsId(), Objects.equals(goodsVOParam.getGoodsType(), GoodsType.VIRTUAL_GOODS.ordinal()));
            }

            //根据主商品 确定当前是否存放 展示 电话输入框
            for (GoodsPackDetailResponse goodsPackDetailParam : goodsPackDetailList) {
                Boolean hasGoodsType = mainGoodsId2HasVirtualMap.get(goodsPackDetailParam.getPackId());
                if (hasGoodsType != null && hasGoodsType) {
                    continue;
                }
                Boolean childHasGoodsType = childGoodsId2HasVirtualMap.get(goodsPackDetailParam.getGoodsId());
                if (childHasGoodsType == null || !childHasGoodsType) {
                    continue;
                }
                mainGoodsId2HasVirtualMap.put(goodsPackDetailParam.getPackId(), true);
            }
        } else {
            GoodsListByIdsRequest requestOuter = new GoodsListByIdsRequest();
            requestOuter.setGoodsIds(spuIdList);
            BaseResponse<GoodsListByIdsResponse> outGoodsList = goodsQueryProvider.listByIds(requestOuter);
            List<GoodsVO> goodsVOList = outGoodsList.getContext().getGoodsVOList();
            for (GoodsVO goodsVOParam : goodsVOList) {
                mainGoodsId2HasVirtualMap.put(goodsVOParam.getGoodsId(), Objects.equals(goodsVOParam.getGoodsType(), GoodsType.VIRTUAL_GOODS.ordinal()));
            }
        }
        return mainGoodsId2HasVirtualMap;
    }

    /**
     * 获取订单商品详情,会员价
     */
    private GoodsInfoResponse getGoodsResponse(List<String> skuIds, CustomerVO customer) {
        GoodsInfoViewByIdsRequest goodsInfoRequest = GoodsInfoViewByIdsRequest.builder()
                .goodsInfoIds(skuIds)
                .isHavSpecText(Constants.yes)
                .build();
        GoodsInfoViewByIdsResponse response = goodsInfoQueryProvider.listViewByIds(goodsInfoRequest).getContext();

        //获取客户的等级
        if (customer!=null && StringUtils.isNotBlank(customer.getCustomerId())) {
            //计算会员价
            List<GoodsInfoVO> goodsInfoVOList = marketingLevelPluginProvider.goodsListFilter(MarketingLevelGoodsListFilterRequest.builder()
                    .goodsInfos(KsBeanUtil.convertList(response.getGoodsInfos(), GoodsInfoDTO.class))
                    .customerDTO(KsBeanUtil.convert(customer, CustomerDTO.class)).build())
                    .getContext().getGoodsInfoVOList();
            response.setGoodsInfos(goodsInfoVOList);
        }
        return GoodsInfoResponse.builder().goodsInfos(response.getGoodsInfos())
                .goodses(response.getGoodses())
                .build();
    }


    /**
     * @description 0元订单批量支付
     * @param request
     * @menu 小程序
     * @status done
     */
    @ApiOperation("0元订单批量支付（支付网关默认为银联")
    @PostMapping("/default")
    public BaseResponse defaultPay(@RequestBody DefaultPayBatchRequest request) {
        TradeDefaultPayBatchRequest tradeDefaultPayBatchRequest = new TradeDefaultPayBatchRequest(request.getTradeIds(), PayWay.UNIONPAY);
        tradeProvider.defaultPayBatch(tradeDefaultPayBatchRequest);
        miniAppOrderProvider.createWxOrderAndPay(tradeDefaultPayBatchRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 购物车-订单结算
     */
    @PostMapping(value = "/stmtCommit")
    public BaseResponse<Boolean> settlementCommit(@RequestBody StmtParamVO paramVO) {
        String customerId = commonUtil.getOperatorId();
        if (StringUtils.isBlank(customerId)) {
            throw new SbcRuntimeException("K-010110");
        }

        List<String> skuIds = new ArrayList<>();
        List<TradeItemDTO> tradeItems = new ArrayList<>();
        List<TradeMarketingDTO> marketings = new ArrayList<>();
        paramVO.getMarketings().forEach(item -> {
            TradeMarketingDTO marketing = new TradeMarketingDTO();
            marketing.setMarketingId(item.getMarketingId());
            marketing.setSkuIds(new ArrayList<>());
            item.getGoodsInfos().stream().forEach(goods -> {
                skuIds.add(goods.getSkuId());
                marketing.getSkuIds().add(goods.getSkuId());
                tradeItems.add(TradeItemDTO.builder().skuId(goods.getSkuId()).num(goods.getCount()).build());
            });
            marketings.add(marketing);
        });

        DistributeChannel distributeChannel = commonUtil.getDistributeChannel();
        ChannelType channelType = distributeChannel.getChannelType();
        TradeItemConfirmSettlementRequest request = new TradeItemConfirmSettlementRequest();
        request.setCustomerId(customerId);
        request.setSkuIds(skuIds);
        request.setInviteeId(commonUtil.getPurchaseInviteeId());
        //request.setOpenFlag(openFlag);
        request.setChannelType(channelType);
        request.setDistributeChannel(distributeChannel);
        request.setTradeItems(tradeItems);
        request.setTradeMarketingList(marketings);
        request.setForceConfirm(false);
        request.setTerminalToken(commonUtil.getTerminalToken());
        //request.setAreaId(confirmRequest.getAreaId());
        return tradeItemProvider.confirmSettlement(request);
    }

    /**
     * 购物车-订单结算
     */
//    @PostMapping(value = "/stmtInfo")
//    public BaseResponse<StmtResultVO> settlementInfo() {
//        String customerId = commonUtil.getOperatorId();
//        //验证用户
//        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(customerId)).getContext();
//
//        List<TradeItemGroupVO> tradeItemGroups = tradeItemQueryProvider.listByTerminalToken(TradeItemByCustomerIdRequest
//                .builder().terminalToken(commonUtil.getTerminalToken()).build()).getContext().getTradeItemGroupList();
//
//        List<TradeConfirmItemVO> items = new ArrayList<>();
//        List<String> skuIds =  tradeItemGroups.stream().flatMap(i -> i.getTradeItems().stream()).map(TradeItemVO::getSkuId).collect(Collectors.toList());
//
//// TODO: 2022/6/29
//
//        //获取订单商品详情,包含区间价，会员级别价salePrice
//        GoodsInfoResponse skuResp = getGoodsResponse(skuIds, customer);
//        Map<String, Integer> cpsMap = skuResp.getGoodses().stream().filter(good -> good.getCpsSpecial() != null).collect(Collectors.toMap(GoodsVO::getGoodsId, GoodsVO::getCpsSpecial));
//        // 营销活动赠品
//        List<String> giftIds =
//                tradeItemGroups.stream().flatMap(i -> i.getTradeMarketingList().stream())
//                        .filter(i -> CollectionUtils.isNotEmpty(i.getGiftSkuIds())).flatMap(r -> r.getGiftSkuIds()
//                        .stream()).distinct().collect(Collectors.toList());
//        giftIds.addAll(tradeItemGroups.stream().flatMap(i -> i.getTradeMarketingList().stream())
//                .filter(i -> CollectionUtils.isNotEmpty(i.getMarkupSkuIds())).flatMap(r -> r.getMarkupSkuIds()
//                        .stream()).distinct().collect(Collectors.toList()));
//        Map<Long, StoreVO> storeMap = storeQueryProvider.listNoDeleteStoreByIds(new ListNoDeleteStoreByIdsRequest
//                (tradeItemGroups.stream().map(g -> g
//                        .getSupplier().getStoreId())
//                        .collect(Collectors.toList()))).getContext().getStoreVOList().stream().collect(Collectors
//                .toMap(StoreVO::getStoreId,
//                        s -> s));
//        TradeGetGoodsResponse giftResp;
//        giftResp = tradeQueryProvider.getGoods(TradeGetGoodsRequest.builder().skuIds(giftIds).build()).getContext();
//        final TradeGetGoodsResponse giftTemp = giftResp;
//        // 组合购标记
//        boolean suitMarketingFlag = tradeItemGroups.stream().anyMatch(s -> Objects.equals(Boolean.TRUE, s.getSuitMarketingFlag()));
//        Integer suitScene = null;
//        if(suitMarketingFlag && tradeItemGroups.stream().anyMatch(s -> s.getSuitScene()!=null)){
//            suitScene = tradeItemGroups.stream().filter(s -> s.getSuitScene()!=null).findFirst().get().getSuitScene();
//        }
//        //拼团标记
//        boolean grouponFlag = tradeItemGroups.stream().anyMatch(s -> s.getGrouponForm() != null && s.getGrouponForm().getOpenGroupon() != null);
//        confirmResponse.setSuitMarketingFlag(suitMarketingFlag);
//        confirmResponse.setSuitScene(suitScene);
//        // 如果为PC商城下单or组合购商品，将分销商品变为普通商品
//        if (ChannelType.PC_MALL.equals(commonUtil.getDistributeChannel().getChannelType()) || suitMarketingFlag || grouponFlag) {
//            tradeItemGroups.forEach(tradeItemGroup ->
//                    tradeItemGroup.getTradeItems().forEach(tradeItem -> {
//                        tradeItem.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS);
//                        if (suitMarketingFlag) {
//                            tradeItem.setBuyPoint(NumberUtils.LONG_ZERO);
//                        }
//                    })
//            );
//            skuResp.getGoodsInfos().forEach(item -> {
//                item.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS);
//                if (suitMarketingFlag) {
//                    item.setBuyPoint(NumberUtils.LONG_ZERO);
//                }
//            });
//        }
//
//        //企业会员判断
//        boolean isIepCustomerFlag = isIepCustomer(customer);
//        boolean isGoodsPoint = systemPointsConfigService.isGoodsPoint();
//
//        Map<String, Long> buyCountMap = new HashMap<>();
//        Map<String, BigDecimal> enterprisePriceMap = new HashMap<>();
//        List<GoodsLevelPriceVO> goodsLevelPrices = new ArrayList<>();
//        if (isIepCustomerFlag) {
//            tradeItemGroups.forEach(e -> {
//                List<TradeItemVO> tradeItems = e.getTradeItems();
//                tradeItems.forEach(item -> {
//                    buyCountMap.put(item.getSkuId(), item.getNum());
//                });
//            });
//            //企业价
//            EnterprisePriceGetRequest enterprisePriceGetRequest = new EnterprisePriceGetRequest();
//            enterprisePriceGetRequest.setGoodsInfoIds(skuIds);
//            enterprisePriceGetRequest.setCustomerId(customer.getCustomerId());
//            enterprisePriceGetRequest.setBuyCountMap(buyCountMap);
//            enterprisePriceGetRequest.setListFlag(false);
//            enterprisePriceGetRequest.setOrderFlag(true);
//
//            EnterprisePriceResponse context = enterpriseGoodsInfoQueryProvider.userPrice(enterprisePriceGetRequest).getContext();
//            Map<String, BigDecimal> priceMap = context.getPriceMap();
//            if (priceMap != null) {
//                enterprisePriceMap.putAll(priceMap);
//            }
//
//            //会员等级价
//            goodsLevelPrices.addAll(this.getGoodsLevelPrices(skuIds, customer));
//        }
//
//        //分销开关
//        boolean distributionPcMall = DefaultFlag.YES.equals(distributionCacheService.queryOpenFlag())
//                && !ChannelType.PC_MALL.equals(commonUtil.getDistributeChannel().getChannelType());
//        //商品验证并填充商品价格
//        List<Long> flashSaleGoodsIds = new ArrayList<>();
//        tradeItemGroups.forEach(
//                g -> {
//                    //周期购使用单品运费
//                    DefaultFlag freightTemplateType = Objects.nonNull(g.getCycleBuyInfo())
//                            ? DefaultFlag.YES
//                            : storeMap.get(g.getSupplier().getStoreId()).getFreightTemplateType();
//                    g.getSupplier().setFreightTemplateType(freightTemplateType);
//                    List<TradeItemVO> tradeItems = g.getTradeItems();
//                    List<TradeItemDTO> tradeItemDTOList = KsBeanUtil.convert(tradeItems, TradeItemDTO.class);
//                    // 分销商品、开店礼包商品、拼团商品、企业购商品，不验证起限定量
//                    skuResp.getGoodsInfos().forEach(item -> {
//                        //企业购商品
//                        boolean isIep = isIepCustomerFlag && isEnjoyIepGoodsInfo(item.getEnterPriseAuditState());
//                        if (DistributionGoodsAudit.CHECKED.equals(item.getDistributionGoodsAudit())
//                                || DefaultFlag.YES.equals(g.getStoreBagsFlag())
//                                || Objects.nonNull(g.getGrouponForm()) || isIep) {
//                            item.setCount(null);
//                            item.setMaxCount(null);
//                        }
//                    });
//                    //商品验证并填充商品价格
//                    List<TradeItemVO> tradeItemVOList =
//                            verifyQueryProvider.verifyGoods(new VerifyGoodsRequest(tradeItemDTOList, Collections
//                                    .emptyList(),
//                                    KsBeanUtil.convert(skuResp, TradeGoodsInfoPageDTO.class),
//                                    g.getSupplier().getStoreId(), true)).getContext().getTradeItems();
//                    tradeItemVOList.stream().forEach(
//                            tradeItemVO -> {
//                                tradeItemVO.setCpsSpecial(cpsMap.get(tradeItemVO.getSpuId()));
//                            }
//                    );
//                    //企业购商品价格回设
//                    if (isIepCustomerFlag) {
//                        tradeItemVOList.forEach(tradeItemVO -> {
//                            if (isEnjoyIepGoodsInfo(tradeItemVO.getEnterPriseAuditState())
//                                    && (!isGoodsPoint || (isGoodsPoint && (tradeItemVO.getBuyPoint() == null || tradeItemVO.getBuyPoint() == 0)))) {
//                                tradeItemVO.setPrice(tradeItemVO.getEnterPrisePrice());
//
//                                BigDecimal price = enterprisePriceMap.get(tradeItemVO.getSkuId());
//                                if (price != null) {
//                                    tradeItemVO.setPrice(price);
//                                }
//                                tradeItemVO.setSplitPrice(tradeItemVO.getEnterPrisePrice().multiply(new BigDecimal(tradeItemVO.getNum())));
//                                tradeItemVO.setLevelPrice(tradeItemVO.getEnterPrisePrice());
//                                //判断当前用户对应企业购商品等级企业价
//                                if (CollectionUtils.isNotEmpty(goodsLevelPrices)) {
//                                    Optional<GoodsLevelPriceVO> first = goodsLevelPrices.stream()
//                                            .filter(goodsLevelPrice -> goodsLevelPrice.getGoodsInfoId().equals(tradeItemVO.getSkuId()))
//                                            .findFirst();
//                                    tradeItemVO.setPrice(first.isPresent() ? first.get().getPrice() : tradeItemVO.getEnterPrisePrice());
//                                }
//                            }
//                        });
//                    }
//                    //抢购商品价格回设
//                    if (StringUtils.isNotBlank(g.getSnapshotType()) && (g.getSnapshotType().equals(Constants.FLASH_SALE_GOODS_ORDER_TYPE))) {
//                        tradeItemVOList.forEach(tradeItemVO -> {
//                            g.getTradeItems().forEach(tradeItem -> {
//                                if (tradeItem.getSkuId().equals(tradeItemVO.getSkuId())) {
//                                    tradeItemVO.setPrice(tradeItem.getPrice());
//                                }
//                            });
//                        });
//                        if (CollectionUtils.isNotEmpty(tradeItemVOList)) {
//                            flashSaleGoodsIds.add(tradeItemVOList.get(0).getFlashSaleGoodsId());
//                        }
//                    }
//                    tradeItemVOList.forEach(tradeItemVO -> {
//
//                        g.getTradeItems().forEach(tradeItem -> {
//                            g.getTradeMarketingList().forEach(tradeItemMarketingVO -> {
//                                if (tradeItemMarketingVO.getSkuIds().contains(tradeItemVO.getSkuId())
//                                        && !tradeItemVO.getMarketingIds().contains(tradeItemMarketingVO.getMarketingId())) {
//                                    tradeItemVO.getMarketingIds().add(tradeItemMarketingVO.getMarketingId());
//                                    tradeItemVO.getMarketingLevelIds().add(tradeItemMarketingVO.getMarketingLevelId());
//                                }
//                            });
//                            if ((Objects.nonNull(tradeItem.getIsAppointmentSaleGoods()) && tradeItem.getIsAppointmentSaleGoods())) {
//                                if (tradeItem.getSkuId().equals(tradeItemVO.getSkuId())) {
//                                    tradeItemVO.setPrice(tradeItem.getPrice());
//                                    tradeItemVO.setIsAppointmentSaleGoods(tradeItem.getIsAppointmentSaleGoods());
//                                    tradeItemVO.setAppointmentSaleId(tradeItem.getAppointmentSaleId());
//                                }
//                            }
//                            if (Objects.nonNull(tradeItem.getIsBookingSaleGoods()) && tradeItem.getIsBookingSaleGoods() && tradeItem.getBookingType() == BookingType.FULL_MONEY) {
//                                if (tradeItem.getSkuId().equals(tradeItemVO.getSkuId())) {
//                                    tradeItemVO.setPrice(tradeItem.getPrice());
//                                    tradeItemVO.setIsBookingSaleGoods(tradeItem.getIsBookingSaleGoods());
//                                    tradeItemVO.setBookingSaleId(tradeItem.getBookingSaleId());
//                                    tradeItemVO.setBookingType(tradeItem.getBookingType());
//                                }
//                            }
//                            if (Objects.nonNull(tradeItem.getIsBookingSaleGoods()) && tradeItem.getIsBookingSaleGoods() && tradeItem.getBookingType() == BookingType.EARNEST_MONEY) {
//                                if (tradeItem.getSkuId().equals(tradeItemVO.getSkuId())) {
//                                    tradeItemVO.setPrice(tradeItem.getOriginalPrice());
//                                    tradeItemVO.setIsBookingSaleGoods(tradeItem.getIsBookingSaleGoods());
//                                    tradeItemVO.setBookingSaleId(tradeItem.getBookingSaleId());
//                                    tradeItemVO.setBookingType(tradeItem.getBookingType());
//                                    tradeItemVO.setHandSelStartTime(tradeItem.getHandSelStartTime());
//                                    tradeItemVO.setHandSelEndTime(tradeItem.getHandSelEndTime());
//                                    tradeItemVO.setTailStartTime(tradeItem.getTailStartTime());
//                                    tradeItemVO.setTailEndTime(tradeItem.getTailEndTime());
//                                    tradeItemVO.setEarnestPrice(tradeItem.getEarnestPrice());
//                                    tradeItemVO.setSwellPrice(tradeItem.getSwellPrice());
//                                }
//                            }
//                        });
//                    });
//                    g.setTradeItems(tradeItemVOList);
//                    // 分销商品、开店礼包商品，重新设回市场价
//                    if (distributionPcMall) {
//                        g.getTradeItems().stream().filter(tradeItemVO -> DefaultFlag.YES.equals(g.getStoreBagsFlag())
//                                || (Objects.isNull(tradeItemVO.getBuyPoint()) || tradeItemVO.getBuyPoint() == 0)).forEach(item -> {
//                            DefaultFlag storeOpenFlag = distributionCacheService.queryStoreOpenFlag(item.getStoreId()
//                                    .toString());
//                            if ((Objects.isNull(item.getIsFlashSaleGoods()) ||
//                                    (Objects.nonNull(item.getIsFlashSaleGoods()) && !item.getIsFlashSaleGoods())) &&
//                                    (Objects.isNull(item.getIsAppointmentSaleGoods()) || !item.getIsAppointmentSaleGoods()) &&
//                                    !(Objects.nonNull(item.getIsBookingSaleGoods()) && item.getIsBookingSaleGoods() && item.getBookingType() == BookingType.FULL_MONEY) &&
//                                    DefaultFlag.YES.equals(storeOpenFlag) && (
//                                    DistributionGoodsAudit.CHECKED.equals(item.getDistributionGoodsAudit())
//                                            || DefaultFlag.YES.equals(g.getStoreBagsFlag()))) {
//                                if (null == item.getOriginalPrice()) {
//                                    item.setOriginalPrice(BigDecimal.ZERO);
//                                }
//                                item.setSplitPrice(item.getOriginalPrice().multiply(new BigDecimal(item.getNum())));
//                                item.setPrice(item.getOriginalPrice());
//                                item.setLevelPrice(item.getOriginalPrice());
//                                if (DefaultFlag.YES.equals(g.getStoreBagsFlag())) {
//                                    item.setBuyPoint(NumberUtils.LONG_ZERO);
//                                }
//                            } else {
//                                item.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS);
//                            }
//                        });
//                    }
//                    confirmResponse.setStoreBagsFlag(g.getStoreBagsFlag());
//                }
//        );
//
//        //秒杀包邮
//        if (CollectionUtils.isNotEmpty(flashSaleGoodsIds)) {
//            FlashSaleGoodsListRequest flashSaleGoodsListRequest = new FlashSaleGoodsListRequest();
//            flashSaleGoodsListRequest.setIdList(flashSaleGoodsIds);
//            List<FlashSaleGoodsVO> flashSaleGoodsVOList = flashSaleGoodsQueryProvider.list(flashSaleGoodsListRequest).getContext().getFlashSaleGoodsVOList();
//            if (CollectionUtils.isNotEmpty(flashSaleGoodsVOList)) {
//                Boolean flashFreeDelivery = Boolean.FALSE;
//                for (FlashSaleGoodsVO flashSaleGoodsVO : flashSaleGoodsVOList) {
//                    flashFreeDelivery = flashSaleGoodsVO.getPostage().equals(NumberUtils.INTEGER_ONE);
//                }
//                confirmResponse.setFlashFreeDelivery(flashFreeDelivery);
//            }
//        }
//
//        List<TradeItemMarketingVO> tradeMarketingList = tradeItemGroups.stream()
//                .flatMap(tradeItemGroupVO -> tradeItemGroupVO.getTradeMarketingList().stream())
//                .collect(Collectors.toList());
//        // 加价购商品信息填充
//        List<Long> marketingIds =
//                tradeMarketingList.parallelStream().filter(i -> CollectionUtils.isNotEmpty(i.getMarkupSkuIds()))
//                        .map(TradeItemMarketingVO::getMarketingId).distinct().collect(Collectors.toList());
//
//        MarkupListRequest markupListRequest = MarkupListRequest.builder().marketingId(marketingIds).build();
//        List<MarkupLevelVO> levelList = markupQueryProvider.getMarkupList(markupListRequest).getContext().getLevelList();
//
//        //赠品信息填充
//        List<String> giftItemIds =
//                tradeMarketingList.parallelStream().filter(i -> CollectionUtils.isNotEmpty(i.getGiftSkuIds()))
//                        .flatMap(r -> r.getGiftSkuIds().stream()).distinct().collect(Collectors.toList());
//        List<TradeItemDTO> gifts =
//                giftItemIds.stream().map(i -> TradeItemDTO.builder().price(BigDecimal.ZERO)
//                        .skuId(i)
//                        .build()).collect(Collectors.toList());
//
//        List<TradeItemVO> giftVoList = verifyQueryProvider.mergeGoodsInfo(new MergeGoodsInfoRequest(gifts
//                , KsBeanUtil.convert(giftTemp, TradeGoodsListDTO.class)))
//                .getContext().getTradeItems();
//        gifts = KsBeanUtil.convert(giftVoList, TradeItemDTO.class);
//        List<TradeItemDTO> markupItem = new ArrayList<>();
//        for (TradeItemGroupVO g : tradeItemGroups) {
//            Map<String, List<MarkupLevelVO>> listMap = levelList.stream().collect(Collectors.groupingBy(l -> "" + l.getMarkupId() + l.getId()));
//            // 通过 加价购id 阶梯id 加购商品sku定位 换购价格
//            List<TradeItemDTO> finalMarkupItem = markupItem;
//            g.getTradeMarketingList().parallelStream().filter(i -> CollectionUtils.isNotEmpty(i.getMarkupSkuIds()))
//                    .forEach(m -> {
//                        List<MarkupLevelVO> markupLevelVOS = listMap.get("" + m.getMarketingId() + m.getMarketingLevelId());
//                        if (CollectionUtils.isNotEmpty(markupLevelVOS)) {
//                            m.getMarkupSkuIds().stream().forEach(sku -> {
//                                markupLevelVOS.stream().flatMap(l -> l.getMarkupLevelDetailList().stream())
//                                        .filter(detailVO -> sku.equals(detailVO.getGoodsInfoId()))
//                                        .forEach(detail -> {
//                                            finalMarkupItem.add(TradeItemDTO.builder()
//                                                    .price(detail.getMarkupPrice())
//                                                    .skuId(detail.getGoodsInfoId())
//                                                    .num(NumberUtils.LONG_ONE)
//                                                    .isMarkupGoods(Boolean.TRUE)
//                                                    .build());
//                                        });
//                            });
//
//                        }
//                    });
//
//            List<TradeItemVO> markupVoList = verifyQueryProvider.mergeGoodsInfo(new MergeGoodsInfoRequest(markupItem
//                    , KsBeanUtil.convert(giftTemp, TradeGoodsListDTO.class)))
//                    .getContext().getTradeItems();
//            // 换购商品阶梯价不受其他影响
//            markupVoList.forEach(m -> {
//                m.setSplitPrice(m.getPrice());
//                m.setLevelPrice(m.getPrice());
//                m.setOriginalPrice(m.getPrice());
//            });
//            markupItem = KsBeanUtil.convert(markupVoList, TradeItemDTO.class);
//            items.add(tradeQueryProvider.queryPurchaseInfo(TradeQueryPurchaseInfoRequest.builder()
//                    .tradeItemGroupDTO(KsBeanUtil.convert(g, TradeItemGroupDTO.class))
//                    .tradeItemList(gifts).markupItemList(markupItem).build()).getContext().getTradeConfirmItemVO());
//        }
//        this.setGoodsStatus(items);
//
//        // 验证小店商品
//        this.validShopGoods(tradeItemGroups, commonUtil.getDistributeChannel());
//        //设置购买总积分
//        confirmResponse.setTotalBuyPoint(items.stream().flatMap(i -> i.getTradeItems().stream())
//                .filter(i -> Objects.isNull(i.getIsMarkupGoods()) || !i.getIsMarkupGoods()).mapToLong(v -> Objects.isNull(v.getBuyPoint()) ? 0 : v.getBuyPoint() * v.getNum()).sum());
//
//        confirmResponse.setTradeConfirmItems(items);
//
//        BigDecimal totalCommission = dealDistribution(tradeItemGroups, confirmResponse);
//
//        // 设置小店名称、返利总价
//        confirmResponse.setShopName(distributionCacheService.getShopName());
//
//        confirmResponse.setTotalCommission(totalCommission);
//
//        // 设置邀请人名字
//        if (StringUtils.isNotEmpty(commonUtil.getDistributeChannel().getInviteeId())) {
//            DistributionCustomerByCustomerIdRequest request = new DistributionCustomerByCustomerIdRequest();
//            request.setCustomerId(commonUtil.getDistributeChannel().getInviteeId());
//            DistributionCustomerVO distributionCustomer = distributionCustomerQueryProvider.getByCustomerId(request)
//                    .getContext().getDistributionCustomerVO();
//            if (distributionCustomer != null) {
//                confirmResponse.setInviteeName(distributionCustomer.getCustomerName());
//            }
//        }
//
//        // 校验拼团信息
//        validGrouponOrder(confirmResponse, tradeItemGroups, customerId);
//        // 校验组合购活动信息
//        dealSuitOrder(confirmResponse, tradeItemGroups, customerId);
//
//        // 填充立即购买的满系营销信息
//        fillMarketingInfo(confirmResponse, tradeItemGroups);
//
//        //填充周期购信息
//        fillCycleBuyInfoToConfirmResponse(confirmResponse, tradeItemGroups);
//
//        // 根据确认订单计算出的信息，查询出使用优惠券页需要的优惠券列表数据
//        DefaultFlag openFlag = distributionCacheService.queryOpenFlag();
//        List<TradeItemInfoDTO> tradeDtos = items.stream().flatMap(confirmItem ->
//                confirmItem.getTradeItems().stream().map(tradeItem -> {
//                    TradeItemInfoDTO dto = new TradeItemInfoDTO();
//                    dto.setBrandId(tradeItem.getBrand());
//                    dto.setCateId(tradeItem.getCateId());
//                    dto.setSpuId(tradeItem.getSpuId());
//                    dto.setSkuId(tradeItem.getSkuId());
//                    dto.setStoreId(confirmItem.getSupplier().getStoreId());
//                    dto.setPrice(tradeItem.getSplitPrice());
//                    dto.setDistributionGoodsAudit(tradeItem.getDistributionGoodsAudit());
//                    if (DefaultFlag.NO.equals(openFlag) || DefaultFlag.NO.equals(
//                            distributionCacheService.queryStoreOpenFlag(String.valueOf(tradeItem.getStoreId())))) {
//                        tradeItem.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS);
//                    }
//                    return dto;
//                })
//        ).collect(Collectors.toList());
//
//        // 加价购填充
//        markupForGoodsInfo(confirmResponse);
//
//        CouponCodeListForUseByCustomerIdRequest requ = CouponCodeListForUseByCustomerIdRequest.builder()
//                .customerId(customerId)
//                .tradeItems(tradeDtos).price(confirmResponse.getTotalPrice()).build();
//
//        confirmResponse.setCouponCodes(couponCodeQueryProvider.listForUseByCustomerId(requ).getContext()
//                .getCouponCodeList());
//
//
//        //打包信息
//        List<String> mainGoodsIdList =  tradeItemGroups.stream().flatMap(i -> i.getTradeItems().stream())
//                .map(TradeItemVO::getSpuId).collect(Collectors.toList());
//        Map<String, Boolean> mainGoodsId2HasVirtualMap = this.getGoodsIdHasVirtual(mainGoodsIdList);
//
//        List<TradeConfirmItemVO> tradeConfirmItems = confirmResponse.getTradeConfirmItems();
//        for (TradeConfirmItemVO tradeConfirmItem : tradeConfirmItems) {
//            List<TradeItemVO> tradeItems = tradeConfirmItem.getTradeItems();
//            for (TradeItemVO tradeItemVO : tradeItems) {
//                tradeItemVO.setShowPhoneNum(mainGoodsId2HasVirtualMap.get(tradeItemVO.getSpuId()) != null && mainGoodsId2HasVirtualMap.get(tradeItemVO.getSpuId()));
//            }
//        }
//
//        //视频号黑名单
//        Map<String, Boolean> goodsId2VideoChannelMap = new HashMap<>();
//        for (TradeConfirmItemVO tradeConfirmItem : confirmResponse.getTradeConfirmItems()) {
//            List<String> skuIdList = tradeConfirmItem.getTradeItems().stream().map(TradeItemVO::getSkuId).collect(Collectors.toList());
//            goodsId2VideoChannelMap.putAll(videoChannelSetFilterControllerProvider.filterGoodsIdHasVideoChannelMap(skuIdList).getContext());
//        }
//
//        List<String> blackListGoodsIdList = new ArrayList<>();
//        // 积分和名单商品不能使用积分，也不参与分摊
//        GoodsBlackListPageProviderRequest goodsBlackListPageProviderRequest = new GoodsBlackListPageProviderRequest();
//        goodsBlackListPageProviderRequest.setBusinessCategoryColl(Collections.singletonList(GoodsBlackListCategoryEnum.POINT_NOT_SPLIT.getCode()));
//        GoodsBlackListPageProviderResponse context = goodsBlackListProvider.listNoPage(goodsBlackListPageProviderRequest).getContext();
//        if (context.getPointNotSplitBlackListModel() != null && !CollectionUtils.isEmpty(context.getPointNotSplitBlackListModel().getGoodsIdList())) {
//            blackListGoodsIdList.addAll(context.getPointNotSplitBlackListModel().getGoodsIdList());
//        }
//
//        for (TradeConfirmItemVO tradeConfirmItem : confirmResponse.getTradeConfirmItems()) {
//            List<TradeItemVO> tradeItems = tradeConfirmItem.getTradeItems();
//            for (TradeItemVO tradeItem : tradeItems) {
//                if(blackListGoodsIdList.contains(tradeItem.getSpuId()) || (goodsId2VideoChannelMap.get(tradeItem.getSpuId()) != null && goodsId2VideoChannelMap.get(tradeItem.getSpuId()))){
//                    tradeItem.setInPointBlackList(true);
//                }
//            }
//        }
//        return BaseResponse.success(confirmResponse);
//    }
}
