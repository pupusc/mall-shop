package com.wanmi.sbc.elastic.goods.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.EsConstants;
import com.wanmi.sbc.elastic.api.response.goods.EsSearchResponse;
import com.wanmi.sbc.elastic.goods.model.root.GoodsLabelNest;
import com.wanmi.sbc.goods.api.provider.goodslabel.GoodsLabelQueryProvider;
import com.wanmi.sbc.goods.bean.vo.GoodsLabelVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryAction;
import org.elasticsearch.index.reindex.UpdateByQueryRequestBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;

/**
 * ES商品标签信息数据源操作
 * Created by daiyitian on 2017/4/21.
 */
@Service
public class EsGoodsLabelService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private GoodsLabelQueryProvider goodsLabelQueryProvider;

    /**
     * 查询商品标签NestMap
     * @return 标签NestMap
     */
    public Map<Long, GoodsLabelNest> getLabelMap() {
        return goodsLabelQueryProvider.cacheList().getContext().getGoodsLabelVOList().stream()
                .map(l -> {
                    GoodsLabelNest nest = new GoodsLabelNest();
                    nest.setGoodsLabelId(l.getGoodsLabelId());
                    nest.setLabelName(l.getLabelName());
                    nest.setLabelSort(l.getLabelSort());
                    nest.setDelFlag(DeleteFlag.NO);
                    nest.setLabelVisible(l.getLabelVisible());
                    return nest;
                })
                .collect(Collectors.toMap(GoodsLabelNest::getGoodsLabelId, l -> l));
    }

    /**
     * 聚合商品标签数据
     *
     * @param response
     * @return
     */
    public List<GoodsLabelVO> extractGoodsLabel(EsSearchResponse response) {
        List<? extends EsSearchResponse.AggregationResultItem> labelBucket = response.getAggResultMap().get("goodsLabelList");
        if (CollectionUtils.isNotEmpty(labelBucket)) {
            List<Long> labelIds =
                    labelBucket.stream().map(EsSearchResponse.AggregationResultItem<String>::getKey).map(NumberUtils::toLong).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(labelIds)){
                List<GoodsLabelVO> list = goodsLabelQueryProvider.cacheList().getContext().getGoodsLabelVOList();
                if (CollectionUtils.isNotEmpty(list)) {
                    return list.stream().filter(l -> labelIds.contains(l.getGoodsLabelId())).collect(Collectors.toList());
                }
            }
        }
        return Collections.emptyList();
    }

    /**
     * 修改商品标签名称
     *
     * @param goodsLabelVO
     */
    public void updateLabelName(GoodsLabelVO goodsLabelVO) {
        updateSkuGoodsLabel(goodsLabelVO, false);
        updateSpuGoodsLabel(goodsLabelVO, false);
    }

    /**
     * 修改商品标签展示
     *
     * @param goodsLabelVO
     */
    public void updateLabelVisible(GoodsLabelVO goodsLabelVO) {
        updateSkuGoodsLabel(goodsLabelVO, true);
        updateSpuGoodsLabel(goodsLabelVO, true);
    }

    /**
     * 修改sku商品标签es
     *
     * @param goodsLabelVO
     * @return
     */
    private Long updateSkuGoodsLabel(GoodsLabelVO goodsLabelVO, boolean visible) {
        Long resCount = 0L;
        if (Objects.nonNull(goodsLabelVO)) {
            Client client = elasticsearchTemplate.getClient();
            UpdateByQueryRequestBuilder updateByQuery = UpdateByQueryAction.INSTANCE.newRequestBuilder(client);
            String str = "for (int i=0;i<ctx._source.goodsLabelList.size();i++){ " +
                    "if(ctx._source.goodsLabelList[i]['goodsLabelId'] == " + goodsLabelVO.getGoodsLabelId() + "){" +
                    "ctx._source.goodsLabelList[i]['labelName']='" + goodsLabelVO.getLabelName() + "';" +
                    "}}";
            if(visible){
                str = "for (int i=0;i<ctx._source.goodsLabelList.size();i++){ " +
                        "if(ctx._source.goodsLabelList[i]['goodsLabelId'] == " + goodsLabelVO.getGoodsLabelId() + "){" +
                        "ctx._source.goodsLabelList[i]['labelVisible']=" + Boolean.TRUE.equals(goodsLabelVO.getLabelVisible()) + "" +
                        "}}";
            }

            Script script = new Script(str);
            logger.info("Sku Script: \n" + script);

            updateByQuery = updateByQuery.source(EsConstants.DOC_GOODS_INFO_TYPE, EsConstants.DOC_GOODS_INFO_TYPE)
                    //查询要修改的结果集
                    .filter(nestedQuery("goodsLabelList", QueryBuilders.termQuery("goodsLabelList.goodsLabelId",
                            goodsLabelVO.getGoodsLabelId()), ScoreMode.None))
                    //修改操作
                    .script(script);
            BulkByScrollResponse response = updateByQuery.get();
            resCount = response.getUpdated();
            logger.info("Sku resCount: " + resCount);
        }
        return resCount;
    }

    /**
     * 修改spu商品标签
     *
     * @param goodsLabelVO
     * @return
     */
    private Long updateSpuGoodsLabel(GoodsLabelVO goodsLabelVO, boolean visible) {
        Long resCount = 0L;
        if (Objects.nonNull(goodsLabelVO)) {
            Client client = elasticsearchTemplate.getClient();
            UpdateByQueryRequestBuilder updateByQuery = UpdateByQueryAction.INSTANCE.newRequestBuilder(client);
            String str = "for (int i=0; i < ctx._source.goodsLabelList.size(); i++){ " +
                    "if(ctx._source.goodsLabelList[i]['goodsLabelId'] == " + goodsLabelVO.getGoodsLabelId() + "){" +
                    "ctx._source.goodsLabelList[i]['labelName']='" + goodsLabelVO.getLabelName() + "';" +
                    "}" +
                    "}";
            if(visible){
                str = "for (int i=0; i < ctx._source.goodsLabelList.size(); i++){ " +
                        "if(ctx._source.goodsLabelList[i]['goodsLabelId'] == " + goodsLabelVO.getGoodsLabelId() + "){" +
                        "ctx._source.goodsLabelList[i]['labelVisible']=" + Boolean.TRUE.equals(goodsLabelVO.getLabelVisible()) + "" +
                        "}" +
                        "}";
            }


            Script script = new Script(str);
            logger.info("Spu Script: \n" + script);
            updateByQuery = updateByQuery.source(EsConstants.DOC_GOODS_TYPE, EsConstants.DOC_GOODS_TYPE)
                    //查询要修改的结果集
                    .filter(nestedQuery("goodsLabelList", QueryBuilders.termQuery("goodsLabelList.goodsLabelId",
                            goodsLabelVO.getGoodsLabelId()), ScoreMode.None))
                    //修改操作
                    .script(script);
            BulkByScrollResponse response = updateByQuery.get();
            resCount = response.getUpdated();
            logger.info("Spu resCount: " + resCount);
        }
        return resCount;
    }

    /**
     * 删除商品标签
     *
     * @param ids
     * @return
     */
    public void deleteSomeLabel(List<Long> ids) {
        deleteSpuSomeLabel(ids);
        deleteSkuSomeLabel(ids);
    }

    /**
     * 删除spu商品标签
     *
     * @param ids
     * @return
     */
    private Long deleteSpuSomeLabel(List<Long> ids) {
        Long resCount = 0L;
        if (CollectionUtils.isNotEmpty(ids)) {
            Client client = elasticsearchTemplate.getClient();

            UpdateByQueryRequestBuilder updateByQuery = UpdateByQueryAction.INSTANCE.newRequestBuilder(client);

            Script script = new Script(
                    "for (int i=0; i < ctx._source.goodsLabelList.size(); i++){ " +
                            "if(" + ids + ".contains(ctx._source.goodsLabelList[i]['goodsLabelId'])){" +
                            "ctx._source.goodsLabelList[i]['delFlag']=1;" +
                            "}}"
            );
            updateByQuery = updateByQuery.source(EsConstants.DOC_GOODS_TYPE, EsConstants.DOC_GOODS_TYPE)
                    //查询要修改的结果集
                    .filter(nestedQuery("goodsLabelList",
                            QueryBuilders.termsQuery("goodsLabelList.goodsLabelId", ids), ScoreMode.None))
                    //修改操作
                    .script(script);
            BulkByScrollResponse response = updateByQuery.get();
            resCount = response.getUpdated();
        }
        return resCount;
    }

    /**
     * 删除sku商品标签
     *
     * @param ids
     * @return
     */
    private Long deleteSkuSomeLabel(List<Long> ids) {
        Long resCount = 0L;
        if (CollectionUtils.isNotEmpty(ids)) {
            Client client = elasticsearchTemplate.getClient();

            UpdateByQueryRequestBuilder updateByQuery = UpdateByQueryAction.INSTANCE.newRequestBuilder(client);

            updateByQuery = updateByQuery.source(EsConstants.DOC_GOODS_INFO_TYPE, EsConstants.DOC_GOODS_INFO_TYPE)
                    //查询要修改的结果集
                    .filter(nestedQuery("goodsLabelList",
                            QueryBuilders.termsQuery("goodsLabelList.goodsLabelId", ids), ScoreMode.None))
                    //修改操作
                    .script(new Script(
                            "for (int i=0;i<ctx._source.goodsLabelList.size();i++) { " +
                                    "if(" + ids + ".contains(ctx._source.goodsLabelList[i]['goodsLabelId'])" +
                                    "){ctx._source.goodsLabelList[i]['delFlag']=1;}}"
                    ));
            BulkByScrollResponse response = updateByQuery.get();
            resCount = response.getUpdated();
        }
        return resCount;
    }

    /**
     * 修改商品标签排序
     *
     * @param list
     * @return
     */
    public void updateGoodsLabelSort(List<GoodsLabelVO> list) {
        updateSpuGoodsLabelSort(list);
        updateSkuGoodsLabelSort(list);
    }

    /**
     * 修改spu商品标签排序
     *
     * @param list
     * @return
     */
    private Long updateSpuGoodsLabelSort(List<GoodsLabelVO> list) {
        Long resCount = 0L;
        if (CollectionUtils.isNotEmpty(list)) {
            Map<String, Object> map =
                    list.stream().collect(Collectors.toMap(vo -> vo.getGoodsLabelId().toString(),
                            GoodsLabelVO::getLabelSort));

            Client client = elasticsearchTemplate.getClient();

            UpdateByQueryRequestBuilder updateByQuery = UpdateByQueryAction.INSTANCE.newRequestBuilder(client);

            updateByQuery = updateByQuery.source(EsConstants.DOC_GOODS_TYPE, EsConstants.DOC_GOODS_TYPE);

            Script script = new Script(ScriptType.INLINE, Script.DEFAULT_SCRIPT_LANG,
                    "for (int i=0;i<ctx._source.goodsLabelList.size();i++) { " +
                            "if(" + map.keySet() + ".contains(ctx._source.goodsLabelList[i]['goodsLabelId'])){" +
                            "ctx._source.goodsLabelList[i]['labelSort'] =" +
                            " params.get(ctx._source.goodsLabelList[i]['goodsLabelId'].toString());" +
                            "}}", map
            );
            logger.info("Script: \n" + script);
            //查询要修改的结果集
            updateByQuery = updateByQuery.filter(nestedQuery("goodsLabelList",
                    QueryBuilders.termsQuery("goodsLabelList.goodsLabelId",
                            map.keySet()), ScoreMode.None))
                    //修改操作
                    .script(script);
            BulkByScrollResponse response = updateByQuery.get();
            resCount += response.getUpdated();
        }
        return resCount;
    }

    /**
     * 修改sku商品标签排序
     *
     * @param list
     * @return
     */
    private Long updateSkuGoodsLabelSort(List<GoodsLabelVO> list) {
        Long resCount = 0L;
        if (CollectionUtils.isNotEmpty(list)) {
            Map<String, Object> map =
                    list.stream().collect(Collectors.toMap(vo -> vo.getGoodsLabelId().toString(),
                            GoodsLabelVO::getLabelSort));

            Client client = elasticsearchTemplate.getClient();

            UpdateByQueryRequestBuilder updateByQuery = UpdateByQueryAction.INSTANCE.newRequestBuilder(client);

            updateByQuery = updateByQuery.source(EsConstants.DOC_GOODS_INFO_TYPE, EsConstants.DOC_GOODS_INFO_TYPE);

            Script script = new Script(ScriptType.INLINE, Script.DEFAULT_SCRIPT_LANG,
                    "for (int i=0;i<ctx._source.goodsLabelList.size();i++) { " +
                            "if(" + map.keySet() + ".contains(ctx._source.goodsLabelList[i]['goodsLabelId'])){" +
                            "ctx._source.goodsLabelList[i]['labelSort'] =" +
                            " params.get(ctx._source.goodsLabelList[i]['goodsLabelId'].toString());" +
                            "}" +
                            "}", map
            );
            logger.info("Script: \n" + script);
            //查询要修改的结果集
            updateByQuery = updateByQuery.filter(nestedQuery("goodsLabelList",
                    QueryBuilders.termsQuery("goodsLabelList.goodsLabelId",
                            map.keySet()), ScoreMode.None))
                    //修改操作
                    .script(script);
            BulkByScrollResponse response = updateByQuery.get();
            resCount += response.getUpdated();
        }
        return resCount;
    }
}
