package com.wanmi.sbc.elastic.spu.serivce;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.StoreType;
import com.wanmi.sbc.common.util.EsConstants;
import com.wanmi.sbc.common.util.IteratorUtils;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsListRequest;
import com.wanmi.sbc.elastic.api.request.spu.EsSpuPageRequest;
import com.wanmi.sbc.elastic.api.response.goods.EsGoodsResponse;
import com.wanmi.sbc.elastic.api.response.goods.EsSearchResponse;
import com.wanmi.sbc.elastic.api.response.spu.EsSpuPageResponse;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.elastic.bean.vo.goods.GoodsInfoNestVO;
import com.wanmi.sbc.elastic.goods.model.root.EsGoods;
import com.wanmi.sbc.elastic.spu.mapper.EsSpuMapper;
import com.wanmi.sbc.elastic.storeInformation.model.root.StoreInformation;
import com.wanmi.sbc.goods.api.provider.cate.GoodsCateQueryProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateChildCateIdsByIdRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsPageRequest;
import com.wanmi.sbc.goods.api.response.goods.GoodsPageResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsBrandSimpleVO;
import com.wanmi.sbc.goods.bean.vo.GoodsCateSimpleVO;
import com.wanmi.sbc.goods.bean.vo.GoodsPageSimpleVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * ES商品信息数据源操作
 * Created by daiyitian on 2017/4/21.
 */
@Slf4j
@Service
public class EsSpuService {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @Autowired
    private GoodsCateQueryProvider goodsCateQueryProvider;

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
        final String fmt = "ctx._source.%s='%s';ctx._source.storeName='%s'";
        Long resCount = 0L;
        if (storeList != null) {
            Client client = elasticsearchTemplate.getClient();
            UpdateByQueryRequestBuilder updateByQuery = UpdateByQueryAction.INSTANCE.newRequestBuilder(client)
                    .source(EsConstants.DOC_GOODS_TYPE, EsConstants.DOC_GOODS_TYPE);
            for (StoreInformation s : storeList) {
                String script = String.format(fmt, "supplierName", s.getSupplierName(), s.getStoreName());
                if (StoreType.PROVIDER.equals(s.getStoreType())) {
                    script = String.format(fmt, "providerName", s.getSupplierName(), s.getStoreName());
                }
                updateByQuery.filter(QueryBuilders.termQuery("storeId", s.getStoreId()));
                updateByQuery.script(new Script(script));
                resCount += updateByQuery.execute().actionGet().getUpdated();

                if (StoreType.PROVIDER.equals(s.getStoreType())) {
                    String providerScript = String.format("ctx._source.providerName='%s'", s.getSupplierName());
                    updateByQuery.filter(QueryBuilders.termQuery("providerId", s.getStoreId()));
                    updateByQuery.script(new Script(providerScript));
                    resCount += updateByQuery.execute().actionGet().getUpdated();
                }
            }
        }
        return resCount;
    }

    public EsSpuPageResponse page(EsSpuPageRequest request){
        EsSpuPageResponse pageResponse = new EsSpuPageResponse();
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

        MicroServicePage<EsGoodsVO> esGoodsVOS = this.basePage(request);
        if(CollectionUtils.isEmpty(esGoodsVOS.getContent())){
            pageResponse.setGoodsPage(new MicroServicePage<>(Collections.emptyList(), PageRequest.of(request.getPageNum(),
                    request.getPageSize()), 0));
            return pageResponse;
        }

        List<EsGoodsVO> goodsVOList = esGoodsVOS.getContent();
        List<String> spuIds = goodsVOList.stream().map(EsGoodsVO::getId).collect(Collectors.toList());
        GoodsPageRequest pageReq = new GoodsPageRequest();
        pageReq.setGoodsIds(spuIds);
        pageReq.setShowVendibilityFlag(request.getShowVendibilityFlag());
        pageReq.setShowPointFlag(request.getShowPointFlag());
        pageReq.setPageSize(request.getPageSize());
        GoodsPageResponse response = goodsQueryProvider.page(pageReq).getContext();

        //填充SPU
        Map<String, GoodsPageSimpleVO> spuMap = response.getGoodsPage().getContent().stream()
                .map(g -> esSpuMapper.goodsToSimpleVO(g))
                .collect(Collectors.toMap(GoodsPageSimpleVO::getGoodsId, g -> g));
        //填充SPU的详细信息
        List<GoodsPageSimpleVO> simpleVOS = goodsVOList.stream()
                .map(g -> {
                    GoodsPageSimpleVO spu = spuMap.get(g.getId());
                    spu.setStock(g.getStock());
                    GoodsInfoNestVO tempGoodsInfo = g.getGoodsInfos().stream()
                            .filter(goodsInfo -> Objects.nonNull(goodsInfo.getMarketPrice()))
                            .min(Comparator.comparing(GoodsInfoNestVO::getMarketPrice)).orElse(null);

                    //取SKU最小市场价
                    spu.setMarketPrice(tempGoodsInfo != null ? tempGoodsInfo.getMarketPrice() : spu.getMarketPrice());
                    //取最小市场价SKU的相应购买积分
                    spu.setBuyPoint(0L);
                    if(tempGoodsInfo!= null && Objects.nonNull(tempGoodsInfo.getBuyPoint())) {
                        spu.setBuyPoint(tempGoodsInfo.getBuyPoint());
                    }
                    //取SKU最小供货价
                    spu.setSupplyPrice(g.getGoodsInfos().stream()
                            .filter(i -> Objects.nonNull(i.getSupplyPrice()))
                            .map(GoodsInfoNestVO::getSupplyPrice).min(BigDecimal::compareTo).orElse(spu.getSupplyPrice()));
                    spu.setProviderName(g.getProviderName());
                    spu.setGoodsChannelTypeSet(g.getGoodsChannelTypeSet());
                    return spu;
                }).collect(Collectors.toList());

        pageResponse.setGoodsPage(new MicroServicePage<>(simpleVOS, request.getPageable(), esGoodsVOS.getTotal()));

        //填充品牌
        pageResponse.setGoodsBrandList(goodsVOList.stream()
                .filter(g -> Objects.nonNull(g.getGoodsBrand()) && StringUtils.isNotBlank(g.getGoodsBrand().getBrandName()))
                .map(g -> esSpuMapper.brandToSimpleVO(g.getGoodsBrand()))
                .filter(IteratorUtils.distinctByKey(GoodsBrandSimpleVO::getBrandId)).collect(Collectors.toList()));

        //填充分类
        pageResponse.setGoodsCateList(goodsVOList.stream()
                .filter(g -> Objects.nonNull(g.getGoodsCate()) && StringUtils.isNotBlank(g.getGoodsCate().getCateName()))
                .map(g -> esSpuMapper.cateToSimpleVO(g.getGoodsCate()))
                .filter(IteratorUtils.distinctByKey(GoodsCateSimpleVO::getCateId)).collect(Collectors.toList()));

        pageResponse.setImportStandard(response.getImportStandard());
        return pageResponse;
    }


    /**
     * 最基础的分页查询
     * @param request
     * @return
     */
    private MicroServicePage<EsGoodsVO> basePage(EsSpuPageRequest request) {
        EsSearchResponse esResponse = elasticsearchTemplate.query(EsSpuSearchCriteriaBuilder.getSearchCriteria(request),
                searchResponse -> EsSearchResponse.buildGoods(searchResponse, resultsMapper));
        return new MicroServicePage<>(esResponse.getGoodsData() == null ? Collections.emptyList(): esResponse.getGoodsData(), PageRequest.of(request.getPageNum(),
                request.getPageSize()), esResponse.getTotal());
    }

    /**
     * 根据spuId列表查询商品
     * @param request skuIds
     * @return
     */
    public EsGoodsResponse listGoodsByIds(EsGoodsListRequest request) {
        EsGoodsResponse response = new EsGoodsResponse();
        List<EsGoods> esGoods = elasticsearchTemplate.queryForList(request.getSearchCriteria(), EsGoods.class);
        response.setGoodsList(KsBeanUtil.convert(esGoods, GoodsVO.class));
        List<GoodsVO> goodsVOS=new ArrayList<>();
        esGoods.forEach(esGoods1 -> {
           GoodsVO goodsVO= KsBeanUtil.convert(esGoods1,GoodsVO.class);
            goodsVO.setGoodsId(esGoods1.getId());
            goodsVOS.add(goodsVO);
        });
        response.setGoodsList(goodsVOS);
        return response;
    }

}
