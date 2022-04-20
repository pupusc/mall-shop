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
import com.soybean.mall.order.request.TradeItemConfirmRequest;
import com.soybean.mall.order.response.OrderConfirmResponse;
import com.soybean.mall.service.CommonService;
import com.soybean.mall.vo.WxAddressInfoVO;
import com.soybean.mall.vo.WxOrderCommitResultVO;
import com.soybean.mall.vo.WxOrderPaymentVO;
import com.soybean.mall.vo.WxProductInfoVO;
import com.soybean.mall.wx.mini.order.bean.request.WxCreateOrderRequest;
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
import com.wanmi.sbc.common.util.*;
import com.wanmi.sbc.customer.api.provider.address.CustomerDeliveryAddressQueryProvider;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.address.CustomerDeliveryAddressByIdRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
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
import com.wanmi.sbc.goods.api.provider.price.GoodsIntervalPriceProvider;
import com.wanmi.sbc.goods.api.request.blacklist.GoodsBlackListPageProviderRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsListByIdsRequest;
import com.wanmi.sbc.goods.api.request.goods.PackDetailByPackIdsRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdsRequest;
import com.wanmi.sbc.goods.api.response.blacklist.GoodsBlackListPageProviderResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsListByIdsResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsPackDetailResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdsResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponCodeQueryProvider;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingLevelPluginProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCodeListForUseByCustomerIdRequest;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingLevelGoodsListFilterRequest;
import com.wanmi.sbc.marketing.bean.dto.TradeItemInfoDTO;
import com.wanmi.sbc.order.api.provider.trade.TradeProvider;
import com.wanmi.sbc.order.api.provider.trade.VerifyQueryProvider;
import com.wanmi.sbc.order.api.request.trade.TradeCommitRequest;
import com.wanmi.sbc.order.api.request.trade.TradeDefaultPayBatchRequest;
import com.wanmi.sbc.order.api.request.trade.VerifyGoodsRequest;
import com.wanmi.sbc.order.bean.dto.TradeGoodsInfoPageDTO;
import com.wanmi.sbc.order.bean.dto.TradeItemDTO;
import com.wanmi.sbc.order.bean.vo.*;
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
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
        rLock.lock();
        List<OrderCommitResultVO> successResults;
        try {
            Operator operator = commonUtil.getOperator();
            tradeCommitRequest.setOperator(operator);
            DistributeChannel channel = new DistributeChannel();
            channel.setChannelType(ChannelType.MINIAPP);
            tradeCommitRequest.setDistributeChannel(channel);
            tradeCommitRequest.setGoodsChannelTypeSet(Collections.singletonList(commonUtil.getTerminal().getCode()));
            BaseResponse<OrderCommitResponse> orderCommitResponseBaseResponse = tradeProvider.commitTrade(tradeCommitRequest);

            successResults = orderCommitResponseBaseResponse.getContext().getOrderCommitResults();

        } catch (Exception e) {
            throw e;
        } finally {
            rLock.unlock();
        }
        return BaseResponse.success(getOrderPaymentResult(successResults,tradeCommitRequest.getOpenId(),tradeCommitRequest.getMiniProgramScene()));
    }

    private WxOrderPaymentVO getOrderPaymentResult(List<OrderCommitResultVO> trades,String openId,Integer miniProgramScene){
        WxOrderPaymentVO wxOrderPaymentVO = new WxOrderPaymentVO();
        wxOrderPaymentVO.setCouponFlag(trades.get(0).getCouponFlag());
        //0元支付不需要生成预支付单
        if(trades.get(0).getTradePrice().getTotalPrice().compareTo(new BigDecimal(0))==0){
            wxOrderPaymentVO.setOrderInfo(convertResult(trades,openId));
            return wxOrderPaymentVO;
        }
        //小程序订单
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
                return  wxOrderPaymentVO;
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
            if(blackListGoodsIdList.contains(tradeItem.getSpuId())){
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



}
