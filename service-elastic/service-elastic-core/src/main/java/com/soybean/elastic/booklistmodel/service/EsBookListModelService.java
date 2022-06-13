package com.soybean.elastic.booklistmodel.service;


import com.soybean.elastic.api.enums.SearchBookListSortTypeEnum;
import com.soybean.elastic.api.req.EsBookListQueryProviderReq;
import com.soybean.elastic.api.req.EsKeyWordBookListQueryProviderReq;
import com.soybean.elastic.api.req.EsSortBookListQueryProviderReq;
import com.soybean.elastic.api.resp.EsBookListModelResp;
import com.soybean.common.resp.CommonPageResp;
import com.soybean.elastic.booklistmodel.model.EsBookListModel;
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
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;
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

    /**
     * 设置搜索的条件信息
     * @param esKeyWordQueryProviderReq
     * @return
     */
    private BoolQueryBuilder packageKeyWordQueryCondition(EsKeyWordBookListQueryProviderReq esKeyWordQueryProviderReq) {
        float defaultBoost = 0.1f;
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
        boolQb.must(matchQuery("bookListBusinessType", esKeyWordQueryProviderReq.getSearchBookListCategory()));
        boolQb.should(matchQuery("bookListName", esKeyWordQueryProviderReq.getKeyword()).
                boost(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_LIST_SEARCH_WEIGHT_BOOK_LIST_NAME, defaultBoost)));
        boolQb.should(nestedQuery("spus", matchQuery("spus.spuName", esKeyWordQueryProviderReq.getKeyword())
                .boost(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_LIST_SEARCH_WEIGHT_SPU_NAME, defaultBoost)), ScoreMode.None));

        return boolQb;
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
            FieldSortBuilder order1 = new FieldSortBuilder("updateTime").order(SortOrder.DESC);
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
        builder.withQuery(this.packageKeyWordQueryCondition(req));
        //排序
        for (FieldSortBuilder fieldSortBuilder : this.packageSort(req)) {
            builder.withSort(fieldSortBuilder);
        }

        NativeSearchQuery build = builder.build();
        log.info("--->>> EsBookListModelService.listKeyWorldEsBookListModel DSL: {}", build.getQuery().toString());
        AggregatedPage<EsBookListModel> resultQueryPage = elasticsearchTemplate.queryForPage(build, EsBookListModel.class);

        return new CommonPageResp<>(resultQueryPage.getTotalElements(), this.packageEsBookListModelResp(resultQueryPage.getContent()));
    }

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
