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
import com.soybean.mall.service.PromoteFilter;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import com.wanmi.sbc.common.enums.ChannelType;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.paidcardcustomerrel.PaidCardCustomerRelQueryProvider;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelListRequest;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.customer.bean.vo.PaidCardCustomerRelVO;
import com.wanmi.sbc.customer.bean.vo.PaidCardVO;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoQueryRequest;
import com.wanmi.sbc.elastic.api.response.goods.EsGoodsInfoResponse;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsInfoVO;
import com.wanmi.sbc.goods.api.provider.spec.GoodsInfoSpecDetailRelQueryProvider;
import com.wanmi.sbc.goods.api.request.spec.GoodsInfoSpecDetailRelBySpuIdsRequest;
import com.wanmi.sbc.goods.api.response.spec.GoodsInfoSpecDetailRelBySpuIdsResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoSpecDetailRelVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponCacheProvider;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponInfoQueryProvider;
import com.wanmi.sbc.marketing.api.provider.market.MarketingQueryProvider;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingPluginProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCacheListForGoodsListRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponInfoByIdRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponInfoDetailByIdRequest;
import com.wanmi.sbc.marketing.api.request.market.MarketingGetByIdRequest;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginGoodsListFilterRequest;
import com.wanmi.sbc.marketing.api.response.coupon.CouponCacheListForGoodsListResponse;
import com.wanmi.sbc.marketing.api.response.coupon.CouponInfoByIdResponse;
import com.wanmi.sbc.marketing.api.response.coupon.CouponInfoDetailByIdResponse;
import com.wanmi.sbc.marketing.api.response.market.MarketingGetByIdForCustomerResponse;
import com.wanmi.sbc.marketing.bean.enums.FullBuyType;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.enums.RangeDayType;
import com.wanmi.sbc.marketing.bean.vo.CouponInfoVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingForEndVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingFullDiscountLevelVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingFullReductionLevelVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingScopeVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingViewVO;
import com.wanmi.sbc.order.api.enums.ShopCartSourceEnum;
import com.wanmi.sbc.order.api.provider.purchase.PurchaseQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.TradePriceProvider;
import com.wanmi.sbc.order.api.request.purchase.PurchaseGetStoreCouponExistRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseInfoRequest;
import com.wanmi.sbc.order.api.request.trade.TradePriceParamBO;
import com.wanmi.sbc.order.api.response.purchase.PurchaseGetStoreCouponExistResponse;
import com.wanmi.sbc.order.api.response.purchase.PurchaseListResponse;
import com.wanmi.sbc.order.api.response.trade.TradePriceResultBO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
    //凑单页促销日期格式化
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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
    @Autowired
    private PaidCardCustomerRelQueryProvider paidCardCustomerRelQueryProvider;
    @Autowired
    private EsGoodsInfoElasticQueryProvider esGoodsInfoElasticQueryProvider;

    /**
     * 购物车-购物车信息
     */
    @PostMapping(value = "/purchaseInfo")
    public BaseResponse<PurchaseInfoResultVO> purchaseInfo(@RequestBody PurchaseInfoParamVO paramVO) {
        CustomerVO customer = commonUtil.getCustomer();
        PurchaseInfoResultVO resultVO = new PurchaseInfoResultVO();

        //查询购物车内容
        BaseResponse<PurchaseListResponse> cartResponse = purchaseQueryProvider.purchaseInfo(
                PurchaseInfoRequest.builder().customer(customer).inviteeId(commonUtil.getPurchaseInviteeId()).shopCartSource(ShopCartSourceEnum.WX_MINI).build());

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
        Map<String, List<MarketingViewVO>> skuId2mktVOs = new HashMap<>();
        //markerting的id->model映射
        Map<Long, MarketingViewVO> mktId2mktVO = new HashMap<>();
        //仅支持满减和满折的营销
        for (Map.Entry<String, List<MarketingViewVO>> entry : cartInfo.getGoodsMarketingMap().entrySet()) {
            for (MarketingViewVO mktVO : entry.getValue()) {
                if (PromoteFilter.supportMkt(mktVO.getSubType())) {
                    mktId2mktVO.put(mktVO.getMarketingId(), mktVO);
                    skuId2mktVOs.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()).add(mktVO);
                }
            }
        }

        //按创建时间倒叙排列
        //cartInfo.setGoodsInfos(cartInfo.getGoodsInfos().stream().sorted(Comparator.comparing(GoodsInfoVO::getCreateTime).reversed()).collect(Collectors.toList()));

        //按照客户指定状态分组：指定促销方案；指定选中商品；
        Map<String, Long> client2mktId = new HashMap<>();
        Map<String, Boolean> client2checkSkuId = new HashMap<>();
        for (PurchaseInfoParamVO.Marketing marketing : paramVO.getMarketings()) {
            for (PurchaseInfoParamVO.GoodsInfo goodsInfo : marketing.getGoodsInfos()) {
                client2mktId.put(goodsInfo.getGoodsInfoId(), marketing.getMarketingId());
                client2checkSkuId.put(goodsInfo.getGoodsInfoId(), Boolean.TRUE.equals(goodsInfo.isChecked()));
            }
        }

        //markerting的id->goods列表映射
        Map<Long, List<GoodsInfoVO>> mktId2skus = new HashMap<>();
        for (GoodsInfoVO goodsInfo : cartInfo.getGoodsInfos()) {
            //处理客户端指定切换的促销方案
            Long mktId = client2mktId.get(goodsInfo.getGoodsInfoId());
            if (mktId == null) {
                List<MarketingViewVO> mktVOs = skuId2mktVOs.get(goodsInfo.getGoodsInfoId());
                mktId = CollectionUtils.isEmpty(mktVOs) ? 0L : mktVOs.get(0).getMarketingId();
            }
            mktId2skus.computeIfAbsent(mktId, key -> new ArrayList<>()).add(goodsInfo);
        }

        //spu信息按照id->model映射
        Map<String, GoodsVO> goodsVOMap = cartInfo.getGoodses().stream().collect(Collectors.toMap(GoodsVO::getGoodsId, item -> item, (a, b) -> a));
        resultVO.setMarketings(new ArrayList<>());

        for (Map.Entry<Long, List<GoodsInfoVO>> entry : mktId2skus.entrySet()) {
            CartInfoResultVO$Marketing marketingVO = new CartInfoResultVO$Marketing();
            resultVO.getMarketings().add(marketingVO);
            marketingVO.setMarketingId(entry.getKey());
            marketingVO.setGoodsInfos(new ArrayList<>());
            for (GoodsInfoVO item : entry.getValue()) {
                boolean checked = Boolean.TRUE.equals(client2checkSkuId.get(item.getGoodsInfoId()));
                //包装返回对象
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
                skuVO.setMaxCount(item.getStock());
                skuVO.setSpecMore(goodsVOMap.containsKey(item.getGoodsId()) && Boolean.FALSE.equals(goodsVOMap.get(item.getGoodsId()).getSingleSpecFlag()));
                skuVO.setChecked(checked); //处理客户端指定选中的商品
                skuVO.setMarketings(buildMarketings(skuId2mktVOs.get(item.getGoodsInfoId())));
                marketingVO.getGoodsInfos().add(skuVO);
            }
            //促销活动信息
            if (mktId2mktVO.get(entry.getKey()) != null) {
                MarketingViewVO marketingBO = mktId2mktVO.get(entry.getKey());
                marketingVO.setType(marketingBO.getMarketingType().toValue());
                marketingVO.setSubType(marketingBO.getSubType().toValue());
                fillPromoteText4cart(marketingVO, marketingBO);
            }
        }
        //计算购物车价格，只包含选中的商品
        List<TradePriceParamBO.GoodsInfo> goodsInfos = new ArrayList<>();
        for (Map.Entry<Long, List<GoodsInfoVO>> entry : mktId2skus.entrySet()) {
            goodsInfos.addAll(
                entry.getValue().stream().filter(item -> Boolean.TRUE.equals(client2checkSkuId.get(item.getGoodsInfoId()))).map(item -> {
                    TradePriceParamBO.GoodsInfo goods = new TradePriceParamBO.GoodsInfo();
                    goods.setMarketingId(entry.getKey() == 0 ? null : entry.getKey());
                    goods.setGoodsInfoId(item.getGoodsInfoId());
                    goods.setBuyCount(item.getBuyCount());
                    return goods;
                }).collect(Collectors.toList())
            );
        }
        //价格信息
        resultVO.setCalcPrice(calcPrice(customer, goodsInfos));
        //会员信息
        resultVO.setVipInfo(vipInfo(customer.getCustomerId()));

        return BaseResponse.success(resultVO);
    }

    private void fillPromoteText4cart(CartInfoResultVO$Marketing mktVO, MarketingViewVO mktBO) {
        List<CartInfoResultVO$Sku> skus = mktVO.getGoodsInfos().stream().filter(CartInfoResultVO$Sku::isChecked).collect(Collectors.toList());
        //促销文案
        Boolean imax = true;
        String text = "";
        if (MarketingSubType.REDUCTION_FULL_COUNT.equals(mktBO.getSubType())) { //满数量减
            if (CollectionUtils.isEmpty(mktBO.getFullReductionLevelList())) {
                return;
            }
            long totalCount = skus.stream().mapToLong(i -> i.getBuyCount()).sum();
            //已满2件减30元，再买5件减300
            for (MarketingFullReductionLevelVO item : mktBO.getFullReductionLevelList()) {
                imax = totalCount >= item.getFullCount();
                if (!imax) {
                    text = text + "，再买" + (item.getFullCount() - totalCount) + "件减" + item.getReduction();
                    break;
                }
                text = "已满" + item.getFullCount() + "件减" + item.getReduction();
            }
        } else if (MarketingSubType.REDUCTION_FULL_AMOUNT.equals(mktBO.getSubType())) { //满金额减
            BigDecimal totalPrice = skus.stream().map(CartInfoResultVO$Sku::getSalePrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            //每满300减150，还差32
            for (MarketingFullReductionLevelVO item : mktBO.getFullReductionLevelList()) {
                imax = totalPrice.compareTo(item.getFullAmount()) >= 0;
                text = "每满" + item.getFullAmount() + "减" + item.getReduction();
                if (!imax) {
                    text = text + "，还差" + item.getFullAmount().subtract(totalPrice);
                    break;
                }
            }
        } else if (MarketingSubType.DISCOUNT_FULL_COUNT.equals(mktBO.getSubType())) { //满数量折
            if (CollectionUtils.isEmpty(mktBO.getFullDiscountLevelList())) {
                return;
            }
            long totalCount = skus.stream().mapToLong(i -> i.getBuyCount()).sum();
            //已满2件打9折，再买5件打8折
            for (MarketingFullDiscountLevelVO item : mktBO.getFullDiscountLevelList()) {
                imax = totalCount >= item.getFullCount();
                if (!imax) {
                    text = text + "，再买" + (item.getFullCount() - totalCount) + "件打" + item.getDiscount() + "折";
                    break;
                }
                text = "已满" + item.getFullCount() + "件打" + item.getDiscount() + "折";
            }
        } else if (MarketingSubType.DISCOUNT_FULL_AMOUNT.equals(mktBO.getSubType())) { //满金额折
            BigDecimal totalPrice = skus.stream().map(CartInfoResultVO$Sku::getSalePrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            //每满300打9折，还差32打8折
            for (MarketingFullDiscountLevelVO item : mktBO.getFullDiscountLevelList()) {
                imax = totalPrice.compareTo(item.getFullAmount()) >= 0;
                if (!imax) {
                    text = text + "，再买" + item.getFullAmount().subtract(totalPrice) + "件打" + item.getDiscount() + "折";
                }
                text = "每满" + item.getFullAmount() + "打" + item.getDiscount() + "折";
            }
        } else {
            throw new SbcRuntimeException(CommonErrorCode.FAILED, "暂时不支持的营销活动");
        }
        mktVO.setMaxPolicy(imax); //最大优惠
        mktVO.setSubTypeText(text); //促销文案
    }

    /**
     * 获取用户的会员信息
     */
    private PurchaseInfoResultVO.VipInfo vipInfo(String customerId) {
        //查询是否购买付费会员卡
        List<PaidCardCustomerRelVO> paidCardCustomerRelVOList = paidCardCustomerRelQueryProvider
                .listCustomerRelFullInfo(PaidCardCustomerRelListRequest.builder()
                        .customerId(customerId)
                        .delFlag(DeleteFlag.NO)
                        .endTimeFlag(LocalDateTime.now())
                        .build()).getContext();
        if (CollectionUtils.isEmpty(paidCardCustomerRelVOList)) {
            return null;
        }
        PaidCardVO paidCardVO = paidCardCustomerRelVOList.stream().map(PaidCardCustomerRelVO::getPaidCardVO).min(Comparator.comparing(PaidCardVO::getDiscountRate)).get();
        PurchaseInfoResultVO.VipInfo vipInfo = new PurchaseInfoResultVO.VipInfo();
        vipInfo.setRate(paidCardVO.getDiscountRate());
        vipInfo.setName(paidCardVO.getName());
        return vipInfo;
    }

    private PurchasePriceResultVO calcPrice(CustomerVO customer, List<TradePriceParamBO.GoodsInfo> goodsInfos) {
        PurchasePriceResultVO calcPrice = new PurchasePriceResultVO();

        if (CollectionUtils.isEmpty(goodsInfos)) {
            return calcPrice;
        }
        TradePriceParamBO paramBO = new TradePriceParamBO();
        paramBO.setCustomerId(customer.getCustomerId());
        paramBO.setGoodsInfos(goodsInfos);
        BaseResponse<TradePriceResultBO> calcPriceResult = tradePriceProvider.calcPrice(paramBO);
        if (calcPriceResult == null || calcPriceResult.getContext() == null) {
            return calcPrice;
        }

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
     * com.wanmi.sbc.order.trade.service.CalcTradePriceService#calc(com.wanmi.sbc.order.trade.model.root.Trade, com.wanmi.sbc.order.trade.request.TradeParams)
     */
    @PostMapping(value = "/composePrice")
    public BaseResponse<PurchasePriceResultVO> composePrice(@RequestBody ComposePriceParamVO paramVO) {
        //验证参数
        if ((paramVO.getMarketingId() == null || paramVO.getMarketingId() == 0) && StringUtils.isBlank(paramVO.getCouponId())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        if (paramVO.getMarketingId() != null && StringUtils.isNotBlank(paramVO.getCouponId())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR, "凑单算价只支持单种类型");
        }
        CustomerVO customer = commonUtil.getCustomer();
        //统一查询购物车内容
        BaseResponse<PurchaseListResponse> cartResponse = purchaseQueryProvider.purchaseInfo(
                PurchaseInfoRequest.builder().customer(customer).inviteeId(commonUtil.getPurchaseInviteeId()).build());

        PurchaseListResponse cartInfo = cartResponse.getContext();
        PurchasePriceResultVO resultVO = new PurchasePriceResultVO();
        //购物车为空
        if (cartInfo == null || CollectionUtils.isEmpty(cartInfo.getGoodsInfos())) {
            return BaseResponse.success(resultVO);
        }

        //过滤符合当前营销的sku，根据营销id
        String tipText = filterSku4composePrice(paramVO, cartInfo);

        //参与算价的商品
        List<TradePriceParamBO.GoodsInfo> calcGoods = cartInfo.getGoodsInfos().stream().map(item -> {
            TradePriceParamBO.GoodsInfo goodsInfo = new TradePriceParamBO.GoodsInfo();
            goodsInfo.setGoodsInfoId(item.getGoodsInfoId());
            goodsInfo.setBuyCount(item.getBuyCount());
            goodsInfo.setMarketingId(paramVO.getMarketingId());
            return goodsInfo;
        }).collect(Collectors.toList());

        PurchasePriceResultVO calcPrice = calcPrice(customer, calcGoods);
        calcPrice.setTipText(tipText);
        return BaseResponse.success(calcPrice);
    }

    @Autowired
    private MarketingPluginProvider marketingPluginProvider;

    @Deprecated
    private String fillPromoteText4composeCoupon(ComposePriceParamVO paramVO, PurchaseListResponse cartInfo, CustomerVO customer) {
        if (StringUtils.isBlank(paramVO.getCouponId())) {
            return null;
        }

        CouponInfoByIdResponse coupon = couponInfoQueryProvider.getById(CouponInfoByIdRequest.builder().couponId(paramVO.getCouponId()).build()).getContext();
        if (coupon == null) {
            log.warn("couponId = {}", paramVO.getCouponId());
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR, "没有找到指定的优惠券信息");
        }
        if (CollectionUtils.isEmpty(cartInfo.getGoodsInfos())) {
            return null;
        }

        MarketingPluginGoodsListFilterRequest filterRequest = new MarketingPluginGoodsListFilterRequest();
        filterRequest.setGoodsInfos(KsBeanUtil.convert(cartInfo.getGoodsInfos(), GoodsInfoDTO.class));
        filterRequest.setCustomerDTO(KsBeanUtil.convert(customer, CustomerDTO.class));
        List<GoodsInfoVO> skuVOs = marketingPluginProvider.goodsListFilter(filterRequest).getContext().getGoodsInfoVOList();

        List<String> skuIds = skuVOs.stream().filter(skuVO->skuVO.getCouponLabels().stream().anyMatch(i->i.getCouponInfoId().equals(paramVO.getCouponId())))
                .map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList());

        Iterator<GoodsInfoVO> iterator = cartInfo.getGoodsInfos().iterator();
        while (iterator.hasNext()) {
            GoodsInfoVO sku = iterator.next();
            if (!skuIds.contains(sku.getGoodsInfoId())) {
                iterator.remove();
            }
        }

        return null;
    }

    private String filterSku4composePrice(ComposePriceParamVO paramVO, PurchaseListResponse cartInfo) {
        //优惠券发起的算价不过滤营销活动
        if (paramVO.getMarketingId() == null) {
            return null;
        }
        MarketingGetByIdRequest mktParam = new MarketingGetByIdRequest();
        mktParam.setMarketingId(paramVO.getMarketingId());
        MarketingGetByIdForCustomerResponse mktResult = marketingQueryProvider.getByIdForCustomer(mktParam).getContext();
        if (mktResult == null || mktResult.getMarketingForEndVO() == null) {
            log.warn("marketingId={}", paramVO.getMarketingId());
            throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS, "指定的营销活动没有找到");
        }
        MarketingForEndVO mktBO = mktResult.getMarketingForEndVO();
        //删除不符合营销的商品
        List<GoodsInfoVO> skuVOs = cartInfo.getGoodsInfos();
        Iterator<GoodsInfoVO> iterator = skuVOs.iterator();
        while (iterator.hasNext()) {
            GoodsInfoVO next = iterator.next();
            if (next.getGoodsMarketing() == null || !next.getGoodsMarketing().getMarketingId().equals(paramVO.getMarketingId())) {
                iterator.remove();
            }
        }

        //商品的总数、总价
        long totalCount = skuVOs.stream().mapToLong(GoodsInfoVO::getBuyCount).sum();
        BigDecimal totalPrice = skuVOs.stream().map(GoodsInfoVO::getSalePrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        //促销文案
        Boolean imax = true;
        String text = "";
        if (MarketingSubType.REDUCTION_FULL_COUNT.equals(mktBO.getSubType())) { //满数量减
            if (CollectionUtils.isEmpty(mktBO.getFullReductionLevelList())) {
                return null;
            }
            //已满2件减30元，再买5件减300
            for (MarketingFullReductionLevelVO item : mktBO.getFullReductionLevelList()) {
                imax = totalCount >= item.getFullCount();
                if (!imax) {
                    text = "再买" + (item.getFullCount() - totalCount) + "件减" + item.getReduction();
                    break;
                }
                text = "已满" + item.getFullCount() + "件减" + item.getReduction();
            }
        } else if (MarketingSubType.REDUCTION_FULL_AMOUNT.equals(mktBO.getSubType())) { //满金额减
            if (CollectionUtils.isEmpty(mktBO.getFullReductionLevelList())) {
                return null;
            }
            //再买20.01元，可享受满100减10
            for (MarketingFullReductionLevelVO item : mktBO.getFullReductionLevelList()) {
                imax = totalPrice.compareTo(item.getFullAmount()) >= 0;
                if (!imax) {
                    text = "再买" + item.getFullAmount().subtract(totalPrice) + "元，可享受满" + item.getFullAmount() + "减" + item.getReduction();
                    break;
                }
                text = "每满" + item.getFullAmount() + "减" + item.getReduction();
            }
        } else if (MarketingSubType.DISCOUNT_FULL_COUNT.equals(mktBO.getSubType())) { //满数量折
            if (CollectionUtils.isEmpty(mktBO.getFullDiscountLevelList())) {
                return null;
            }
            //已满2件打9折，再买5件打8折
            for (MarketingFullDiscountLevelVO item : mktBO.getFullDiscountLevelList()) {
                imax = totalCount >= item.getFullCount();
                if (!imax) {
                    text = "再买" + (item.getFullCount() - totalCount) + "件打" + item.getDiscount() + "折";
                    break;
                }
                text = "已满" + item.getFullCount() + "件打" + item.getDiscount() + "折";
            }
        } else if (MarketingSubType.DISCOUNT_FULL_AMOUNT.equals(mktBO.getSubType())) { //满金额折
            //每满300打9折，还差32打8折
            for (MarketingFullDiscountLevelVO item : mktBO.getFullDiscountLevelList()) {
                imax = totalPrice.compareTo(item.getFullAmount()) >= 0;
                if (!imax) {
                    text = "再买" + item.getFullAmount().subtract(totalPrice) + "件打" + item.getDiscount() + "折";
                }
                text = "每满" + item.getFullAmount() + "打" + item.getDiscount() + "折";
            }
        } else {
            throw new SbcRuntimeException(CommonErrorCode.FAILED, "暂时不支持的营销活动");
        }
        return text;
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
        String text = "限时促销：";
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
        promoteInfo.setName(mkt.getMarketingName());

        //List<String> spuIds = mkt.getGoodsList().getGoodses().stream().map(GoodsVO::getGoodsId).collect(Collectors.toList());
        //按照h5方式搜索
        EsGoodsInfoQueryRequest queryRequest = new EsGoodsInfoQueryRequest();
        queryRequest.setAuditStatus(CheckStatus.CHECKED.toValue());
        queryRequest.setStoreState(StoreState.OPENING.toValue());
        queryRequest.setAddedFlag(AddedFlag.YES.toValue());
        queryRequest.setDelFlag(DeleteFlag.NO.toValue());
        queryRequest.setVendibility(Constants.yes);
        queryRequest.setCateAggFlag(true);
        queryRequest.setSortFlag(paramVO.getSpuSortType() == null ? 0 : paramVO.getSpuSortType());
        queryRequest.setPageNum(paramVO.getPageNum()-1);
        queryRequest.setPageSize(paramVO.getPageSize());
        String now = DateUtil.format(LocalDateTime.now(), DateUtil.FMT_TIME_4);
        queryRequest.setContractStartDate(now);
        queryRequest.setContractEndDate(now);
        queryRequest.setGoodsChannelTypeSet(Collections.singletonList(commonUtil.getTerminal().getCode()));
        queryRequest.setGoodsInfoIds(mkt.getMarketingScopeList().stream().map(MarketingScopeVO::getScopeId).collect(Collectors.toList()));

        EsGoodsInfoResponse esGoodsInfoResponse = esGoodsInfoElasticQueryProvider.page(queryRequest).getContext();
        List<EsGoodsInfoVO> goodsInfoVOs = esGoodsInfoResponse.getEsGoodsInfoPage().getContent();
        List<String> spuIds = goodsInfoVOs.stream().map(EsGoodsInfoVO::getGoodsId).distinct().collect(Collectors.toList());
        //返回参数
        PromoteGoodsResultVO result = buildPromoteGoodsResultVO(spuIds, paramVO.getPageNum(), paramVO.getPageSize(), paramVO.getKeyword(), queryRequest.getSortFlag());
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
        if (RangeDayType.DAYS.equals(coupon.getRangeDayType())) {
            promoteInfo.setStartTime(LocalDateTime.now().format(formatter));
            promoteInfo.setEndTime(LocalDateTime.now().plusDays(coupon.getEffectiveDays()).format(formatter));
        }
        if (RangeDayType.RANGE_DAY.equals(coupon.getRangeDayType())) {
            promoteInfo.setStartTime(coupon.getStartTime().format(formatter));
            promoteInfo.setEndTime(coupon.getEndTime().format(formatter));
        }
        //促销文案
        String text = "优惠券：";
        if (FullBuyType.FULL_MONEY.equals(coupon.getFullBuyType())) {
            text += ("满" + coupon.getFullBuyPrice() + "减" + coupon.getDenomination());
        } else if (FullBuyType.NO_THRESHOLD.equals(coupon.getFullBuyType())) {
            text += ("减" + coupon.getDenomination());
        } else {
            text += "其他";
        }
        promoteInfo.setTipText(text);
        promoteInfo.setName(coupon.getCouponName());
        //通过ES搜索优惠券适用商品
        EsGoodsInfoQueryRequest esGoodsInfoQueryRequest = new EsGoodsInfoQueryRequest();
        esGoodsInfoQueryRequest.setAuditStatus(CheckStatus.CHECKED.toValue());
        esGoodsInfoQueryRequest.setStoreState(StoreState.OPENING.toValue());
        esGoodsInfoQueryRequest.setAddedFlag(AddedFlag.YES.toValue());
        esGoodsInfoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
        esGoodsInfoQueryRequest.setSortFlag(paramVO.getSpuSortType() == null ? 0 : paramVO.getSpuSortType());
        esGoodsInfoQueryRequest.setPageNum(paramVO.getPageNum()-1);
        esGoodsInfoQueryRequest.setPageSize(paramVO.getPageSize());
        esGoodsInfoQueryRequest.setCateAggFlag(true);
        String now = DateUtil.format(LocalDateTime.now(), DateUtil.FMT_TIME_4);
        esGoodsInfoQueryRequest.setContractStartDate(now);
        esGoodsInfoQueryRequest.setContractEndDate(now);
        esGoodsInfoQueryRequest.setVendibility(Constants.yes);
        esGoodsInfoQueryRequest.setGoodsChannelTypeSet(Collections.singletonList(commonUtil.getTerminal().getCode()));

        switch (coupon.getScopeType()) {
            case ALL: break;
            case BOSS_CATE: esGoodsInfoQueryRequest.setCateIds(coupon.getScopeIds().stream().map(i->Long.valueOf(i)).collect(Collectors.toList())); break;
            case BRAND: esGoodsInfoQueryRequest.setBrandIds(coupon.getScopeIds().stream().map(i->Long.valueOf(i)).collect(Collectors.toList())); break;
            case SKU: esGoodsInfoQueryRequest.setGoodsInfoIds(coupon.getScopeIds()); break;
            case STORE_CATE: esGoodsInfoQueryRequest.setStoreCateIds(coupon.getScopeIds().stream().map(i->Long.valueOf(i)).collect(Collectors.toList())); break;
            default: break;
        }

        EsGoodsInfoResponse esGoodsInfoResponse = esGoodsInfoElasticQueryProvider.page(esGoodsInfoQueryRequest).getContext();
        List<EsGoodsInfoVO> goodsInfoVOs = esGoodsInfoResponse.getEsGoodsInfoPage().getContent();
        List<String> spuIds = goodsInfoVOs.stream().map(EsGoodsInfoVO::getGoodsId).distinct().collect(Collectors.toList());

        PromoteGoodsResultVO result = buildPromoteGoodsResultVO(spuIds, paramVO.getPageNum(), paramVO.getPageSize(), paramVO.getKeyword(), esGoodsInfoQueryRequest.getSortFlag());
        result.setPromoteInfo(promoteInfo);
        return BusinessResponse.success(result, new Page(paramVO.getPageNum(), paramVO.getPageSize(), (int)esGoodsInfoResponse.getEsGoodsInfoPage().getTotal()));
    }

    private PromoteGoodsResultVO buildPromoteGoodsResultVO(List<String> spuIds, Integer pageNum, Integer pageSize, String keyword, Integer sortFlag) {
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
        spuParam.setKeyword(keyword);
        spuParam.setSpuIds(spuIds);
        spuParam.setSpuSortType(sortFlag);
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
        result.setCalcPrice(handCart4FitGoods(fitGoods, commonUtil.getCustomer()));
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

    private PurchasePriceResultVO handCart4FitGoods(List<PromoteFitGoodsResultVO> fitGoods, CustomerVO customer) {
        //统一查询购物车内容
        BaseResponse<PurchaseListResponse> cartResponse = purchaseQueryProvider.purchaseInfo(
                PurchaseInfoRequest.builder().customer(customer).inviteeId(commonUtil.getPurchaseInviteeId()).build());

        PurchaseListResponse cartInfo = cartResponse.getContext();
        if (cartInfo == null || CollectionUtils.isEmpty(cartInfo.getGoodsInfos())) {
            return new PurchasePriceResultVO();
        }
        //处理采购数量
        Map<String, GoodsInfoVO> skuId2sku = cartInfo.getGoodsInfos().stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, i -> i));
        for (PromoteFitGoodsResultVO fitGood : fitGoods) {
            GoodsInfoVO skuVO = skuId2sku.get(fitGood.getSkuId());
            fitGood.setBuyCount(skuVO == null ? 0 : skuVO.getBuyCount().intValue());
        }

        //sku对应的营销列表映射
        Map<String, List<MarketingViewVO>> skuId2mkts = cartInfo.getGoodsMarketingMap();
        //仅支持满减和满折的营销
        for (List<MarketingViewVO> marketings : skuId2mkts.values()) {
            Iterator<MarketingViewVO> iter = marketings.iterator();
            while (iter.hasNext()) {
                MarketingViewVO mktView = iter.next();
                if (!PromoteFilter.supportMkt(mktView.getSubType())) {
                    iter.remove();
                }
            }
        }

        //markerting的id->goods列表映射
        Map<Long, List<GoodsInfoVO>> makertingGoodsMap = new HashMap<>();
        for (GoodsInfoVO goodsInfo : cartInfo.getGoodsInfos()) {
            List<MarketingViewVO> mkts = skuId2mkts.get(goodsInfo.getGoodsInfoId());
            Long marketingId = CollectionUtils.isEmpty(mkts) ? 0L : mkts.get(0).getMarketingId();
            makertingGoodsMap.computeIfAbsent(marketingId, key -> new ArrayList<>()).add(goodsInfo);
        }

        List<TradePriceParamBO.GoodsInfo> goodsInfos = new ArrayList<>();
        for (Map.Entry<Long, List<GoodsInfoVO>> entry : makertingGoodsMap.entrySet()) {
            //过滤指定mktId下的商品参与算价
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

