package com.soybean.elastic.booklistmodel.service;


import com.soybean.elastic.api.enums.SearchBookListSortTypeEnum;
import com.soybean.elastic.api.req.EsBookListQueryProviderReq;
import com.soybean.elastic.api.req.EsKeyWordBookListQueryProviderReq;
import com.soybean.elastic.api.req.EsSortBookListQueryProviderReq;
import com.soybean.elastic.api.resp.EsBookListModelResp;
import com.soybean.common.resp.CommonPageResp;
import com.soybean.elastic.booklistmodel.model.EsBookListModel;
import com.soybean.elastic.collect.factory.AbstractCollectFactory;
import com.soybean.elastic.constant.ConstantMultiMatchField;
import com.wanmi.sbc.goods.api.provider.booklistmodel.BookListModelProvider;
import com.wanmi.sbc.setting.api.constant.SearchWeightConstant;
import com.wanmi.sbc.setting.api.provider.search.SearchWeightProvider;
import com.wanmi.sbc.setting.api.provider.topic.TopicConfigProvider;
import com.wanmi.sbc.setting.api.request.RankRelResponse;
import com.wanmi.sbc.setting.api.response.search.SearchWeightResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/2 4:26 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
@Slf4j
public class EsBookListModelService {


    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private SearchWeightProvider searchWeightProvider;

    @Autowired
    private TopicConfigProvider topicConfigProvider;

    /**
     * 设置搜索的条件信息 这个是关键词的搜索
     * @param esKeyWordQueryProviderReq
     * @return
     */
    private BoolQueryBuilder packageKeyWordQueryCondition(EsKeyWordBookListQueryProviderReq esKeyWordQueryProviderReq) {


        //查询条件
        BoolQueryBuilder boolQb = QueryBuilders.boolQuery();
        boolQb.must(termQuery("delFlag", esKeyWordQueryProviderReq.getDelFlag()));
        boolQb.must(termQuery("bookListCategory", esKeyWordQueryProviderReq.getSearchBookListCategory()));

        BoolQueryBuilder boolQbChild = QueryBuilders.boolQuery();

        boolQbChild.should().add(matchQuery(ConstantMultiMatchField.FIELD_BOOK_LIST_BOOKLISTNAME, esKeyWordQueryProviderReq.getKeyword()).minimumShouldMatch(ConstantMultiMatchField.FIELD_MINIMUM_SHOULD_MATCH));
        boolQbChild.should().add(nestedQuery("spus", matchQuery(ConstantMultiMatchField.FIELD_BOOK_LIST_SPU_SPUNAME, esKeyWordQueryProviderReq.getKeyword()).minimumShouldMatch(ConstantMultiMatchField.FIELD_MINIMUM_SHOULD_MATCH), ScoreMode.None));
        boolQbChild.should().add(nestedQuery("spus", matchQuery(ConstantMultiMatchField.FIELD_BOOK_LIST_SPU_SPUNAME_KEYWORD, esKeyWordQueryProviderReq.getKeyword()).minimumShouldMatch(ConstantMultiMatchField.FIELD_MINIMUM_SHOULD_MATCH), ScoreMode.None));
        boolQb.must(boolQbChild);
//
//
//        boolQb.must(QueryBuilders.boolQuery()
//                .filter(QueryBuilders.boolQuery()
//                        .should(matchQuery("bookListName", esKeyWordQueryProviderReq.getKeyword()).
//                                boost(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_LIST_SEARCH_WEIGHT_BOOK_LIST_NAME, defaultBoost)))
//                        .should(nestedQuery("spus", matchQuery("spus.spuName", esKeyWordQueryProviderReq.getKeyword())
//                                .boost(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_LIST_SEARCH_WEIGHT_SPU_NAME, defaultBoost)), ScoreMode.None))
//                        .should(nestedQuery("spus", matchQuery("spus.spuName.keyword", esKeyWordQueryProviderReq.getKeyword())
//                                .boost(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_LIST_SEARCH_WEIGHT_SPU_DIM_NAME, defaultBoost)), ScoreMode.None))));

        return boolQb;
    }

    /**
     * 自定义权重
     * @param req
     * @return
     */
    private FunctionScoreQueryBuilder.FilterFunctionBuilder[] filterFunctionBuilder(EsKeyWordBookListQueryProviderReq req) {
        float defaultBoost = 1f;
        List<SearchWeightResp> context = searchWeightProvider.list(SearchWeightConstant.BOOK_LIST_SEARCH_WEIGHT_KEY).getContext();
        Map<String, Float> searchWeightMap = new HashMap<>();
        for (SearchWeightResp searchWeightResp : context) {
            if (StringUtils.isEmpty(searchWeightResp.getWeightValue())) {
                continue;
            }
            searchWeightMap.put(searchWeightResp.getWeightKey(), Float.parseFloat(searchWeightResp.getWeightValue()));
        }
        FunctionScoreQueryBuilder.FilterFunctionBuilder[] arr = new FunctionScoreQueryBuilder.FilterFunctionBuilder[]{
                new FunctionScoreQueryBuilder
                        .FilterFunctionBuilder(QueryBuilders.matchQuery(ConstantMultiMatchField.FIELD_BOOK_LIST_BOOKLISTNAME, req.getKeyword())
                        , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_LIST_SEARCH_WEIGHT_BOOK_LIST_NAME, defaultBoost))),
                new FunctionScoreQueryBuilder
                        .FilterFunctionBuilder(QueryBuilders.nestedQuery("spus", QueryBuilders.matchQuery(ConstantMultiMatchField.FIELD_BOOK_LIST_SPU_SPUNAME,req.getKeyword()), ScoreMode.None)
                        , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_LIST_SEARCH_WEIGHT_SPU_NAME, defaultBoost))),
                new FunctionScoreQueryBuilder
                        .FilterFunctionBuilder(QueryBuilders.nestedQuery("spus", QueryBuilders.matchQuery(ConstantMultiMatchField.FIELD_BOOK_LIST_SPU_SPUNAME_KEYWORD,req.getKeyword()), ScoreMode.None)
                        , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_LIST_SEARCH_WEIGHT_SPU_DIM_NAME, defaultBoost)))
        };
        return arr;
    }

    /**
     * 设置排序
     */
    private List<FieldSortBuilder> packageSort(EsSortBookListQueryProviderReq sortQueryProviderReq) {
        List<FieldSortBuilder> fieldSortBuilders = new ArrayList<>();
        if (sortQueryProviderReq.getBooklistSortType() == null) {
            return fieldSortBuilders;
        }
        if (sortQueryProviderReq.getBooklistSortType() == SearchBookListSortTypeEnum.UPDATE_TIME.getCode()) {
            FieldSortBuilder order = new FieldSortBuilder("updateTime").order(SortOrder.DESC);
            fieldSortBuilders.add(order);
        } else if (sortQueryProviderReq.getBooklistSortType() == SearchBookListSortTypeEnum.HAS_TOP_UPDATE_TIME.getCode()) {
            FieldSortBuilder order1 = new FieldSortBuilder("hasTop").order(SortOrder.DESC);
            FieldSortBuilder order2 = new FieldSortBuilder("updateTime").order(SortOrder.DESC);
            fieldSortBuilders.addAll(Arrays.asList(order1, order2));
        }
        return fieldSortBuilders;
    }

    /**
     * 关键词搜索
     * @param req
     * @return
     */
    public CommonPageResp<List<EsBookListModelResp>> listKeyWorldEsBookListModel(EsKeyWordBookListQueryProviderReq req){

        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withIndices(AbstractCollectFactory.INDEX_ES_BOOK_LIST_MODEL);

        //分页 从0开始
        req.setPageNum(Math.max((req.getPageNum() - 1), 0));
        builder.withPageable(PageRequest.of(req.getPageNum(), req.getPageSize()));
        //查询 条件
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(this.packageKeyWordQueryCondition(req), this.filterFunctionBuilder(req)).scoreMode(FunctionScoreQuery.ScoreMode.MULTIPLY);

        builder.withQuery(functionScoreQueryBuilder);
        //排序
        for (FieldSortBuilder fieldSortBuilder : this.packageSort(req)) {
            builder.withSort(fieldSortBuilder);
        }

        NativeSearchQuery build = builder.build();
        log.info("--->>> EsBookListModelService.listKeyWorldEsBookListModel DSL: {}", build.getQuery().toString());
        AggregatedPage<EsBookListModel> resultQueryPage = elasticsearchTemplate.queryForPage(build, EsBookListModel.class);
        long totalElements = resultQueryPage.getTotalElements();
        List<EsBookListModelResp> esBookListModelResps = this.packageEsBookListModelResp(resultQueryPage.getContent());
        return new CommonPageResp<>(totalElements, esBookListModelResps);
    }

    /**
     * 关键词搜索
     * @param req
     * @return
     */
    public CommonPageResp<List<EsBookListModelResp>> listKeyWorldEsBookListModelV2(EsKeyWordBookListQueryProviderReq req){

        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withIndices(AbstractCollectFactory.INDEX_ES_BOOK_LIST_MODEL);

        //分页 从0开始
        req.setPageNum(Math.max((req.getPageNum() - 1), 0));
        builder.withPageable(PageRequest.of(req.getPageNum(), req.getPageSize()));
        //查询 条件
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(this.packageKeyWordQueryCondition(req), this.filterFunctionBuilder(req)).scoreMode(FunctionScoreQuery.ScoreMode.MULTIPLY);

        builder.withQuery(functionScoreQueryBuilder);
        //排序
        for (FieldSortBuilder fieldSortBuilder : this.packageSort(req)) {
            builder.withSort(fieldSortBuilder);
        }

        NativeSearchQuery build = builder.build();
        log.info("--->>> EsBookListModelService.listKeyWorldEsBookListModel DSL: {}", build.getQuery().toString());
        AggregatedPage<EsBookListModel> resultQueryPage = elasticsearchTemplate.queryForPage(build, EsBookListModel.class);
        RankRelResponse allRankRel = topicConfigProvider.getAllRankRel();
        long totalElements = allRankRel.getRelIdList().size();
        List<EsBookListModelResp> esBookListModelResps = this.packageEsBookListModelResp(resultQueryPage.getContent()).stream().filter(e-> !CollectionUtils.isEmpty(allRankRel.getRelIdList())&&allRankRel.getRelIdList().contains(Integer.parseInt(e.getBookListId().toString()))).collect(Collectors.toList());
        return new CommonPageResp<>(totalElements, esBookListModelResps);
    }

    /**
     * 封装结果
     * @param esBookListModelList
     * @return
     */
    private List<EsBookListModelResp> packageEsBookListModelResp(List<EsBookListModel> esBookListModelList) {
        List<EsBookListModelResp> result = new ArrayList<>();
        for (EsBookListModel esBookListModel : esBookListModelList) {
            EsBookListModelResp esBookListModelResp = new EsBookListModelResp();
            BeanUtils.copyProperties(esBookListModel, esBookListModelResp);
            result.add(esBookListModelResp);
        }
        return result;
    }

    /**
     * 设置普通搜索的条件信息
     * @param req
     * @return
     */
    private BoolQueryBuilder packageQueryCondition(EsBookListQueryProviderReq req) {

        //查询条件
        BoolQueryBuilder boolQb = QueryBuilders.boolQuery();
        boolQb.must(nestedQuery("spus", termsQuery("spus.spuId", req.getSpuIds()), ScoreMode.None));

        return boolQb;
    }

    /**
     * 书单/榜单搜索
     * @param request
     * @return
     */
    public CommonPageResp<List<EsBookListModelResp>> listEsBookListModel(EsBookListQueryProviderReq request) {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withIndices(AbstractCollectFactory.INDEX_ES_BOOK_LIST_MODEL);

        //分页 从0开始
        request.setPageNum(Math.max((request.getPageNum() - 1), 0));
        builder.withPageable(PageRequest.of(request.getPageNum(), request.getPageSize()));
        builder.withQuery(this.packageQueryCondition(request));
        //排序
        for (FieldSortBuilder fieldSortBuilder : this.packageSort(request)) {
            builder.withSort(fieldSortBuilder);
        }
        NativeSearchQuery build = builder.build();
        log.info("--->>> EsBookListModelService.listEsBookListModel DSL: {}", build.getQuery().toString());
        AggregatedPage<EsBookListModel> resultQueryPage = elasticsearchTemplate.queryForPage(build, EsBookListModel.class);
        return new CommonPageResp<>(resultQueryPage.getTotalElements(), this.packageEsBookListModelResp(resultQueryPage.getContent()));
    }
}
