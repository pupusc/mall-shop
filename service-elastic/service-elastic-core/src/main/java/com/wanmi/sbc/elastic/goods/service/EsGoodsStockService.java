package com.wanmi.sbc.elastic.goods.service;

import com.wanmi.sbc.common.util.EsConstants;
import com.wanmi.sbc.common.util.StringUtil;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryAction;
import org.elasticsearch.index.reindex.UpdateByQueryRequestBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;

/**
 * ES商品信息数据源操作
 * Created by daiyitian on 2017/4/21.
 */
@Service
public class EsGoodsStockService {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 增量Spu库存
     *
     * @param spuId spuId
     * @param stock 库存
     * @return
     */
    public void subStockBySpuId(String spuId, Long stock) {
        if (StringUtils.isNotEmpty(spuId) && Objects.nonNull(stock)) {
            Client client = elasticsearchTemplate.getClient();
            UpdateByQueryRequestBuilder updateByQuery = UpdateByQueryAction.INSTANCE.newRequestBuilder(client);
            String providerGoodsId = null;
            GetResponse response = client.get(new GetRequest(EsConstants.DOC_GOODS_TYPE, EsConstants.DOC_GOODS_TYPE, spuId)).actionGet();
            if (response != null && MapUtils.isNotEmpty(response.getSource())) {
                providerGoodsId = StringUtil.cast(response.getSource().get("providerGoodsId"), String.class);
            }
            //供应商
            if(StringUtils.isNotBlank(providerGoodsId)){
                //更新供应商商品ES
                updateByQuery = updateByQuery.source(EsConstants.DOC_GOODS_TYPE)
                        //查询要修改的结果集
                        .filter(QueryBuilders.idsQuery(EsConstants.DOC_GOODS_TYPE).addIds(providerGoodsId))
                        //修改操作
                        .script(new Script("ctx._source.stock -= " + stock));
                updateByQuery.get().getUpdated();

                //更新供应商关联商家商品ES
                updateByQuery = updateByQuery.source(EsConstants.DOC_GOODS_TYPE)
                        //查询要修改的结果集
                        .filter(QueryBuilders.termQuery("providerGoodsId", providerGoodsId))
                        //修改操作
                        .script(new Script("ctx._source.stock -= " + stock));
                updateByQuery.get().getUpdated();
            }else {
                updateByQuery = updateByQuery.source(EsConstants.DOC_GOODS_TYPE)
                        //查询要修改的结果集
                        .filter(QueryBuilders.idsQuery(EsConstants.DOC_GOODS_TYPE).addIds(spuId))
                        //修改操作
                        .script(new Script("ctx._source.stock -= " + stock));
                updateByQuery.get().getUpdated();
            }
        }
    }

    /**
     * 增量Sku库存
     *
     * @param skuId spuId
     * @param stock 库存
     * @return
     */
    public void subStockBySkuId(String skuId, Long stock) {
        if (StringUtils.isNotEmpty(skuId) && Objects.nonNull(stock)) {
            Client client = elasticsearchTemplate.getClient();
            UpdateByQueryRequestBuilder updateByQuery = UpdateByQueryAction.INSTANCE.newRequestBuilder(client);
            updateByQuery = updateByQuery.source(EsConstants.DOC_GOODS_INFO_TYPE)
                    //查询要修改的结果集
                    .filter(QueryBuilders.idsQuery(EsConstants.DOC_GOODS_INFO_TYPE).addIds(skuId))
                    //修改操作
                    .script(new Script("ctx._source.goodsInfo.stock -= " + stock));
            updateByQuery.get().getUpdated();
        }
    }

    /**
     * 覆盖更新ES SPU库存数据
     * @param spuId
     * @param stock
     */
    public void resetStockBySpuId(String spuId,Long stock){
        if (StringUtils.isNotEmpty(spuId) && Objects.nonNull(stock)) {
            Client client = elasticsearchTemplate.getClient();
            UpdateByQueryRequestBuilder updateByQuery = UpdateByQueryAction.INSTANCE.newRequestBuilder(client);
            updateByQuery = updateByQuery.source(EsConstants.DOC_GOODS_TYPE)
                    //查询要修改的结果集
                    .filter(QueryBuilders.idsQuery(EsConstants.DOC_GOODS_TYPE).addIds(spuId))
                    //修改操作
                    .script(new Script("ctx._source.stock = " + stock));
            updateByQuery.get().getUpdated();
        }
    }

    /**
     * 覆盖更新ES SKu库存数据
     * @param skuId
     * @param stock
     */
    public void resetStockBySkuId(String skuId,Long stock){
        if (StringUtils.isNotEmpty(skuId) && Objects.nonNull(stock)) {
            Client client = elasticsearchTemplate.getClient();
            UpdateByQueryRequestBuilder updateByQuery = UpdateByQueryAction.INSTANCE.newRequestBuilder(client);
            updateByQuery = updateByQuery.source(EsConstants.DOC_GOODS_INFO_TYPE)
                    //查询要修改的结果集
                    .filter(QueryBuilders.idsQuery(EsConstants.DOC_GOODS_INFO_TYPE).addIds(skuId))
                    //修改操作
                    .script(new Script("ctx._source.goodsInfo.stock = " + stock));
            updateByQuery.get().getUpdated();
        }
    }

    /**
     * 覆盖更新ES SPU库存数据
     * @param spusMap
     */
    public void batchResetStockBySpuId(Map spusMap){
        if (!spusMap.isEmpty()){
            Client client = elasticsearchTemplate.getClient();
            Set spus = spusMap.keySet();
            String[] skuIds = new String[spus.size()];
            int i = 0;
            Iterator it = spus.iterator();
            while (it.hasNext()) {
                skuIds[i] = (String) it.next();
                i++;
            }
            UpdateByQueryRequestBuilder updateByQuery = UpdateByQueryAction.INSTANCE.newRequestBuilder(client);
            updateByQuery = updateByQuery.source(EsConstants.DOC_GOODS_INFO_TYPE)
                    //查询要修改的结果集
                    .filter(QueryBuilders.idsQuery(EsConstants.DOC_GOODS_INFO_TYPE).addIds(skuIds))
                    //修改操作
                    .script(new Script(Script.DEFAULT_SCRIPT_TYPE, Script.DEFAULT_SCRIPT_LANG,
                            "if(params[ctx._source.id] != null) ctx._source.stock = params[ctx._source.id]", spusMap));
            updateByQuery.get().getUpdated();
        }
    }

    /**
     * 覆盖更新ES SKu库存数据
     * @param skusMap
     */
    public void batchResetStockBySkuId(Map skusMap){
        if (!skusMap.isEmpty()){
            Client client = elasticsearchTemplate.getClient();
            Set skus = skusMap.keySet();
            String[] skuIds = new String[skus.size()];
            int i = 0;
            Iterator it = skus.iterator();
            while (it.hasNext()) {
                skuIds[i] = (String) it.next();
                i++;
            }
            UpdateByQueryRequestBuilder updateByQuery = UpdateByQueryAction.INSTANCE.newRequestBuilder(client);
            updateByQuery = updateByQuery.source(EsConstants.DOC_GOODS_INFO_TYPE)
                    //查询要修改的结果集
                    .filter(QueryBuilders.idsQuery(EsConstants.DOC_GOODS_INFO_TYPE).addIds(skuIds))
                    //修改操作
                    .script(new Script(Script.DEFAULT_SCRIPT_TYPE, Script.DEFAULT_SCRIPT_LANG,
                            "if(params[ctx._source.id] != null) ctx._source.goodsInfo.stock = params[ctx._source.id]", skusMap));
            updateByQuery.get().getUpdated();
        }
    }

    public void batchResetGoodsInfoStockBySpuId(Map<String, Integer> spusMap){
        if (!spusMap.isEmpty()){
            Client client = elasticsearchTemplate.getClient();
            UpdateByQueryRequestBuilder updateByQuery = UpdateByQueryAction.INSTANCE.newRequestBuilder(client);
            for (String skuId : spusMap.keySet()){
                Long stock = Long.valueOf(spusMap.get(skuId));
                if (StringUtils.isNotEmpty(skuId) && Objects.nonNull(stock)) {
                    updateByQuery = updateByQuery.source(EsConstants.DOC_GOODS_TYPE)
                            //查询要修改的结果集
                            .filter(QueryBuilders.idsQuery(EsConstants.DOC_GOODS_TYPE).addIds(skuId))
                            //修改操作
                            .script(new Script("ctx._source.goodsInfos[0]['stock'] = " + stock));
                    updateByQuery.get().getUpdated();
                }
            }
        }
    }
}
