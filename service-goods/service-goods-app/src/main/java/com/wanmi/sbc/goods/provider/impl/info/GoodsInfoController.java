package com.wanmi.sbc.goods.provider.impl.info;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoProvider;
import com.wanmi.sbc.goods.api.request.info.*;
import com.wanmi.sbc.goods.api.response.info.*;
import com.wanmi.sbc.goods.ares.GoodsAresService;
import com.wanmi.sbc.goods.bean.dto.DistributionGoodsInfoModifyDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoMinusStockDTO;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import com.wanmi.sbc.goods.bean.enums.GoodsSource;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.distributionmatter.service.DistributionGoodsMatterService;
import com.wanmi.sbc.goods.distributor.goods.service.DistributorGoodsInfoService;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.request.GoodsInfoSaveRequest;
import com.wanmi.sbc.goods.info.service.GoodsInfoService;
import com.wanmi.sbc.goods.info.service.GoodsInfoStockService;
import com.wanmi.sbc.goods.info.service.GoodsService;
import com.wanmi.sbc.goods.price.model.root.GoodsCustomerPrice;
import com.wanmi.sbc.goods.price.model.root.GoodsIntervalPrice;
import com.wanmi.sbc.goods.price.model.root.GoodsLevelPrice;
import com.wanmi.sbc.goods.standard.model.root.StandardGoodsRel;
import com.wanmi.sbc.goods.standard.model.root.StandardSku;
import com.wanmi.sbc.goods.standard.repository.StandardGoodsRelRepository;
import com.wanmi.sbc.goods.standard.repository.StandardGoodsRepository;
import com.wanmi.sbc.goods.standard.repository.StandardSkuRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>对商品info操作接口</p>
 * Created by daiyitian on 2018-11-5-下午6:23.
 */
@RestController
@Validated
public class GoodsInfoController implements GoodsInfoProvider {

    @Autowired
    private GoodsInfoService goodsInfoService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsAresService goodsAresService;

    @Autowired
    private DistributorGoodsInfoService distributorGoodsInfoService;

//    @Autowired
//    private DistributionGoodsMatterService distributionGoodsMatterService;

    @Autowired
    private StandardGoodsRelRepository standardGoodsRelRepository;

    @Autowired
    private StandardGoodsRepository standardGoodsRepository;

    @Autowired
    private StandardSkuRepository standardSkuRepository;

    @Autowired
    private GoodsInfoStockService goodsInfoStockService;

    /**
     * 根据商品sku编号批量删除商品sku信息
     *
     * @param request 包含商品sku编号商品sku信息删除结构 {@link GoodsInfoDeleteByIdsRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override

    public BaseResponse deleteByIds(@RequestBody @Valid GoodsInfoDeleteByIdsRequest request) {
        goodsInfoService.delete(request.getGoodsInfoIds());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 修改商品sku信息
     *
     * @param request 商品sku信息修改结构 {@link GoodsInfoModifyRequest}
     * @return 商品sku信息 {@link GoodsInfoModifyResponse}
     */
    @Override
    @Transactional
    public BaseResponse<GoodsInfoModifyResponse> modify(@RequestBody @Valid GoodsInfoModifyRequest request) {
        GoodsInfo info = new GoodsInfo();
        KsBeanUtil.copyPropertiesThird(request.getGoodsInfo(), info);
        GoodsInfoSaveRequest saveRequest = new GoodsInfoSaveRequest();
        saveRequest.setGoodsInfo(info);
        info = goodsInfoService.edit(saveRequest);
        Goods goods = goodsService.getGoodsById(info.getGoodsId());
        //同步商品库上下架状态
        if (goods != null && GoodsSource.PROVIDER.toValue() == goods.getGoodsSource()) {
            StandardGoodsRel standardGoodsRel = standardGoodsRelRepository.findByGoodsIdAndDelFlag(info.getGoodsId(), DeleteFlag.NO);
            if (standardGoodsRel != null) {
                if (standardGoodsRel != null) {
                    standardGoodsRepository.updateAddedFlag(standardGoodsRel.getStandardId(), goods.getAddedFlag());
                    List<StandardSku> standardSkuList = standardSkuRepository.findByGoodsId(standardGoodsRel.getStandardId());
                    if (CollectionUtils.isNotEmpty(standardSkuList)) {
                        for (StandardSku standardSku : standardSkuList) {
                            if (info.getGoodsInfoId().equals(standardSku.getProviderGoodsInfoId())) {
                                standardSkuRepository.updateAddedFlagAndGoodsInfoNo(standardSku.getGoodsInfoId(), info.getAddedFlag(), info.getGoodsInfoNo());
                            }
                        }
                    }
                }
            }
        }

        //ares埋点-商品-后台修改单个商品sku
        goodsAresService.dispatchFunction("editOneGoodsSku", new Object[]{info});

        GoodsInfoModifyResponse response = new GoodsInfoModifyResponse();
        KsBeanUtil.copyPropertiesThird(info, response);
        return BaseResponse.success(response);
    }

    /**
     * 修改商品sku设价信息
     *
     * @param request 商品sku设价信息修改结构 {@link GoodsInfoPriceModifyRequest}
     * @return 商品sku设价信息 {@link GoodsInfoPriceModifyResponse}
     */
    @Override

    public BaseResponse<GoodsInfoPriceModifyResponse> modifyPrice(@RequestBody @Valid GoodsInfoPriceModifyRequest
                                                                          request) {
        GoodsInfoSaveRequest saveRequest = new GoodsInfoSaveRequest();
        GoodsInfo info = new GoodsInfo();
        KsBeanUtil.copyPropertiesThird(request.getGoodsInfo(), info);
        saveRequest.setGoodsInfo(info);
        //等级设价
        if (CollectionUtils.isNotEmpty(request.getGoodsLevelPrices())) {
            saveRequest.setGoodsLevelPrices(KsBeanUtil.convert(request.getGoodsLevelPrices(), GoodsLevelPrice.class));
        }
        //客户设价
        if (CollectionUtils.isNotEmpty(request.getGoodsCustomerPrices())) {
            saveRequest.setGoodsCustomerPrices(KsBeanUtil.convert(request.getGoodsCustomerPrices(), GoodsCustomerPrice.class));
        }
        //区间设价
        if (CollectionUtils.isNotEmpty(request.getGoodsIntervalPrices())) {
            saveRequest.setGoodsIntervalPrices(KsBeanUtil.convert(request.getGoodsIntervalPrices(), GoodsIntervalPrice.class));
        }

        info = goodsInfoService.editPrice(saveRequest);
        //ares埋点-商品-后台修改单个商品sku
        goodsAresService.dispatchFunction("editOneGoodsSku", new Object[]{info});

        GoodsInfoPriceModifyResponse response = new GoodsInfoPriceModifyResponse();
        KsBeanUtil.copyPropertiesThird(info, response);
        return BaseResponse.success(response);
    }

    /**
     * 修改商品sku上下架
     *
     * @param request 商品上下架修改结构 {@link GoodsInfoModifyAddedStatusRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override

    public BaseResponse modifyAddedStatus(@RequestBody @Valid GoodsInfoModifyAddedStatusRequest request) {
        goodsInfoService.updateAddedStatus(request.getAddedFlag(), request.getGoodsInfoIds());
        return BaseResponse.SUCCESSFUL();
    }

//    /**
//     * 根据商品skuId增加商品sku库存
//     *
//     * @param request 包含skuId的商品sku库存增量结构 {@link GoodsInfoPlusStockByIdRequest}
//     * @return 操作结果 {@link BaseResponse}
//     */
//    @Override
//
//    public BaseResponse plusStockById(@RequestBody @Valid GoodsInfoPlusStockByIdRequest request) {
//        goodsInfoService.addStockById(request.getStock(), request.getGoodsInfoId());
//        return BaseResponse.SUCCESSFUL();
//    }

    /**
     * 批量增量商品sku库存
     *
     * @param request 包含多个库存的sku库存增量结构 {@link GoodsInfoBatchPlusStockRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse batchPlusStock(@RequestBody @Valid GoodsInfoBatchPlusStockRequest request) {
        goodsInfoService.batchAddStock(request.getStockList());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 根据商品skuId扣除商品sku库存
     *
     * @param request 包含skuId的商品sku库存减量结构 {@link GoodsInfoMinusStockByIdRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override

    public BaseResponse minusStockById(@RequestBody @Valid GoodsInfoMinusStockByIdRequest request) {
        goodsInfoService.subStockById(request.getStock(), request.getGoodsInfoId());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 批量扣除商品sku库存
     *
     * @param request 包含多个库存的sku库存减量结构 {@link GoodsInfoBatchMinusStockRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse batchMinusStock(@RequestBody @Valid GoodsInfoBatchMinusStockRequest request) {
        goodsInfoService.batchSubStock(request.getStockList());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 根据库存状态/上下架状态/相关店铺状态来填充商品数据的有效性
     *
     * @param request 商品列表数据结构 {@link GoodsInfoFillGoodsStatusRequest}
     * @return 包含商品有效状态的商品列表数据 {@link GoodsInfoFillGoodsStatusResponse}
     */
    @Override

    public BaseResponse<GoodsInfoFillGoodsStatusResponse> fillGoodsStatus(@RequestBody @Valid
                                                                                  GoodsInfoFillGoodsStatusRequest request) {
        List<GoodsInfo> goodsInfoList =
                goodsInfoService.fillGoodsStatus(KsBeanUtil.convert(request.getGoodsInfos(), GoodsInfo.class));
        return BaseResponse.success(GoodsInfoFillGoodsStatusResponse.builder()
                .goodsInfos(KsBeanUtil.convert(goodsInfoList, GoodsInfoVO.class)).build());
    }

    @Override
    public BaseResponse updateSkuSmallProgram(@RequestBody @Valid
                                                      GoodsInfoSmallProgramCodeRequest request) {
        goodsInfoService.updateSkuSmallProgram(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse clearSkuSmallProgramCode() {
        goodsInfoService.clearSkuSmallProgramCode();
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 分销商品审核通过(单个)
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse checkDistributionGoods(@RequestBody @Valid DistributionGoodsCheckRequest request) {
        goodsInfoService.checkDistributionGoods(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 批量审核分销商品
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse batchCheckDistributionGoods(@RequestBody @Valid DistributionGoodsBatchCheckRequest request) {
        goodsInfoService.batchCheckDistributionGoods(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 驳回分销商品
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse refuseCheckDistributionGoods(@RequestBody @Valid DistributionGoodsRefuseRequest request) {
        goodsInfoService.refuseCheckDistributionGoods(request.getGoodsInfoId(),
                DistributionGoodsAudit.NOT_PASS,
                request.getDistributionGoodsAuditReason());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 禁止分销商品
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse forbidCheckDistributionGoods(@RequestBody @Valid DistributionGoodsForbidRequest request) {
        goodsInfoService.refuseCheckDistributionGoods(request.getGoodsInfoId(), DistributionGoodsAudit.FORBID,
                request.getDistributionGoodsAuditReason());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 删除分销商品
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse delDistributionGoods(@RequestBody @Valid DistributionGoodsDeleteRequest request) {
        goodsInfoService.delDistributionGoods(request);
        // 同步删除分销员与商品关联表
        distributorGoodsInfoService.deleteByGoodsInfoId(request.getGoodsInfoId());

        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 添加分销商品
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse<DistributionGoodsAddResponse> addDistributionGoods(@RequestBody @Valid DistributionGoodsAddRequest request) {
        DistributionGoodsAddResponse goodsAddResponse = new DistributionGoodsAddResponse();
        if (CollectionUtils.isEmpty(request.getDistributionGoodsInfoModifyDTOS())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        List<String> goodsInfoIds = request.getDistributionGoodsInfoModifyDTOS().stream().map(DistributionGoodsInfoModifyDTO::getGoodsInfoId).collect(Collectors.toList());
        // 添加分销商品前，验证所添加的sku是否符合条件
        List<String> invalidList = goodsInfoService.getInvalidGoodsInfoByGoodsInfoIds(goodsInfoIds);
        if (CollectionUtils.isNotEmpty(invalidList)) {
            goodsAddResponse.setGoodsInfoIds(invalidList);
        } else {
            for (DistributionGoodsInfoModifyDTO modifyDTO : request.getDistributionGoodsInfoModifyDTOS()) {
                goodsInfoService.modifyCommissionDistributionGoods(modifyDTO.getGoodsInfoId(), modifyDTO
                                .getCommissionRate(), modifyDTO.getDistributionCommission()
                        , request.getDistributionGoodsAudit());
            }
        }
        return BaseResponse.success(goodsAddResponse);
    }

    /**
     * 已审核通过 编辑分销商品佣金
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse modifyDistributionGoodsCommission(@RequestBody @Valid DistributionGoodsModifyRequest request) {
        goodsInfoService.modifyCommissionDistributionGoods(request.getGoodsInfoId(), request.getCommissionRate(),
                request.getDistributionCommission(), DistributionGoodsAudit.CHECKED);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 审核未通过或禁止分销的商品重新编辑后，状态为待审核
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse modifyDistributionGoods(@RequestBody @Valid DistributionGoodsModifyRequest request) {
        goodsInfoService.modifyCommissionDistributionGoods(request.getGoodsInfoId(), request.getCommissionRate()
                , request.getDistributionCommission(), DistributionGoodsAudit.WAIT_CHECK);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 商品ID<spu> 修改商品审核状态
     *
     * @param request
     */
    @Override
    public BaseResponse distributeTogeneralGoods(@RequestBody @Valid DistributionGoodsChangeRequest request) {
        goodsInfoService.modifyDistributeState(request.getGoodsId(), DistributionGoodsAudit.COMMON_GOODS);
        distributorGoodsInfoService.deleteByGoodsId(request.getGoodsId());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 供应商商品库存同步
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse<ProviderGoodsStockSyncResponse> providerGoodsStockSync(@RequestBody @Valid ProviderGoodsStockSyncRequest request) {
        List<GoodsInfo> goodsInfoList = KsBeanUtil.convert(request.getGoodsInfoList(), GoodsInfo.class);
        goodsInfoService.updateGoodsInfoSupplyPriceAndStock(goodsInfoList);
        return BaseResponse.success(ProviderGoodsStockSyncResponse.builder()
                .goodsInfoList(KsBeanUtil.convert(goodsInfoList, GoodsInfoVO.class)).build());
    }

    /**
     * 释放冻结库存
     * @param releaseFrozenStockList
     * @return
     */
    @Override
    public BaseResponse decryFreezeStock(List<GoodsInfoMinusStockDTO> releaseFrozenStockList){
        goodsInfoStockService.decryFreezeStock(releaseFrozenStockList);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse updateStockSyncFlagById(Integer stockSyncFlag, String goodsInfoId) {
        goodsInfoService.updateStockSyncFlagById(stockSyncFlag, goodsInfoId);
        return BaseResponse.SUCCESSFUL();
    }


    @Override
    public BaseResponse updateCostPriceSyncFlagById(Integer stockSyncFlag, String goodsInfoId) {
        goodsInfoService.updateCostPriceSyncFlagById(stockSyncFlag, goodsInfoId);
        return BaseResponse.SUCCESSFUL();
    }
}
