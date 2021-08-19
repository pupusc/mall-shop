package com.wanmi.sbc.order.api.provider.purchase;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsMarketingDTO;
import com.wanmi.sbc.order.api.request.purchase.*;
import com.wanmi.sbc.order.api.response.purchase.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>采购单查询接口</p>
 * author: sunkun
 * Date: 2018-11-30
 */
@FeignClient(value = "${application.order.name}", contextId = "PurchaseQueryProvider")
public interface PurchaseQueryProvider {

    /**
     * 查询迷你采购单
     * @param request 查询迷你采购单请求结构 {@link PurchaseMiniListRequest}
     * @return {@link PurchaseMiniListResponse}
     */
    @PostMapping("/order/${application.order.version}/purchase/mini-list")
    BaseResponse<PurchaseMiniListResponse> minilist(@RequestBody @Valid PurchaseMiniListRequest request);

    /**
     * 采购单列表
     * @param request 采购单列表请求结构 {@link PurchaseListRequest}
     * @return {@link PurchaseListResponse}
     */
    @PostMapping("/order/${application.order.version}/purchase/list")
    BaseResponse<PurchaseListResponse> list(@RequestBody @Valid PurchaseListRequest request);

    /**
     * 查询采购单
     * @param request 查询采购单请求结构 {@link PurchaseQueryRequest}
     * @return {@link PurchaseQueryResponse}
     */
    @PostMapping("/order/${application.order.version}/purchase/query")
    BaseResponse<PurchaseQueryResponse> query(@RequestBody @Valid PurchaseQueryRequest request);

    /**
     * 获取店铺下，是否有优惠券营销，展示优惠券标签
     * @param request 获取店铺下，是否有优惠券营销，展示优惠券标签请求结构 {@link PurchaseGetStoreCouponExistRequest}
     * @return
     */
    @PostMapping("/order/${application.order.version}/purchase/get-store-coupon-exist")
    BaseResponse<PurchaseGetStoreCouponExistResponse> getStoreCouponExist(@RequestBody @Valid PurchaseGetStoreCouponExistRequest request);

    /**
     * 获取店铺营销信息
     * @param request 获取店铺营销信息请求结构 {@link PurchaseGetStoreMarketingRequest}
     * @return {@link PurchaseGetStoreMarketingResponse}
     */
    @PostMapping("/order/${application.order.version}/purchase/get-store-marketing")
    BaseResponse<PurchaseGetStoreMarketingResponse> getStoreMarketing(@RequestBody @Valid PurchaseGetStoreMarketingRequest request);

    /**
     * 获取采购单商品选择的营销
     * @param request 获取采购单商品选择的营销请求结构 {@link PurchaseQueryGoodsMarketingListRequest}
     * @return {@link PurchaseQueryGoodsMarketingListResponse}
     */
    @PostMapping("/order/${application.order.version}/purchase/query-goods-marketing-list")
    BaseResponse<PurchaseQueryGoodsMarketingListResponse> queryGoodsMarketingList(@RequestBody @Valid PurchaseQueryGoodsMarketingListRequest request);

    /**
     * 获取商品营销信息
     * @param request 获取商品营销信息请求结构 {@link PurchaseGetGoodsMarketingRequest}
     * @return {@link PurchaseGetGoodsMarketingResponse}
     */
    @PostMapping("/order/${application.order.version}/purchase/get-goods-marketing")
    BaseResponse<PurchaseGetGoodsMarketingResponse> getGoodsMarketing(@RequestBody @Valid PurchaseGetGoodsMarketingRequest request);

    /**
     * 获取采购单商品数量
     * @param request 获取采购单商品数量请求结构 {@link PurchaseCountGoodsRequest}
     * @return {@link PurchaseCountGoodsResponse}
     */
    @PostMapping("/order/${application.order.version}/purchase/count-goods")
    BaseResponse<PurchaseCountGoodsResponse> countGoods(@RequestBody @Valid PurchaseCountGoodsRequest request);

    /**
     * 未登录时,根据前端缓存信息查询迷你购物车信息
     * @param frontReq
     * @return
     */
    @PostMapping("/order/${application.order.version}/purchase/mini-list-front")
    BaseResponse<MiniPurchaseResponse> miniListFront(@RequestBody @Valid PurchaseFrontMiniRequest frontReq);

    /**
     * 未登陆时,根据前端传入的采购单信息,查询组装必要信息
     * @param request
     * @return
     */
    @PostMapping("/order/${application.order.version}/purchase/list-front")
    BaseResponse<PurchaseResponse> listFront(@RequestBody @Valid  PurchaseFrontRequest request);

    /**
     *未登录时,验证并设置前端传入的商品使用营销信息
     * @return
     */
    @PostMapping("/order/${application.order.version}/purchase/validate-and-set-goods-marketings")
    BaseResponse<PurchaseResponse> validateAndSetGoodsMarketings(@RequestBody @Valid ValidateAndSetGoodsMarketingsRequest request);


    /**
     * 采购单社交分销信息
     * @param request
     * @return
     */
    @PostMapping("/order/${application.order.version}/purchase/distribution")
    BaseResponse<Purchase4DistributionResponse> distribution(@RequestBody @Valid Purchase4DistributionRequest request);


    /**
     * 商品是否允许加入采购单（商品发布、编辑是否设置允许加入购物车）
     * @param request
     * @return
     */
    @PostMapping("/order/${application.order.version}/purchase/check-add")
    BaseResponse checkAdd(@RequestBody @Valid PurchaseSaveRequest request);

    /**
     * 查询购物车信息
     * @param request
     * @return
     */
    @PostMapping("/order/${application.order.version}/purchase/purchase-info")
    BaseResponse<PurchaseListResponse> purchaseInfo(@RequestBody @Valid PurchaseInfoRequest request);

}
