package com.soybean.elastic.spu.service;

import com.soybean.common.resp.CommonPageResp;
import com.soybean.elastic.api.enums.BookListSortType;
import com.soybean.elastic.api.enums.SpuSortType;
import com.soybean.elastic.api.req.EsKeyWordQueryProviderReq;
import com.soybean.elastic.api.req.EsSortQueryProviderReq;
import com.soybean.elastic.api.resp.EsSpuResp;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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

/**
 * Description: 搜索商品信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/9 5:15 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
@Slf4j
public class EsSpuService {

    @Autowired
    private SearchWeightProvider searchWeightProvider;

    /**
     * 设置搜索的条件信息
     *
     * @param esKeyWordQueryProviderReq
     * @return
     */
    private BoolQueryBuilder packageKeyWordQueryCondition(EsKeyWordQueryProviderReq esKeyWordQueryProviderReq) {
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

        boolQb.should(matchQuery("spuName", esKeyWordQueryProviderReq.getKeyword())).
                boost(searchWeightMap.getOrDefault(SearchWeightConstant.SPU_NAME, defaultBoost));
        boolQb.should(matchQuery("spuSubName", esKeyWordQueryProviderReq.getKeyword())).
                boost(searchWeightMap.getOrDefault(SearchWeightConstant.SPU_SUB_NAME, defaultBoost));

        boolQb.should(nestedQuery("anchorRecoms", matchQuery("anchorRecoms.recomName", esKeyWordQueryProviderReq.getKeyword())
                .boost(searchWeightMap.getOrDefault(SearchWeightConstant.SPU_ANCHOR_RECOM, defaultBoost)), ScoreMode.None));

        //商品
        boolQb.should(nestedQuery("classifys", matchQuery("classifys.classifyName", esKeyWordQueryProviderReq.getKeyword())
                .boost(searchWeightMap.getOrDefault(SearchWeightConstant.SPU_CLASSY_NAME, defaultBoost)), ScoreMode.None));

        boolQb.should(nestedQuery("classifys", matchQuery("classifys.fClassifyName", esKeyWordQueryProviderReq.getKeyword())
                .boost(searchWeightMap.getOrDefault(SearchWeightConstant.SPU_CLASSY_SECOND_NAME, defaultBoost)), ScoreMode.None));

        //图书商品
        boolQb.should(nestedQuery("book", matchQuery("book.bookName", esKeyWordQueryProviderReq.getKeyword())
                .boost(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_NAME, defaultBoost)), ScoreMode.None));

        boolQb.should(nestedQuery("book", matchQuery("book.bookOriginName", esKeyWordQueryProviderReq.getKeyword())
                .boost(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_ORIGIN_NAME, defaultBoost)), ScoreMode.None));

        boolQb.should(nestedQuery("book", matchQuery("book.bookDesc", esKeyWordQueryProviderReq.getKeyword())
                .boost(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_DESC, defaultBoost)), ScoreMode.None));

        boolQb.should(nestedQuery("book", matchQuery("book.authorNames", esKeyWordQueryProviderReq.getKeyword())
                .boost(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_AUTHOR_NAME, defaultBoost)), ScoreMode.None));

        boolQb.should(nestedQuery("book", matchQuery("book.publisher", esKeyWordQueryProviderReq.getKeyword())
                .boost(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_PUBLISHER, defaultBoost)), ScoreMode.None));

        boolQb.should(nestedQuery("book", matchQuery("book.producer", esKeyWordQueryProviderReq.getKeyword())
                .boost(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_PRODUCER, defaultBoost)), ScoreMode.None));

        boolQb.should(nestedQuery("book", matchQuery("book.clumpName", esKeyWordQueryProviderReq.getKeyword())
                .boost(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_CLUMP_NAME, defaultBoost)), ScoreMode.None));

        boolQb.should(nestedQuery("book", matchQuery("book.awards.awardName", esKeyWordQueryProviderReq.getKeyword())
                .boost(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_AWARDS, defaultBoost)), ScoreMode.None));

        boolQb.should(nestedQuery("book", matchQuery("book.groupName", esKeyWordQueryProviderReq.getKeyword())
                .boost(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_GROUP_NAME, defaultBoost)), ScoreMode.None));

        boolQb.should(nestedQuery("book", matchQuery("book.seriesName", esKeyWordQueryProviderReq.getKeyword())
                .boost(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_SERIES_NAME, defaultBoost)), ScoreMode.None));

        boolQb.should(nestedQuery("book", matchQuery("book.bindingName", esKeyWordQueryProviderReq.getKeyword())
                .boost(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_BINDING_NAME, defaultBoost)), ScoreMode.None));

        boolQb.should(nestedQuery("book", matchQuery("book.tags.tagName", esKeyWordQueryProviderReq.getKeyword())
                .boost(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_TAG_NAME, defaultBoost)), ScoreMode.None));

        boolQb.should(nestedQuery("book", matchQuery("book.tags.sTagName", esKeyWordQueryProviderReq.getKeyword())
                .boost(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_SECOND_TAG_NAME, defaultBoost)), ScoreMode.None));
        return boolQb;
    }


    /**
     * 设置排序
     */
    private List<FieldSortBuilder> packageSort(EsSortQueryProviderReq sortQueryProviderReq) {
        List<FieldSortBuilder> fieldSortBuilders = new ArrayList<>();
        if (sortQueryProviderReq.getSpuSortType() == null) {
            return fieldSortBuilders;
        }
        FieldSortBuilder order = null;
        if (sortQueryProviderReq.getSpuSortType() == SpuSortType.DEFAULT) {
            order = new FieldSortBuilder("_score").order(SortOrder.DESC);
        } else if (sortQueryProviderReq.getSpuSortType() == SpuSortType.SCORE) {
            order = new FieldSortBuilder("book.score").order(SortOrder.DESC);
        } else if (sortQueryProviderReq.getSpuSortType() == SpuSortType.NEW_ADDED) {
            order = new FieldSortBuilder("addedTime").order(SortOrder.DESC);
        } else if (sortQueryProviderReq.getSpuSortType() == SpuSortType.FAVOR_COMMENT) {
            order = new FieldSortBuilder("favorCommentRate").order(SortOrder.DESC);
        } else if (sortQueryProviderReq.getSpuSortType() == SpuSortType.HIGH_PRICE) {
            order = new FieldSortBuilder("salesPrice").order(SortOrder.DESC);
        } else if (sortQueryProviderReq.getSpuSortType() == SpuSortType.LOWER_PRICE) {
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

    public CommonPageResp<List<EsSpuResp>> listKeyWorldEsSpu(EsKeyWordQueryProviderReq esKeyWordQueryProviderReq) {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withIndices(AbstractCollectFactory.INDEX_ES_BOOK_LIST_MODEL);

        //分页 从0开始
        builder.withPageable(PageRequest.of(esKeyWordQueryProviderReq.getPageNum(), esKeyWordQueryProviderReq.getPageSize()));
        //查询 条件
        builder.withQuery(this.packageKeyWordQueryCondition(esKeyWordQueryProviderReq));
        //排序
        for (FieldSortBuilder fieldSortBuilder : this.packageSort(esKeyWordQueryProviderReq)) {
            builder.withSort(fieldSortBuilder);
        }


        NativeSearchQuery build = builder.build();
        log.info("--->>> EsBookListModelService.listKeyWorldEsBookListModel DSL: {}", build.getQuery().toString());
        return null;
    }
}