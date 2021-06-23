package com.wanmi.sbc.elastic.standard.service;

import com.aliyuncs.linkedmall.model.v20180116.QueryItemInventoryResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.StoreType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.EsConstants;
import com.wanmi.sbc.elastic.api.request.standard.EsStandardInitRequest;
import com.wanmi.sbc.elastic.api.request.standard.EsStandardPageRequest;
import com.wanmi.sbc.elastic.api.response.standard.EsStandardPageResponse;
import com.wanmi.sbc.elastic.bean.vo.goods.EsStandardGoodsPageVO;
import com.wanmi.sbc.elastic.standard.mapper.EsStandardMapper;
import com.wanmi.sbc.elastic.standard.model.root.EsStandardGoods;
import com.wanmi.sbc.elastic.storeInformation.model.root.StoreInformation;
import com.wanmi.sbc.goods.api.provider.brand.GoodsBrandQueryProvider;
import com.wanmi.sbc.goods.api.provider.cate.GoodsCateQueryProvider;
import com.wanmi.sbc.goods.api.provider.standard.StandardGoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.standard.StandardSkuQueryProvider;
import com.wanmi.sbc.goods.api.request.brand.GoodsBrandListRequest;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateListByConditionRequest;
import com.wanmi.sbc.goods.api.request.standard.StandardGoodsPageRequest;
import com.wanmi.sbc.goods.api.request.standard.StandardGoodsRelByGoodsIdsRequest;
import com.wanmi.sbc.goods.api.request.standard.StandardIdsByGoodsIdsRequest;
import com.wanmi.sbc.goods.api.request.standard.StandardPartColsListByGoodsIdsRequest;
import com.wanmi.sbc.goods.bean.enums.GoodsSource;
import com.wanmi.sbc.goods.bean.vo.*;
import com.wanmi.sbc.linkedmall.api.provider.stock.LinkedMallStockQueryProvider;
import com.wanmi.sbc.linkedmall.api.request.stock.GoodsStockGetRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.UpdateByQueryAction;
import org.elasticsearch.index.reindex.UpdateByQueryRequestBuilder;
import org.elasticsearch.script.Script;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
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
public class EsStandardService {


    @Autowired
    private StandardGoodsQueryProvider standardGoodsQueryProvider;

    @Autowired
    private StandardSkuQueryProvider standardSkuQueryProvider;

    @Autowired
    private LinkedMallStockQueryProvider linkedMallStockQueryProvider;

    @Autowired
    private GoodsBrandQueryProvider goodsBrandQueryProvider;

    @Autowired
    private GoodsCateQueryProvider goodsCateQueryProvider;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private EsStandardMapper esStandardMapper;

    /**
     * 保存
     * @param goods
     */
    public void save(EsStandardGoods goods) {
        if (!elasticsearchTemplate.indexExists(EsConstants.DOC_STANDARD_GOODS)) {
            //商品索引
            elasticsearchTemplate.getClient().admin().indices()
                    .prepareCreate(EsConstants.DOC_STANDARD_GOODS).execute().actionGet();
            elasticsearchTemplate.putMapping(EsStandardGoods.class);
        }
        IndexQuery iq = new IndexQuery();
        iq.setId(goods.getGoodsId());
        iq.setObject(goods);
        iq.setIndexName(EsConstants.DOC_STANDARD_GOODS);
        iq.setType(EsConstants.DOC_STANDARD_GOODS);
        elasticsearchTemplate.index(iq);
    }

    /**
     * 分页
     * @param request
     * @return
     */
    public EsStandardPageResponse page(EsStandardPageRequest request) {
        EsStandardPageResponse response = new EsStandardPageResponse();
        Page<EsStandardGoods> goodsPage = elasticsearchTemplate.queryForPage(EsStandardSearchCriteriaBuilder.getSearchCriteria(request), EsStandardGoods.class);
        if (CollectionUtils.isNotEmpty(goodsPage.getContent())) {
            List<String> goodsIds = goodsPage.getContent().stream().map(EsStandardGoods::getGoodsId).collect(Collectors.toList());
            List<StandardSkuVO> skuList = standardSkuQueryProvider.listPartColsByGoodsIds(
                    StandardPartColsListByGoodsIdsRequest.builder().goodsIds(goodsIds)
                            .cols(Arrays.asList("goodsId", "marketPrice", "costPrice", "stock", "thirdPlatformSpuId", "thirdPlatformSkuId", "supplyPrice"))
                            .build()).getContext().getStandardSkuList();

            Map<String, List<StandardSkuVO>> skuMap = skuList.stream().collect(Collectors.groupingBy(StandardSkuVO::getGoodsId));
            Page<EsStandardGoodsPageVO> pageVOS = goodsPage.map(g -> {
                EsStandardGoodsPageVO pageVO = esStandardMapper.standardToPageVO(g);
                List<StandardSkuVO> standardSkuList = skuMap.getOrDefault(g.getGoodsId(), Collections.emptyList());
                //取SKU最小市场价
                pageVO.setMarketPrice(standardSkuList.stream()
                        .filter(goodsInfo -> Objects.nonNull(goodsInfo.getMarketPrice()))
                        .map(StandardSkuVO::getMarketPrice).min(BigDecimal::compareTo).orElse(g.getMarketPrice()));
                pageVO.setSupplyPrice(standardSkuList.stream()
                        .filter(goodsInfo -> Objects.nonNull(goodsInfo.getSupplyPrice()))
                        .map(StandardSkuVO::getSupplyPrice).min(BigDecimal::compareTo).orElse(g.getSupplyPrice()));
                //合计库存
                pageVO.setStock(standardSkuList.stream().filter(goodsInfo -> Objects.nonNull(goodsInfo.getStock()))
                        .mapToLong(StandardSkuVO::getStock).sum());
                return pageVO;
            });

            List<Long> brandIds = goodsPage.getContent().stream()
                    .filter(g -> Objects.nonNull(g.getBrandId()))
                    .map(EsStandardGoods::getBrandId).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(brandIds)){
                List<GoodsBrandVO>  goodsBrandVOList = goodsBrandQueryProvider.list(
                        GoodsBrandListRequest.builder().delFlag(DeleteFlag.NO.toValue()).brandIds(brandIds).build()).getContext().getGoodsBrandVOList();
                response.setGoodsBrandList(goodsBrandVOList.stream().map(esStandardMapper::brandToSimpleVO).collect(Collectors.toList()));
            }

            List<Long> cateIds = goodsPage.getContent().stream().filter(g -> Objects.nonNull(g.getCateId()))
                    .map(EsStandardGoods::getCateId).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(cateIds)){
                GoodsCateListByConditionRequest cateRequest = new GoodsCateListByConditionRequest();
                cateRequest.setCateIds(cateIds);
                cateRequest.setDelFlag(DeleteFlag.NO.toValue());
                List<GoodsCateVO> goodsBrandVOList = goodsCateQueryProvider.listByCondition(cateRequest).getContext().getGoodsCateVOList();
                response.setGoodsCateList(goodsBrandVOList.stream().map(esStandardMapper::cateToSimpleVO).collect(Collectors.toList()));
            }

            //如果是linkedmall商品，实时查库存
            List<Long> itemIds = goodsPage.getContent().stream()
                    .filter(v -> Integer.valueOf(GoodsSource.LINKED_MALL.toValue()).equals(v.getGoodsSource()))
                    .map(v -> Long.valueOf(v.getThirdPlatformSpuId()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(itemIds)) {
                List<QueryItemInventoryResponse.Item> stocks = null;
                if (itemIds.size() > 0) {
                    stocks = linkedMallStockQueryProvider.batchGoodsStockByDivisionCode(new GoodsStockGetRequest(itemIds, "0", null)).getContext();
                }
                if (stocks != null) {
                    List<StandardSkuVO> tempSkuList = skuList.stream()
                            .filter(s -> Objects.nonNull(s.getThirdPlatformSpuId()) && itemIds.contains(Long.valueOf(s.getThirdPlatformSpuId())))
                            .collect(Collectors.toList());
                    for (StandardSkuVO standardSku : tempSkuList) {
                        for (QueryItemInventoryResponse.Item spuStock : stocks) {
                            Optional<QueryItemInventoryResponse.Item.Sku> stock = spuStock.getSkuList().stream()
                                    .filter(v -> String.valueOf(spuStock.getItemId()).equals(standardSku.getThirdPlatformSpuId()) && String.valueOf(v.getSkuId()).equals(standardSku.getThirdPlatformSkuId()))
                                    .findFirst();
                            stock.ifPresent(sku -> standardSku.setStock(sku.getInventory().getQuantity()));
                        }
                    }
                    for (EsStandardGoodsPageVO standardGoods : pageVOS) {
                        Long spuStock = tempSkuList.stream()
                                .filter(v -> v.getGoodsId().equals(standardGoods.getGoodsId()))
                                .map(StandardSkuVO::getStock).reduce(0L, (aLong, aLong2) -> aLong + aLong2);
                        standardGoods.setStock(spuStock);
                    }
                }
            }
            response.setStandardGoodsPage(new MicroServicePage(pageVOS, pageVOS.getPageable()));
        }
        return response;
    }

    /**
     * 初始化SPU持化于ES
     */
    public void init(EsStandardInitRequest request) {
        boolean isClear = request.isClearEsIndex();
        boolean isMapping = false;
        if (elasticsearchTemplate.indexExists(EsConstants.DOC_STANDARD_GOODS)) {
            if (isClear) {
                elasticsearchTemplate.deleteIndex(EsConstants.DOC_STANDARD_GOODS);
                isMapping = true;
            }
        } else {
            isMapping = true;
        }

        if(isMapping) {
            //重建商品索引
            elasticsearchTemplate.getClient().admin().indices()
                    .prepareCreate(EsConstants.DOC_STANDARD_GOODS).execute().actionGet();
            elasticsearchTemplate.putMapping(EsStandardGoods.class);
        }

        if(request.getGoodsIds() == null){
            request.setGoodsIds(new ArrayList<>());
        }
        //根据商品id获取商品库id
        if(CollectionUtils.isNotEmpty(request.getRelGoodsIds())){
            List<String> standardIds = standardGoodsQueryProvider.listStandardIdsByGoodsIds(StandardIdsByGoodsIdsRequest.builder()
                    .goodsIds(request.getRelGoodsIds()).build()).getContext().getStandardIds();
            if(CollectionUtils.isEmpty(standardIds)){
                return;
            }
            request.getGoodsIds().addAll(standardIds);
        }
        /*
         * 一个ID因采用uuid有32位字符串，目前mysql的SQL语句最大默认限制1M，通过mysql的配置文件（my.ini）中的max_allowed_packet来调整
         * 每批查询2000个GoodsID，根据jvm内存、服务请求超时时间来综合考虑调整。
         */
        if(request.getPageSize() == null){
            request.setPageSize(2000);
        }
        if (request.getPageIndex() == null) {
            request.setPageIndex(0);
        }

        StandardGoodsPageRequest pageRequest = new StandardGoodsPageRequest();
        pageRequest.setGoodsIds(request.getGoodsIds());
        pageRequest.setBrandIds(request.getBrandIds());
        pageRequest.setCateIds(request.getCateIds());
        pageRequest.setGoodsSource(request.getGoodsSource());
        pageRequest.setPageSize(request.getPageSize());
        pageRequest.setPageNum(request.getPageIndex());
        MicroServicePage<StandardGoodsVO> page = standardGoodsQueryProvider.simplePage(pageRequest).getContext().getStandardGoodsPage();
        if (page.getTotalElements() <= 0) {
            return;
        }

        log.info("商品所有索引开始");
        long startTime = System.currentTimeMillis();

        long pageCount = 1L;
        long m = page.getTotalElements() % pageRequest.getPageSize();
        if (m > 0) {
            pageCount = page.getTotalElements() / pageRequest.getPageSize() + 1;
        } else {
            pageCount = page.getTotalElements() / pageRequest.getPageSize();
        }

        int errorThrow = 0;//满10次，退出循环往上抛异常
        for (int i = request.getPageIndex(); i <= pageCount; i++) {
            try {
                List<StandardGoodsVO> goodsList = page.getContent();
                if (i > 0) {
                    pageRequest.setPageNum(i);
                    goodsList = standardGoodsQueryProvider.simplePage(pageRequest).getContext().getStandardGoodsPage().getContent();
                }

                if (CollectionUtils.isNotEmpty(goodsList)) {
                    List<String> goodsIds = goodsList.stream().map(StandardGoodsVO::getGoodsId).collect(Collectors.toList());
                    StandardPartColsListByGoodsIdsRequest infoQueryRequest = new StandardPartColsListByGoodsIdsRequest();
                    infoQueryRequest.setGoodsIds(goodsIds);
                    infoQueryRequest.setCols(Arrays.asList("goodsInfoNo", "goodsId"));//只返回"goodsInfoNo", "goodsId"内容
                    List<StandardSkuVO> skuList = standardSkuQueryProvider.listPartColsByGoodsIds(infoQueryRequest).getContext().getStandardSkuList();
                    Map<String, List<String>> skuNoMap = skuList.stream()
                            .collect(Collectors.groupingBy(StandardSkuVO::getGoodsId,
                                    Collectors.mapping(StandardSkuVO::getGoodsInfoNo, Collectors.toList())));

                    Map<String, List<Long>> storeIdMap = standardGoodsQueryProvider.listRelByGoodsIds(StandardGoodsRelByGoodsIdsRequest.builder()
                            .standardIds(goodsIds).build()).getContext().getStandardGoodsRelList().stream()
                            .collect(Collectors.groupingBy(StandardGoodsRelVO::getGoodsId,
                                    Collectors.mapping(StandardGoodsRelVO::getStoreId, Collectors.toList())));

                    //遍历SKU，填充SPU、图片
                    List<IndexQuery> esGoodsList = new ArrayList<>();
                    goodsList.forEach(goods -> {
                        EsStandardGoods esGoods = esStandardMapper.standardToEs(goods);
                        esGoods.setGoodsInfoNos(skuNoMap.get(esGoods.getGoodsId()));
                        esGoods.setRelStoreIds(storeIdMap.get(esGoods.getGoodsId()));
                        IndexQuery iq = new IndexQuery();
                        iq.setId(esGoods.getGoodsId());
                        iq.setObject(esGoods);
                        iq.setIndexName(EsConstants.DOC_STANDARD_GOODS);
                        iq.setType(EsConstants.DOC_STANDARD_GOODS);
                        esGoodsList.add(iq);
                    });
                    //持久化商品
                    elasticsearchTemplate.bulkIndex(esGoodsList);
                    elasticsearchTemplate.refresh(EsStandardGoods.class);
                }
            } catch (Exception e) {
                log.error("初始化商品库页码位置".concat(String.valueOf(i)).concat("，异常："), e);
                errorThrow++;
                if (errorThrow >= 10) {
                    throw new SbcRuntimeException("K-030407", new Object[]{i});
                }
                i--;
            } catch (Throwable t) {
                log.error("初始化商品库页码位置".concat(String.valueOf(i)).concat("，异常："), t);
                throw new SbcRuntimeException("K-030407", new Object[]{i});
            }
        }

        log.info(String.format("商品库所有索引结束->花费%s毫秒", (System.currentTimeMillis() - startTime)));
    }


    public void deleteByIds(List<String> ids) {
        if (elasticsearchTemplate.indexExists(EsConstants.DOC_STANDARD_GOODS)) {
            DeleteQuery deleteQuery = new DeleteQuery();
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            boolQueryBuilder.must(QueryBuilders.idsQuery().addIds(ids.stream().toArray(String[]::new)));
            deleteQuery.setQuery(boolQueryBuilder);
            elasticsearchTemplate.delete(deleteQuery, EsStandardGoods.class);
        }
    }

    /**
     * 修改更新商家名称信息
     *
     * @param storeList 店铺名称
     * @return
     */
    public Long updateProviderName(List<StoreInformation> storeList) {
        final String fmt = "ctx._source.providerName='%s'";
        Long resCount = 0L;
        if (CollectionUtils.isNotEmpty(storeList)) {
            Client client = elasticsearchTemplate.getClient();
            UpdateByQueryRequestBuilder updateByQuery = UpdateByQueryAction.INSTANCE.newRequestBuilder(client)
                    .source(EsConstants.DOC_STANDARD_GOODS, EsConstants.DOC_STANDARD_GOODS);
            for (StoreInformation s : storeList) {
                if (StoreType.PROVIDER.equals(s.getStoreType())) {
                    String script = String.format(fmt, s.getSupplierName());
                    updateByQuery.filter(QueryBuilders.termQuery("storeId", s.getStoreId()));
                    updateByQuery.script(new Script(script));
                    resCount += updateByQuery.execute().actionGet().getUpdated();
                }
            }
        }
        return resCount;
    }
}
