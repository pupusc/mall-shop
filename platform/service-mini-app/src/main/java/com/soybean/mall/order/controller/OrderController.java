package com.soybean.mall.order.controller;

import com.alibaba.fastjson.JSON;
import com.soybean.mall.cart.vo.PromoteInfoResultVO$Coupon;
import com.soybean.mall.common.CommonUtil;
import com.soybean.mall.order.api.provider.order.MiniAppOrderProvider;
import com.soybean.mall.order.api.request.order.GetPaymentParamsRequest;
import com.soybean.mall.order.api.response.OrderCommitResponse;
import com.soybean.mall.order.bean.vo.OrderCommitResultVO;
import com.soybean.mall.order.bean.vo.WxOrderPaymentParamsVO;
import com.soybean.mall.order.common.DefaultPayBatchRequest;
import com.soybean.mall.order.common.PayServiceHelper;
import com.soybean.mall.order.request.OrderPaymentParamReq;
import com.soybean.mall.order.request.StmtParamVO;
import com.soybean.mall.order.request.TradeItemConfirmRequest;
import com.soybean.mall.order.response.OrderConfirmResponse;
import com.soybean.mall.order.response.SettlementResultVO$GoodsInfo;
import com.soybean.mall.order.response.StmtResultVO;
import com.soybean.mall.service.CommonService;
import com.soybean.mall.service.PromoteFilter;
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
import com.wanmi.sbc.common.enums.TerminalSource;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.address.CustomerDeliveryAddressQueryProvider;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.fandeng.ExternalProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.address.CustomerDeliveryAddressByIdRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.request.fandeng.FanDengPointRequest;
import com.wanmi.sbc.customer.api.request.store.NoDeleteStoreByIdRequest;
import com.wanmi.sbc.customer.api.response.address.CustomerDeliveryAddressByIdResponse;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.api.enums.GoodsBlackListCategoryEnum;
import com.wanmi.sbc.goods.api.provider.blacklist.GoodsBlackListProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.VideoChannelSetFilterControllerProvider;
import com.wanmi.sbc.goods.api.provider.price.GoodsIntervalPriceProvider;
import com.wanmi.sbc.goods.api.request.blacklist.GoodsBlackListPageProviderRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsListByIdsRequest;
import com.wanmi.sbc.goods.api.request.goods.PackDetailByPackIdsRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoStoreIdBySkuIdRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdsRequest;
import com.wanmi.sbc.goods.api.response.blacklist.GoodsBlackListPageProviderResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsListByIdsResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsPackDetailResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoStoreIdBySkuIdResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdsResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponCacheProvider;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponCodeQueryProvider;
import com.wanmi.sbc.marketing.api.provider.market.MarketingCommonQueryProvider;
import com.wanmi.sbc.marketing.api.provider.markup.MarkupQueryProvider;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingLevelPluginProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCodeListForUseByCustomerIdRequest;
import com.wanmi.sbc.marketing.api.request.market.InfoForPurchseRequest;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingLevelGoodsListFilterRequest;
import com.wanmi.sbc.marketing.api.response.coupon.CouponCodeListForUseByCustomerIdResponse;
import com.wanmi.sbc.marketing.api.response.market.MarketInfoForPurchaseResponse;
import com.wanmi.sbc.marketing.bean.dto.TradeItemInfoDTO;
import com.wanmi.sbc.marketing.bean.dto.TradeMarketingDTO;
import com.wanmi.sbc.marketing.bean.enums.FullBuyType;
import com.wanmi.sbc.marketing.bean.enums.GiftType;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.vo.CouponCodeVO;
import com.wanmi.sbc.marketing.bean.vo.GoodsInfoMarketingVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingBuyoutPriceLevelVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingFullDiscountLevelVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingFullGiftDetailVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingFullGiftLevelVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingFullReductionLevelVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingHalfPriceSecondPieceLevelVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingViewVO;
import com.wanmi.sbc.order.api.provider.purchase.PurchaseQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeItemProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeItemQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.TradePriceProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.VerifyQueryProvider;
import com.wanmi.sbc.order.api.request.purchase.PurchaseGetGoodsMarketingRequest;
import com.wanmi.sbc.order.api.request.trade.TradeCommitRequest;
import com.wanmi.sbc.order.api.request.trade.TradeDefaultPayBatchRequest;
import com.wanmi.sbc.order.api.request.trade.TradeGetByIdRequest;
import com.wanmi.sbc.order.api.request.trade.TradeItemByCustomerIdRequest;
import com.wanmi.sbc.order.api.request.trade.TradeItemConfirmSettlementRequest;
import com.wanmi.sbc.order.api.request.trade.TradePriceParamBO;
import com.wanmi.sbc.order.api.request.trade.TradeQueryPurchaseInfoRequest;
import com.wanmi.sbc.order.api.request.trade.VerifyGoodsRequest;
import com.wanmi.sbc.order.api.response.trade.TradeGetByIdResponse;
import com.wanmi.sbc.order.api.response.trade.TradePriceResultBO;
import com.wanmi.sbc.order.api.response.trade.TradeQueryPurchaseInfoResponse;
import com.wanmi.sbc.order.bean.dto.TradeGoodsInfoPageDTO;
import com.wanmi.sbc.order.bean.dto.TradeItemDTO;
import com.wanmi.sbc.order.bean.dto.TradeItemGroupDTO;
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
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @menu 小程序
 */
@Slf4j
@RestController
@RequestMapping("/wx/order")
public class OrderController {
    //结算页优惠券格式
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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
    @Autowired
    private TradePriceProvider tradePriceProvider;
    @Autowired
    private MarkupQueryProvider markupQueryProvider;
    @Autowired
    private CouponCacheProvider couponCacheProvider;
    @Resource
    private CouponCodeQueryProvider couponCodeQueryProvider;
    @Autowired
    private MarketingCommonQueryProvider marketingCommonQueryProvider;

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    @Autowired
    private PurchaseQueryProvider purchaseQueryProvider;

    @Autowired
    private ExternalProvider externalProvider;


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
                log.info("OrderController.commit paramRequest:{}", JSON.toJSONString(tradeCommitRequest));
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
     * 创建订单
     */
    @RequestMapping(value = "/commit4cart", method = RequestMethod.POST)
    @MultiSubmitWithToken
    public BaseResponse<WxOrderPaymentVO> commit4cart(@RequestBody @Valid TradeCommitRequest tradeCommitRequest) {
        //验证操作用户
        Operator operator = commonUtil.getOperator();
        if (StringUtils.isBlank(operator.getUserId())) {
            throw new SbcRuntimeException("K-010110");
        }
        //验证地址信息
        CustomerDeliveryAddressByIdResponse address = customerDeliveryAddressQueryProvider
                .getById(new CustomerDeliveryAddressByIdRequest(tradeCommitRequest.getConsigneeId())).getContext();
        if (address == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR, "指定的地址信息不存在");
        }
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

        RLock rLock = redissonClient.getFairLock(commonUtil.getOperatorId());
        List<OrderCommitResultVO> successResults;
        try {
            if (rLock.tryLock(3, 3, TimeUnit.SECONDS)) {
                tradeCommitRequest.setOperator(operator);
                DistributeChannel channel = new DistributeChannel();
                channel.setChannelType(ChannelType.MINIAPP);
                tradeCommitRequest.setMiniProgramCart(true);
                tradeCommitRequest.setTerminalToken(commonUtil.getTerminalToken());
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
                req.setAppid(appId);//作废
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
                BaseResponse<WxOrderPaymentParamsVO> response = miniAppOrderProvider.createWxOrderAndGetPaymentsParams(getPaymentParamsRequest);
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
        result.setOrderTimeOut(trades.get(0).getOrderTimeOut());
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

        PurchaseGetGoodsMarketingRequest marketingRequest = new PurchaseGetGoodsMarketingRequest();
        marketingRequest.setGoodsInfos(skuResp.getGoodsInfos());
        marketingRequest.setCustomer(customer);
        Map<String, List<MarketingViewVO>> marketingResponse = purchaseQueryProvider.getGoodsMarketing(marketingRequest).getContext().getMap();

        Map<String, GoodsInfoVO> skuMap = skuResp.getGoodsInfos().stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, g -> g));

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

        // 折扣信息
        List<TradeMarketingDTO> tradeMarketingList = new ArrayList<>();
        for (TradeItemVO tmpTradeItemVO : tradeItemVOList) {
            if ((Objects.isNull(tmpTradeItemVO.getIsAppointmentSaleGoods()) || Boolean.FALSE.equals(tmpTradeItemVO.getIsAppointmentSaleGoods()))
                    && (Objects.isNull(tmpTradeItemVO.getIsBookingSaleGoods()) || Boolean.FALSE.equals(tmpTradeItemVO.getIsBookingSaleGoods()))
                    && skuMap.containsKey(tmpTradeItemVO.getSkuId())) {
                GoodsInfoVO sku = skuMap.get(tmpTradeItemVO.getSkuId());
                if (sku.getDistributionGoodsAudit() != DistributionGoodsAudit.CHECKED) {
                    TradeMarketingDTO tradeMarketing = this.chooseDefaultMarketing(KsBeanUtil.convert(tmpTradeItemVO, TradeItemDTO.class), marketingResponse.get(tmpTradeItemVO.getSkuId()));
                    if (tradeMarketing != null) {
                        tradeMarketingList.add(tradeMarketing);
                        tmpTradeItemVO.setMarketingIds(Collections.singletonList(tradeMarketing.getMarketingId()));
                        tmpTradeItemVO.setMarketingLevelIds(Collections.singletonList(tradeMarketing.getMarketingLevelId()));
                    }
                } else {
                    // 4.2.非礼包、分销商品，重置商品价格
                    tmpTradeItemVO.setPrice(sku.getMarketPrice());
                }
            }
        }

        if (CollectionUtils.isNotEmpty(tradeMarketingList)) {
            Iterator<TradeMarketingDTO> it = tradeMarketingList.iterator();
            Long currentPoint = -1L;
            while (it.hasNext()){
                TradeMarketingDTO tradeMarketingDTO = it.next();
                if(tradeMarketingDTO.getMarketingSubType() != null && tradeMarketingDTO.getMarketingSubType() == 10){
                    //检查用户积分是否足够
                    if(currentPoint == -1){
                        String fanDengUserNo = commonUtil.getCustomer().getFanDengUserNo();
                        currentPoint = externalProvider.getByUserNoPoint(FanDengPointRequest.builder().userNo(fanDengUserNo).build()).getContext().getCurrentPoint();
                    }
                    if(currentPoint == null || currentPoint < tradeMarketingDTO.getPointNeed()){
                        it.remove();
                    }
                }
            }
        }

        TradeItemGroupDTO tradeItemGroup = new TradeItemGroupDTO();
        tradeItemGroup.setTradeMarketingList(tradeMarketingList);
        tradeItemGroup.setTradeItems(KsBeanUtil.convert(tradeItemVOList,TradeItemDTO.class));
        TradeQueryPurchaseInfoRequest tradeQueryPurchaseInfoRequest = new TradeQueryPurchaseInfoRequest();
        tradeQueryPurchaseInfoRequest.setTradeItemGroupDTO(tradeItemGroup);
        tradeQueryPurchaseInfoRequest.setMarkupItemList(new ArrayList<>());
        tradeQueryPurchaseInfoRequest.setTradeItemList(new ArrayList<>());
        TradeQueryPurchaseInfoResponse tradeQueryPurchaseInfoResponse =
                tradeQueryProvider.queryPurchaseInfo(tradeQueryPurchaseInfoRequest).getContext();
        TradeConfirmItemVO tempTradeConfirmItemVO = tradeQueryPurchaseInfoResponse.getTradeConfirmItemVO();



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


        tradeConfirmItemVO.setTradeItems(tempTradeConfirmItemVO.getTradeItems());
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

        //保存优惠券信息 TODO 临时方案，后续需要进行优化
        this.saveUserCoupon2Redis(customer, confirmResponse);

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
     * 保存优惠券到 redis
     * @param customer
     * @param confirmResponse
     * @return
     */
    private List<CouponCodeVO> saveUserCoupon2Redis(CustomerGetByIdResponse customer, OrderConfirmResponse confirmResponse) {

        List<TradeItemInfoDTO> tradeItemInfoDTOList = confirmResponse.getTradeConfirmItems().stream().flatMap(confirmItem ->
                confirmItem.getTradeItems().stream().map(tradeItem -> {
                    TradeItemInfoDTO dto = new TradeItemInfoDTO();
                    dto.setBrandId(tradeItem.getBrand());
                    dto.setCateId(tradeItem.getCateId());
                    dto.setSpuId(tradeItem.getSpuId());
                    dto.setSkuId(tradeItem.getSkuId());
                    dto.setStoreId(confirmItem.getSupplier().getStoreId());
                    dto.setPrice(tradeItem.getSplitPrice());
                    dto.setDistributionGoodsAudit(tradeItem.getDistributionGoodsAudit());
                    return dto;
                })
        ).collect(Collectors.toList());

        CouponCodeListForUseByCustomerIdRequest request = new CouponCodeListForUseByCustomerIdRequest();
        request.setCustomerId(customer.getCustomerId());
        request.setTradeItems(tradeItemInfoDTOList);
        request.setPrice(confirmResponse.getTotalPrice());
        return couponCodeQueryProvider.listForUseByCustomerId(request).getContext().getCouponCodeList();
    }

    /**
     * 选择商品默认的营销，以及它的level
     */
    private TradeMarketingDTO chooseDefaultMarketing(TradeItemDTO tradeItem, List<MarketingViewVO> marketings) {

        BigDecimal total = tradeItem.getPrice().multiply(new BigDecimal(tradeItem.getNum()));
        Long num = tradeItem.getNum();

        TradeMarketingDTO tradeMarketing = new TradeMarketingDTO();
        tradeMarketing.setSkuIds(Collections.singletonList(tradeItem.getSkuId()));
        tradeMarketing.setGiftSkuIds(new ArrayList<>());
        if (CollectionUtils.isNotEmpty(marketings)) {
//            // 积分换购优先级最高
//            for (MarketingViewVO marketing : marketings) {
//                // 积分换购
//                if(marketing.getSubType() == MarketingSubType.POINT_BUY){
//                    String skuId = tradeItem.getSkuId();
//                    List<MarketingPointBuyLevelVO> pointBuyLevelList = marketing.getPointBuyLevelList();
//                    if(CollectionUtils.isNotEmpty(pointBuyLevelList)){
//                        for (MarketingPointBuyLevelVO marketingPointBuyLevelVO : pointBuyLevelList) {
//                            if(marketingPointBuyLevelVO.getSkuId().equals(skuId)){
//                                tradeMarketing.setMarketingLevelId(pointBuyLevelList.get(0).getId());
//                                tradeMarketing.setMarketingId(pointBuyLevelList.get(0).getMarketingId());
//                                tradeMarketing.setMarketingSubType(marketing.getSubType().toValue());
//                                tradeMarketing.setPointNeed(pointBuyLevelList.get(0).getPointNeed());
//                                return tradeMarketing;
//                            }
//                        }
//                    }
//                }
//            }
            for (MarketingViewVO marketing : marketings) {
                // 满金额减
                if (marketing.getSubType() == MarketingSubType.REDUCTION_FULL_AMOUNT) {
                    List<MarketingFullReductionLevelVO> levels = marketing.getFullReductionLevelList();
                    if (CollectionUtils.isNotEmpty(levels)) {
                        levels.sort(Comparator.comparing(MarketingFullReductionLevelVO::getFullAmount).reversed());
                        for (MarketingFullReductionLevelVO level : levels) {
                            if (level.getFullAmount().compareTo(total) != 1) {
                                tradeMarketing.setMarketingLevelId(level.getReductionLevelId());
                                tradeMarketing.setMarketingId(level.getMarketingId());
                                return tradeMarketing;
                            }
                        }
                    }
                }

                // 满数量减
                if (marketing.getSubType() == MarketingSubType.REDUCTION_FULL_COUNT) {
                    List<MarketingFullReductionLevelVO> levels = marketing.getFullReductionLevelList();
                    if (CollectionUtils.isNotEmpty(levels)) {
                        levels.sort(Comparator.comparing(MarketingFullReductionLevelVO::getFullCount).reversed());
                        for (MarketingFullReductionLevelVO level : levels) {
                            if (level.getFullCount().compareTo(num) != 1) {
                                tradeMarketing.setMarketingLevelId(level.getReductionLevelId());
                                tradeMarketing.setMarketingId(level.getMarketingId());
                                return tradeMarketing;
                            }
                        }
                    }
                }

                // 满金额折
                if (marketing.getSubType() == MarketingSubType.DISCOUNT_FULL_AMOUNT) {
                    List<MarketingFullDiscountLevelVO> levels = marketing.getFullDiscountLevelList();
                    if (CollectionUtils.isNotEmpty(levels)) {
                        levels.sort(Comparator.comparing(MarketingFullDiscountLevelVO::getFullAmount).reversed());
                        for (MarketingFullDiscountLevelVO level : levels) {
                            if (level.getFullAmount().compareTo(total) != 1) {
                                tradeMarketing.setMarketingLevelId(level.getDiscountLevelId());
                                tradeMarketing.setMarketingId(level.getMarketingId());
                                return tradeMarketing;
                            }
                        }
                    }
                }
                // 满数量折
                if (marketing.getSubType() == MarketingSubType.DISCOUNT_FULL_COUNT) {
                    List<MarketingFullDiscountLevelVO> levels = marketing.getFullDiscountLevelList();
                    if (CollectionUtils.isNotEmpty(levels)) {
                        levels.sort(Comparator.comparing(MarketingFullDiscountLevelVO::getFullCount).reversed());
                        for (MarketingFullDiscountLevelVO level : levels) {
                            if (level.getFullCount().compareTo(num) != 1) {
                                tradeMarketing.setMarketingLevelId(level.getDiscountLevelId());
                                tradeMarketing.setMarketingId(level.getMarketingId());
                                return tradeMarketing;
                            }
                        }
                    }
                }

                // 满金额赠
                if (marketing.getSubType() == MarketingSubType.GIFT_FULL_AMOUNT) {
                    List<MarketingFullGiftLevelVO> levels = marketing.getFullGiftLevelList();
                    if (CollectionUtils.isNotEmpty(levels)) {
                        levels.sort(Comparator.comparing(MarketingFullGiftLevelVO::getFullAmount).reversed());
                        for (MarketingFullGiftLevelVO level : levels) {
                            if (level.getFullAmount().compareTo(total) != 1) {
                                tradeMarketing.setMarketingLevelId(level.getGiftLevelId());
                                tradeMarketing.setMarketingId(level.getMarketingId());
                                List<String> giftIds =
                                        level.getFullGiftDetailList().stream().map(MarketingFullGiftDetailVO::getProductId).collect(Collectors.toList());
                                if (GiftType.ONE.equals(level.getGiftType())) {
                                    giftIds = Collections.singletonList(giftIds.get(0));
                                }
                                tradeMarketing.setGiftSkuIds(giftIds);
                                return tradeMarketing;
                            }
                        }
                    }
                }
                // 满数量赠
                if (marketing.getSubType() == MarketingSubType.GIFT_FULL_COUNT) {
                    List<MarketingFullGiftLevelVO> levels = marketing.getFullGiftLevelList();
                    if (CollectionUtils.isNotEmpty(levels)) {
                        levels.sort(Comparator.comparing(MarketingFullGiftLevelVO::getFullCount).reversed());
                        for (MarketingFullGiftLevelVO level : levels) {
                            if (level.getFullCount().compareTo(num) != 1) {
                                tradeMarketing.setMarketingLevelId(level.getGiftLevelId());
                                tradeMarketing.setMarketingId(level.getMarketingId());
                                List<String> giftIds =
                                        level.getFullGiftDetailList().stream().map(MarketingFullGiftDetailVO::getProductId).collect(Collectors.toList());
                                if (GiftType.ONE.equals(level.getGiftType())) {
                                    giftIds = Collections.singletonList(giftIds.get(0));
                                }
                                tradeMarketing.setGiftSkuIds(giftIds);
                                return tradeMarketing;
                            }
                        }
                    }
                }

                // 打包一口价
//                if (marketing.getSubType() == MarketingSubType.BUYOUT_PRICE) {
//                    List<MarketingBuyoutPriceLevelVO> levels = marketing.getBuyoutPriceLevelList();
//                    if (CollectionUtils.isNotEmpty(levels)) {
//                        levels.sort(Comparator.comparing(MarketingBuyoutPriceLevelVO::getChoiceCount).reversed());
//                        for (MarketingBuyoutPriceLevelVO level : levels) {
//                            if (level.getChoiceCount().compareTo(num) != 1) {
//                                tradeMarketing.setMarketingLevelId(level.getReductionLevelId());
//                                tradeMarketing.setMarketingId(level.getMarketingId());
//                                return tradeMarketing;
//                            }
//                        }
//                    }
//                }

                // 第二件半价
//                if (marketing.getSubType() == MarketingSubType.HALF_PRICE_SECOND_PIECE) {
//                    List<MarketingHalfPriceSecondPieceLevelVO> levels = marketing.getHalfPriceSecondPieceLevel();
//                    if (CollectionUtils.isNotEmpty(levels)) {
//                        levels.sort(Comparator.comparing(MarketingHalfPriceSecondPieceLevelVO::getNumber).reversed());
//                        for (MarketingHalfPriceSecondPieceLevelVO level : levels) {
//                            if (level.getNumber().compareTo(num) != 1) {
//                                tradeMarketing.setMarketingLevelId(level.getId());
//                                tradeMarketing.setMarketingId(level.getMarketingId());
//                                return tradeMarketing;
//                            }
//                        }
//                    }
//                }
            }
        }
        return null;
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
     * 获取支付参数信息
     * @param orderPaymentParamReq
     * @return
     */
    @PostMapping("/miniOrderPaymentParam")
    public BaseResponse<WxOrderPaymentVO> miniOrderPaymentParams(@RequestBody OrderPaymentParamReq orderPaymentParamReq) {

        if (!Objects.equals(commonUtil.getTerminal(), TerminalSource.MALL_NORMAL)
                && !Objects.equals(commonUtil.getTerminal(), TerminalSource.MINIPROGRAM)) {
            throw new SbcRuntimeException("K-000001", "只有小程序和视频号可以调用");
        }

        WxOrderPaymentVO wxOrderPaymentVO = new WxOrderPaymentVO();
        //获取订单信息
        TradeGetByIdRequest tradeGetByIdRequest = new TradeGetByIdRequest();
        tradeGetByIdRequest.setTid(orderPaymentParamReq.getTid());
        TradeGetByIdResponse context = tradeQueryProvider.getOrderById(tradeGetByIdRequest).getContext();
        if (context.getTradeVO() == null) {
            throw new SbcRuntimeException("K-000001", String.format("%s 订单不存在", orderPaymentParamReq.getTid()));
        }
        TradeVO tradeVO = context.getTradeVO();
        //0元支付不需要生成预支付单
        if(tradeVO.getTradePrice().getTotalPrice().compareTo(new BigDecimal(0)) <= 0){
            throw new SbcRuntimeException("K-000001", String.format("%s 不需要调用此方法", orderPaymentParamReq.getTid()));
        } else {

            //2 小程序下单
            if (Objects.equals(commonUtil.getTerminal(), TerminalSource.MINIPROGRAM)) {
                //生成预支付订单
                WxPayForJSApiRequest req = wxPayCommon(tradeVO.getBuyer().getOpenId(), orderPaymentParamReq.getTid());
                req.setAppid(appId);
                BaseResponse<Map<String,String>> prepayResult= wxPayProvider.wxPayForLittleProgram(req);
                if(prepayResult == null || prepayResult.getContext().isEmpty()){
                    throw new SbcRuntimeException("K-000001", "小程序支付参数生成有误");
                }
                wxOrderPaymentVO.setTimeStamp(prepayResult.getContext().get("timeStamp"));
                wxOrderPaymentVO.setNonceStr(prepayResult.getContext().get("nonceStr"));
                wxOrderPaymentVO.setPrepayId(prepayResult.getContext().get("package"));
                wxOrderPaymentVO.setPaySign(prepayResult.getContext().get("paySign"));
                wxOrderPaymentVO.setSignType("MD5");
            }else{

                BaseResponse<WxOrderPaymentParamsVO> response = miniAppOrderProvider.getWxOrderPaymentParams(tradeVO.getBuyer().getOpenId(), tradeVO.getId());
                if(response == null || response.getContext() ==null){
                    throw new SbcRuntimeException("K-000001", "网络异常请重新下单！！");
                }
                wxOrderPaymentVO.setPrepayId(response.getContext().getPrepayId());
                wxOrderPaymentVO.setPaySign(response.getContext().getPaySign());
                wxOrderPaymentVO.setNonceStr(response.getContext().getNonceStr());
                wxOrderPaymentVO.setTimeStamp(response.getContext().getTimeStamp());
                wxOrderPaymentVO.setSignType(response.getContext().getSignType());
            }
            OrderCommitResultVO orderCommitResultVO = KsBeanUtil.convert(tradeVO, OrderCommitResultVO.class);
            wxOrderPaymentVO.setOrderInfo(this.convertResult(Collections.singletonList(orderCommitResultVO), tradeVO.getBuyer().getOpenId()));
            String prepayId = wxOrderPaymentVO.getPrepayId();
            String ppid = "";
            if(StringUtils.isNotEmpty(prepayId) && prepayId.length() > 10){
                ppid = prepayId.substring(10,prepayId.length());
            }
            wxOrderPaymentVO.getOrderInfo().getOrderDetail().getPayInfo().setPrepayId(ppid);
            wxOrderPaymentVO.setOrderInfoStr(JSON.toJSONString(wxOrderPaymentVO.getOrderInfo()));
        }
        return BaseResponse.success(wxOrderPaymentVO);
    }

    /**
     * 购物车-订单结算提交
     */
    @PostMapping(value = "/stmtCommit")
    public BaseResponse<Boolean> settlementCommit(@RequestBody StmtParamVO paramVO) {
        String customerId = commonUtil.getOperatorId();
        if (StringUtils.isBlank(customerId)) {
            throw new SbcRuntimeException("K-010110");
        }
        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(customerId)).getContext();
        if (customer == null) {
            log.warn("没有找到指定的用户, customerId={}", customerId);
            throw new SbcRuntimeException(CommonErrorCode.FAILED, "没有找对指定的用户");
        }

        List<TradeItemDTO> tradeItems = paramVO.getMarketings().stream().flatMap(i->i.getGoodsInfos().stream()).map(goods->
            TradeItemDTO.builder().skuId(goods.getSkuId()).num(goods.getCount()).build()).collect(Collectors.toList());

        List<String> skuIds = tradeItems.stream().map(TradeItemDTO::getSkuId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(skuIds)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR, "提交的sku不能为空");
        }

        List<TradeMarketingDTO> marketings = paramVO.getMarketings().stream()
                //小程序购物车暂时只支持满减和满折营销活动
                .filter(i->Objects.nonNull(i.getMarketingId()) && i.getMarketingId()>0 && Objects.nonNull(i.getMarketingLevelId()) && i.getMarketingLevelId()>0)
                .map(item-> {
                    TradeMarketingDTO mktDTO = new TradeMarketingDTO();
                    mktDTO.setMarketingId(item.getMarketingId());
                    mktDTO.setMarketingLevelId(item.getMarketingLevelId());
                    mktDTO.setSkuIds(item.getGoodsInfos().stream().map(i->i.getSkuId()).collect(Collectors.toList()));
                    return mktDTO;
                }).collect(Collectors.toList());

        BaseResponse<GoodsInfoStoreIdBySkuIdResponse> storeIdResp = goodsInfoQueryProvider.getStoreIdByGoodsId(new GoodsInfoStoreIdBySkuIdRequest(skuIds.get(0)));
        if (storeIdResp == null || storeIdResp.getContext() == null) {
            log.warn("根据商品没有找到对应的店铺信息, skuId={}", skuIds.get(0));
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR, "根据商品没有找到对应的店铺信息");
        }

        //验证营销活动->查询商品支持的营销活动
        List<GoodsInfoMarketingVO> marketingInfos = skuIds.stream()
                .map(i -> GoodsInfoMarketingVO.builder().goodsInfoId(i).storeId(storeIdResp.getContext().getStoreId()).build())
                .collect(Collectors.toList());

        MarketInfoForPurchaseResponse marketResp = marketingCommonQueryProvider.queryInfoForPurchase(new InfoForPurchseRequest(marketingInfos, customer, null)).getContext();
        Map<Long, MarketingViewVO> mktMap = marketResp.getGoodsInfos().stream()
                .filter(i->Objects.nonNull(i.getMarketingViewList()))
                .flatMap(item -> item.getMarketingViewList().stream())
                .collect(Collectors.toMap(MarketingViewVO::getMarketingId, i -> i, (a, b) -> a));

        for (TradeMarketingDTO item : marketings) {
            MarketingViewVO mkt = mktMap.get(item.getMarketingId());
            if (mkt == null) {
                log.warn("营销方案不存在或者已过期, mktId={}", item.getMarketingId());
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR, "营销方案已不存在");
            }
            if (!PromoteFilter.supportMkt(mkt.getSubType())) {
                log.warn("小程序当前仅支持满减或满折营销, subType={}", mkt.getSubType());
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR, "营销方案类型错误");
            }
        }

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
     * 购物车-订单结算查询
     */
    @PostMapping(value = "/stmtInfo")
    public BaseResponse<StmtResultVO> settlementInfo() {
        String customerId = commonUtil.getOperatorId();
        if (StringUtils.isBlank(customerId)) {
            throw new SbcRuntimeException("K-010110");
        }
        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(customerId)).getContext();
        if (customer == null) {
            log.warn("没有找到指定的用户, customerId={}", customerId);
            throw new SbcRuntimeException(CommonErrorCode.FAILED, "没有找对指定的用户");
        }

        //查询结算快照
        List<TradeItemGroupVO> tradeItemGroups = tradeItemQueryProvider.listByTerminalToken(TradeItemByCustomerIdRequest
                .builder().terminalToken(commonUtil.getTerminalToken()).build()).getContext().getTradeItemGroupList();
        if (CollectionUtils.isEmpty(tradeItemGroups)) {
            throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS, "没有找到商品的结算信息");
        }

        //默认只有一个店铺
        TradeItemGroupVO tradeItemGroup = tradeItemGroups.get(0);

        List<TradeItemDTO> tradeItems = tradeItemGroup.getTradeItems().stream()
                .map(item -> TradeItemDTO.builder().skuId(item.getSkuId()).num(item.getNum()).build()).collect(Collectors.toList());

        //获取订单商品详情和会员价salePrice
        GoodsInfoResponse skuResp = getGoodsResponse(tradeItems.stream().map(TradeItemDTO::getSkuId).collect(Collectors.toList()), customer);
        List<String> spuIds = skuResp.getGoodses().stream().map(GoodsVO::getGoodsId).collect(Collectors.toList());
        //设置是否展示输入电话输入框
        //获取商品下的打包信息 TODO 修改此处的时候，同时修改 h5的 purchase 接口
        Map<String, Boolean> mainGoodsId2HasVirtualMap = this.getGoodsIdHasVirtual(spuIds);

//        List<TradeConfirmItemVO> items = new ArrayList<>(1);
        //一期只能购买一个商品，只有一个商家
        StoreVO store = storeQueryProvider.getNoDeleteStoreById(NoDeleteStoreByIdRequest.builder()
                .storeId(skuResp.getGoodsInfos().get(0).getStoreId()).build()).getContext().getStoreVO();

//        TradeConfirmItemVO tradeConfirmItemVO = new TradeConfirmItemVO();
        //商品验证并填充商品价格
        List<TradeItemVO> tradeItemVOS = verifyQueryProvider.verifyGoods(new VerifyGoodsRequest(tradeItems, Collections.emptyList(),
                KsBeanUtil.convert(skuResp, TradeGoodsInfoPageDTO.class), store.getStoreId(), true)).getContext().getTradeItems();
        tradeItemGroup.setTradeItems(tradeItemVOS);

        TradeQueryPurchaseInfoRequest tradeQueryPurchaseInfoRequest = new TradeQueryPurchaseInfoRequest();
        tradeQueryPurchaseInfoRequest.setTradeItemGroupDTO(KsBeanUtil.convert(tradeItemGroup, TradeItemGroupDTO.class));
        tradeQueryPurchaseInfoRequest.setMarkupItemList(new ArrayList<>());
        tradeQueryPurchaseInfoRequest.setTradeItemList(new ArrayList<>());
        TradeQueryPurchaseInfoResponse tradeQueryPurchaseInfoResponse =
                tradeQueryProvider.queryPurchaseInfo(tradeQueryPurchaseInfoRequest).getContext();
        TradeConfirmItemVO tradeConfirmItemVO = tradeQueryPurchaseInfoResponse.getTradeConfirmItemVO();

        List<TradeItemVO> tradeItemVOList = tradeConfirmItemVO.getTradeItems();
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
            if(priceByGoodsId.getContext() != null) {
                tradeItem.setPropPrice(Double.valueOf(priceByGoodsId.getContext()));
            }
            if(blackListGoodsIdList.contains(tradeItem.getSpuId()) || (goodsId2VideoChannelMap.get(tradeItem.getSpuId()) != null && goodsId2VideoChannelMap.get(tradeItem.getSpuId()))){
                tradeItem.setInPointBlackList(true);
            }
            //设置是否显示输入框
            tradeItem.setShowPhoneNum(mainGoodsId2HasVirtualMap.get(tradeItem.getSpuId()) != null && mainGoodsId2HasVirtualMap.get(tradeItem.getSpuId()));
        }

        //--------------------------------------------------------------------------------------------
        //按照id->model映射
        Map<String, TradeItemVO> tradeItemMap = tradeItemVOList.stream().collect(Collectors.toMap(TradeItemVO::getSkuId, item->item, (a,b)->a));
        List<TradePriceParamBO.GoodsInfo> calcSkus = new ArrayList<>();  //餐与算价的商品列表
        List<SettlementResultVO$GoodsInfo> viewSkus = new ArrayList<>(); //返回客户端的商品列表
        Map<String, Long> sku2market = new HashMap<>();
        for (TradeItemMarketingVO makertVO : tradeItemGroup.getTradeMarketingList()) {
            makertVO.getSkuIds().forEach(skuId -> sku2market.put(skuId, makertVO.getMarketingId()));
        }

        for (TradeItemVO item : tradeItemVOList) {
            TradePriceParamBO.GoodsInfo calcSku = new TradePriceParamBO.GoodsInfo();
            calcSku.setGoodsInfoId(item.getSkuId());
            calcSku.setBuyCount(item.getNum());
            calcSku.setMarketingId(sku2market.get(item.getSkuId()));
            calcSkus.add(calcSku);
            //客户端展示对象
            if (tradeItemMap.get(item.getSkuId()) != null) {
                SettlementResultVO$GoodsInfo viewSku = new SettlementResultVO$GoodsInfo();
                viewSku.setSkuId(item.getSkuId());
                TradeItemVO tradeItem = tradeItemMap.get(item.getSkuId());
                viewSku.setSkuName(tradeItem.getSkuName());
                viewSku.setSpuId(tradeItem.getSpuId());
                viewSku.setSpuName(tradeItem.getSpuName());
                viewSku.setPropPrice(tradeItem.getPropPrice());
                viewSku.setPayPrice(tradeItem.getPrice());
                viewSku.setNum(tradeItem.getNum());
                viewSku.setGoodsCubage(tradeItem.getGoodsCubage());
                viewSku.setGoodsWeight(tradeItem.getGoodsWeight());
                viewSku.setInPointBlackList(tradeItem.getInPointBlackList());
                viewSku.setShowPhoneNum(tradeItem.getShowPhoneNum());
                viewSku.setPic(tradeItem.getPic());
                viewSku.setFreightTempId(tradeItem.getFreightTempId());
                viewSku.setGoodsType(tradeItem.getGoodsType().toValue());
                viewSku.setSplitPrice(tradeItem.getSplitPrice());
                viewSkus.add(viewSku);
            }
        }
        //商品信息
        StmtResultVO resultVO = new StmtResultVO();
        resultVO.setGoodsInfos(viewSkus);
        //--------------------------------------------------------------------------------------------------------------------------------
        //计算价格
        TradePriceParamBO paramBO = new TradePriceParamBO();
        paramBO.setCustomerId(customerId);
        paramBO.setGoodsInfos(calcSkus);
        BaseResponse<TradePriceResultBO> priceResponse = tradePriceProvider.calcPrice(paramBO);
        if (priceResponse != null) {
            resultVO.setCalcPrice(priceResponse.getContext());
        }
        //--------------------------------------------------------------------------------------------------------------------------------
        //查询优惠券
        List<TradeItemInfoDTO> tradeDtos =  new ArrayList<>();
        for (TradeItemVO item : tradeItemVOList) {
            TradeItemInfoDTO tradeDto = new TradeItemInfoDTO();
            tradeDto.setBrandId(item.getBrand());
            tradeDto.setCateId(item.getCateId());
            tradeDto.setSpuId(item.getSpuId());
            tradeDto.setSkuId(item.getSkuId());
            tradeDto.setStoreId(item.getStoreId());
            tradeDto.setPrice(item.getSplitPrice());
            tradeDto.setDistributionGoodsAudit(item.getDistributionGoodsAudit());
            tradeDtos.add(tradeDto);
        }
        CouponCodeListForUseByCustomerIdRequest couponParam = CouponCodeListForUseByCustomerIdRequest.builder()
                .customerId(customerId).tradeItems(tradeDtos).price(priceResponse.getContext().getPayPrice()).build();
        BaseResponse<CouponCodeListForUseByCustomerIdResponse> couponResult = couponCodeQueryProvider.listForUseByCustomerId(couponParam);
        if (couponResult.getContext() == null || couponResult.getContext().getCouponCodeList() == null) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED, "查询用户优惠券返回值错误");
        }

        resultVO.setCoupons(new ArrayList<>());
        for (CouponCodeVO couponBO : couponResult.getContext().getCouponCodeList()) {
            PromoteInfoResultVO$Coupon couponVO = new PromoteInfoResultVO$Coupon();
            couponVO.setStartTime(couponBO.getStartTime().format(formatter));
            couponVO.setEndTime(couponBO.getEndTime().format(formatter));
            couponVO.setActivityId(couponBO.getActivityId());
            couponVO.setCouponId(couponBO.getCouponId());
            couponVO.setCouponCodeId(couponBO.getCouponCodeId());
            couponVO.setCouponType(couponBO.getCouponType().toValue());
            couponVO.setCouponName(couponBO.getCouponName());
            couponVO.setCouponDesc(couponBO.getCouponDesc());
            couponVO.setDenomination(couponBO.getDenomination());
            couponVO.setLimitPrice(FullBuyType.FULL_MONEY.equals(couponBO.getFullBuyType()) ? couponBO.getFullBuyPrice() : BigDecimal.ZERO);
            couponVO.setLimitScope(couponBO.getScopeType().toValue());
            couponVO.setNearOverdue(couponBO.isNearOverdue());
            couponVO.setStatus(couponBO.getStatus().toValue());
            resultVO.getCoupons().add(couponVO);
        }
        //----------------------------------------------------------------
        //商家信息
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
        resultVO.setSupplier(supplier);

        return BaseResponse.success(resultVO);
    }
}
