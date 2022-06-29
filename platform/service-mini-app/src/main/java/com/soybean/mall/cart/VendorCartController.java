package com.soybean.mall.cart;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.soybean.mall.cart.vo.CalcPriceItem;
import com.soybean.mall.cart.vo.CartInfoResultVO$Marketing;
import com.soybean.mall.cart.vo.CartInfoResultVO$Sku;
import com.soybean.mall.cart.vo.CartInfoResultVO$Store;
import com.soybean.mall.cart.vo.ComposePriceParamVO;
import com.soybean.mall.cart.vo.CouponGoodsParamVO;
import com.soybean.mall.cart.vo.MarketingGoodsParamVO;
import com.soybean.mall.cart.vo.PromoteCouponParamVO;
import com.soybean.mall.cart.vo.PromoteGoodsResultVO;
import com.soybean.mall.cart.vo.PromoteInfoResultVO$Coupon;
import com.soybean.mall.cart.vo.PromoteInfoResultVO$Marketing;
import com.soybean.mall.cart.vo.PromoteMarketingParamVO;
import com.soybean.mall.cart.vo.PurchaseInfoParamVO;
import com.soybean.mall.cart.vo.PurchaseInfoResultVO;
import com.soybean.mall.cart.vo.PurchasePriceResultVO;
import com.soybean.mall.common.CommonUtil;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponCacheProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCacheListForGoodsListRequest;
import com.wanmi.sbc.marketing.api.response.coupon.CouponCacheListForGoodsListResponse;
import com.wanmi.sbc.marketing.bean.enums.FullBuyType;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.enums.ScopeType;
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
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
        //spu信息按照id->model映射
        Map<String, GoodsVO> goodsVOMap = cartInfo.getGoodses().stream().collect(Collectors.toMap(GoodsVO::getGoodsId, item -> item, (a, b) -> a));
        //按照客户指定状态分组：指定促销方案；指定选中商品；
        Map<String, Long> goods2Marketing = new HashMap<>();
        Map<String, Boolean> goods2Checked = new HashMap<>();
        for (PurchaseInfoParamVO.Marketing marketing : paramVO.getMarketings()) {
            for (PurchaseInfoParamVO.GoodsInfo goodsInfo : marketing.getGoodsInfos()) {
                goods2Marketing.put(goodsInfo.getGoodsInfoId(), marketing.getMarketingId());
                goods2Checked.put(goodsInfo.getGoodsInfoId(), Boolean.TRUE.equals(goodsInfo.isChecked()));
            }
        }
        //markerting的ip->goods列表映射
        Map<Long, List<GoodsInfoVO>> makertingGoodsMap = new HashMap<>();
        for (GoodsInfoVO goodsInfo : cartInfo.getGoodsInfos()) {
            //处理客户端指定切换的促销方案
            Long marketingId = goods2Marketing.get(goodsInfo.getGoodsInfoId());
            if (marketingId == null) {
                marketingId = goodsInfo.getGoodsMarketing()==null ? 0L : goodsInfo.getGoodsMarketing().getMarketingId();
            }
            makertingGoodsMap.computeIfAbsent(marketingId, key -> new ArrayList<>()).add(goodsInfo);
        }
        //markerting的id->model映射
        Map<Long, MarketingViewVO> markertingMap = new HashMap<>();
        cartInfo.getGoodsMarketingMap().values().forEach(item-> item.stream().forEach(mk-> markertingMap.put(mk.getMarketingId(), mk)));

        //sku对应的营销列表映射
        Map<String, List<MarketingViewVO>> goodsMarketingMap = cartInfo.getGoodsMarketingMap();
        resultVO.setMarketings(new ArrayList<>());
        for (Map.Entry<Long, List<GoodsInfoVO>> entry : makertingGoodsMap.entrySet()) {
            CartInfoResultVO$Marketing marketing = new CartInfoResultVO$Marketing();
            marketing.setMarketingId(entry.getKey());
            marketing.setGoodsInfos(entry.getValue().stream().map(item->{
                CartInfoResultVO$Sku skuVO = new CartInfoResultVO$Sku();
                skuVO.setGoodsId(item.getGoodsId());
                skuVO.setGoodsNo(item.getGoodsNo());
                skuVO.setGoodsStatus(item.getGoodsStatus());
                skuVO.setGoodsType(item.getGoodsType());
                skuVO.setGoodsInfoId(item.getGoodsInfoId());
                skuVO.setGoodsInfoImg(item.getGoodsInfoImg());
                skuVO.setGoodsInfoNo(item.getGoodsInfoNo());
                skuVO.setBuyCount(item.getBuyCount());
                skuVO.setMarketPrice(item.getMarketPrice());
                skuVO.setSalePrice(item.getSalePrice());
                skuVO.setSpecText(item.getSpecText());
                skuVO.setSpecMore(goodsVOMap.containsKey(item.getGoodsId()) && Boolean.FALSE.equals(goodsVOMap.get(item.getGoodsId()).getSingleSpecFlag()));
                skuVO.setChecked(goods2Checked.containsKey(item.getGoodsInfoId())); //处理客户端指定选中的商品
                skuVO.setMarketings(buildMarketings(goodsMarketingMap.get(item.getGoodsInfoId())));
                return skuVO;
            }).collect(Collectors.toList()));
            resultVO.getMarketings().add(marketing);
        }

        //计算购物车价格，只包含选中的商品
        List<TradePriceParamBO.GoodsInfo> goodsInfos = new ArrayList<>();
        for (Map.Entry<Long, List<GoodsInfoVO>> entry : makertingGoodsMap.entrySet()) {
            goodsInfos.addAll(
                entry.getValue().stream().filter(
                        item -> goods2Checked.containsKey(item.getGoodsInfoId())
                ).map(item -> {
                    TradePriceParamBO.GoodsInfo goods = new TradePriceParamBO.GoodsInfo();
                    goods.setMarketingId(entry.getKey());
                    goods.setGoodsInfoId(item.getGoodsInfoId());
                    goods.setBuyCount(item.getBuyCount());
                    return goods;
                }).collect(Collectors.toList())
            );
        }
        TradePriceParamBO paramBO = new TradePriceParamBO();
        paramBO.setCustomerId(customer.getCustomerId());
        paramBO.setGoodsInfos(goodsInfos);
        BaseResponse<TradePriceResultBO> calcPriceResult = tradePriceProvider.calcPrice(paramBO);

        if (calcPriceResult.getContext() != null) {
            PurchasePriceResultVO calcPrice = new PurchasePriceResultVO();
            calcPrice.setTotalPrice(calcPriceResult.getContext().getTotalPrice());
            calcPrice.setPayPrice(calcPriceResult.getContext().getPayPrice());
            calcPrice.setCutPrice(calcPriceResult.getContext().getCutPrice());
            calcPrice.setTotalPriceItems(calcPriceResult.getContext().getTotalPriceItems().stream()
                    .map(item -> new CalcPriceItem(item.getAmount(), item.getDesc(), item.getType())).collect(Collectors.toList()));
            calcPrice.setCutPriceItems(calcPriceResult.getContext().getCutPriceItems().stream()
                    .map(item -> new CalcPriceItem(item.getAmount(), item.getDesc(), item.getType())).collect(Collectors.toList()));
            resultVO.setCalcPrice(calcPrice);
        }
        return BaseResponse.success(resultVO);
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

        TradePriceParamBO paramBO = new TradePriceParamBO();
        paramBO.setCustomerId(customer.getCustomerId());
        paramBO.setCouponId(paramVO.getCouponId());
        paramBO.setGoodsInfos(calcGoods);
        BaseResponse<TradePriceResultBO> calcPriceResult = tradePriceProvider.calcPrice(paramBO);

        if (calcPriceResult.getContext() != null) {
            resultVO.setTotalPrice(calcPriceResult.getContext().getTotalPrice());
            resultVO.setPayPrice(calcPriceResult.getContext().getPayPrice());
            resultVO.setCutPrice(calcPriceResult.getContext().getCutPrice());
            resultVO.setTotalPriceItems(calcPriceResult.getContext().getTotalPriceItems().stream()
                    .map(item -> new CalcPriceItem(item.getAmount(), item.getDesc(), item.getType())).collect(Collectors.toList()));
            resultVO.setCutPriceItems(calcPriceResult.getContext().getCutPriceItems().stream()
                    .map(item -> new CalcPriceItem(item.getAmount(), item.getDesc(), item.getType())).collect(Collectors.toList()));
        }
        return BaseResponse.success(resultVO);
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
            couponVO.setStartTime(item.getCouponStartTime());
            couponVO.setEndTime(item.getCouponEndTime());
            couponVO.setCouponId(item.getCouponId());
            couponVO.setCouponType(item.getCouponType().toValue());
            couponVO.setCouponName(item.getCouponName());
            couponVO.setCouponDesc(item.getCouponDesc());
            couponVO.setDenomination(BigDecimal.valueOf(item.getDenomination()));
            couponVO.setLimitPrice(FullBuyType.FULL_MONEY.equals(item.getFullBuyType()) ? BigDecimal.valueOf(item.getFullBuyPrice()) : BigDecimal.ZERO);
            couponVO.setLimitPriceText("满" + couponVO.getLimitPrice() + "可用");
            couponVO.setLimitScope(item.getScopeType().toValue());
            if (ScopeType.BRAND.equals(item.getScopeType())) {
                couponVO.setLimitScopeText("限品牌");
                couponVO.setLimitScopeDesc("仅可购买指定品牌部分商品");
            }
            if (ScopeType.STORE_CATE.equals(item.getScopeType()) || ScopeType.BOSS_CATE.equals(item.getScopeType())) {
                couponVO.setLimitScopeText("限分类");
                couponVO.setLimitScopeDesc("仅可购买指定分类部分商品");
            }
            couponVO.setLimitScopeText(item.getScopeType().name());
            couponVO.setCanFetch(item.isLeftFlag());
            couponVO.setCanFetchDesc("已抢光");
            couponVO.setHasFetch(item.isHasFetched());
            couponVO.setNearOverdue(item.isCouponWillEnd());
            fillNearOverdueText(couponVO);
            couponVO.setStoreNameText("仅樊登读书自营旗舰店可用");

            return couponVO;
        }).collect(Collectors.toList());
        return BaseResponse.success(couponVOs);
    }

    /**
     * 购物车-凑单商品-活动
     */
    @PostMapping(value = "/marketingGoods")
    public BaseResponse<PromoteGoodsResultVO> marketingGoods(MarketingGoodsParamVO param) {
        return null;
    }

    /**
     * 购物车-凑单商品-优惠券
     */
    @PostMapping(value = "/couponGoods")
    public BaseResponse<PromoteGoodsResultVO> couponGoods(CouponGoodsParamVO param) {
        return null;
    }

    private void fillNearOverdueText(PromoteInfoResultVO$Coupon couponVO) {
        if (couponVO.isNearOverdue()) {
            try {
                long days = (System.currentTimeMillis() - DateUtils.parseDate(couponVO.getEndTime(), "yyyy-MM-dd").getTime()) / (1000*3600*24);
                couponVO.setNearOverdueText("距过期仅剩"+ days +"天");
            } catch (ParseException e) {
                log.warn("日期格式解析错误", e);
                couponVO.setNearOverdueText("已经快要过期了");
            }
        }
    }
}

