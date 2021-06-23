package com.wanmi.sbc.elastic.api.response.goods;

import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsInfoVO;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ResultsMapper;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Es 商品查询 返回结果
 *
 * @author liangck
 * @version 1.0
 * @since 16/6/28 17:29
 */
@Data
@ApiModel
public class EsSearchResponse implements Serializable {
    public static final Logger LOGGER = LoggerFactory.getLogger(EsSearchResponse.class);

    /**
     * 总数
     */
    @ApiModelProperty(value = "总数")
    private Long total;

    /**
     * 查询结果
     */
    @ApiModelProperty(value = "查询结果")
    private List<EsGoodsInfoVO> data;

    /**
     * 查询结果
     */
    @ApiModelProperty(value = "查询结果")
    private List<EsGoodsVO> goodsData;

    /**
     * 聚合结果
     */
    @ApiModelProperty(value = "聚合结果")
    private Map<String, List<? extends AggregationResultItem>> aggResultMap;

    /**
     * 原始的ES查询结果
     */
    @ApiModelProperty(value = "原始的ES查询结果")
    private SearchResponse originResponse;

    /**
     * 原始查询字符串
     */
    @ApiModelProperty(value = "原始查询字符串")
    private String queryString;

    /**
     * 修正后的查询字符串
     */
    @ApiModelProperty(value = "修正后的查询字符串")
    private String validQueryString;

    /**
     * 添加聚合结果
     *
     * @param result
     * @return
     */
    public EsSearchResponse addAggResult(AggregationResult result) {
        if (Objects.isNull(result)) {
            return this;
        }

        if (Objects.isNull(aggResultMap)) {
            aggResultMap = new HashMap<>();
        }

        aggResultMap.put(result.getName(), result.getChilds());

        return this;
    }

    /**
     * 转换 ES 查询结果
     *
     * @param response      查询结果
     * @param resultsMapper 结果映射
     * @return
     */
    private EsSearchResponse addQueryResults(SearchResponse response, ResultsMapper resultsMapper) {
        if (Objects.nonNull(resultsMapper)) {
            data = resultsMapper.mapResults(response, EsGoodsInfoVO.class, null).getContent();
        }

        if (CollectionUtils.isNotEmpty(data)) {
            boolean hasHighLight = false;
            if (response.getHits().getHits().length > 0) {
                if (MapUtils.isNotEmpty(response.getHits().getAt(0).getHighlightFields())) {
                    hasHighLight = true;
                }
            }

            if (hasHighLight) {
                IntStream.range(0, data.size()).parallel().forEach(index -> {
                    EsGoodsInfoVO esGoodsInfo = data.get(index);
                    SearchHit sh = response.getHits().getAt(index);
                    sh.getHighlightFields().entrySet().stream().forEach(entry -> {
                        try {
                            PropertyUtils.setProperty(esGoodsInfo, entry.getKey(), entry.getValue());
                        } catch (Exception e) {
                            LOGGER.error("Set EsGoodsInfo highLight property error = {}, Property key = {}, value = " +
                                    "{}", e, entry.getKey(), entry.getValue());
                        }
                    });
                });
            }
        }

        return this;
    }


    /**
     * 转换 ES 查询结果
     *
     * @param response      查询结果
     * @param resultsMapper 结果映射
     * @return
     */
    private EsSearchResponse addQueryGoodsResults(SearchResponse response, ResultsMapper resultsMapper) {
        if (Objects.nonNull(resultsMapper)) {
            goodsData = resultsMapper.mapResults(response, EsGoodsVO.class, null).getContent();
        }

        if (CollectionUtils.isNotEmpty(goodsData)) {
            boolean hasHighLight = false;
            if (response.getHits().getHits().length > 0) {
                if (MapUtils.isNotEmpty(response.getHits().getAt(0).getHighlightFields())) {
                    hasHighLight = true;
                }
            }

            if (hasHighLight) {
                IntStream.range(0, goodsData.size()).parallel().forEach(index -> {
                    EsGoodsVO esGoods = goodsData.get(index);
                    SearchHit sh = response.getHits().getAt(index);
                    sh.getHighlightFields().entrySet().stream().forEach(entry -> {
                        try {
                            PropertyUtils.setProperty(esGoods, entry.getKey(), entry.getValue());
                        } catch (Exception e) {
                            LOGGER.error("Set EsGoodsInfo highLight property error = {}, Property key = {}, value = " +
                                    "{}", e, entry.getKey(), entry.getValue());
                        }
                    });
                });
            }
        }

        return this;
    }

    /**
     * 转换 聚合结果
     *
     * @param response
     * @return
     */
    private EsSearchResponse addAggResults(SearchResponse response) {
        if (Objects.nonNull(response.getAggregations())) {
            response.getAggregations().asList().stream().map(this::convertRootAggResult).filter(Objects::nonNull)
                    .forEachOrdered(this::addAggResult);
        }

        return this;
    }

    /**
     * 转换根聚合结果
     *
     * @param rootAggregation
     * @return
     */
    private AggregationResult convertRootAggResult(Aggregation rootAggregation) {
        return new AggregationResult(rootAggregation.getName(), convertSubAggResult(rootAggregation));
    }

    /**
     * 转换聚合结果
     *
     * @param aggregation
     * @return
     */
    private List<AggregationResultItem> convertSubAggResult(Aggregation aggregation) {
        if (Objects.nonNull(aggregation)) {
            if (aggregation instanceof Terms) {
                return convertTermsAggResult(aggregation);
            } else if (aggregation instanceof Nested) {
                return convertNestedAggResult(aggregation);
            }
        }

        return null;
    }

    /**
     * 将 ES Terms聚合结果转化为返回值
     *
     * @param aggregation ES terms聚合结果
     * @return AggregationReuslt
     */
    private List<AggregationResultItem> convertTermsAggResult(Aggregation aggregation) {
        List<AggregationResultItem> termsAggItemList = ((Terms) aggregation).getBuckets().stream().map(bucket -> {
            AggregationResultItem resultItem = new AggregationResultItem(bucket.getKeyAsString(), bucket.getDocCount());
            List<AggregationResultItem> childs =
                    bucket.getAggregations().asList().stream().map(this::convertSubAggResult).findFirst().orElse(null);
            resultItem.setChilds(childs);
            return resultItem;
        }).collect(Collectors.toList());

        return termsAggItemList;
    }

    /**
     * 转化 nested 聚合结果
     *
     * @param aggregation
     * @return
     */
    private List<AggregationResultItem> convertNestedAggResult(Aggregation aggregation) {
        return ((Nested) aggregation).getAggregations().asList().stream().map(this::convertSubAggResult).findFirst().orElse(null);
    }

    /**
     * 存储原始Es 搜索结果
     *
     * @param response 原始搜索结果
     * @return
     */
    private EsSearchResponse addOriginSearchResponse(SearchResponse response) {
        this.originResponse = response;

        return this;
    }

    /**
     * 设置结果总数
     *
     * @param response ES总的搜索结果数
     * @return
     */
    public EsSearchResponse addTotalNum(SearchResponse response) {
        this.total = response.getHits().getTotalHits();
        return this;
    }

    /**
     * 返回空结果
     *
     * @return
     */
    public static EsSearchResponse empty() {
        EsSearchResponse response = new EsSearchResponse();
        response.setTotal(0L);
        response.setData(Collections.emptyList());

        return response;
    }

    /**
     * 根据ES查询返回结果 构建 EsSearchResponse实例
     *
     * @param searchResponse
     * @param resultsMapper
     * @return
     */
    public static EsSearchResponse build(SearchResponse searchResponse, ResultsMapper resultsMapper) {
        return new EsSearchResponse().addQueryResults(searchResponse, resultsMapper).addTotalNum(searchResponse)
                .addAggResults(searchResponse).addOriginSearchResponse(searchResponse);
    }


    /**
     * 根据ES查询返回结果 构建 EsSearchResponse实例
     *
     * @param searchResponse
     * @param resultsMapper
     * @return
     */
    public static EsSearchResponse buildGoods(SearchResponse searchResponse, ResultsMapper resultsMapper) {
        return new EsSearchResponse().addQueryGoodsResults(searchResponse, resultsMapper).addTotalNum(searchResponse)
                .addAggResults(searchResponse).addOriginSearchResponse(searchResponse);
    }

// --------------------------------- static classes ----------------------------------------------

    /**
     * 单个字段的聚合结果
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class AggregationResult extends AggregationResultItem {
        private String name;

        public AggregationResult(String name, List<AggregationResultItem> items) {
            this.name = name;
            this.setChilds(items);
        }
    }

    /**
     * 聚合结果
     *
     * @param <T>
     */
    @Data
    @NoArgsConstructor
    public static class AggregationResultItem<T extends Serializable> implements Serializable {
        /**
         * 聚合结果
         */
        private T key;

        /**
         * 文档数
         */
        private long count;

        /**
         * 子聚合
         */
        private List<AggregationResultItem> childs;

        public AggregationResultItem(T key, long count) {
            this.key = key;
            this.count = count;
        }

        /**
         * 添加子聚合
         *
         * @param child
         */
        public void addChilds(AggregationResultItem child) {
            if (null == child) {
                childs = new ArrayList<>();
            }

            childs.add(child);
        }

    }
}
