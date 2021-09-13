package com.wanmi.sbc.elastic.provider.impl.sku;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.IteratorUtils;
import com.wanmi.sbc.elastic.api.provider.sku.EsSkuQueryProvider;
import com.wanmi.sbc.elastic.api.request.sku.EsSkuPageRequest;
import com.wanmi.sbc.elastic.api.response.sku.EsSkuPageResponse;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsInfoVO;
import com.wanmi.sbc.elastic.bean.vo.goods.GoodsInfoNestVO;
import com.wanmi.sbc.elastic.sku.service.EsSkuService;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateChildCateIdsByIdRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsByConditionRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByConditionRequest;
import com.wanmi.sbc.goods.api.request.storecate.StoreCateListGoodsRelByStoreCateIdAndIsHaveSelfRequest;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoListByConditionResponse;
import com.wanmi.sbc.goods.api.response.storecate.StoreCateListGoodsRelByStoreCateIdAndIsHaveSelfResponse;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.enums.GoodsStatus;
import com.wanmi.sbc.goods.bean.vo.GoodsBrandSimpleVO;
import com.wanmi.sbc.goods.bean.vo.GoodsCateSimpleVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.goods.bean.vo.StoreCateGoodsRelaVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: dyt
 * @Date: Created In 18:19 2020/11/30
 * @Description: TODO
 */
@RestController
@Validated
public class EsSkuQueryController implements EsSkuQueryProvider {

    @Autowired
    private EsSkuService esSkuService;

    @Override
    public BaseResponse<EsSkuPageResponse> page(@RequestBody EsSkuPageRequest request) {
        EsSkuPageResponse result = null;
        if(request.getGoodsIds() == null) {
            request.setGoodsIds(new ArrayList<>());
        }

        EsSkuPageResponse response = new EsSkuPageResponse();
        response.setGoodsInfoPage(new MicroServicePage<>(Collections.emptyList(), request.getPageable(), 0));
        //获取该分类的所有子分类
        if (request.getCateId() != null) {
            GoodsCateChildCateIdsByIdRequest idRequest = new GoodsCateChildCateIdsByIdRequest();
            idRequest.setCateId(request.getCateId());
            request.setCateIds(esSkuService.goodsCateQueryProvider.getChildCateIdById(idRequest).getContext().getChildCateIdList());
            if (CollectionUtils.isNotEmpty(request.getCateIds())) {
                request.getCateIds().add(request.getCateId());
                request.setCateId(null);
            }
        }

        //补充店铺分类
        if(request.getStoreCateId() != null) {
            BaseResponse<StoreCateListGoodsRelByStoreCateIdAndIsHaveSelfResponse> baseResponse = esSkuService.storeCateQueryProvider.listGoodsRelByStoreCateIdAndIsHaveSelf(new StoreCateListGoodsRelByStoreCateIdAndIsHaveSelfRequest(request.getStoreCateId(), true));
            StoreCateListGoodsRelByStoreCateIdAndIsHaveSelfResponse cateIdAndIsHaveSelfResponse = baseResponse.getContext();
            if (Objects.nonNull(cateIdAndIsHaveSelfResponse)) {
                List<StoreCateGoodsRelaVO> relas = cateIdAndIsHaveSelfResponse.getStoreCateGoodsRelaVOList();
                if (CollectionUtils.isEmpty(relas)) {
                    result = response;
                } else {
                    request.getGoodsIds().addAll(relas.stream().map(StoreCateGoodsRelaVO::getGoodsId).collect(Collectors.toList()));
                }
            }else{
                result = response;
            }
        }
        if (result == null) {
            MicroServicePage<EsGoodsInfoVO> esGoodsVOS = esSkuService.basePage(request);
            if(CollectionUtils.isEmpty(esGoodsVOS.getContent())){
                result = response;
            } else {
                List<EsGoodsInfoVO> skuList = esGoodsVOS.getContent();
                List<String> skuIds = skuList.stream().map(EsGoodsInfoVO::getId).collect(Collectors.toList());
                GoodsInfoListByConditionRequest pageReq = new GoodsInfoListByConditionRequest();
                pageReq.setGoodsInfoIds(skuIds);
                pageReq.setShowPointFlag(request.getShowPointFlag());
                pageReq.setShowProviderInfoFlag(request.getShowProviderInfoFlag());
                pageReq.setShowVendibilityFlag(request.getShowVendibilityFlag());
                pageReq.setFillLmInfoFlag(request.getFillLmInfoFlag());
                GoodsInfoListByConditionResponse skuResponse = esSkuService.goodsInfoQueryProvider.listByCondition(pageReq).getContext();
                List<String> spuIds = skuList.stream().map(EsGoodsInfoVO::getGoodsId).collect(Collectors.toList());
                Map<String, GoodsVO> goodsVOMap = esSkuService.goodsQueryProvider.listByCondition(GoodsByConditionRequest.builder().goodsIds(spuIds).build())
                        .getContext().getGoodsVOList().stream().collect(Collectors.toMap(GoodsVO::getGoodsId, g -> g));//填充SPU
                Map<String, GoodsInfoVO> spuMap = skuResponse.getGoodsInfos().stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, g -> g));//填充SPU的详细信息
                List<GoodsInfoVO> goodsInfoVOS = skuList.stream()
                        .map(g -> {
                            GoodsInfoVO sku = spuMap.get(g.getId());
                            sku.setStock(g.getGoodsInfo().getStock());
                            GoodsInfoNestVO tempGoodsInfo = g.getGoodsInfo();
                            //取SKU最小市场价
                            sku.setMarketPrice(tempGoodsInfo != null ? tempGoodsInfo.getMarketPrice() : sku.getMarketPrice());
                            sku.setSalePrice(sku.getMarketPrice() == null ? BigDecimal.ZERO : sku.getMarketPrice());
                            if (StringUtils.isBlank(sku.getGoodsInfoImg())) {
                                sku.setGoodsInfoImg(tempGoodsInfo != null ? tempGoodsInfo.getGoodsInfoImg() : sku.getGoodsInfoImg());
                            }
                            sku.setSpecText(tempGoodsInfo != null ? tempGoodsInfo.getSpecText() : null);
                            //取最小市场价SKU的相应购买积分
                            sku.setBuyPoint(0L);
                            if (tempGoodsInfo != null && Objects.nonNull(tempGoodsInfo.getBuyPoint())) {
                                sku.setBuyPoint(tempGoodsInfo.getBuyPoint());
                            }
                            if (Objects.equals(DeleteFlag.NO, sku.getDelFlag())
                                    && Objects.equals(CheckStatus.CHECKED, sku.getAuditStatus())
                                    && Constants.yes.equals(sku.getVendibility())) {
                                sku.setGoodsStatus(GoodsStatus.OK);
                                if (Objects.isNull(sku.getStock()) || sku.getStock() < 1) {
                                    sku.setGoodsStatus(GoodsStatus.OUT_STOCK);
                                }
                            } else {
                                sku.setGoodsStatus(GoodsStatus.INVALID);
                            }

                            sku.setStoreName(tempGoodsInfo != null ? tempGoodsInfo.getStoreName() : null);
                            GoodsVO goodsVO = goodsVOMap.getOrDefault(g.getGoodsId(), new GoodsVO());
                            sku.setPriceType(goodsVO.getPriceType());
                            sku.setSaleType(goodsVO.getSaleType());
                            sku.setAllowPriceSet(goodsVO.getAllowPriceSet());
                            sku.setSaleType(goodsVO.getSaleType());
                            sku.setAllowPriceSet(goodsVO.getAllowPriceSet());
                            sku.setPriceType(goodsVO.getPriceType());
                            sku.setGoodsUnit(goodsVO.getGoodsUnit());
                            sku.setGoodsCubage(goodsVO.getGoodsCubage());
                            sku.setGoodsWeight(goodsVO.getGoodsWeight());
                            sku.setFreightTempId(goodsVO.getFreightTempId());
                            return sku;
                        }).collect(Collectors.toList());
                response.setGoodsInfoPage(new MicroServicePage<>(goodsInfoVOS, request.getPageable(), esGoodsVOS.getTotal()));//填充品牌
                response.setBrands(skuList.stream()
                        .filter(g -> Objects.nonNull(g.getGoodsBrand()) && StringUtils.isNotBlank(g.getGoodsBrand().getBrandName()))
                        .map(g -> esSkuService.esSpuMapper.brandToSimpleVO(g.getGoodsBrand()))
                        .filter(IteratorUtils.distinctByKey(GoodsBrandSimpleVO::getBrandId)).collect(Collectors.toList()));//填充分类
                response.setCates(skuList.stream()
                        .filter(g -> Objects.nonNull(g.getGoodsCate()) && StringUtils.isNotBlank(g.getGoodsCate().getCateName()))
                        .map(g -> esSkuService.esSpuMapper.cateToSimpleVO(g.getGoodsCate()))
                        .filter(IteratorUtils.distinctByKey(GoodsCateSimpleVO::getCateId)).collect(Collectors.toList()));
                result = response;
            }

        }

        return BaseResponse.success(result);
    }
}
