package com.soybean.mall.cart;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.soybean.mall.cart.vo.CartInfoResultVO$Group;
import com.soybean.mall.cart.vo.CartInfoResultVO$Sku;
import com.soybean.mall.cart.vo.CartInfoResultVO$Store;
import com.soybean.mall.cart.vo.CouponGoodsParamVO;
import com.soybean.mall.cart.vo.MarketingGoodsParamVO;
import com.soybean.mall.cart.vo.PromoteCouponParamVO;
import com.soybean.mall.cart.vo.PromoteGoodsResultVO;
import com.soybean.mall.cart.vo.PromoteInfoResultVO$Coupon;
import com.soybean.mall.cart.vo.PromoteInfoResultVO$Marketing;
import com.soybean.mall.cart.vo.PromoteMarketingParamVO;
import com.soybean.mall.cart.vo.PurchaseInfoResultVO;
import com.soybean.mall.cart.vo.PurchasePriceParamVO;
import com.soybean.mall.cart.vo.PurchasePriceResultVO;
import com.soybean.mall.common.CommonUtil;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingViewVO;
import com.wanmi.sbc.order.api.provider.purchase.PurchaseQueryProvider;
import com.wanmi.sbc.order.api.request.purchase.PurchaseGetStoreCouponExistRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseInfoRequest;
import com.wanmi.sbc.order.api.response.purchase.PurchaseGetStoreCouponExistResponse;
import com.wanmi.sbc.order.api.response.purchase.PurchaseListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
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

    /**
     * 购物车-购物车信息
     * @see com.wanmi.sbc.purchase.PurchaseBaseController#purchaseInfo(com.wanmi.sbc.order.api.request.purchase.PurchaseListRequest)
     */
    @PostMapping(value = "/purchaseInfo")
    public BaseResponse<PurchaseInfoResultVO> purchaseInfo() {
        BaseResponse<PurchaseListResponse> response = purchaseQueryProvider.purchaseInfo(PurchaseInfoRequest.builder()
                .customer(commonUtil.getCustomer()).inviteeId(commonUtil.getPurchaseInviteeId()).build());

        PurchaseInfoResultVO resultVO = new PurchaseInfoResultVO();
        PurchaseListResponse context = response.getContext();
        if (context == null) {
            return BaseResponse.success(resultVO);
        }

        if (CollectionUtils.isNotEmpty(context.getStores())) {
            CartInfoResultVO$Store store = new CartInfoResultVO$Store();
            store.setStoreId(context.getStores().get(0).getStoreId());
            store.setStoreName(context.getStores().get(0).getStoreName());
            BaseResponse<PurchaseGetStoreCouponExistResponse> conponResponse =
                    purchaseQueryProvider.getStoreCouponExist(PurchaseGetStoreCouponExistRequest.builder()
                            .inviteeId(commonUtil.getPurchaseInviteeId()).customer(commonUtil.getCustomer()).build());
            store.setAbleCoupon(conponResponse.getContext() != null && Boolean.TRUE.equals(conponResponse.getContext().getMap().get(store.getStoreId())));
            resultVO.setStore(store);
        }

        if (CollectionUtils.isEmpty(context.getGoodsInfos())) {
            return BaseResponse.success(resultVO);
        }

        Map<Long, List<GoodsInfoVO>> makertingGoodsMap = new HashMap<>();
        for (GoodsInfoVO goodsInfo : context.getGoodsInfos()) {
            Long type = goodsInfo.getGoodsMarketing()==null ? 0L : goodsInfo.getGoodsMarketing().getMarketingId();
            makertingGoodsMap.computeIfAbsent(type, key -> new ArrayList<>()).add(goodsInfo);
        }
        Map<Long, MarketingViewVO> markertingMap = new HashMap<>();
        context.getGoodsMarketingMap().values().forEach(item-> item.stream().forEach(mk-> markertingMap.put(mk.getMarketingId(), mk)));

        resultVO.setMarketings(new ArrayList<>());
        for (Map.Entry<Long, List<GoodsInfoVO>> entry : makertingGoodsMap.entrySet()) {
            CartInfoResultVO$Group group = new CartInfoResultVO$Group();
            group.setGoodsInfos(entry.getValue().stream().map(item->{
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
                skuVO.setSpecs(); // TODO: 2022/6/26
                return skuVO;
            }).collect(Collectors.toList()));
        }
        return BaseResponse.success(resultVO);
    }

    /**
     * 购物车-购物车金额
     * @see com.wanmi.sbc.order.trade.service.CalcTradePriceService#calc(com.wanmi.sbc.order.trade.model.root.Trade, com.wanmi.sbc.order.trade.request.TradeParams)
     */
    @PostMapping(value = "/purchasePrice")
    public BaseResponse<PurchasePriceResultVO> purchasePrice(@RequestBody PurchasePriceParamVO paramVO) {
        // TODO: 2022/6/26 计算价格
        return null;
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
        return null;
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
}

