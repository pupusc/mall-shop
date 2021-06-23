package com.wanmi.sbc.order.api.provider.purchase;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.order.api.request.purchase.*;
import com.wanmi.sbc.order.api.response.purchase.PurchaseCalcMarketingResponse;
import com.wanmi.sbc.order.api.response.purchase.PurchaseFillBuyCountResponse;
import com.wanmi.sbc.order.api.response.purchase.PurchaseListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>采购单操作接口</p>
 * author: sunkun
 * Date: 2018-11-30
 */
@FeignClient(value = "${application.order.name}", contextId = "PurchaseProvider")
public interface PurchaseProvider {

    /**
     * 新增采购单
     * @param request 新增采购单请求结构 {@link PurchaseSaveRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/purchase/save")
    BaseResponse save(@RequestBody @Valid PurchaseSaveRequest request);

    /**
     * 批量加入采购单
     * @param request 批量加入采购单请求结构 {@link PurchaseBatchSaveRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/purchase/batch-save")
    BaseResponse batchSave(@RequestBody @Valid PurchaseBatchSaveRequest request);

    /**
     * 商品收藏调整商品数量
     * @param request 商品收藏调整商品数量请求结构 {@link PurchaseUpdateNumRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/purchase/update-num")
    BaseResponse updateNum(@RequestBody @Valid PurchaseUpdateNumRequest request);

    /**
     * 删除采购单
     * @param request 删除采购单请求结构 {@link PurchaseDeleteRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/purchase/delete")
    BaseResponse delete(@RequestBody @Valid PurchaseDeleteRequest request);

    /**
     * 采购单商品移入收藏夹
     * @param request 采购单商品移入收藏夹请求结构 {@link PurchaseAddFollowRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/purchase/add-follow")
    BaseResponse addFollow(@RequestBody @Valid PurchaseAddFollowRequest request);

    /**
     * 清除失效商品
     * @param request 清除失效商品请求结构 {@link PurchaseClearLoseGoodsRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/purchase/clear-lose-goods")
    BaseResponse clearLoseGoods(@RequestBody @Valid PurchaseClearLoseGoodsRequest request);

    /**
     * 计算采购单金额
     * @param request 计算采购单金额请求结构 {@link PurchaseCalcAmountRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/purchase/calc-amount")
    BaseResponse<PurchaseListResponse> calcAmount(@RequestBody @Valid PurchaseCalcAmountRequest request);

    /**
     * 计算采购单中参加同种营销的商品列表/总额/优惠
     * @param request 计算采购单中参加同种营销的商品列表/总额/优惠请求结构 {@link PurchaseCalcMarketingRequest}
     * @return {@link PurchaseCalcMarketingResponse}
     */
    @PostMapping("/order/${application.order.version}/purchase/calc-marketing-by-marketing-id")
    BaseResponse<PurchaseCalcMarketingResponse> calcMarketingByMarketingId(@RequestBody @Valid PurchaseCalcMarketingRequest request);

    /**
     * 同步商品使用的营销
     * @param request 同步商品使用的营销请求结构 {@link PurchaseSyncGoodsMarketingsRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/purchase/sync-goods-marketings")
    BaseResponse syncGoodsMarketings(@RequestBody @Valid PurchaseSyncGoodsMarketingsRequest request);

    /**
     * 修改商品使用的营销
     * @param request 修改商品使用的营销请求结构 {@link PurchaseModifyGoodsMarketingRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/purchase/modify-goods-marketing")
    BaseResponse modifyGoodsMarketing(@RequestBody @Valid PurchaseModifyGoodsMarketingRequest request);


    /**
     * 填充客户购买数
     * @param request 填充客户购买数请求结构 {@link PurchaseFillBuyCountRequest}
     * @return {@link PurchaseFillBuyCountResponse}
     */
    @PostMapping("/order/${application.order.version}/purchase/fill-buy-count")
    BaseResponse<PurchaseFillBuyCountResponse> fillBuyCount(@RequestBody @Valid PurchaseFillBuyCountRequest request);

    /**
     * 合并登录前后采购单
     * @param request
     * @return
     */
    @PostMapping("/order/${application.order.version}/purchase/merge-purchase")
    BaseResponse mergePurchase(@RequestBody @Valid PurchaseMergeRequest request);
}

