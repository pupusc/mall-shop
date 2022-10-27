package com.soybean.elastic.spu.service;

import com.soybean.common.req.CommonPageQueryReq;
import com.soybean.common.resp.CommonPageResp;
import com.soybean.elastic.api.enums.SearchSpuNewAggsCategoryEnum;
import com.soybean.elastic.api.enums.SearchSpuNewCategoryEnum;
import com.soybean.elastic.api.enums.SearchSpuNewLabelCategoryEnum;
import com.soybean.elastic.api.enums.SearchSpuNewPriceRangeEnum;
import com.soybean.elastic.api.req.EsKeyWordSpuNewQueryProviderReq;
import com.soybean.elastic.api.resp.EsSpuNewAggResp;
import com.soybean.elastic.api.resp.EsSpuNewResp;
import com.soybean.elastic.spu.model.EsSpuNew;
import com.soybean.elastic.spu.model.sub.SubAnchorRecomNew;
import com.soybean.elastic.spu.model.sub.SubBookLabelNew;
import com.wanmi.sbc.elastic.api.common.CommonEsSearchCriteriaBuilder;
import com.wanmi.sbc.setting.api.constant.SearchAggsConstant;
import com.wanmi.sbc.setting.api.provider.search.SearchAggsProvider;
import com.wanmi.sbc.setting.api.provider.search.SearchWeightProvider;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/4 11:10 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
public abstract class AbstractEsSpuNewService {

    @Autowired
    protected SearchWeightProvider searchWeightProvider;

    @Autowired
    protected ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    protected SearchAggsProvider searchAggsProvider;

    /**
     * 二次封装请求的正常的对象信息
     * @param req
     * @return
     */
    protected BoolQueryBuilder packageEsSpuNewReq(CommonPageQueryReq req) {
        BoolQueryBuilder boolQb = CommonEsSearchCriteriaBuilder.getSpuNewCommonBuilder(req);

        boolQb.must(termQuery("delFlag", req.getDelFlag()));
        boolQb.must(termQuery("auditStatus", 1));
        boolQb.must(termQuery("addedFlag", 1));
        return boolQb;
    }

    /**
     * 聚合 列表信息
     * @param resultQueryPage
     * @return
     */
    protected EsSpuNewAggResp<List<EsSpuNewResp>> packageEsSpuNewAggResp(AggregatedPage<EsSpuNew> resultQueryPage, EsKeyWordSpuNewQueryProviderReq req) {
        EsSpuNewAggResp<List<EsSpuNewResp>> esSpuNewAggResp = new EsSpuNewAggResp<>();

        Map<String, List<String>> key2SearchAggsMap = searchAggsProvider.list(SearchAggsConstant.SPU_SEARCH_AGGS_KEY).getContext();

        List<EsSpuNewAggResp.AggsCategoryResp> resultCategoryAggs = new ArrayList<>();


        //商品类型
        List<EsSpuNewAggResp.AggsResp> spuAggsList = new ArrayList<>();
        Terms spuCategory = resultQueryPage.getAggregations().get("spuCategory");
        for (Terms.Bucket bucket : spuCategory.getBuckets()) {
//            EsSpuNewAggResp.AggsResp spuAggsResp = new EsSpuNewAggResp.AggsResp();
            SearchSpuNewCategoryEnum searchSpuNewCategoryEnum = SearchSpuNewCategoryEnum.get(bucket.getKeyAsNumber().intValue());
            if (searchSpuNewCategoryEnum == null) {
                continue;
            }
//            spuAggsResp.setAggsId(searchSpuNewCategoryEnum.getCode().toString());
//            spuAggsResp.setAggsName(searchSpuNewCategoryEnum.getMessage());
//            spuAggsList.add(spuAggsResp);
            spuAggsList.add(this.packageAggsResp(searchSpuNewCategoryEnum.getCode().toString(),
                    searchSpuNewCategoryEnum.getMessage(), false, searchSpuNewCategoryEnum.getCode().toString()));
        }
        resultCategoryAggs.add(this.packageAggsCategory(SearchSpuNewAggsCategoryEnum.AGGS_SPU_CATEGORY, spuAggsList));



        //聚合标签
        boolean hasShowLabel = true;
        List<EsSpuNewAggResp.AggsResp> labelsAggsList = new ArrayList<>();
        Nested labels = resultQueryPage.getAggregations().get("labels");
        Terms labelNames = labels.getAggregations().get("labelCategory");
        for (Terms.Bucket bucket : labelNames.getBuckets()) {
            SearchSpuNewLabelCategoryEnum spuNewLabelCategoryEnum = SearchSpuNewLabelCategoryEnum.get(bucket.getKeyAsNumber().intValue());
            if (spuNewLabelCategoryEnum == null) {
                continue;
            }

            if (key2SearchAggsMap.get(SearchAggsConstant.SPU_SEARCH_AGGS_LABEL_CATEGORY_KEY) != null
                && key2SearchAggsMap.get(SearchAggsConstant.SPU_SEARCH_AGGS_LABEL_CATEGORY_KEY).contains(spuNewLabelCategoryEnum.getMessage())) {
//                EsSpuNewAggResp.AggsResp labelAggs = new EsSpuNewAggResp.AggsResp();
//                labelAggs.setAggsId(spuNewLabelCategoryEnum.getCode().toString());
//                labelAggs.setAggsName(spuNewLabelCategoryEnum.getMessage());
//                labelAggs.setHasShow(hasShowLabel);
//                labelsAggsList.add(labelAggs);
                labelsAggsList.add(this.packageAggsResp(spuNewLabelCategoryEnum.getCode().toString(),
                        spuNewLabelCategoryEnum.getMessage(), hasShowLabel, spuNewLabelCategoryEnum.getCode().toString()));
            }
        }
        resultCategoryAggs.add(this.packageAggsCategory(SearchSpuNewAggsCategoryEnum.AGGS_LABEL, labelsAggsList));



//        //请求参数信息
        List<EsSpuNewAggResp.AggsResp> reqs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(req.getLabelCategorys())) {
            for (Integer labelCategory : req.getLabelCategorys()) {
                SearchSpuNewLabelCategoryEnum spuNewLabelCategoryEnum = SearchSpuNewLabelCategoryEnum.get(labelCategory);
                if (spuNewLabelCategoryEnum == null) {
                    continue;
                }
//                EsSpuNewAggResp.AggsResp param = new EsSpuNewAggResp.AggsResp();
//                param.setAggsId(spuNewLabelCategoryEnum.getCode().toString());
//                param.setAggsName(spuNewLabelCategoryEnum.getMessage());
//                param.setHasShow(hasShowLabel);
//                reqs.add(param);
                reqs.add(this.packageAggsResp(spuNewLabelCategoryEnum.getCode().toString(),
                        spuNewLabelCategoryEnum.getMessage(), hasShowLabel, spuNewLabelCategoryEnum.getCode().toString()));
            }
        }
        esSpuNewAggResp.setReq(this.packageAggsCategory(SearchSpuNewAggsCategoryEnum.AGGS_LABEL, reqs));


        /**
         * 聚合1级店铺分类
         */
        List<EsSpuNewAggResp.AggsResp> fclassifyAggsList = new ArrayList<>();
        Terms fclassifyNames = resultQueryPage.getAggregations().get("fclassifyName");
        for (Terms.Bucket bucket : fclassifyNames.getBuckets()) {
            if (key2SearchAggsMap.get(SearchAggsConstant.SPU_SEARCH_AGGS_FCLASSIFY_NAME_KEY) != null
                    && key2SearchAggsMap.get(SearchAggsConstant.SPU_SEARCH_AGGS_FCLASSIFY_NAME_KEY).contains(bucket.getKeyAsString())) {
                fclassifyAggsList.add(this.packageAggsResp("", bucket.getKeyAsString(), false, bucket.getKeyAsString()));
            }
        }
        resultCategoryAggs.add(this.packageAggsCategory(SearchSpuNewAggsCategoryEnum.AGGS_FCLASSIFY, fclassifyAggsList));


        Nested book = resultQueryPage.getAggregations().get("book");

        /**
         * 作者
         */
        List<EsSpuNewAggResp.AggsResp> authorAggsList = new ArrayList<>();
        Terms authorName = book.getAggregations().get("authorName");
        for (Terms.Bucket bucket : authorName.getBuckets()) {
            if (key2SearchAggsMap.get(SearchAggsConstant.BOOK_SEARCH_AGGS_AUTHOR_NAMES_KEY) != null
                    && key2SearchAggsMap.get(SearchAggsConstant.BOOK_SEARCH_AGGS_AUTHOR_NAMES_KEY).contains(bucket.getKeyAsString())) {
                authorAggsList.add(this.packageAggsResp("", bucket.getKeyAsString(), false, bucket.getKeyAsString()));
            }
        }
        resultCategoryAggs.add(this.packageAggsCategory(SearchSpuNewAggsCategoryEnum.AGGS_AUTHOR, authorAggsList));


        /**
         * 出版社
         */
        List<EsSpuNewAggResp.AggsResp> publisherAggsList = new ArrayList<>();
        Terms publisher = book.getAggregations().get("publisherName");
        for (Terms.Bucket bucket : publisher.getBuckets()) {
            if (key2SearchAggsMap.get(SearchAggsConstant.BOOK_SEARCH_AGGS_PUBLISHER_NAME_KEY) != null
                    && key2SearchAggsMap.get(SearchAggsConstant.BOOK_SEARCH_AGGS_PUBLISHER_NAME_KEY).contains(bucket.getKeyAsString())) {
                publisherAggsList.add(this.packageAggsResp("", bucket.getKeyAsString(), false, bucket.getKeyAsString()));

            }
        }
        resultCategoryAggs.add(this.packageAggsCategory(SearchSpuNewAggsCategoryEnum.AGGS_PUBLISHER, publisherAggsList));


        /**
         * 奖项
         */
        List<EsSpuNewAggResp.AggsResp> awardAggsList = new ArrayList<>();
        Terms awardName = book.getAggregations().get("awardName");
        for (Terms.Bucket bucket : awardName.getBuckets()) {
            if (key2SearchAggsMap.get(SearchAggsConstant.BOOK_SEARCH_AGGS_AWARD_NAME_KEY) != null
                    && key2SearchAggsMap.get(SearchAggsConstant.BOOK_SEARCH_AGGS_AWARD_NAME_KEY).contains(bucket.getKeyAsString())) {
                awardAggsList.add(this.packageAggsResp("", bucket.getKeyAsString(), false, bucket.getKeyAsString()));

            }
        }
        resultCategoryAggs.add(this.packageAggsCategory(SearchSpuNewAggsCategoryEnum.AGGS_PUBLISHER, awardAggsList));

        /**
         * 从书
         */
        List<EsSpuNewAggResp.AggsResp> clumpAggsList = new ArrayList<>();
        Terms clumpName = book.getAggregations().get("clumpName");
        for (Terms.Bucket bucket : clumpName.getBuckets()) {
            if (key2SearchAggsMap.get(SearchAggsConstant.BOOK_SEARCH_AGGS_CLUMP_NAME_KEY) != null
                    && key2SearchAggsMap.get(SearchAggsConstant.BOOK_SEARCH_AGGS_CLUMP_NAME_KEY).contains(bucket.getKeyAsString())) {
                clumpAggsList.add(this.packageAggsResp("", bucket.getKeyAsString(), false, bucket.getKeyAsString()));

            }
        }
        resultCategoryAggs.add(this.packageAggsCategory(SearchSpuNewAggsCategoryEnum.AGGS_CLUMP, clumpAggsList));


        /**
         * 出品方 producerName
         */
        List<EsSpuNewAggResp.AggsResp> producerAggsList = new ArrayList<>();
        Terms producerName = book.getAggregations().get("producerName");
        for (Terms.Bucket bucket : producerName.getBuckets()) {
            if (key2SearchAggsMap.get(SearchAggsConstant.BOOK_SEARCH_AGGS_PRODUCER_NAME_KEY) != null
                    && key2SearchAggsMap.get(SearchAggsConstant.BOOK_SEARCH_AGGS_PRODUCER_NAME_KEY).contains(bucket.getKeyAsString())) {
                producerAggsList.add(this.packageAggsResp("", bucket.getKeyAsString(), false, bucket.getKeyAsString()));
            }
        }
        resultCategoryAggs.add(this.packageAggsCategory(SearchSpuNewAggsCategoryEnum.AGGS_PRODUCER, producerAggsList));

        /**
         * 价格区间
         */
        List<EsSpuNewAggResp.AggsResp> priceRangeAggsList = new ArrayList<>();
        for (SearchSpuNewPriceRangeEnum value : SearchSpuNewPriceRangeEnum.values()) {
            priceRangeAggsList.add(this.packageAggsResp(value.getCode().toString(), value.getMessage(), false, value.getCode().toString()));
        }
        resultCategoryAggs.add(this.packageAggsCategory(SearchSpuNewAggsCategoryEnum.AGGS_PRICE_RANGE, priceRangeAggsList));

        resultCategoryAggs.sort(new Comparator<EsSpuNewAggResp.AggsCategoryResp>() {
            @Override
            public int compare(EsSpuNewAggResp.AggsCategoryResp o1, EsSpuNewAggResp.AggsCategoryResp o2) {
                return o1.getSort() - o2.getSort();
            }
        });
        esSpuNewAggResp.setAggsCategorys(resultCategoryAggs);
        esSpuNewAggResp.setResult(new CommonPageResp<>(resultQueryPage.getTotalElements(), this.packageEsSpuNewResp(resultQueryPage.getContent())));
        return esSpuNewAggResp;
    }

    /**
     * 打包商品
     * @param esSpuNewList
     * @return
     */
    protected List<EsSpuNewResp> packageEsSpuNewResp(List<EsSpuNew> esSpuNewList) {
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
                EsSpuNewResp.Book book = new EsSpuNewResp.Book();
                book.setIsbn(esSpuNew.getBook().getIsbn());
                book.setAuthorNames(esSpuNew.getBook().getAuthorNames());
                book.setScore(esSpuNew.getBook().getScore());
                book.setPublisher(esSpuNew.getBook().getPublisher());
                book.setFixPrice(esSpuNew.getBook().getFixPrice());

                if (!CollectionUtils.isEmpty(esSpuNew.getBook().getTags())) {
                    List<EsSpuNewResp.Book.SubBookLabel> subBookLabels = new ArrayList<>();
                    for (SubBookLabelNew tag : esSpuNew.getBook().getTags()) {
                        EsSpuNewResp.Book.SubBookLabel subBookLabel = new EsSpuNewResp.Book.SubBookLabel();
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


    /**
     * 包装聚合分类对象信息
     * @return
     */
    private EsSpuNewAggResp.AggsCategoryResp packageAggsCategory(SearchSpuNewAggsCategoryEnum searchSpuNewAggsCategoryEnum,  List<EsSpuNewAggResp.AggsResp> aggsList) {
        EsSpuNewAggResp.AggsCategoryResp aggsCategoryResp = new EsSpuNewAggResp.AggsCategoryResp();
        aggsCategoryResp.setCategory(searchSpuNewAggsCategoryEnum.getCode());
        aggsCategoryResp.setCategoryName(searchSpuNewAggsCategoryEnum.getMessage());
        aggsCategoryResp.setSort(searchSpuNewAggsCategoryEnum.getSort());
        aggsCategoryResp.setAggsList(aggsList);
        return aggsCategoryResp;
    }

    /**
     * 打包aggsResp
     * @param aggsId
     * @param aggsName
     * @param hasShow
     * @param hash
     * @return
     */
    private EsSpuNewAggResp.AggsResp packageAggsResp(String aggsId, String aggsName, boolean hasShow, String hash) {
        EsSpuNewAggResp.AggsResp aggsResp = new EsSpuNewAggResp.AggsResp();
        aggsResp.setAggsId(aggsId);
        aggsResp.setAggsName(aggsName);
        aggsResp.setHasShow(hasShow);
        aggsResp.setHash(hash);
        return aggsResp;
    }
}
