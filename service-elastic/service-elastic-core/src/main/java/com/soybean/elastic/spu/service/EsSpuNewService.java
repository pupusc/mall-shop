package com.soybean.elastic.spu.service;
import java.math.BigDecimal;
import com.google.common.collect.Lists;
import com.soybean.elastic.api.resp.EsSpuNewResp.Book;

import com.soybean.common.resp.CommonPageResp;
import com.soybean.elastic.api.enums.SearchSpuNewCategoryEnum;
import com.soybean.elastic.api.enums.SearchSpuNewSortTypeEnum;
import com.soybean.elastic.api.req.EsKeyWordSpuNewQueryProviderReq;
import com.soybean.elastic.api.req.EsSortSpuNewQueryProviderReq;
import com.soybean.elastic.api.resp.EsSpuNewResp;
import com.soybean.elastic.collect.factory.AbstractCollectFactory;
import com.soybean.elastic.spu.model.EsSpuNew;
import com.soybean.elastic.spu.model.sub.SubAnchorRecomNew;
import com.soybean.elastic.spu.model.sub.SubBookLabelNew;
import com.wanmi.sbc.elastic.api.common.CommonEsSearchCriteriaBuilder;
import com.wanmi.sbc.setting.api.constant.SearchWeightConstant;
import com.wanmi.sbc.setting.api.provider.weight.SearchWeightProvider;
import com.wanmi.sbc.setting.api.response.weight.SearchWeightResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.NestedSortBuilder;
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
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

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

        //查询条件
        req.setKeyword(QueryParser.escape(req.getKeyword()));
//        BoolQueryBuilder boolQb = CommonEsSearchCriteriaBuilder.getSpuNewCommonBuilder(req);

        BoolQueryBuilder boolQb = QueryBuilders.boolQuery();
        boolQb.must(termQuery("delFlag", req.getDelFlag()));
        boolQb.must(termQuery("auditStatus", 1));
        boolQb.must(termQuery("addedFlag", 1));
        boolQb.must(termQuery("spuCategory", req.getSearchSpuNewCategory()));

        boolQb.should().add(matchQuery("spuName", req.getKeyword()));
        boolQb.should().add(matchQuery("spuName.keyword", req.getKeyword()));
        boolQb.should().add(matchQuery("spuSubName", req.getKeyword()));
        boolQb.should().add(matchQuery("anchorRecoms.recomName", req.getKeyword()));
        boolQb.should().add(matchQuery("classify.classifyName", req.getKeyword()));
        boolQb.should().add(matchQuery("classify.fClassifyName", req.getKeyword()));

        boolQb.should().add(nestedQuery("book", matchQuery("book.bookName", req.getKeyword()), ScoreMode.None));
        boolQb.should().add(nestedQuery("book", termQuery("book.bookName.keyword", req.getKeyword()), ScoreMode.None));
        boolQb.should().add(nestedQuery("book", matchQuery("book.bookOriginName", req.getKeyword()), ScoreMode.None));
        boolQb.should().add(nestedQuery("book", matchQuery("book.bookDesc", req.getKeyword()), ScoreMode.None));
        boolQb.should().add(nestedQuery("book", matchQuery("book.authorNames", req.getKeyword()), ScoreMode.None));
        boolQb.should().add(nestedQuery("book", matchQuery("book.publisher", req.getKeyword()), ScoreMode.None));
        boolQb.should().add(nestedQuery("book", matchQuery("book.publisher.keyword", req.getKeyword()), ScoreMode.None));
        boolQb.should().add(nestedQuery("book", matchQuery("book.producer", req.getKeyword()), ScoreMode.None));
        boolQb.should().add(nestedQuery("book", matchQuery("book.clumpName", req.getKeyword()), ScoreMode.None));
        boolQb.should().add(nestedQuery("book", matchQuery("book.awards.awardName", req.getKeyword()), ScoreMode.None));
        boolQb.should().add(nestedQuery("book", matchQuery("book.groupName", req.getKeyword()), ScoreMode.None));
        boolQb.should().add(nestedQuery("book", matchQuery("book.seriesName", req.getKeyword()), ScoreMode.None));
        boolQb.should().add(nestedQuery("book", matchQuery("book.bindingName", req.getKeyword()), ScoreMode.None));
        boolQb.should().add(nestedQuery("book", matchQuery("book.tags.tagName", req.getKeyword()), ScoreMode.None));
        boolQb.should().add(nestedQuery("book", matchQuery("book.tags.sTagName", req.getKeyword()), ScoreMode.None));

        return boolQb;
    }

    private FunctionScoreQueryBuilder.FilterFunctionBuilder[] filterFunctionBuilder(EsKeyWordSpuNewQueryProviderReq req) {
        float defaultBoost = 0.1f;
        List<SearchWeightResp> context = searchWeightProvider.list(SearchWeightConstant.SPU_SEARCH_WEIGHT_KEY).getContext();
        Map<String, Float> searchWeightMap = new HashMap<>();
        for (SearchWeightResp searchWeightResp : context) {
            if (StringUtils.isEmpty(searchWeightResp.getWeightValue())) {
                continue;
            }
            searchWeightMap.put(searchWeightResp.getWeightKey(), Float.parseFloat(searchWeightResp.getWeightValue()));
        }

        FunctionScoreQueryBuilder.FilterFunctionBuilder[] arr = new FunctionScoreQueryBuilder.FilterFunctionBuilder[21];
        FunctionScoreQueryBuilder.FilterFunctionBuilder voluntary1 = new FunctionScoreQueryBuilder
                .FilterFunctionBuilder(QueryBuilders.matchQuery("spuName", req.getKeyword())
                , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.SPU_NAME, defaultBoost)));
        FunctionScoreQueryBuilder.FilterFunctionBuilder voluntary2 = new FunctionScoreQueryBuilder
                .FilterFunctionBuilder(QueryBuilders.matchQuery("spuName.keyword", req.getKeyword())
                , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.SPU_DIM_NAME, defaultBoost)));
        FunctionScoreQueryBuilder.FilterFunctionBuilder voluntary3 = new FunctionScoreQueryBuilder
                .FilterFunctionBuilder(QueryBuilders.matchQuery("spuSubName", req.getKeyword())
                , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.SPU_SUB_NAME, defaultBoost)));
        FunctionScoreQueryBuilder.FilterFunctionBuilder voluntary4 = new FunctionScoreQueryBuilder
                .FilterFunctionBuilder(QueryBuilders.matchQuery("anchorRecoms.recomName"
                , req.getKeyword()), ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.SPU_ANCHOR_RECOM, defaultBoost)));
        FunctionScoreQueryBuilder.FilterFunctionBuilder voluntary5 = new FunctionScoreQueryBuilder
                .FilterFunctionBuilder(QueryBuilders.matchQuery("classify.classifyName", req.getKeyword())
                , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.SPU_CLASSY_NAME, defaultBoost)));
        FunctionScoreQueryBuilder.FilterFunctionBuilder voluntary6 = new FunctionScoreQueryBuilder
                .FilterFunctionBuilder(QueryBuilders.matchQuery("classify.fClassifyName", req.getKeyword())
                , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.SPU_CLASSY_SECOND_NAME, defaultBoost)));
        FunctionScoreQueryBuilder.FilterFunctionBuilder voluntary7 = new FunctionScoreQueryBuilder
                .FilterFunctionBuilder(QueryBuilders.nestedQuery("book", QueryBuilders.matchQuery("book.bookName",req.getKeyword()), ScoreMode.None)
                , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_NAME, defaultBoost)));
        FunctionScoreQueryBuilder.FilterFunctionBuilder voluntary8 = new FunctionScoreQueryBuilder
                .FilterFunctionBuilder(QueryBuilders.nestedQuery("book", QueryBuilders.matchQuery("book.bookName.keyword",req.getKeyword()), ScoreMode.None)
                , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_DIM_NAME, defaultBoost)));
        FunctionScoreQueryBuilder.FilterFunctionBuilder voluntary9 = new FunctionScoreQueryBuilder
                .FilterFunctionBuilder(QueryBuilders.nestedQuery("book", QueryBuilders.matchQuery("book.bookOriginName",req.getKeyword()), ScoreMode.None)
                , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_ORIGIN_NAME, defaultBoost)));
        FunctionScoreQueryBuilder.FilterFunctionBuilder voluntary10 = new FunctionScoreQueryBuilder
                .FilterFunctionBuilder(QueryBuilders.nestedQuery("book", QueryBuilders.matchQuery("book.bookDesc",req.getKeyword()), ScoreMode.None)
                , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_DESC, defaultBoost)));
        FunctionScoreQueryBuilder.FilterFunctionBuilder voluntary11 = new FunctionScoreQueryBuilder
                .FilterFunctionBuilder(QueryBuilders.nestedQuery("book", QueryBuilders.matchQuery("book.authorNames",req.getKeyword()), ScoreMode.None)
                , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_AUTHOR_NAME, defaultBoost)));
        FunctionScoreQueryBuilder.FilterFunctionBuilder voluntary12 = new FunctionScoreQueryBuilder
                .FilterFunctionBuilder(QueryBuilders.nestedQuery("book", QueryBuilders.matchQuery("book.publisher",req.getKeyword()), ScoreMode.None)
                , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_PUBLISHER, defaultBoost)));
        FunctionScoreQueryBuilder.FilterFunctionBuilder voluntary13 = new FunctionScoreQueryBuilder
                .FilterFunctionBuilder(QueryBuilders.nestedQuery("book", QueryBuilders.matchQuery("book.publisher.keyword",req.getKeyword()), ScoreMode.None)
                , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_DIM_PUBLISHER, defaultBoost)));
        FunctionScoreQueryBuilder.FilterFunctionBuilder voluntary14 = new FunctionScoreQueryBuilder
                .FilterFunctionBuilder(QueryBuilders.nestedQuery("book", QueryBuilders.matchQuery("book.producer",req.getKeyword()), ScoreMode.None)
                , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_PRODUCER, defaultBoost)));
        FunctionScoreQueryBuilder.FilterFunctionBuilder voluntary15 = new FunctionScoreQueryBuilder
                .FilterFunctionBuilder(QueryBuilders.nestedQuery("book", QueryBuilders.matchQuery("book.clumpName",req.getKeyword()), ScoreMode.None)
                , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_CLUMP_NAME, defaultBoost)));
        FunctionScoreQueryBuilder.FilterFunctionBuilder voluntary16 = new FunctionScoreQueryBuilder
                .FilterFunctionBuilder(QueryBuilders.nestedQuery("book", QueryBuilders.matchQuery("book.awards.awardName",req.getKeyword()), ScoreMode.None)
                , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_AUTHOR_NAME, defaultBoost)));
        FunctionScoreQueryBuilder.FilterFunctionBuilder voluntary17 = new FunctionScoreQueryBuilder
                .FilterFunctionBuilder(QueryBuilders.nestedQuery("book", QueryBuilders.matchQuery("book.groupName",req.getKeyword()), ScoreMode.None)
                , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_GROUP_NAME, defaultBoost)));
        FunctionScoreQueryBuilder.FilterFunctionBuilder voluntary18 = new FunctionScoreQueryBuilder
                .FilterFunctionBuilder(QueryBuilders.nestedQuery("book", QueryBuilders.matchQuery("book.seriesName",req.getKeyword()), ScoreMode.None)
                , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_SERIES_NAME, defaultBoost)));
        FunctionScoreQueryBuilder.FilterFunctionBuilder voluntary19 = new FunctionScoreQueryBuilder
                .FilterFunctionBuilder(QueryBuilders.nestedQuery("book", QueryBuilders.matchQuery("book.bindingName",req.getKeyword()), ScoreMode.None)
                , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_BINDING_NAME, defaultBoost)));
        FunctionScoreQueryBuilder.FilterFunctionBuilder voluntary20 = new FunctionScoreQueryBuilder
                .FilterFunctionBuilder(QueryBuilders.nestedQuery("book", QueryBuilders.matchQuery("book.tags.tagName",req.getKeyword()), ScoreMode.None)
                , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_TAG_NAME, defaultBoost)));
        FunctionScoreQueryBuilder.FilterFunctionBuilder voluntary21 = new FunctionScoreQueryBuilder
                .FilterFunctionBuilder(QueryBuilders.nestedQuery("book", QueryBuilders.matchQuery("book.tags.sTagName",req.getKeyword()), ScoreMode.None)
                , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_SECOND_TAG_NAME, defaultBoost)));

        FunctionScoreQueryBuilder.FilterFunctionBuilder voluntary22 = new FunctionScoreQueryBuilder
                .FilterFunctionBuilder(QueryBuilders.nestedQuery("book", QueryBuilders.matchQuery("book.tags.sTagName",req.getKeyword()), ScoreMode.None)
                , ScoreFunctionBuilders.weightFactorFunction(searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_SECOND_TAG_NAME, defaultBoost)));

//        FunctionScoreQueryBuilder.FilterFunctionBuilder voluntary22 = new FunctionScoreQueryBuilder
//                .FilterFunctionBuilder(QueryBuilders.matchQuery("delFlag", 0)
//                , ScoreFunctionBuilders.weightFactorFunction(defaultBoost));
//        FunctionScoreQueryBuilder.FilterFunctionBuilder voluntary23 = new FunctionScoreQueryBuilder
//                .FilterFunctionBuilder(QueryBuilders.matchQuery("auditStatus", 1)
//                , ScoreFunctionBuilders.weightFactorFunction(defaultBoost));
//        FunctionScoreQueryBuilder.FilterFunctionBuilder voluntary24 = new FunctionScoreQueryBuilder
//                .FilterFunctionBuilder(QueryBuilders.matchQuery("addedFlag", 1)
//                , ScoreFunctionBuilders.weightFactorFunction(defaultBoost));
//        FunctionScoreQueryBuilder.FilterFunctionBuilder voluntary25 = new FunctionScoreQueryBuilder
//                .FilterFunctionBuilder(QueryBuilders.matchQuery("spuCategory", 1)
//                , ScoreFunctionBuilders.weightFactorFunction(defaultBoost));
        arr[0] = voluntary1;
        arr[1] = voluntary2;
        arr[2] = voluntary3;
        arr[3] = voluntary4;
        arr[4] = voluntary5;
        arr[5] = voluntary6;
        arr[6] = voluntary7;
        arr[7] = voluntary8;
        arr[8] = voluntary9;
        arr[9] = voluntary10;
        arr[10] = voluntary11;
        arr[11] = voluntary12;
        arr[12] = voluntary13;
        arr[13] = voluntary14;
        arr[14] = voluntary15;
        arr[15] = voluntary16;
        arr[16] = voluntary17;
        arr[17] = voluntary18;
        arr[18] = voluntary19;
        arr[19] = voluntary20;
        arr[20] = voluntary21;

//        arr[21] = voluntary22;
//        arr[22] = voluntary23;
//        arr[23] = voluntary24;
//        arr[24] = voluntary25;
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

        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(this.packageKeyWordQueryCondition(req), this.filterFunctionBuilder(req)).scoreMode(FunctionScoreQuery.ScoreMode.SUM);


        //查询 条件
        builder.withQuery(functionScoreQueryBuilder);
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
//            esSpuResp.setSpuId(esSpuNew.getSpuId());
//            esSpuResp.setSpuName(esSpuNew.getSpuName());
//            esSpuResp.setSpuSubName(esSpuNew.getSpuSubName());
//            esSpuResp.setSpuCategory(esSpuNew.getSpuCategory());
//            esSpuResp.setSalesPrice(esSpuNew.getSalesPrice());
//            esSpuResp.setPic(esSpuNew.getPic());
//            esSpuResp.setUnBackgroundPic(esSpuNew.getUnBackgroundPic());

            List<EsSpuNewResp.SubAnchorRecom> subAnchorRecoms = new ArrayList<>();
            if (!CollectionUtils.isEmpty(esSpuNew.getAnchorRecoms())) {
                for (SubAnchorRecomNew anchorRecom : esSpuNew.getAnchorRecoms()) {
                    EsSpuNewResp.SubAnchorRecom subAnchorRecom = new EsSpuNewResp.SubAnchorRecom();
                    subAnchorRecom.setRecomId(anchorRecom.getRecomId());
                    subAnchorRecom.setRecomName(anchorRecom.getRecomName());
                    subAnchorRecoms.add(subAnchorRecom);
                }
            }
            esSpuResp.setAnchorRecoms(subAnchorRecoms);

            if (esSpuNew.getClassify() != null) {
                EsSpuNewResp.SubClassify subClassify = new EsSpuNewResp.SubClassify();
                subClassify.setFClassifyId(esSpuNew.getClassify().getFClassifyId());
                subClassify.setFClassifyName(esSpuNew.getClassify().getFClassifyName());
                subClassify.setClassifyId(esSpuNew.getClassify().getClassifyId());
                subClassify.setClassifyName(esSpuNew.getClassify().getClassifyName());
                esSpuResp.setClassify(subClassify);
            }

            if (esSpuNew.getBook() != null) {
                Book book = new Book();
                book.setIsbn(esSpuNew.getBook().getIsbn());
                book.setAuthorNames(esSpuNew.getBook().getAuthorNames());
                book.setScore(esSpuNew.getBook().getScore());
                book.setPublisher(esSpuNew.getBook().getPublisher());
                book.setFixPrice(esSpuNew.getBook().getFixPrice());

                if (!CollectionUtils.isEmpty(esSpuNew.getBook().getTags())) {
                    List<Book.SubBookLabel> subBookLabels = new ArrayList<>();
                    for (SubBookLabelNew tag : esSpuNew.getBook().getTags()) {
                        Book.SubBookLabel subBookLabel = new Book.SubBookLabel();
                        subBookLabel.setSTagId(tag.getSTagId());
                        subBookLabel.setSTagName(tag.getSTagName());
                        subBookLabel.setTagId(tag.getTagId());
                        subBookLabel.setTagName(tag.getTagName());
                        subBookLabels.add(subBookLabel);
                    }
                    book.setTags(subBookLabels);
                }
                esSpuResp.setBook(book);
            }
            result.add(esSpuResp);
        }
        return result;
    }
}