package com.soybean.elastic.spu.service;

import com.soybean.elastic.api.enums.SearchSpuNewAggsCategoryEnum;
import com.soybean.elastic.api.enums.SearchSpuNewPriceRangeEnum;
import com.soybean.elastic.api.enums.SearchSpuNewSortTypeEnum;
import com.soybean.elastic.api.req.EsKeyWordSpuNewQueryProviderReq;
import com.soybean.elastic.api.req.EsSortSpuNewQueryProviderReq;
import com.soybean.elastic.api.resp.EsSpuNewAggResp;
import com.soybean.elastic.api.resp.EsSpuNewResp;
import com.soybean.elastic.collect.factory.AbstractCollectFactory;
import com.soybean.elastic.constant.ConstantMultiMatchField;
import com.soybean.elastic.spu.model.EsSpuNew;
import com.wanmi.sbc.setting.api.constant.SearchWeightConstant;
import com.wanmi.sbc.setting.api.response.search.SearchWeightResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.NestedSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

/**
 * Description: 搜索商品信息,只提供对外的搜索词信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/9 5:15 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
@Slf4j
public class EsSpuNewService extends AbstractEsSpuNewService{



    /**
     * 设置搜索的条件信息, 这里是首页关键词搜索功能使用，普通内部搜索请使用 {@link EsNormalSpuNewService}
     *
     * @param req
     * @return
     */
    private BoolQueryBuilder packageKeyWordQueryCondition(EsKeyWordSpuNewQueryProviderReq req) {


        BoolQueryBuilder boolQb = super.packageEsSpuNewReq(req);

        if (req.getSearchSpuNewCategory() != null) {
            boolQb.must(termQuery("spuCategory", req.getSearchSpuNewCategory()));
        }

        //不展示spu信息
        if (CollectionUtils.isNotEmpty(req.getUnSpuIds())) {
            if (req.getUnSpuIds().size() > 1) {
                boolQb.mustNot(termsQuery(ConstantMultiMatchField.FIELD_SPU_SPUId, req.getUnSpuIds()));
            } else {
                boolQb.mustNot(termQuery(ConstantMultiMatchField.FIELD_SPU_SPUId, req.getUnSpuIds().get(0)));
            }
        }

        //指定spu信息
        if (CollectionUtils.isNotEmpty(req.getSpuIds())) {
            if (req.getSpuIds().size() > 1) {
                boolQb.must(termsQuery(ConstantMultiMatchField.FIELD_SPU_SPUId, req.getSpuIds()));
            } else {
                boolQb.must(termQuery(ConstantMultiMatchField.FIELD_SPU_SPUId, req.getSpuIds().get(0)));
            }
        }

        //传递值，0表示非知识顾问商品可以访问
        if (req.getCpsSpecial() != null && Objects.equals(req.getCpsSpecial(), 0)) {
            boolQb.must(termQuery(ConstantMultiMatchField.FIELD_SPU_CPSSPECIAL, 0));
        }

        /**
         * 标签类别
         */
        if (CollectionUtils.isNotEmpty(req.getLabelCategorys())) {
            if (req.getLabelCategorys().size() > 1) {
                boolQb.must(nestedQuery("labels", termsQuery("labels.category", req.getLabelCategorys()), ScoreMode.None));
            } else {
                boolQb.must(nestedQuery("labels", termQuery("labels.category", req.getLabelCategorys().get(0)), ScoreMode.None));
            }
        }

        /**
         * 拼接参数信息
         */
        req.setAggsReqs(req.getAggsReqs() == null ? new ArrayList<>() : req.getAggsReqs());
        for (EsKeyWordSpuNewQueryProviderReq.AggsCategoryReq aggsReq : req.getAggsReqs()) {
            Integer category = aggsReq.getCategory();
            SearchSpuNewAggsCategoryEnum searchSpuNewAggsCategoryEnum = SearchSpuNewAggsCategoryEnum.get(category);
            List<EsKeyWordSpuNewQueryProviderReq.AggsReq> aggsList = aggsReq.getAggsList();
            if (CollectionUtils.isEmpty(aggsList)) {
                continue;
            }

            List<String> aggsNameList = aggsList.stream().map(EsKeyWordSpuNewQueryProviderReq.AggsReq::getAggsName).filter(StringUtils::isNotBlank).collect(Collectors.toList());
            List<String> aggsIdList = aggsList.stream().map(EsKeyWordSpuNewQueryProviderReq.AggsReq::getAggsId).filter(StringUtils::isNotBlank).collect(Collectors.toList());

            /**
             * 商品类别
             */
            if (Objects.equals(SearchSpuNewAggsCategoryEnum.AGGS_SPU_CATEGORY, searchSpuNewAggsCategoryEnum)) {

                if (aggsIdList.size() > 1) {
                    boolQb.must(termsQuery("spuCategory", aggsIdList));
                } else {
                    boolQb.must(termQuery("spuCategory", aggsIdList.get(0)));
                }
            }

            /**
             * 价格范围
             */
            if (Objects.equals(SearchSpuNewAggsCategoryEnum.AGGS_PRICE_RANGE, searchSpuNewAggsCategoryEnum)) {

                int from = 9999;
                int to = 0;
                for (String aggsId : aggsIdList) {
                    SearchSpuNewPriceRangeEnum searchSpuNewPriceRangeEnum = SearchSpuNewPriceRangeEnum.get(Integer.valueOf(aggsId));
                    if (searchSpuNewPriceRangeEnum == null) {
                        continue;
                    }
                    from = Math.min(from, searchSpuNewPriceRangeEnum.getFrom());
                    to = Math.max(to, searchSpuNewPriceRangeEnum.getTo());
                }

                if (to > from) {
                    RangeQueryBuilder salesPrice = QueryBuilders.rangeQuery("salesPrice");
                    salesPrice.gte(from);
                    salesPrice.lte(to);
                    boolQb.must(salesPrice);
                }
            }
            /**
             * 标签类别
             */
            if (Objects.equals(SearchSpuNewAggsCategoryEnum.AGGS_LABEL, searchSpuNewAggsCategoryEnum)) {
                if (aggsIdList.size() > 1) {
                    boolQb.must(nestedQuery("labels", termsQuery("labels.category", aggsIdList), ScoreMode.None));
                } else {
                    boolQb.must(nestedQuery("labels", termQuery("labels.category", aggsIdList.get(0)), ScoreMode.None));
                }
            }
            /**
             * 店铺类别
             */
            if (Objects.equals(SearchSpuNewAggsCategoryEnum.AGGS_FCLASSIFY, searchSpuNewAggsCategoryEnum)) {

                if (aggsNameList.size() > 1) {
                    boolQb.must(termsQuery("classify.fclassifyName", aggsNameList));
                } else {
                    boolQb.must(termQuery("classify.fclassifyName", aggsNameList.get(0)));
                }

//                if (aggsList.size() > 1) {
//                    boolQb.must(termsQuery("classify.fclassifyId", aggsList));
//                } else {
//                    boolQb.must(termQuery("classify.fclassifyId", aggsList.get(0)));
//                }
            }

            /**
             * 作者
             */
            if (Objects.equals(SearchSpuNewAggsCategoryEnum.AGGS_AUTHOR, searchSpuNewAggsCategoryEnum)) {

                if (aggsNameList.size() > 1) {
                    boolQb.must(nestedQuery("book", termsQuery("book.authorNames", aggsNameList), ScoreMode.None));
                } else {
                    boolQb.must(nestedQuery("book", termQuery("book.authorNames", aggsNameList.get(0)), ScoreMode.None));
                }
            }

            /**
             * 出版社
             */
            if (Objects.equals(SearchSpuNewAggsCategoryEnum.AGGS_PUBLISHER, searchSpuNewAggsCategoryEnum)) {

                if (aggsNameList.size() > 1) {
                    boolQb.must(nestedQuery("book", termsQuery("book.publisher.keyword", aggsNameList), ScoreMode.None));
                } else {
                    boolQb.must(nestedQuery("book", termQuery("book.publisher.keyword", aggsNameList.get(0)), ScoreMode.None));
                }
            }

            /**
             * 奖项
             */
            if (Objects.equals(SearchSpuNewAggsCategoryEnum.AGGS_AWARD, searchSpuNewAggsCategoryEnum)) {

                if (aggsNameList.size() > 1) {
                    boolQb.must(nestedQuery("book", termsQuery("book.awards.awardName.keyword", aggsNameList), ScoreMode.None));
                } else {
                    boolQb.must(nestedQuery("book", termQuery("book.awards.awardName.keyword", aggsNameList.get(0)), ScoreMode.None));
                }
            }

            /**
             * 从书
             */
            if (Objects.equals(SearchSpuNewAggsCategoryEnum.AGGS_CLUMP, searchSpuNewAggsCategoryEnum)) {

                if (aggsNameList.size() > 1) {
                    boolQb.must(nestedQuery("book", termsQuery("book.clumpName.keyword", aggsNameList), ScoreMode.None));
                } else {
                    boolQb.must(nestedQuery("book", termQuery("book.clumpName.keyword", aggsNameList.get(0)), ScoreMode.None));
                }
            }

            /**
             * 从书
             */
            if (Objects.equals(SearchSpuNewAggsCategoryEnum.AGGS_PRODUCER, searchSpuNewAggsCategoryEnum)) {
                if (aggsNameList.size() > 1) {
                    boolQb.must(nestedQuery("book", termsQuery("book.producer.keyword", aggsNameList), ScoreMode.None));
                } else {
                    boolQb.must(nestedQuery("book", termQuery("book.producer.keyword", aggsNameList.get(0)), ScoreMode.None));
                }
            }
        }


        //如果没有关键词，则直接返回查询条件数据
        if (StringUtils.isBlank(req.getKeyword())) {
            return boolQb;
        }
        //查询条件
        req.setKeyword(QueryParser.escape(req.getKeyword()));
        BoolQueryBuilder boolQbChild = QueryBuilders.boolQuery();
        boolQbChild.should().add(matchQuery(ConstantMultiMatchField.FIELD_SPU_SPUNAME, req.getKeyword()).minimumShouldMatch(ConstantMultiMatchField.FIELD_MINIMUM_SHOULD_MATCH));
        boolQbChild.should().add(matchQuery(ConstantMultiMatchField.FIELD_SPU_SPUNAME_KEYWORD, req.getKeyword()).minimumShouldMatch(ConstantMultiMatchField.FIELD_MINIMUM_SHOULD_MATCH));
        boolQbChild.should().add(matchQuery(ConstantMultiMatchField.FIELD_SPU_SPUSUBNAME, req.getKeyword()).minimumShouldMatch(ConstantMultiMatchField.FIELD_MINIMUM_SHOULD_MATCH));
        boolQbChild.should().add(matchQuery(ConstantMultiMatchField.FIELD_SPU_ANCHORRECOMS_RECOMNAME, req.getKeyword()).minimumShouldMatch(ConstantMultiMatchField.FIELD_MINIMUM_SHOULD_MATCH));
        boolQbChild.should().add(matchQuery(ConstantMultiMatchField.FIELD_SPU_CLASSIFY_CLASSIFYNAME, req.getKeyword()).minimumShouldMatch(ConstantMultiMatchField.FIELD_MINIMUM_SHOULD_MATCH));
        boolQbChild.should().add(matchQuery(ConstantMultiMatchField.FIELD_SPU_CLASSIFY_FCLASSIFYNAME, req.getKeyword()).minimumShouldMatch(ConstantMultiMatchField.FIELD_MINIMUM_SHOULD_MATCH));
//        boolQbChild.should().add(matchQuery(ConstantMultiMatchField.FIELD_SPU_CLASSIFY_FCLASSIFYNAME, req.getKeyword()).minimumShouldMatch(ConstantMultiMatchField.FIELD_MINIMUM_SHOULD_MATCH));


        boolQbChild.should().add(nestedQuery("book", matchQuery(ConstantMultiMatchField.FIELD_SPU_BOOK_BOOKNAME, req.getKeyword()).minimumShouldMatch(ConstantMultiMatchField.FIELD_MINIMUM_SHOULD_MATCH), ScoreMode.None));
        boolQbChild.should().add(nestedQuery("book", matchQuery(ConstantMultiMatchField.FIELD_SPU_BOOK_BOOKNAME_KEYWORD, req.getKeyword()).minimumShouldMatch(ConstantMultiMatchField.FIELD_MINIMUM_SHOULD_MATCH), ScoreMode.None));
        boolQbChild.should().add(nestedQuery("book", matchQuery(ConstantMultiMatchField.FIELD_SPU_BOOK_BOOKORIGINNAME, req.getKeyword()).minimumShouldMatch(ConstantMultiMatchField.FIELD_MINIMUM_SHOULD_MATCH), ScoreMode.None));
        boolQbChild.should().add(nestedQuery("book", matchQuery(ConstantMultiMatchField.FIELD_SPU_BOOK_BOOKDESC, req.getKeyword()).minimumShouldMatch(ConstantMultiMatchField.FIELD_MINIMUM_SHOULD_MATCH), ScoreMode.None));
        boolQbChild.should().add(nestedQuery("book", matchQuery(ConstantMultiMatchField.FIELD_SPU_BOOK_AUTHORNAMES, req.getKeyword()).minimumShouldMatch(ConstantMultiMatchField.FIELD_MINIMUM_SHOULD_MATCH), ScoreMode.None));
        boolQbChild.should().add(nestedQuery("book", matchQuery(ConstantMultiMatchField.FIELD_SPU_BOOK_PUBLISHER, req.getKeyword()).minimumShouldMatch(ConstantMultiMatchField.FIELD_MINIMUM_SHOULD_MATCH), ScoreMode.None));
        boolQbChild.should().add(nestedQuery("book", matchQuery(ConstantMultiMatchField.FIELD_SPU_BOOK_PUBLISHER_KEYWORD, req.getKeyword()).minimumShouldMatch(ConstantMultiMatchField.FIELD_MINIMUM_SHOULD_MATCH), ScoreMode.None));
        boolQbChild.should().add(nestedQuery("book", matchQuery(ConstantMultiMatchField.FIELD_SPU_BOOK_PRODUCER, req.getKeyword()).minimumShouldMatch(ConstantMultiMatchField.FIELD_MINIMUM_SHOULD_MATCH), ScoreMode.None));
        boolQbChild.should().add(nestedQuery("book", matchQuery(ConstantMultiMatchField.FIELD_SPU_BOOK_CLUMPNAME, req.getKeyword()).minimumShouldMatch(ConstantMultiMatchField.FIELD_MINIMUM_SHOULD_MATCH), ScoreMode.None));
        boolQbChild.should().add(nestedQuery("book", matchQuery(ConstantMultiMatchField.FIELD_SPU_BOOK_AWARDS_AWARDNAME, req.getKeyword()).minimumShouldMatch(ConstantMultiMatchField.FIELD_MINIMUM_SHOULD_MATCH), ScoreMode.None));
        boolQbChild.should().add(nestedQuery("book", matchQuery(ConstantMultiMatchField.FIELD_SPU_BOOK_GROUPNAME, req.getKeyword()).minimumShouldMatch(ConstantMultiMatchField.FIELD_MINIMUM_SHOULD_MATCH), ScoreMode.None));
        boolQbChild.should().add(nestedQuery("book", matchQuery(ConstantMultiMatchField.FIELD_SPU_BOOK_BINDINGNAME, req.getKeyword()).minimumShouldMatch(ConstantMultiMatchField.FIELD_MINIMUM_SHOULD_MATCH), ScoreMode.None));
        boolQbChild.should().add(nestedQuery("book", matchQuery(ConstantMultiMatchField.FIELD_SPU_BOOK_TAGS_TAGNAME, req.getKeyword()).minimumShouldMatch(ConstantMultiMatchField.FIELD_MINIMUM_SHOULD_MATCH), ScoreMode.None));
        boolQbChild.should().add(nestedQuery("book", matchQuery(ConstantMultiMatchField.FIELD_SPU_BOOK_TAGS_STAGNAME, req.getKeyword()).minimumShouldMatch(ConstantMultiMatchField.FIELD_MINIMUM_SHOULD_MATCH), ScoreMode.None));

        //标签
        boolQbChild.should().add(nestedQuery("labels", matchQuery(ConstantMultiMatchField.FIELD_SPU_LABEL_NAME, req.getKeyword()).minimumShouldMatch(ConstantMultiMatchField.FIELD_MINIMUM_SHOULD_MATCH), ScoreMode.None));
        boolQbChild.should().add(nestedQuery("labels", matchQuery(ConstantMultiMatchField.FIELD_SPU_LABEL_NAME_KEYWORD, req.getKeyword()).minimumShouldMatch(ConstantMultiMatchField.FIELD_MINIMUM_SHOULD_MATCH), ScoreMode.None));
        boolQb.must(boolQbChild);
        return boolQb;
    }

    /**
     * 自定义权重
     * @param req
     * @return
     */
    private FunctionScoreQueryBuilder.FilterFunctionBuilder[] filterFunctionBuilder(EsKeyWordSpuNewQueryProviderReq req) {
        if (StringUtils.isBlank(req.getKeyword())) {
            return new FunctionScoreQueryBuilder.FilterFunctionBuilder[0];
        }
        float defaultBoost = 1f;
        List<SearchWeightResp> context = searchWeightProvider.list(SearchWeightConstant.SPU_SEARCH_WEIGHT_KEY).getContext();
        Map<String, Float> searchWeightMap = new HashMap<>();
        for (SearchWeightResp searchWeightResp : context) {
            if (StringUtils.isEmpty(searchWeightResp.getWeightValue())) {
                continue;
            }
            searchWeightMap.put(searchWeightResp.getWeightKey(), Float.parseFloat(searchWeightResp.getWeightValue()));
        }

        FunctionScoreQueryBuilder.FilterFunctionBuilder[] arr = new FunctionScoreQueryBuilder.FilterFunctionBuilder[]{
                new FunctionScoreQueryBuilder
                        .FilterFunctionBuilder(QueryBuilders.matchQuery(ConstantMultiMatchField.FIELD_SPU_SPUNAME, req.getKeyword())
                        , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.SPU_NAME, defaultBoost))),
                new FunctionScoreQueryBuilder
                        .FilterFunctionBuilder(QueryBuilders.matchQuery(ConstantMultiMatchField.FIELD_SPU_SPUNAME_KEYWORD, req.getKeyword())
                        , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.SPU_DIM_NAME, defaultBoost))),
                new FunctionScoreQueryBuilder
                        .FilterFunctionBuilder(QueryBuilders.matchQuery(ConstantMultiMatchField.FIELD_SPU_SPUSUBNAME, req.getKeyword())
                        , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.SPU_SUB_NAME, defaultBoost))),
                new FunctionScoreQueryBuilder
                        .FilterFunctionBuilder(QueryBuilders.matchQuery(ConstantMultiMatchField.FIELD_SPU_ANCHORRECOMS_RECOMNAME
                        , req.getKeyword()), ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.SPU_ANCHOR_RECOM, defaultBoost))),
                new FunctionScoreQueryBuilder
                        .FilterFunctionBuilder(QueryBuilders.matchQuery(ConstantMultiMatchField.FIELD_SPU_CLASSIFY_CLASSIFYNAME, req.getKeyword())
                        , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.SPU_CLASSY_NAME, defaultBoost))),
                new FunctionScoreQueryBuilder
                        .FilterFunctionBuilder(QueryBuilders.matchQuery(ConstantMultiMatchField.FIELD_SPU_CLASSIFY_FCLASSIFYNAME, req.getKeyword())
                        , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.SPU_CLASSY_SECOND_NAME, defaultBoost))),
                new FunctionScoreQueryBuilder
                        .FilterFunctionBuilder(QueryBuilders.nestedQuery("book", QueryBuilders.matchQuery(ConstantMultiMatchField.FIELD_SPU_BOOK_BOOKNAME,req.getKeyword()), ScoreMode.None)
                        , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_NAME, defaultBoost))),
                new FunctionScoreQueryBuilder
                        .FilterFunctionBuilder(QueryBuilders.nestedQuery("book", QueryBuilders.matchQuery(ConstantMultiMatchField.FIELD_SPU_BOOK_BOOKNAME_KEYWORD,req.getKeyword()), ScoreMode.None)
                        , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_DIM_NAME, defaultBoost))),
                new FunctionScoreQueryBuilder
                        .FilterFunctionBuilder(QueryBuilders.nestedQuery("book", QueryBuilders.matchQuery(ConstantMultiMatchField.FIELD_SPU_BOOK_BOOKORIGINNAME,req.getKeyword()), ScoreMode.None)
                        , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_ORIGIN_NAME, defaultBoost))),
                new FunctionScoreQueryBuilder
                        .FilterFunctionBuilder(QueryBuilders.nestedQuery("book", QueryBuilders.matchQuery(ConstantMultiMatchField.FIELD_SPU_BOOK_BOOKDESC,req.getKeyword()), ScoreMode.None)
                        , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_DESC, defaultBoost))),
                new FunctionScoreQueryBuilder
                        .FilterFunctionBuilder(QueryBuilders.nestedQuery("book", QueryBuilders.matchQuery(ConstantMultiMatchField.FIELD_SPU_BOOK_AUTHORNAMES,req.getKeyword()), ScoreMode.None)
                        , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_AUTHOR_NAME, defaultBoost))),
                new FunctionScoreQueryBuilder
                        .FilterFunctionBuilder(QueryBuilders.nestedQuery("book", QueryBuilders.matchQuery(ConstantMultiMatchField.FIELD_SPU_BOOK_PUBLISHER,req.getKeyword()), ScoreMode.None)
                        , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_PUBLISHER, defaultBoost))),
                new FunctionScoreQueryBuilder
                        .FilterFunctionBuilder(QueryBuilders.nestedQuery("book", QueryBuilders.matchQuery(ConstantMultiMatchField.FIELD_SPU_BOOK_PUBLISHER_KEYWORD,req.getKeyword()), ScoreMode.None)
                        , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_DIM_PUBLISHER, defaultBoost))),
                new FunctionScoreQueryBuilder
                        .FilterFunctionBuilder(QueryBuilders.nestedQuery("book", QueryBuilders.matchQuery(ConstantMultiMatchField.FIELD_SPU_BOOK_PRODUCER,req.getKeyword()), ScoreMode.None)
                        , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_PRODUCER, defaultBoost))),
                new FunctionScoreQueryBuilder
                        .FilterFunctionBuilder(QueryBuilders.nestedQuery("book", QueryBuilders.matchQuery(ConstantMultiMatchField.FIELD_SPU_BOOK_CLUMPNAME,req.getKeyword()), ScoreMode.None)
                        , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_CLUMP_NAME, defaultBoost))),
                new FunctionScoreQueryBuilder
                        .FilterFunctionBuilder(QueryBuilders.nestedQuery("book", QueryBuilders.matchQuery(ConstantMultiMatchField.FIELD_SPU_BOOK_AWARDS_AWARDNAME,req.getKeyword()), ScoreMode.None)
                        , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_AWARDS_NAME, defaultBoost))),
                new FunctionScoreQueryBuilder
                        .FilterFunctionBuilder(QueryBuilders.nestedQuery("book", QueryBuilders.matchQuery(ConstantMultiMatchField.FIELD_SPU_BOOK_GROUPNAME,req.getKeyword()), ScoreMode.None)
                        , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_GROUP_NAME, defaultBoost))),
                new FunctionScoreQueryBuilder
                        .FilterFunctionBuilder(QueryBuilders.nestedQuery("book", QueryBuilders.matchQuery(ConstantMultiMatchField.FIELD_SPU_BOOK_BINDINGNAME,req.getKeyword()), ScoreMode.None)
                        , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_BINDING_NAME, defaultBoost))),
                new FunctionScoreQueryBuilder
                        .FilterFunctionBuilder(QueryBuilders.nestedQuery("book", QueryBuilders.matchQuery(ConstantMultiMatchField.FIELD_SPU_BOOK_TAGS_TAGNAME,req.getKeyword()), ScoreMode.None)
                        , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_TAG_NAME, defaultBoost))),
                new FunctionScoreQueryBuilder
                        .FilterFunctionBuilder(QueryBuilders.nestedQuery("book", QueryBuilders.matchQuery(ConstantMultiMatchField.FIELD_SPU_BOOK_TAGS_STAGNAME,req.getKeyword()), ScoreMode.None)
                        , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_SECOND_TAG_NAME, defaultBoost))),

                new FunctionScoreQueryBuilder
                        .FilterFunctionBuilder(QueryBuilders.nestedQuery("labels", QueryBuilders.matchQuery(ConstantMultiMatchField.FIELD_SPU_LABEL_NAME,req.getKeyword()), ScoreMode.None)
                        , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.SPU_LABEL_NAME, defaultBoost))),
                new FunctionScoreQueryBuilder
                        .FilterFunctionBuilder(QueryBuilders.nestedQuery("labels", QueryBuilders.matchQuery(ConstantMultiMatchField.FIELD_SPU_LABEL_NAME_KEYWORD,req.getKeyword()), ScoreMode.None)
                        , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.SPU_LABEL_DIM_NAME, defaultBoost)))
        };
        return arr;
    }


    /**
     * 设置排序
     */
    private List<FieldSortBuilder> packageSort(EsSortSpuNewQueryProviderReq sortQueryProviderReq) {
        List<FieldSortBuilder> fieldSortBuilders = new ArrayList<>();
        if (sortQueryProviderReq.getSpuSortType() == null) {
            return fieldSortBuilders;
        }
        FieldSortBuilder order = null;
        if (Objects.equals(sortQueryProviderReq.getSpuSortType(), SearchSpuNewSortTypeEnum.DEFAULT.getCode())) {
//            order = new FieldSortBuilder("_score").order(SortOrder.DESC);

        } else if (Objects.equals(sortQueryProviderReq.getSpuSortType(), SearchSpuNewSortTypeEnum.SCORE.getCode())) {
            order = new FieldSortBuilder("book.score").order(SortOrder.DESC).setNestedSort(new NestedSortBuilder("book"));
        } else if (Objects.equals(sortQueryProviderReq.getSpuSortType(), SearchSpuNewSortTypeEnum.NEW_ADDED.getCode())) {
            order = new FieldSortBuilder("addedTime").order(SortOrder.DESC);
        } else if (Objects.equals(sortQueryProviderReq.getSpuSortType(), SearchSpuNewSortTypeEnum.FAVOR_COMMENT.getCode())) {
            order = new FieldSortBuilder("comment.goodEvaluateRatio").order(SortOrder.DESC).setNestedSort(new NestedSortBuilder("comment"));
        } else if (Objects.equals(sortQueryProviderReq.getSpuSortType(), SearchSpuNewSortTypeEnum.HIGH_PRICE.getCode())) {
            order = new FieldSortBuilder("salesPrice").order(SortOrder.DESC);
        } else if (Objects.equals(sortQueryProviderReq.getSpuSortType(), SearchSpuNewSortTypeEnum.LOWER_PRICE.getCode())) {
            order = new FieldSortBuilder("salesPrice").order(SortOrder.ASC);
        }
        if (order != null) {
            fieldSortBuilders.add(order);
            FieldSortBuilder order1 = new FieldSortBuilder("salesNum").order(SortOrder.DESC);
            FieldSortBuilder order2 = new FieldSortBuilder("salesPrice").order(SortOrder.DESC);
            FieldSortBuilder order3 = new FieldSortBuilder("addedTime").order(SortOrder.DESC);
            fieldSortBuilders.addAll(Arrays.asList(order1, order2, order3));
        }
        return fieldSortBuilders;
    }


    /**
     * 聚合结果
     * @return
     */
    private List<AbstractAggregationBuilder> packageAggregations(/*List<SearchAggsResp> context*/) {
        List<AbstractAggregationBuilder> aggregationBuilderList = new ArrayList<>();

//        for (SearchAggsResp searchAggsResp : context) {
//            if (Objects.equals(searchAggsResp.getAggsKey(), SearchAggsConstant.SPU_SEARCH_AGGS_LABEL_CATEGORY_KEY)) {
//                NestedAggregationBuilder nestedAggregationBuilder =
//                        AggregationBuilders.nested("labels", "labels").subAggregation(AggregationBuilders.terms("labelCategory").field("labels.category"));
//                aggregationBuilderList.add(nestedAggregationBuilder);
//            }
//
//            if (Objects.equals(searchAggsResp.getAggsKey(), SearchAggsConstant.SPU_SEARCH_AGGS_FCLASSIFY_NAME_KEY)) {
//                TermsAggregationBuilder fclassifyName = AggregationBuilders.terms("fclassifyName").field("classify.fclassifyName");
//                aggregationBuilderList.add(fclassifyName);
//            }
//
//            if (Objects.equals(searchAggsResp.getAggsKey(), SearchAggsConstant.BOOK_SEARCH_AGGS_AUTHOR_NAMES_KEY)
//                || Objects.equals(searchAggsResp.getAggsKey(), SearchAggsConstant.BOOK_SEARCH_AGGS_PUBLISHER_KEY)
//                || Objects.equals(searchAggsResp.getAggsKey(), SearchAggsConstant.BOOK_SEARCH_AGGS_AWARD_NAME_KEY)
//                || Objects.equals(searchAggsResp.getAggsKey(), SearchAggsConstant.BOOK_SEARCH_AGGS_CLUMP_NAME_KEY)) {
//                NestedAggregationBuilder nested = AggregationBuilders.nested("book", "book");
//                if (Objects.equals(searchAggsResp.getAggsKey(), SearchAggsConstant.BOOK_SEARCH_AGGS_AUTHOR_NAMES_KEY)) {
//                    nested.subAggregation(AggregationBuilders.terms("authorName").field("book.authorNames"));
//                }
//                if (Objects.equals(searchAggsResp.getAggsKey(), SearchAggsConstant.BOOK_SEARCH_AGGS_PUBLISHER_KEY)) {
//                    nested.subAggregation(AggregationBuilders.terms("publisherName").field("book.publisher.keyword"));
//                }
//                if (Objects.equals(searchAggsResp.getAggsKey(), SearchAggsConstant.BOOK_SEARCH_AGGS_AWARD_NAME_KEY)) {
//                    nested.subAggregation(AggregationBuilders.terms("awardName").field("book.awards.awardName.keyword"));
//                }
//                if (Objects.equals(searchAggsResp.getAggsKey(), SearchAggsConstant.BOOK_SEARCH_AGGS_CLUMP_NAME_KEY)) {
//                    nested.subAggregation(AggregationBuilders.terms("clumpName").field("book.clumpName.keyword"));
//                }
//                aggregationBuilderList.add(nested);
//            }
//        }
        NestedAggregationBuilder nestedAggregationBuilder =
                        AggregationBuilders.nested("labels", "labels").subAggregation(AggregationBuilders.terms("labelCategory").field("labels.category"));
        aggregationBuilderList.add(nestedAggregationBuilder);

        TermsAggregationBuilder fclassifyName = AggregationBuilders.terms("fclassifyName").field("classify.fclassifyName");
        aggregationBuilderList.add(fclassifyName);

        TermsAggregationBuilder spuCategory = AggregationBuilders.terms("spuCategory").field("spuCategory");
        aggregationBuilderList.add(spuCategory);

        NestedAggregationBuilder book =
                AggregationBuilders.nested("book", "book")
                        .subAggregation(AggregationBuilders.terms("authorName").field("book.authorNames"))
                        .subAggregation(AggregationBuilders.terms("publisherName").field("book.publisher.keyword"))
                        .subAggregation(AggregationBuilders.terms("awardName").field("book.awards.awardName.keyword"))
                        .subAggregation(AggregationBuilders.terms("clumpName").field("book.clumpName.keyword"))
                        .subAggregation(AggregationBuilders.terms("producerName").field("book.producer.keyword"))
                        .subAggregation(AggregationBuilders.terms("tagName").field("book.tags.tagName"));
        aggregationBuilderList.add(book);

        return aggregationBuilderList;
    }

    /**
     * 关键词搜索
     * @param req
     * @return
     */
    public EsSpuNewAggResp<List<EsSpuNewResp>> listKeyWorldEsSpu(EsKeyWordSpuNewQueryProviderReq req) {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withIndices(AbstractCollectFactory.INDEX_ES_SPU_NEW);

        //分页 从0开始
        req.setPageNum(Math.max((req.getPageNum() - 1), 0));
        builder.withPageable(PageRequest.of(req.getPageNum(), req.getPageSize()));

        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(this.packageKeyWordQueryCondition(req), this.filterFunctionBuilder(req)).scoreMode(FunctionScoreQuery.ScoreMode.MULTIPLY);


        //查询 条件
        builder.withQuery(functionScoreQueryBuilder);

        //聚合
        for (AbstractAggregationBuilder packageAggregation : this.packageAggregations(/*context*/)) {
            builder.addAggregation(packageAggregation);
        }

        //排序
        for (FieldSortBuilder fieldSortBuilder : this.packageSort(req)) {
            builder.withSort(fieldSortBuilder);
        }



        NativeSearchQuery build = builder.build();
        log.info("--->>> EsBookListModelService.listKeyWorldEsSpu DSL: {}", build.getQuery().toString());
        AggregatedPage<EsSpuNew> resultQueryPage = elasticsearchTemplate.queryForPage(build, EsSpuNew.class);

//        return new CommonPageResp<>(resultQueryPage.getTotalElements(), this.packageEsSpuNewResp(resultQueryPage.getContent()));
        return super.packageEsSpuNewAggResp(resultQueryPage, req);
    }


}