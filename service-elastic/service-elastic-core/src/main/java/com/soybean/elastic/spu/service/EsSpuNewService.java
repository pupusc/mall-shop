package com.soybean.elastic.spu.service;

import com.soybean.common.resp.CommonPageResp;
import com.soybean.elastic.api.enums.SearchSpuNewCategoryEnum;
import com.soybean.elastic.api.enums.SearchSpuNewSortTypeEnum;
import com.soybean.elastic.api.req.EsKeyWordSpuNewQueryProviderReq;
import com.soybean.elastic.api.req.EsSortSpuNewQueryProviderReq;
import com.soybean.elastic.api.resp.EsSpuNewResp;
import com.soybean.elastic.collect.factory.AbstractCollectFactory;
import com.soybean.elastic.spu.model.EsSpuNew;
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
import java.util.Objects;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;

/**
 * Description: 搜索商品信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/9 5:15 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
@Slf4j
public class EsSpuNewService {

    @Autowired
    private SearchWeightProvider searchWeightProvider;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 设置搜索的条件信息
     *
     * @param req
     * @return
     */
    private BoolQueryBuilder packageKeyWordQueryCondition(EsKeyWordSpuNewQueryProviderReq req) {
        float defaultBoost = 0.1f;
        List<SearchWeightResp> context = searchWeightProvider.list(SearchWeightConstant.SPU_SEARCH_WEIGHT_KEY).getContext();
        Map<String, Float> searchWeightMap = new HashMap<>();
        for (SearchWeightResp searchWeightResp : context) {
            if (StringUtils.isEmpty(searchWeightResp.getWeightValue())) {
                continue;
            }
            searchWeightMap.put(searchWeightResp.getWeightKey(), Float.parseFloat(searchWeightResp.getWeightValue()));
        }
        //查询条件
        BoolQueryBuilder boolQb = QueryBuilders.boolQuery();

        boolQb.must(matchQuery("spuCategory", req.getSearchSpuNewCategory()));
        boolQb.should(matchQuery("spuName", req.getKeyword()).boost(searchWeightMap.getOrDefault(SearchWeightConstant.SPU_NAME, defaultBoost)));
        boolQb.should(matchQuery("spuSubName", req.getKeyword()).boost(searchWeightMap.getOrDefault(SearchWeightConstant.SPU_SUB_NAME, defaultBoost)));

        boolQb.should(nestedQuery("anchorRecoms", matchQuery("anchorRecoms.recomName", req.getKeyword())
                .boost(searchWeightMap.getOrDefault(SearchWeightConstant.SPU_ANCHOR_RECOM, defaultBoost)), ScoreMode.None));
//        boolQb.should(matchQuery("anchorRecoms.recomName", req.getKeyword()).boost(searchWeightMap.getOrDefault(SearchWeightConstant.SPU_ANCHOR_RECOM, defaultBoost)));
//                .boost(searchWeightMap.getOrDefault(SearchWeightConstant.SPU_ANCHOR_RECOM, defaultBoost)), ScoreMode.None));

        if (Objects.equals(req.getSearchSpuNewCategory(), SearchSpuNewCategoryEnum.SPU.getCode())) {
            //商品
            boolQb.should(nestedQuery("classify", matchQuery("classify.classifyName", req.getKeyword())
                    .boost(searchWeightMap.getOrDefault(SearchWeightConstant.SPU_CLASSY_NAME, defaultBoost)), ScoreMode.None));

            boolQb.should(nestedQuery("classify", matchQuery("classify.fClassifyName", req.getKeyword())
                    .boost(searchWeightMap.getOrDefault(SearchWeightConstant.SPU_CLASSY_SECOND_NAME, defaultBoost)), ScoreMode.None));
        } else {
            //图书商品
            boolQb.should(nestedQuery("book", matchQuery("book.bookName", req.getKeyword())
                    .boost(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_NAME, defaultBoost)), ScoreMode.None));

            boolQb.should(nestedQuery("book", matchQuery("book.bookOriginName", req.getKeyword())
                    .boost(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_ORIGIN_NAME, defaultBoost)), ScoreMode.None));

            boolQb.should(nestedQuery("book", matchQuery("book.bookDesc", req.getKeyword())
                    .boost(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_DESC, defaultBoost)), ScoreMode.None));

            boolQb.should(nestedQuery("book", matchQuery("book.authorNames", req.getKeyword())
                    .boost(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_AUTHOR_NAME, defaultBoost)), ScoreMode.None));

            boolQb.should(nestedQuery("book", matchQuery("book.publisher", req.getKeyword())
                    .boost(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_PUBLISHER, defaultBoost)), ScoreMode.None));

            boolQb.should(nestedQuery("book", matchQuery("book.producer", req.getKeyword())
                    .boost(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_PRODUCER, defaultBoost)), ScoreMode.None));

            boolQb.should(nestedQuery("book", matchQuery("book.clumpName", req.getKeyword())
                    .boost(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_CLUMP_NAME, defaultBoost)), ScoreMode.None));

            boolQb.should(nestedQuery("book", matchQuery("book.awards.awardName", req.getKeyword())
                    .boost(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_AWARDS, defaultBoost)), ScoreMode.None));

            boolQb.should(nestedQuery("book", matchQuery("book.groupName", req.getKeyword())
                    .boost(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_GROUP_NAME, defaultBoost)), ScoreMode.None));

            boolQb.should(nestedQuery("book", matchQuery("book.seriesName", req.getKeyword())
                    .boost(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_SERIES_NAME, defaultBoost)), ScoreMode.None));

            boolQb.should(nestedQuery("book", matchQuery("book.bindingName", req.getKeyword())
                    .boost(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_BINDING_NAME, defaultBoost)), ScoreMode.None));

            boolQb.should(nestedQuery("book", matchQuery("book.tags.tagName", req.getKeyword())
                    .boost(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_TAG_NAME, defaultBoost)), ScoreMode.None));

            boolQb.should(nestedQuery("book", matchQuery("book.tags.sTagName", req.getKeyword())
                    .boost(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_SECOND_TAG_NAME, defaultBoost)), ScoreMode.None));
        }


        return boolQb;
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
            order = new FieldSortBuilder("_score").order(SortOrder.DESC);
        } else if (Objects.equals(sortQueryProviderReq.getSpuSortType(), SearchSpuNewSortTypeEnum.SCORE.getCode())) {
            order = new FieldSortBuilder("book.score").order(SortOrder.DESC);
        } else if (Objects.equals(sortQueryProviderReq.getSpuSortType(), SearchSpuNewSortTypeEnum.NEW_ADDED.getCode())) {
            order = new FieldSortBuilder("addedTime").order(SortOrder.DESC);
        } else if (Objects.equals(sortQueryProviderReq.getSpuSortType(), SearchSpuNewSortTypeEnum.FAVOR_COMMENT.getCode())) {
            order = new FieldSortBuilder("favorCommentRate").order(SortOrder.DESC);
        } else if (Objects.equals(sortQueryProviderReq.getSpuSortType(), SearchSpuNewSortTypeEnum.HIGH_PRICE.getCode())) {
            order = new FieldSortBuilder("salesPrice").order(SortOrder.DESC);
        } else if (Objects.equals(sortQueryProviderReq.getSpuSortType(), SearchSpuNewSortTypeEnum.LOWER_PRICE.getCode())) {
            order = new FieldSortBuilder("salesPrice").order(SortOrder.ASC);
        }
        if (order != null) {
            fieldSortBuilders.add(order);
        }

        FieldSortBuilder order1 = new FieldSortBuilder("salesNum").order(SortOrder.DESC);
        FieldSortBuilder order2 = new FieldSortBuilder("salesPrice").order(SortOrder.DESC);
        FieldSortBuilder order3 = new FieldSortBuilder("addedTime").order(SortOrder.DESC);
        fieldSortBuilders.addAll(Arrays.asList(order1, order2, order3));
        return fieldSortBuilders;
    }

    /**
     * 关键词搜索
     * @param req
     * @return
     */
    public CommonPageResp<List<EsSpuNewResp>> listKeyWorldEsSpu(EsKeyWordSpuNewQueryProviderReq req) {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withIndices(AbstractCollectFactory.INDEX_ES_SPU_NEW);

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
        log.info("--->>> EsBookListModelService.listKeyWorldEsSpu DSL: {}", build.getQuery().toString());
        AggregatedPage<EsSpuNew> resultQueryPage = elasticsearchTemplate.queryForPage(build, EsSpuNew.class);
        return new CommonPageResp<>(resultQueryPage.getTotalElements(), this.packageEsSpuNewResp(resultQueryPage.getContent()));
    }

    /**
     * 打包商品
     * @param esSpuNewList
     * @return
     */
    private List<EsSpuNewResp>  packageEsSpuNewResp(List<EsSpuNew> esSpuNewList) {
        List<EsSpuNewResp> result = new ArrayList<>();
        for (EsSpuNew esSpuNew : esSpuNewList) {
            EsSpuNewResp esSpuResp = new EsSpuNewResp();
            BeanUtils.copyProperties(esSpuNew, esSpuResp);
            result.add(esSpuResp);
        }
        return result;
    }
}