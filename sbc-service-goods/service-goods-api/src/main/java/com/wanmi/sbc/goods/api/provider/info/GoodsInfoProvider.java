package com.wanmi.sbc.goods.api.provider.info;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.info.*;
import com.wanmi.sbc.goods.api.response.info.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>对商品info操作接口</p>
 * Created by daiyitian on 2018-11-5-下午6:23.
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsInfoProvider")
public interface GoodsInfoProvider {

    /**
     * 根据商品sku编号批量删除商品sku信息
     *
     * @param request 包含商品sku编号商品sku信息删除结构 {@link GoodsInfoDeleteByIdsRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/info/delete-by-ids")
    BaseResponse deleteByIds(@RequestBody @Valid GoodsInfoDeleteByIdsRequest request);

    /**
     * 修改商品sku信息
     *
     * @param request 商品sku信息修改结构 {@link GoodsInfoModifyRequest}
     * @return 商品sku信息 {@link GoodsInfoModifyResponse}
     */
    @PostMapping("/goods/${application.goods.version}/info/modify")
    BaseResponse<GoodsInfoModifyResponse> modify(@RequestBody @Valid GoodsInfoModifyRequest request);

    /**
     * 修改商品sku设价信息
     *
     * @param request 商品sku设价信息修改结构 {@link GoodsInfoPriceModifyRequest}
     * @return 商品sku设价信息 {@link GoodsInfoPriceModifyResponse}
     */
    @PostMapping("/goods/${application.goods.version}/info/modify-price")
    BaseResponse<GoodsInfoPriceModifyResponse> modifyPrice(@RequestBody @Valid GoodsInfoPriceModifyRequest request);

    /**
     * 修改商品sku上下架
     *
     * @param request 商品上下架修改结构 {@link GoodsInfoModifyAddedStatusRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/info/modify-added-status")
    BaseResponse modifyAddedStatus(@RequestBody @Valid GoodsInfoModifyAddedStatusRequest request);

    /**
     * 根据商品skuId增加商品sku库存
     *
     * @param request 包含skuId的商品sku库存增量结构 {@link GoodsInfoPlusStockByIdRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/info/plus-stock-by-id")
    BaseResponse plusStockById(@RequestBody @Valid GoodsInfoPlusStockByIdRequest request);

    /**
     * 批量增量商品sku库存
     *
     * @param request 包含多个库存的sku库存增量结构 {@link GoodsInfoBatchPlusStockRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/info/batch-plus-stock")
    BaseResponse batchPlusStock(@RequestBody @Valid GoodsInfoBatchPlusStockRequest request);

    /**
     * 根据商品skuId扣除商品sku库存
     *
     * @param request 包含skuId的商品sku库存减量结构 {@link GoodsInfoMinusStockByIdRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/info/minus-stock-by-id")
    BaseResponse minusStockById(@RequestBody @Valid GoodsInfoMinusStockByIdRequest request);

    /**
     * 批量扣除商品sku库存
     *
     * @param request 包含多个库存的sku库存减量结构 {@link GoodsInfoBatchMinusStockRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/info/batch-minus-stock")
    BaseResponse batchMinusStock(@RequestBody @Valid GoodsInfoBatchMinusStockRequest request);

    /**
     * 根据库存状态/上下架状态/相关店铺状态来填充商品数据的有效性
     *
     * @param request 商品列表数据结构 {@link GoodsInfoFillGoodsStatusRequest}
     * @return 包含商品有效状态的商品列表数据 {@link GoodsInfoFillGoodsStatusResponse}
     */
    @PostMapping("/goods/${application.goods.version}/info/fill-goods-status")
    BaseResponse<GoodsInfoFillGoodsStatusResponse> fillGoodsStatus(@RequestBody @Valid
                                                                           GoodsInfoFillGoodsStatusRequest request);

    /**
     * 更新sku的小程序码
     * @param request
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/info/update-sku-smallprogram")
    BaseResponse updateSkuSmallProgram(@RequestBody @Valid
                                                  GoodsInfoSmallProgramCodeRequest request);


    @PostMapping("/goods/${application.goods.version}/info/clear-sku-smallprogram")
    BaseResponse clearSkuSmallProgramCode();

    /**
     * 分销商品审核通过(单个)
     * @param request
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/info/check-sku")
    BaseResponse checkDistributionGoods(@RequestBody @Valid DistributionGoodsCheckRequest request);

    /**
     * 批量审核分销商品
     * @param request
     */
    @PostMapping("/goods/${application.goods.version}/info/batch-check-sku")
    BaseResponse batchCheckDistributionGoods(@RequestBody @Valid DistributionGoodsBatchCheckRequest request);

    /**
     * 驳回分销商品
     * @param request
     */
    @PostMapping("/goods/${application.goods.version}/info/refuse-check-sku")
    BaseResponse refuseCheckDistributionGoods(@RequestBody @Valid DistributionGoodsRefuseRequest request);

    /**
     * 禁止分销商品
     * @param request
     */
    @PostMapping("/goods/${application.goods.version}/info/forbid-check-sku")
    BaseResponse forbidCheckDistributionGoods(@RequestBody @Valid DistributionGoodsForbidRequest request);

    /**
     * 删除分销商品
     * @param request
     */
    @PostMapping("/goods/${application.goods.version}/info/delete-sku")
    BaseResponse delDistributionGoods(@RequestBody @Valid DistributionGoodsDeleteRequest request);

    /**
     * 添加分销商品
     * @param request
     */
    @PostMapping("/goods/${application.goods.version}/info/add-sku")
    BaseResponse<DistributionGoodsAddResponse> addDistributionGoods(@RequestBody @Valid DistributionGoodsAddRequest request);

    /**
     * 编辑分销商品佣金
     * @param request
     */
    @PostMapping("/goods/${application.goods.version}/info/modify-sku-commission")
    BaseResponse modifyDistributionGoodsCommission(@RequestBody @Valid DistributionGoodsModifyRequest request);

    /**
     * 编辑分销商品
     * @param request
     */
    @PostMapping("/goods/${application.goods.version}/info/modify-sku")
    BaseResponse modifyDistributionGoods(@RequestBody @Valid DistributionGoodsModifyRequest request);

    /**
     * 分销商品改为普通商品
     * @param request
     */
    @PostMapping("/goods/${application.goods.version}/info/distribute-to-generalGoods")
    BaseResponse distributeTogeneralGoods(@RequestBody @Valid DistributionGoodsChangeRequest request);

    /**
     * 供应商商品库存同步
     * @param request
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/info/provider-goods-stock-sync")
    BaseResponse<ProviderGoodsStockSyncResponse> providerGoodsStockSync(@RequestBody @Valid ProviderGoodsStockSyncRequest request);

}
