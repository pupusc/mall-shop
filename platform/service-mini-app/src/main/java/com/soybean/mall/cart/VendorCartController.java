package com.soybean.mall.cart;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.soybean.common.resp.CommonPageResp;
import com.soybean.elastic.api.provider.spu.EsSpuNewProvider;
import com.soybean.elastic.api.resp.EsSpuNewResp;
import com.soybean.mall.cart.vo.CalcPriceItem;
import com.soybean.mall.cart.vo.CartInfoResultVO$Marketing;
import com.soybean.mall.cart.vo.CartInfoResultVO$Sku;
import com.soybean.mall.cart.vo.CartInfoResultVO$Store;
import com.soybean.mall.cart.vo.ComposePriceParamVO;
import com.soybean.mall.cart.vo.CouponGoodsParamVO;
import com.soybean.mall.cart.vo.MarketingGoodsParamVO;
import com.soybean.mall.cart.vo.PromoteCouponParamVO;
import com.soybean.mall.cart.vo.PromoteFitGoodsResultVO;
import com.soybean.mall.cart.vo.PromoteGoodsResultVO;
import com.soybean.mall.cart.vo.PromoteInfoResultVO$Coupon;
import com.soybean.mall.cart.vo.PromoteInfoResultVO$Marketing;
import com.soybean.mall.cart.vo.PromoteMarketingParamVO;
import com.soybean.mall.cart.vo.PurchaseInfoParamVO;
import com.soybean.mall.cart.vo.PurchaseInfoResultVO;
import com.soybean.mall.cart.vo.PurchasePriceResultVO;
import com.soybean.mall.common.CommonUtil;
import com.soybean.mall.goods.req.KeyWordSpuQueryReq;
import com.soybean.mall.goods.response.SpuNewBookListResp;
import com.soybean.mall.goods.service.SpuNewSearchService;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import com.wanmi.sbc.common.enums.ChannelType;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.goods.api.provider.spec.GoodsInfoSpecDetailRelQueryProvider;
import com.wanmi.sbc.goods.api.request.spec.GoodsInfoSpecDetailRelBySpuIdsRequest;
import com.wanmi.sbc.goods.api.response.spec.GoodsInfoSpecDetailRelBySpuIdsResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoSpecDetailRelVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponCacheProvider;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponInfoQueryProvider;
import com.wanmi.sbc.marketing.api.provider.market.MarketingQueryProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCacheListForGoodsListRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponInfoDetailByIdRequest;
import com.wanmi.sbc.marketing.api.request.market.MarketingGetByIdRequest;
import com.wanmi.sbc.marketing.api.response.coupon.CouponCacheListForGoodsListResponse;
import com.wanmi.sbc.marketing.api.response.coupon.CouponInfoDetailByIdResponse;
import com.wanmi.sbc.marketing.api.response.market.MarketingGetByIdForCustomerResponse;
import com.wanmi.sbc.marketing.bean.enums.FullBuyType;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.vo.CouponGoodsVO;
import com.wanmi.sbc.marketing.bean.vo.CouponInfoVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingForEndVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingViewVO;
import com.wanmi.sbc.order.api.provider.purchase.PurchaseQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.TradePriceProvider;
import com.wanmi.sbc.order.api.request.purchase.PurchaseGetStoreCouponExistRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseInfoRequest;
import com.wanmi.sbc.order.api.request.trade.TradePriceParamBO;
import com.wanmi.sbc.order.api.response.purchase.PurchaseGetStoreCouponExistResponse;
import com.wanmi.sbc.order.api.response.purchase.PurchaseListResponse;
import com.wanmi.sbc.order.api.response.trade.TradePriceResultBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @menu 小程序
 * @desc 购物车
 * @author Liang Jun
 * @date 2022-06-15 11:14:00
 */
@Slf4j
@RestController
@RequestMapping("/vendorCart")
public class VendorCartController {
    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private PurchaseQueryProvider purchaseQueryProvider;
    @Autowired
    private TradePriceProvider tradePriceProvider;
    @Autowired
    private CouponCacheProvider couponCacheProvider;
    @Autowired
    private CouponInfoQueryProvider couponInfoQueryProvider;
    @Autowired
    private MarketingQueryProvider marketingQueryProvider;
    @Autowired
    private EsSpuNewProvider esSpuNewProvider;
    @Autowired
    private SpuNewSearchService spuNewSearchService;
    @Autowired
    private GoodsInfoSpecDetailRelQueryProvider goodsInfoSpecDetailRelQueryProvider;

    /**
     * 购物车-购物车信息
     * @see com.wanmi.sbc.purchase.PurchaseBaseController#purchaseInfo(com.wanmi.sbc.order.api.request.purchase.PurchaseListRequest)
     */
    @PostMapping(value = "/purchaseInfo")
    public BaseResponse<PurchaseInfoResultVO> purchaseInfo(@RequestBody PurchaseInfoParamVO paramVO) {
        CustomerVO customer = commonUtil.getCustomer();
        if (customer == null) {
            throw new SbcRuntimeException("用户登录信息不存在，请重新登录");
        }
        //统一查询购物车内容
        BaseResponse<PurchaseListResponse> cartResponse = purchaseQueryProvider.purchaseInfo(
                PurchaseInfoRequest.builder().customer(customer).inviteeId(commonUtil.getPurchaseInviteeId()).build());

        PurchaseInfoResultVO resultVO = new PurchaseInfoResultVO();
        PurchaseListResponse cartInfo = cartResponse.getContext();
        if (cartInfo == null) {
            return BaseResponse.success(resultVO);
        }
        //店铺信息
        if (CollectionUtils.isNotEmpty(cartInfo.getStores())) {
            CartInfoResultVO$Store store = new CartInfoResultVO$Store();
            store.setStoreId(cartInfo.getStores().get(0).getStoreId());
            store.setStoreName(cartInfo.getStores().get(0).getStoreName());
            BaseResponse<PurchaseGetStoreCouponExistResponse> conponResponse = purchaseQueryProvider.getStoreCouponExist(
                    PurchaseGetStoreCouponExistRequest.builder().customer(customer).inviteeId(commonUtil.getPurchaseInviteeId()).build());
            store.setAbleCoupon(conponResponse.getContext() != null && Boolean.TRUE.equals(conponResponse.getContext().getMap().get(store.getStoreId())));
            resultVO.setStore(store);
        }
        //商品信息
        if (CollectionUtils.isEmpty(cartInfo.getGoodsInfos())) {
            return BaseResponse.success(resultVO);
        }

        //sku对应的营销列表映射
        Map<String, List<MarketingViewVO>> goodsMarketingMap = cartInfo.getGoodsMarketingMap();
        //markerting的id->model映射
        Map<Long, MarketingViewVO> markertingMap = new HashMap<>();
        //仅支持满减和满折的营销
        for (List<MarketingViewVO> marketings : goodsMarketingMap.values()) {
            Iterator<MarketingViewVO> iter = marketings.iterator();
            while (iter.hasNext()) {
                MarketingViewVO next = iter.next();
                boolean support = MarketingSubType.REDUCTION_FULL_AMOUNT.equals(next.getSubType())
                        || MarketingSubType.REDUCTION_FULL_COUNT.equals(next.getSubType())
                        || MarketingSubType.DISCOUNT_FULL_AMOUNT.equals(next.getSubType())
                        || MarketingSubType.DISCOUNT_FULL_COUNT.equals(next.getSubType());
                if (!support) {
                    iter.remove();
                }
                markertingMap.put(next.getMarketingId(), next);
            }
        }

        //按创建时间倒叙排列
        //cartInfo.setGoodsInfos(cartInfo.getGoodsInfos().stream().sorted(Comparator.comparing(GoodsInfoVO::getCreateTime).reversed()).collect(Collectors.toList()));
        //spu信息按照id->model映射
        Map<String, GoodsVO> goodsVOMap = cartInfo.getGoodses().stream().collect(Collectors.toMap(GoodsVO::getGoodsId, item -> item, (a, b) -> a));
        //按照客户指定状态分组：指定促销方案；指定选中商品；
        Map<String, Long> client2Marketing = new HashMap<>();
        Map<String, Boolean> client2Checked = new HashMap<>();
        for (PurchaseInfoParamVO.Marketing marketing : paramVO.getMarketings()) {
            for (PurchaseInfoParamVO.GoodsInfo goodsInfo : marketing.getGoodsInfos()) {
                client2Marketing.put(goodsInfo.getGoodsInfoId(), marketing.getMarketingId());
                client2Checked.put(goodsInfo.getGoodsInfoId(), Boolean.TRUE.equals(goodsInfo.isChecked()));
            }
        }
        //markerting的id->goods列表映射
        Map<Long, List<GoodsInfoVO>> makertingGoodsMap = new HashMap<>();
        for (GoodsInfoVO goodsInfo : cartInfo.getGoodsInfos()) {
            //处理客户端指定切换的促销方案
            Long marketingId = client2Marketing.get(goodsInfo.getGoodsInfoId());
            if (marketingId == null) {
                List<MarketingViewVO> marketingViewVOS = goodsMarketingMap.get(goodsInfo.getGoodsInfoId());
                marketingId = CollectionUtils.isEmpty(marketingViewVOS) ? 0L : marketingViewVOS.get(0).getMarketingId();
            }
            makertingGoodsMap.computeIfAbsent(marketingId, key -> new ArrayList<>()).add(goodsInfo);
        }

        resultVO.setMarketings(new ArrayList<>());
        for (Map.Entry<Long, List<GoodsInfoVO>> entry : makertingGoodsMap.entrySet()) {
            CartInfoResultVO$Marketing marketingVO = new CartInfoResultVO$Marketing();
            marketingVO.setMarketingId(entry.getKey());

            //促销活动信息
            if (markertingMap.get(entry.getKey()) != null) {
                MarketingViewVO marketingBO = markertingMap.get(entry.getKey());
                marketingVO.setType(marketingBO.getMarketingType().toValue());
                marketingVO.setSubType(marketingBO.getSubType().toValue());
            }
            marketingVO.setGoodsInfos(entry.getValue().stream().map(item->{
                CartInfoResultVO$Sku skuVO = new CartInfoResultVO$Sku();
                skuVO.setGoodsId(item.getGoodsId());
                skuVO.setGoodsNo(item.getGoodsNo());
                skuVO.setGoodsStatus(item.getGoodsStatus());
                skuVO.setGoodsType(item.getGoodsType());
                skuVO.setGoodsInfoId(item.getGoodsInfoId());
                skuVO.setGoodsInfoImg(item.getGoodsInfoImg());
                skuVO.setGoodsInfoNo(item.getGoodsInfoNo());
                skuVO.setGoodsInfoName(item.getGoodsInfoName());
                skuVO.setBuyCount(item.getBuyCount());
                skuVO.setMarketPrice(item.getMarketPrice());
                skuVO.setSalePrice(item.getSalePrice());
                skuVO.setSpecText(item.getSpecText());
                skuVO.setMaxCount(item.getMaxCount());
                skuVO.setSpecMore(goodsVOMap.containsKey(item.getGoodsId()) && Boolean.FALSE.equals(goodsVOMap.get(item.getGoodsId()).getSingleSpecFlag()));
                skuVO.setChecked(client2Checked.containsKey(item.getGoodsInfoId())); //处理客户端指定选中的商品
                skuVO.setMarketings(buildMarketings(goodsMarketingMap.get(item.getGoodsInfoId())));
                return skuVO;
            }).collect(Collectors.toList()));
            resultVO.getMarketings().add(marketingVO);
        }
        //计算购物车价格，只包含选中的商品
        List<TradePriceParamBO.GoodsInfo> goodsInfos = new ArrayList<>();
        for (Map.Entry<Long, List<GoodsInfoVO>> entry : makertingGoodsMap.entrySet()) {
            goodsInfos.addAll(
                entry.getValue().stream().filter(item -> client2Checked.containsKey(item.getGoodsInfoId())).map(item -> {
                    TradePriceParamBO.GoodsInfo goods = new TradePriceParamBO.GoodsInfo();
                    goods.setMarketingId(entry.getKey() == 0 ? null : entry.getKey());
                    goods.setGoodsInfoId(item.getGoodsInfoId());
                    goods.setBuyCount(item.getBuyCount());
                    return goods;
                }).collect(Collectors.toList())
            );
        }
        resultVO.setCalcPrice(calcPrice(customer, goodsInfos));
        return BaseResponse.success(resultVO);
    }

    private PurchasePriceResultVO calcPrice(CustomerVO customer, List<TradePriceParamBO.GoodsInfo> goodsInfos) {
        if (CollectionUtils.isEmpty(goodsInfos)) {
            return null;
        }
        TradePriceParamBO paramBO = new TradePriceParamBO();
        paramBO.setCustomerId(customer.getCustomerId());
        paramBO.setGoodsInfos(goodsInfos);
        BaseResponse<TradePriceResultBO> calcPriceResult = tradePriceProvider.calcPrice(paramBO);
        if (calcPriceResult == null || calcPriceResult.getContext() == null) {
            return null;
        }
        PurchasePriceResultVO calcPrice = new PurchasePriceResultVO();
        calcPrice.setTotalPrice(calcPriceResult.getContext().getTotalPrice());
        calcPrice.setPayPrice(calcPriceResult.getContext().getPayPrice());
        calcPrice.setCutPrice(calcPriceResult.getContext().getCutPrice());
        if (CollectionUtils.isNotEmpty(calcPriceResult.getContext().getTotalPriceItems())) {
            calcPrice.setTotalPriceItems(calcPriceResult.getContext().getTotalPriceItems().stream()
                    .map(item -> new CalcPriceItem(item.getAmount(), item.getDesc(), item.getType())).collect(Collectors.toList()));
        }
        if (CollectionUtils.isNotEmpty(calcPriceResult.getContext().getCutPriceItems())) {
            calcPrice.setCutPriceItems(calcPriceResult.getContext().getCutPriceItems().stream()
                    .map(item -> new CalcPriceItem(item.getAmount(), item.getDesc(), item.getType())).collect(Collectors.toList()));
        }
        return calcPrice;
    }

    private List<CartInfoResultVO$Sku.Marketing> buildMarketings(List<MarketingViewVO> marketingViewVOS) {
        if (CollectionUtils.isEmpty(marketingViewVOS)) {
            return Collections.EMPTY_LIST;
        }
        return  marketingViewVOS.stream().map(view -> {
            CartInfoResultVO$Sku.Marketing mkt = new CartInfoResultVO$Sku.Marketing();
            mkt.setMarketingId(view.getMarketingId());
            mkt.setMarketingName(view.getMarketingName());
            if (CollectionUtils.isNotEmpty(view.getFullReductionLevelList())) {
                //满金额减
                if (MarketingSubType.REDUCTION_FULL_AMOUNT.equals(view.getSubType())) {
                    mkt.setMarketingDesc(view.getFullReductionLevelList().stream().map(item -> "满" + item.getFullAmount() + "减" + item.getReduction()).collect(Collectors.joining(",")));
                } else if (MarketingSubType.REDUCTION_FULL_COUNT.equals(view.getSubType())) {
                    mkt.setMarketingDesc(view.getFullReductionLevelList().stream().map(item -> "满" + item.getFullCount() + "件减" + item.getReduction()).collect(Collectors.joining(",")));
                } else {
                    mkt.setMarketingDesc("其他");
                }
            }
            return mkt;
        }).collect(Collectors.toList());
    }

    /**
     * 购物车-凑单项金额
     * @see com.wanmi.sbc.order.trade.service.CalcTradePriceService#calc(com.wanmi.sbc.order.trade.model.root.Trade, com.wanmi.sbc.order.trade.request.TradeParams)
     */
    @PostMapping(value = "/composePrice")
    public BaseResponse<PurchasePriceResultVO> composePrice(@RequestBody ComposePriceParamVO paramVO) {
        CustomerVO customer = commonUtil.getCustomer();
        if (customer == null) {
            throw new SbcRuntimeException("用户登录信息不存在，请重新登录");
        }
        //统一查询购物车内容
        BaseResponse<PurchaseListResponse> cartResponse = purchaseQueryProvider.purchaseInfo(
                PurchaseInfoRequest.builder().customer(customer).inviteeId(commonUtil.getPurchaseInviteeId()).build());

        PurchasePriceResultVO resultVO = new PurchasePriceResultVO();
        PurchaseListResponse cartInfo = cartResponse.getContext();
        if (cartInfo == null) {
            return BaseResponse.success(resultVO);
        }
        //商品信息
        if (CollectionUtils.isEmpty(cartInfo.getGoodsInfos())) {
            return BaseResponse.success(resultVO);
        }

        List<GoodsInfoVO> goodsInfos = cartInfo.getGoodsInfos();
        //过滤符合当前营销的sku
        if (paramVO.getMarketingId() != null) {
            goodsInfos = goodsInfos.stream().filter(item -> item.getGoodsMarketing() != null && paramVO.getMarketingId().equals(item.getGoodsMarketing().getMarketingId()))
                    .collect(Collectors.toList());
        }
        //参与算价的商品
        List<TradePriceParamBO.GoodsInfo> calcGoods = goodsInfos.stream().map(item -> {
            TradePriceParamBO.GoodsInfo goodsInfo = new TradePriceParamBO.GoodsInfo();
            goodsInfo.setGoodsInfoId(item.getGoodsInfoId());
            goodsInfo.setBuyCount(item.getBuyCount());
            goodsInfo.setMarketingId(paramVO.getMarketingId());
            return goodsInfo;
        }).collect(Collectors.toList());
        return BaseResponse.success(calcPrice(customer, calcGoods));
    }

    /**
     * 购物车-商品营销列表
     */
    @PostMapping(value = "/promoteMarketing")
    public BaseResponse<List<PromoteInfoResultVO$Marketing>> promoteMarketing(@Valid @RequestBody PromoteMarketingParamVO param) {
        return null;
    }

    /**
     * 购物车-商品优惠券列表
     */
    @PostMapping(value = "/promoteCoupon")
    public BaseResponse<List<PromoteInfoResultVO$Coupon>> promoteCoupon(@Valid @RequestBody PromoteCouponParamVO param) {
        CouponCacheListForGoodsListRequest request = new CouponCacheListForGoodsListRequest();
        request.setGoodsInfoIds(param.getGoodsInfoIds());
        request.setCustomerId(commonUtil.getOperatorId());
        BaseResponse<CouponCacheListForGoodsListResponse> couponResponse = couponCacheProvider.listCouponForGoodsList(request);

        if (couponResponse.getContext() == null) {
            return BaseResponse.success(Collections.EMPTY_LIST);
        }

        List<PromoteInfoResultVO$Coupon> couponVOs = couponResponse.getContext().getCouponViews().stream().map(item -> {
            PromoteInfoResultVO$Coupon couponVO = new PromoteInfoResultVO$Coupon();
            couponVO.setActivityId(item.getActivityId());
            couponVO.setStartTime(item.getCouponStartTime());
            couponVO.setEndTime(item.getCouponEndTime());
            couponVO.setCouponId(item.getCouponId());
            couponVO.setCouponType(item.getCouponType().toValue());
            couponVO.setCouponName(item.getCouponName());
            couponVO.setCouponDesc(item.getCouponDesc());
            couponVO.setDenomination(BigDecimal.valueOf(item.getDenomination()));
            couponVO.setLimitPrice(FullBuyType.FULL_MONEY.equals(item.getFullBuyType()) ? BigDecimal.valueOf(item.getFullBuyPrice()) : BigDecimal.ZERO);
            couponVO.setLimitScope(item.getScopeType().toValue());
            couponVO.setCanFetch(item.isLeftFlag());
            couponVO.setHasFetch(item.isHasFetched());
            couponVO.setNearOverdue(item.isCouponWillEnd());
            couponVO.setRangeDayType(item.getRangeDayType().toValue());
            couponVO.setEffectiveDays(item.getEffectiveDays());
            return couponVO;
        }).collect(Collectors.toList());
        return BaseResponse.success(couponVOs);
    }

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    /**
     * 购物车-凑单商品-活动
     */
    @PostMapping(value = "/marketingGoods")
    public BusinessResponse<PromoteGoodsResultVO> marketingGoods(@RequestBody @Valid MarketingGoodsParamVO paramVO) {
        //查询营销活动
        MarketingGetByIdRequest mktParam = new MarketingGetByIdRequest();
        mktParam.setMarketingId(paramVO.getId());
        BaseResponse<MarketingGetByIdForCustomerResponse> mktResp = marketingQueryProvider.getByIdForCustomer(mktParam);
        if (mktResp == null || mktResp.getContext() == null || mktResp.getContext().getMarketingForEndVO() == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR, "指定的营销活动不存在");
        }

        MarketingForEndVO mkt = mktResp.getContext().getMarketingForEndVO();
        //活动信息
        PromoteGoodsResultVO.PromoteInfo promoteInfo = new PromoteGoodsResultVO.PromoteInfo();
        promoteInfo.setStartTime(mkt.getBeginTime().format(formatter));
        promoteInfo.setEndTime(mkt.getEndTime().format(formatter));
        //促销文案
        String text = "限时促销";
        if (MarketingSubType.REDUCTION_FULL_AMOUNT.equals(mkt.getSubType())) {
            text += mkt.getFullReductionLevelList().stream().map(item-> "满" + item.getFullAmount() + "减" + item.getReduction()).collect(Collectors.joining(","));
        } else if (MarketingSubType.REDUCTION_FULL_COUNT.equals(mkt.getSubType())) {
            text += mkt.getFullReductionLevelList().stream().map(item-> "满" + item.getFullCount() + "件减" + item.getReduction()).collect(Collectors.joining(","));
        } else if (MarketingSubType.DISCOUNT_FULL_AMOUNT.equals(mkt.getSubType())) {
            text += mkt.getFullReductionLevelList().stream().map(item-> "满" + item.getFullAmount() + "打" + item.getReduction() + "折").collect(Collectors.joining(","));
        } else if (MarketingSubType.DISCOUNT_FULL_COUNT.equals(mkt.getSubType())) {
            text += mkt.getFullReductionLevelList().stream().map(item-> "满" + item.getFullAmount() + "件打" + item.getReduction() + "折").collect(Collectors.joining(","));
        } else {
            text += "其他";
        }
        promoteInfo.setTipText(text);
        List<String> spuIds = mkt.getGoodsList().getGoodses().stream().map(GoodsVO::getGoodsId).collect(Collectors.toList());
        //返回参数
        PromoteGoodsResultVO result = buildPromoteGoodsResultVO(spuIds, paramVO.getPageNum(), paramVO.getPageSize(), paramVO.getKeyword());
        result.setPromoteInfo(promoteInfo);
        return BusinessResponse.success(result, new Page(paramVO.getPageNum(), paramVO.getPageSize(), result.getTotal().intValue()));
    }

    /**
     * 购物车-凑单商品-优惠券
     */
    @PostMapping(value = "/couponGoods")
    public BaseResponse<PromoteGoodsResultVO> couponGoods(@RequestBody @Valid CouponGoodsParamVO paramVO) {
        //查询优惠券活动
        CouponInfoDetailByIdRequest couponParam = CouponInfoDetailByIdRequest.builder().couponId(paramVO.getId()).build();
        BaseResponse<CouponInfoDetailByIdResponse> couponResp = couponInfoQueryProvider.getDetailById(couponParam);

        if (couponResp == null || couponResp.getContext() == null || couponResp.getContext().getCouponInfo() == null) {
            log.warn("没有找到对应的优惠券：couponId={}", paramVO.getId());
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR, "指定的优惠券信息不存在");
        }
        CouponInfoVO coupon = couponResp.getContext().getCouponInfo();
        //营销信息
        PromoteGoodsResultVO.PromoteInfo promoteInfo = new PromoteGoodsResultVO.PromoteInfo();
        promoteInfo.setStartTime(coupon.getStartTime().format(formatter));
        promoteInfo.setEndTime(coupon.getEndTime().format(formatter));

        if (FullBuyType.FULL_MONEY.equals(coupon.getFullBuyType())) {
            promoteInfo.setTipText("满" + coupon.getFullBuyPrice() + "减" + coupon.getDenomination());
        } else if (FullBuyType.NO_THRESHOLD.equals(coupon.getFullBuyType())) {
            promoteInfo.setTipText("减" + coupon.getDenomination());
        } else {
            promoteInfo.setTipText("其他");
        }

        CouponGoodsVO couponGoods = couponResp.getContext().getGoodsList();
        //适用的spu集合
        List<String> spuIds = couponGoods.getGoodses().stream().map(GoodsVO::getGoodsId).distinct().collect(Collectors.toList());
        PromoteGoodsResultVO result = buildPromoteGoodsResultVO(spuIds, paramVO.getPageNum(), paramVO.getPageSize(), paramVO.getKeyword());
        result.setPromoteInfo(promoteInfo);
        return BusinessResponse.success(result, new Page(paramVO.getPageNum(), paramVO.getPageSize(), result.getTotal().intValue()));
    }

    private PromoteGoodsResultVO buildPromoteGoodsResultVO(List<String> spuIds, Integer pageNum, Integer pageSize, String keyword) {
        //返回参数
        PromoteGoodsResultVO result = new PromoteGoodsResultVO();
        if (CollectionUtils.isEmpty(spuIds)) {
            return result;
        }

        //查询商品
        KeyWordSpuQueryReq spuParam = new KeyWordSpuQueryReq();
        spuParam.setChannelTypes(Arrays.asList(ChannelType.MINIAPP.toValue()));
        spuParam.setPageNum(pageNum);
        spuParam.setPageSize(pageSize);
        spuParam.setDelFlag(DeleteFlag.NO.toValue());
        spuParam.setSearchSpuNewCategory(2);
        spuParam.setKeyword(keyword);
        spuParam.setSpuIds(spuIds);
        //走搜索路线
        spuParam.setChannelTypes(Collections.singletonList(commonUtil.getTerminal().getCode()));
        CommonPageResp<List<EsSpuNewResp>> context = esSpuNewProvider.listKeyWorldEsSpu(spuParam).getContext();
        List<SpuNewBookListResp> spuNewBookListResps = spuNewSearchService.listSpuNewSearch(context.getContent());

        List<PromoteFitGoodsResultVO> fitGoods = spuNewBookListResps.stream().map(item -> {
            PromoteFitGoodsResultVO fitGoodsVO = new PromoteFitGoodsResultVO();
            BeanUtils.copyProperties(item, fitGoodsVO);
            return fitGoodsVO;
        }).collect(Collectors.toList());
        result.setFitGoods(fitGoods);
        result.setTotal(context.getTotal());

        //计算购物车价格
        result.setCalcPrice(calcPrice4FitGoods(commonUtil.getCustomer()));
        //处理商品规格
        BaseResponse<GoodsInfoSpecDetailRelBySpuIdsResponse> specResp =
                goodsInfoSpecDetailRelQueryProvider.listBySpuIds(new GoodsInfoSpecDetailRelBySpuIdsRequest(spuIds));
        if (specResp.getContext() != null) {
            Map<String, List<GoodsInfoSpecDetailRelVO>> spuidGroup =
                    specResp.getContext().getGoodsInfoSpecDetailRelVOList().stream().collect(Collectors.groupingBy(GoodsInfoSpecDetailRelVO::getGoodsId));
            for (PromoteFitGoodsResultVO fitGood : fitGoods) {
                List<GoodsInfoSpecDetailRelVO> specs = spuidGroup.get(fitGood.getSpuId());
                if (specs == null) {
                    continue;
                }
                fitGood.setSpecMore(specs.size() > 1);
                fitGood.setSpecText(specs.stream().filter(item->fitGood.getSkuId().equals(item.getGoodsId())).map(item->item.getDetailName()).collect(Collectors.joining(",")));
            }
        }
        return result;
    }

    private PurchasePriceResultVO calcPrice4FitGoods(CustomerVO customer) {
        //统一查询购物车内容
        BaseResponse<PurchaseListResponse> cartResponse = purchaseQueryProvider.purchaseInfo(
                PurchaseInfoRequest.builder().customer(customer).inviteeId(commonUtil.getPurchaseInviteeId()).build());

        PurchaseListResponse cartInfo = cartResponse.getContext();
        if (cartInfo == null || CollectionUtils.isEmpty(cartInfo.getGoodsInfos())) {
            return new PurchasePriceResultVO();
        }

        //sku对应的营销列表映射
        Map<String, List<MarketingViewVO>> goodsMarketingMap = cartInfo.getGoodsMarketingMap();
        //仅支持满减和满折的营销
        for (List<MarketingViewVO> marketings : goodsMarketingMap.values()) {
            Iterator<MarketingViewVO> iter = marketings.iterator();
            while (iter.hasNext()) {
                MarketingViewVO mktView = iter.next();
                boolean support = MarketingSubType.REDUCTION_FULL_AMOUNT.equals(mktView.getSubType())
                        || MarketingSubType.REDUCTION_FULL_COUNT.equals(mktView.getSubType())
                        || MarketingSubType.DISCOUNT_FULL_AMOUNT.equals(mktView.getSubType())
                        || MarketingSubType.DISCOUNT_FULL_COUNT.equals(mktView.getSubType());
                if (!support) {
                    iter.remove();
                }
            }
        }

        //markerting的id->goods列表映射
        Map<Long, List<GoodsInfoVO>> makertingGoodsMap = new HashMap<>();
        for (GoodsInfoVO goodsInfo : cartInfo.getGoodsInfos()) {
            List<MarketingViewVO> marketingViewVOS = goodsMarketingMap.get(goodsInfo.getGoodsInfoId());
            Long marketingId = CollectionUtils.isEmpty(marketingViewVOS) ? 0L : marketingViewVOS.get(0).getMarketingId();
            makertingGoodsMap.computeIfAbsent(marketingId, key -> new ArrayList<>()).add(goodsInfo);
        }

        List<TradePriceParamBO.GoodsInfo> goodsInfos = new ArrayList<>();
        for (Map.Entry<Long, List<GoodsInfoVO>> entry : makertingGoodsMap.entrySet()) {
            for (GoodsInfoVO item : entry.getValue()) {
                TradePriceParamBO.GoodsInfo goods = new TradePriceParamBO.GoodsInfo();
                goods.setMarketingId(entry.getKey()==0 ? null : entry.getKey());
                goods.setGoodsInfoId(item.getGoodsInfoId());
                goods.setBuyCount(item.getBuyCount());
                goodsInfos.add(goods);
            }
        }
        return calcPrice(customer, goodsInfos);
    }
}

