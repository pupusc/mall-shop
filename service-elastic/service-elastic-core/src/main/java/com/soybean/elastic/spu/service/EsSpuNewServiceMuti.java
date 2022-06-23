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
import com.soybean.elastic.constant.ConstantMultiMatchField;
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
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.index.search.MultiMatchQuery;
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
public class EsSpuNewServiceMuti {

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
        float defaultBoost = 1f;

        List<SearchWeightResp> context = searchWeightProvider.list(SearchWeightConstant.SPU_SEARCH_WEIGHT_KEY).getContext();
        Map<String, Float> searchWeightMap = new HashMap<>();
        for (SearchWeightResp searchWeightResp : context) {
            if (StringUtils.isEmpty(searchWeightResp.getWeightValue())) {
                continue;
            }
            searchWeightMap.put(searchWeightResp.getWeightKey(), Float.parseFloat(searchWeightResp.getWeightValue()));
        }


        //查询条件
        req.setKeyword(QueryParser.escape(req.getKeyword()));
        BoolQueryBuilder boolQb = CommonEsSearchCriteriaBuilder.getSpuNewCommonBuilder(req);
        boolQb.must(termQuery("delFlag", req.getDelFlag()));
        boolQb.must(termQuery("auditStatus", 1));
        boolQb.must(termQuery("addedFlag", 1));
        boolQb.must(termQuery("spuCategory", req.getSearchSpuNewCategory()));

        String[] spuAttr = new String[]{
                ConstantMultiMatchField.FIELD_SPU_SPUNAME,
                ConstantMultiMatchField.FIELD_SPU_SPUNAME_KEYWORD,
                ConstantMultiMatchField.FIELD_SPU_SPUSUBNAME,
                ConstantMultiMatchField.FIELD_SPU_ANCHORRECOMS_RECOMNAME,
                ConstantMultiMatchField.FIELD_SPU_CLASSIFY_CLASSIFYNAME,
                ConstantMultiMatchField.FIELD_SPU_CLASSIFY_FCLASSIFYNAME};
        Map<String, Float> spuWeightMap = new HashMap<>();
        spuWeightMap.put(ConstantMultiMatchField.FIELD_SPU_SPUNAME, searchWeightMap.getOrDefault(SearchWeightConstant.SPU_NAME, defaultBoost));
        spuWeightMap.put(ConstantMultiMatchField.FIELD_SPU_SPUNAME_KEYWORD,searchWeightMap.getOrDefault(SearchWeightConstant.SPU_DIM_NAME, defaultBoost));
        spuWeightMap.put(ConstantMultiMatchField.FIELD_SPU_SPUSUBNAME, searchWeightMap.getOrDefault(SearchWeightConstant.SPU_SUB_NAME, defaultBoost));
        spuWeightMap.put(ConstantMultiMatchField.FIELD_SPU_ANCHORRECOMS_RECOMNAME, searchWeightMap.getOrDefault(SearchWeightConstant.SPU_ANCHOR_RECOM, defaultBoost));
        spuWeightMap.put(ConstantMultiMatchField.FIELD_SPU_CLASSIFY_CLASSIFYNAME, searchWeightMap.getOrDefault(SearchWeightConstant.SPU_CLASSY_SECOND_NAME, defaultBoost));
        spuWeightMap.put(ConstantMultiMatchField.FIELD_SPU_CLASSIFY_FCLASSIFYNAME, searchWeightMap.getOrDefault(SearchWeightConstant.SPU_CLASSY_NAME, defaultBoost));
        boolQb.should().add(QueryBuilders.multiMatchQuery(req.getKeyword(), spuAttr).fields(spuWeightMap).type(MultiMatchQueryBuilder.Type.MOST_FIELDS).operator(Operator.AND));




        String[] bookAttr = new String[]{
                ConstantMultiMatchField.FIELD_SPU_BOOK_BOOKNAME,
                ConstantMultiMatchField.FIELD_SPU_BOOK_BOOKNAME_KEYWORD,
                ConstantMultiMatchField.FIELD_SPU_BOOK_BOOKORIGINNAME,
                ConstantMultiMatchField.FIELD_SPU_BOOK_BOOKDESC,
                ConstantMultiMatchField.FIELD_SPU_BOOK_AUTHORNAMES,
                ConstantMultiMatchField.FIELD_SPU_BOOK_PUBLISHER,
                ConstantMultiMatchField.FIELD_SPU_BOOK_PUBLISHER_KEYWORD,
                ConstantMultiMatchField.FIELD_SPU_BOOK_PRODUCER,
                ConstantMultiMatchField.FIELD_SPU_BOOK_CLUMPNAME,
                ConstantMultiMatchField.FIELD_SPU_BOOK_AWARDS_AWARDNAME,
                ConstantMultiMatchField.FIELD_SPU_BOOK_GROUPNAME,
                ConstantMultiMatchField.FIELD_SPU_BOOK_BINDINGNAME,
                ConstantMultiMatchField.FIELD_SPU_BOOK_TAGS_TAGNAME,
                ConstantMultiMatchField.FIELD_SPU_BOOK_TAGS_STAGNAME };
        Map<String, Float> spuBookWeightMap = new HashMap<>();
        spuBookWeightMap.put(ConstantMultiMatchField.FIELD_SPU_BOOK_BOOKNAME, searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_NAME, defaultBoost));
        spuBookWeightMap.put(ConstantMultiMatchField.FIELD_SPU_BOOK_BOOKNAME_KEYWORD, searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_DIM_NAME, defaultBoost));
        spuBookWeightMap.put(ConstantMultiMatchField.FIELD_SPU_BOOK_BOOKORIGINNAME, searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_ORIGIN_NAME, defaultBoost));
        spuBookWeightMap.put(ConstantMultiMatchField.FIELD_SPU_BOOK_BOOKDESC, searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_DESC, defaultBoost));
        spuBookWeightMap.put(ConstantMultiMatchField.FIELD_SPU_BOOK_AUTHORNAMES, searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_AUTHOR_NAME, defaultBoost));
        spuBookWeightMap.put(ConstantMultiMatchField.FIELD_SPU_BOOK_PUBLISHER, searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_PUBLISHER, defaultBoost));
        spuBookWeightMap.put(ConstantMultiMatchField.FIELD_SPU_BOOK_PUBLISHER_KEYWORD, searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_DIM_PUBLISHER, defaultBoost));
        spuBookWeightMap.put(ConstantMultiMatchField.FIELD_SPU_BOOK_PRODUCER, searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_PRODUCER, defaultBoost));
        spuBookWeightMap.put(ConstantMultiMatchField.FIELD_SPU_BOOK_CLUMPNAME, searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_CLUMP_NAME, defaultBoost));
        spuBookWeightMap.put(ConstantMultiMatchField.FIELD_SPU_BOOK_AWARDS_AWARDNAME, searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_AWARDS_NAME, defaultBoost));
        spuBookWeightMap.put(ConstantMultiMatchField.FIELD_SPU_BOOK_GROUPNAME, searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_GROUP_NAME, defaultBoost));
        spuBookWeightMap.put(ConstantMultiMatchField.FIELD_SPU_BOOK_BINDINGNAME, searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_BINDING_NAME, defaultBoost));
        spuBookWeightMap.put(ConstantMultiMatchField.FIELD_SPU_BOOK_TAGS_TAGNAME, searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_TAG_NAME, defaultBoost));
        spuBookWeightMap.put(ConstantMultiMatchField.FIELD_SPU_BOOK_TAGS_STAGNAME, searchWeightMap.getOrDefault(SearchWeightConstant.BOOK_SECOND_TAG_NAME, defaultBoost));
        boolQb.should().add(QueryBuilders.nestedQuery("book",
                QueryBuilders.multiMatchQuery(req.getKeyword(), bookAttr).fields(spuBookWeightMap).type(MultiMatchQueryBuilder.Type.MOST_FIELDS).operator(Operator.AND), ScoreMode.None));

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
                subClassify.setFClassifyId(esSpuNew.getClassify().getFclassifyId());
                subClassify.setFClassifyName(esSpuNew.getClassify().getFclassifyName());
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
                        subBookLabel.setStagId(tag.getStagId());
                        subBookLabel.setStagName(tag.getStagName());
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