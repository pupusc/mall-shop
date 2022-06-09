package com.soybean.elastic.booklistmodel.service;


import com.soybean.elastic.api.req.EsKeyWordQueryProviderReq;
import com.soybean.elastic.api.resp.EsBookListModelResp;
import com.soybean.common.resp.CommonPageResp;
import com.soybean.elastic.collect.factory.AbstractCollectFactory;
import com.wanmi.sbc.setting.api.constant.SearchWeightConstant;
import com.wanmi.sbc.setting.api.provider.weight.SearchWeightProvider;
import com.wanmi.sbc.setting.api.response.weight.SearchWeightResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;

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

    /**
     * 设置条件信息
     * @param esKeyWordQueryProviderReq
     * @return
     */
    private BoolQueryBuilder packageQueryCondition(EsKeyWordQueryProviderReq esKeyWordQueryProviderReq) {
        float defaultBoost = 0f;
        List<SearchWeightResp> context = searchWeightProvider.list(SearchWeightConstant.BOOK_LIST_SEARCH_WEIGHT_KEY).getContext();
        Map<String, Float> searchWeightMap = new HashMap<>();
        for (SearchWeightResp searchWeightResp : context) {
            if (StringUtils.isEmpty(searchWeightResp.getWeightValue())) {
                continue;
            }
            searchWeightMap.put(searchWeightResp.getWeightKey(), Float.parseFloat(searchWeightResp.getWeightValue()));
        }
        //查询条件
        BoolQueryBuilder boolQb = QueryBuilders.boolQuery();
//        BoolQueryBuilder boolQb = CommonEsSearchCriteriaBuilder.getSpuCommonSearchCriterialBuilder(request);
        boolQb.should(matchQuery("bookListName", esKeyWordQueryProviderReq.getKeyword())).
                boost(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_LIST_SEARCH_WEIGHT_BOOK_LIST_NAME, defaultBoost));
        boolQb.should(nestedQuery("spus", matchQuery("spuName", esKeyWordQueryProviderReq.getKeyword())
                .boost(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_LIST_SEARCH_WEIGHT_SPU_NAME, defaultBoost)), ScoreMode.None));

        return boolQb;
    }

    /**
     * 设置排序
     */
    private List<FieldSortBuilder> packageSort() {
        List<FieldSortBuilder> fieldSortBuilders = new ArrayList<>();
//        FieldSortBuilder order = new FieldSortBuilder("").order(SortOrder.ASC);
//        fieldSortBuilders.add(order);
        return fieldSortBuilders;
    }

    /**
     * 关键词搜索
     * @param esKeyWordQueryProviderReq
     * @return
     */
    public CommonPageResp<List<EsBookListModelResp>> listKeyWorldEsBookListModel(EsKeyWordQueryProviderReq esKeyWordQueryProviderReq){

        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withIndices(AbstractCollectFactory.INDEX_ES_BOOK_LIST_MODEL);

        //分页 从0开始
        builder.withPageable(PageRequest.of(esKeyWordQueryProviderReq.getPageNum(), esKeyWordQueryProviderReq.getPageSize()));
        //查询 条件
        builder.withQuery(this.packageQueryCondition(esKeyWordQueryProviderReq));
        //排序
        for (FieldSortBuilder sortBuilder : this.packageSort()) {
            builder.withSort(sortBuilder);
        }

        NativeSearchQuery build = builder.build();
        log.info("--->>> EsBookListModelService.listEsBookListModel DSL: {}", build.getQuery().toString());
        AggregatedPage<EsBookListModelResp> resultQueryPage = elasticsearchTemplate.queryForPage(build, EsBookListModelResp.class);
        return new CommonPageResp<>(resultQueryPage.getTotalElements(), resultQueryPage.getContent());
    }
}
