package com.wanmi.sbc.elastic.sku.service;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.EsConstants;
import com.wanmi.sbc.common.util.IteratorUtils;
import com.wanmi.sbc.elastic.api.request.sku.EsSkuPageRequest;
import com.wanmi.sbc.elastic.api.response.goods.EsSearchResponse;
import com.wanmi.sbc.elastic.api.response.sku.EsSkuPageResponse;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsInfoVO;
import com.wanmi.sbc.elastic.bean.vo.goods.GoodsInfoNestVO;
import com.wanmi.sbc.elastic.spu.mapper.EsSpuMapper;
import com.wanmi.sbc.elastic.storeInformation.model.root.StoreInformation;
import com.wanmi.sbc.goods.api.provider.cate.GoodsCateQueryProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.storecate.StoreCateQueryProvider;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateChildCateIdsByIdRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsByConditionRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByConditionRequest;
import com.wanmi.sbc.goods.api.request.storecate.StoreCateListGoodsRelByStoreCateIdAndIsHaveSelfRequest;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoListByConditionResponse;
import com.wanmi.sbc.goods.api.response.storecate.StoreCateListGoodsRelByStoreCateIdAndIsHaveSelfResponse;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.enums.GoodsStatus;
import com.wanmi.sbc.goods.bean.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.UpdateByQueryAction;
import org.elasticsearch.index.reindex.UpdateByQueryRequestBuilder;
import org.elasticsearch.script.Script;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ES商品信息数据源操作
 * Created by daiyitian on 2017/4/21.
 */
@Slf4j
@Service
public class EsSkuService {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private GoodsCateQueryProvider goodsCateQueryProvider;

    @Autowired
    private StoreCateQueryProvider storeCateQueryProvider;

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @Autowired
    private ResultsMapper resultsMapper;

    @Autowired
    private EsSpuMapper esSpuMapper;

    /**
     * 修改更新商家名称信息
     *
     * @param storeList 店铺名称
     * @return
     */
    public Long updateCompanyName(List<StoreInformation> storeList) {
        final String fmt = "ctx._source.goodsInfo.storeName='%s'";
        Long resCount = 0L;
        if (storeList != null) {
            Client client = elasticsearchTemplate.getClient();
            UpdateByQueryRequestBuilder updateByQuery = UpdateByQueryAction.INSTANCE.newRequestBuilder(client)
                    .source(EsConstants.DOC_GOODS_INFO_TYPE, EsConstants.DOC_GOODS_INFO_TYPE);
            for (StoreInformation s : storeList) {
                String script = String.format(fmt, s.getStoreName());
                updateByQuery.filter(QueryBuilders.termQuery("goodsInfo.storeId", s.getStoreId()));
                updateByQuery.script(new Script(script));
                resCount += updateByQuery.execute().actionGet().getUpdated();
            }
        }
        return resCount;
    }

    public EsSkuPageResponse page(EsSkuPageRequest request){
        if(request.getGoodsIds() == null) {
            request.setGoodsIds(new ArrayList<>());
        }

        EsSkuPageResponse response = new EsSkuPageResponse();
        response.setGoodsInfoPage(new MicroServicePage<>(Collections.emptyList(), request.getPageable(), 0));
        //获取该分类的所有子分类
        if (request.getCateId() != null) {
            GoodsCateChildCateIdsByIdRequest idRequest = new GoodsCateChildCateIdsByIdRequest();
            idRequest.setCateId(request.getCateId());
            request.setCateIds(goodsCateQueryProvider.getChildCateIdById(idRequest).getContext().getChildCateIdList());
            if (CollectionUtils.isNotEmpty(request.getCateIds())) {
                request.getCateIds().add(request.getCateId());
                request.setCateId(null);
            }
        }

        //补充店铺分类
        if(request.getStoreCateId() != null) {
            BaseResponse<StoreCateListGoodsRelByStoreCateIdAndIsHaveSelfResponse> baseResponse = storeCateQueryProvider.listGoodsRelByStoreCateIdAndIsHaveSelf(new StoreCateListGoodsRelByStoreCateIdAndIsHaveSelfRequest(request.getStoreCateId(), true));
            StoreCateListGoodsRelByStoreCateIdAndIsHaveSelfResponse cateIdAndIsHaveSelfResponse = baseResponse.getContext();
            if (Objects.nonNull(cateIdAndIsHaveSelfResponse)) {
                List<StoreCateGoodsRelaVO> relas = cateIdAndIsHaveSelfResponse.getStoreCateGoodsRelaVOList();
                if (CollectionUtils.isEmpty(relas)) {
                    return response;
                }
                request.getGoodsIds().addAll(relas.stream().map(StoreCateGoodsRelaVO::getGoodsId).collect(Collectors.toList()));
            }else{
                return response;
            }
        }

        MicroServicePage<EsGoodsInfoVO> esGoodsVOS = this.basePage(request);
        if(CollectionUtils.isEmpty(esGoodsVOS.getContent())){
            return response;
        }

        List<EsGoodsInfoVO> skuList = esGoodsVOS.getContent();
        List<String> skuIds = skuList.stream().map(EsGoodsInfoVO::getId).collect(Collectors.toList());
        GoodsInfoListByConditionRequest pageReq = new GoodsInfoListByConditionRequest();
        pageReq.setGoodsInfoIds(skuIds);
        pageReq.setShowPointFlag(request.getShowPointFlag());
        pageReq.setShowProviderInfoFlag(request.getShowProviderInfoFlag());
        pageReq.setShowVendibilityFlag(request.getShowVendibilityFlag());
        pageReq.setFillLmInfoFlag(request.getFillLmInfoFlag());
        GoodsInfoListByConditionResponse skuResponse = goodsInfoQueryProvider.listByCondition(pageReq).getContext();

        List<String> spuIds = skuList.stream().map(EsGoodsInfoVO::getGoodsId).collect(Collectors.toList());

        Map<String, GoodsVO> goodsVOMap = goodsQueryProvider.listByCondition(GoodsByConditionRequest.builder().goodsIds(spuIds).build())
                .getContext().getGoodsVOList().stream().collect(Collectors.toMap(GoodsVO::getGoodsId, g->g));

        //填充SPU
        Map<String, GoodsInfoVO> spuMap = skuResponse.getGoodsInfos().stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, g -> g));
        //填充SPU的详细信息
        List<GoodsInfoVO> goodsInfoVOS = skuList.stream()
                .map(g -> {
                    GoodsInfoVO sku = spuMap.get(g.getId());
                    sku.setStock(g.getGoodsInfo().getStock());
                    GoodsInfoNestVO tempGoodsInfo = g.getGoodsInfo();
                    //取SKU最小市场价
                    sku.setMarketPrice(tempGoodsInfo != null ? tempGoodsInfo.getMarketPrice() : sku.getMarketPrice());
                    sku.setSalePrice(sku.getMarketPrice() == null ? BigDecimal.ZERO : sku.getMarketPrice());
                    if(StringUtils.isBlank(sku.getGoodsInfoImg())) {
                        sku.setGoodsInfoImg(tempGoodsInfo != null ? tempGoodsInfo.getGoodsInfoImg() : sku.getGoodsInfoImg());
                    }
                    sku.setSpecText(tempGoodsInfo != null ? tempGoodsInfo.getSpecText() : null);
                    //取最小市场价SKU的相应购买积分
                    sku.setBuyPoint(0L);
                    if(tempGoodsInfo!= null && Objects.nonNull(tempGoodsInfo.getBuyPoint())) {
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

        response.setGoodsInfoPage(new MicroServicePage<>(goodsInfoVOS, request.getPageable(), esGoodsVOS.getTotal()));

        //填充品牌
        response.setBrands(skuList.stream()
                .filter(g -> Objects.nonNull(g.getGoodsBrand()) && StringUtils.isNotBlank(g.getGoodsBrand().getBrandName()))
                .map(g -> esSpuMapper.brandToSimpleVO(g.getGoodsBrand()))
                .filter(IteratorUtils.distinctByKey(GoodsBrandSimpleVO::getBrandId)).collect(Collectors.toList()));

        //填充分类
        response.setCates(skuList.stream()
                .filter(g -> Objects.nonNull(g.getGoodsCate()) && StringUtils.isNotBlank(g.getGoodsCate().getCateName()))
                .map(g -> esSpuMapper.cateToSimpleVO(g.getGoodsCate()))
                .filter(IteratorUtils.distinctByKey(GoodsCateSimpleVO::getCateId)).collect(Collectors.toList()));

        return response;
    }

    /**
     * 最基础的分页查询
     * @param request
     * @return
     */
    private MicroServicePage<EsGoodsInfoVO> basePage(EsSkuPageRequest request) {
        EsSearchResponse esResponse = elasticsearchTemplate.query(EsSkuSearchCriteriaBuilder.getSearchCriteria(request),
                searchResponse -> EsSearchResponse.build(searchResponse, resultsMapper));
        return new MicroServicePage<>(esResponse.getData() == null ? Collections.emptyList() : esResponse.getData(), PageRequest.of(request.getPageNum(),
                request.getPageSize()), esResponse.getTotal());
    }
}
