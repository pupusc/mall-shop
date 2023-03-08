package com.wanmi.sbc.elastic.standard.service;

import com.wanmi.sbc.common.util.ElasticCommonUtil;
import com.wanmi.sbc.common.util.EsConstants;
import com.wanmi.sbc.elastic.api.request.standard.EsStandardPageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * <p>EsGoods表动态查询条件构建器</p>
 * @author dyt
 * @date 2020-12-04 10:39:15
 */
public class EsStandardSearchCriteriaBuilder {

    /**
     * 封装公共条件
     *
     * @return
     */
    private static QueryBuilder getWhereCriteria(EsStandardPageRequest request) {
        BoolQueryBuilder boolQb = QueryBuilders.boolQuery();
        //批量商品编号
        if (CollectionUtils.isNotEmpty(request.getGoodsIds())) {
            boolQb.must(idsQuery().addIds(request.getGoodsIds().toArray(new String[]{})));
        }
        //查询品牌编号
        if (request.getBrandId() != null && request.getBrandId() > 0) {
            boolQb.must(termQuery("brandId", request.getBrandId()));
        }
        //查询分类编号
        if (request.getCateId() != null && request.getCateId() > 0) {
            boolQb.must(termQuery("cateId", request.getCateId()));
        }
        //批量查询品牌编号
        if (CollectionUtils.isNotEmpty(request.getBrandIds())) {
            boolQb.must(termsQuery("brandId", request.getBrandIds()));
        }
        //批量查询分类编号
        if (CollectionUtils.isNotEmpty(request.getCateIds())) {
            boolQb.must(termsQuery("cateId", request.getCateIds()));
        }
        //模糊查询SPU编码
        if (StringUtils.isNotEmpty(request.getLikeGoodsNo())) {
            boolQb.must(wildcardQuery("goodsNo", ElasticCommonUtil.replaceEsLikeWildcard(request.getLikeGoodsNo().trim())));
        }
        //模糊查询SKU编码
        if (StringUtils.isNotEmpty(request.getLikeGoodsInfoNo())) {
            boolQb.must(wildcardQuery("goodsInfoNos", ElasticCommonUtil.replaceEsLikeWildcard(request.getLikeGoodsInfoNo().trim())));
        }
        //模糊查询名称
        if (StringUtils.isNotEmpty(request.getLikeGoodsName())) {
            boolQb.must(wildcardQuery("goodsName", ElasticCommonUtil.replaceEsLikeWildcard(request.getLikeGoodsName().trim())));
        }
        //模糊查询供应商名称
        if (StringUtils.isNotEmpty(request.getLikeProviderName())) {
            boolQb.must(wildcardQuery("providerName", ElasticCommonUtil.replaceEsLikeWildcard(request.getLikeProviderName().trim())));
        }

        // 小于或等于 搜索条件:创建开始时间截止
        if (request.getCreateTimeBegin() != null) {
            boolQb.must(rangeQuery("createTime").gte(request.getCreateTimeBegin()));
        }

        // 大于或等于 搜索条件:创建结束时间开始
        if (request.getCreateTimeEnd() != null) {
            boolQb.must(rangeQuery("createTime").lte(request.getCreateTimeEnd()));
        }

        //批量查询品牌编号
        if (CollectionUtils.isNotEmpty(request.getOrNullBrandIds())) {
            BoolQueryBuilder bq = QueryBuilders.boolQuery();
            bq.should(termsQuery("brandId", request.getOrNullBrandIds()));
            bq.should(QueryBuilders.boolQuery().mustNot(existsQuery("brandId")));
            boolQb.must(bq);
        }

        //非商品编号
        if (StringUtils.isNotBlank(request.getNotGoodsId())) {
            boolQb.mustNot(termQuery("goodsId", request.getNotGoodsId()));
        }
        //商品来源，0供应商，1商家
        if (request.getGoodsSource() != null && CollectionUtils.isEmpty(request.getGoodsSourceList())) {
            boolQb.must(termQuery("goodsSource", request.getGoodsSource()));
        }
        if(request.getThirdPlatformType()!=null){
            boolQb.must(termQuery("thirdPlatformType", request.getThirdPlatformType()));
        }
        if(request.getAddedFlag()!=null){
            boolQb.must(termQuery("addedFlag", request.getAddedFlag()));
        }
        if(request.getDelFlag()!=null){
            boolQb.must(termQuery("delFlag", request.getDelFlag()));
        }

        if(CollectionUtils.isNotEmpty(request.getGoodsSourceList())){
            boolQb.must(termsQuery("goodsSource", request.getGoodsSourceList()));
        }

        //商品库导入条件
        if (request.getToLeadType() != null) {
            if (request.getToLeadType() == 1) {
                boolQb.must(termQuery("relStoreIds", request.getStoreId()));
            } else if (request.getToLeadType() == 2) {
                boolQb.mustNot(termQuery("relStoreIds", request.getStoreId()));
            }
        }
        return boolQb;
    }


    private static List<SortBuilder> getSorts(EsStandardPageRequest request) {
        List<SortBuilder> sortBuilders = new ArrayList<>();
        if(MapUtils.isNotEmpty(request.getSortMap())){
            request.getSortMap().forEach((k, v) -> {
                sortBuilders.add(new FieldSortBuilder(k).order(SortOrder.DESC.name().equalsIgnoreCase(v) ? SortOrder.DESC : SortOrder.ASC));
            });
        }
        return sortBuilders;
    }



    public static SearchQuery getSearchCriteria(EsStandardPageRequest request) {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withIndices(EsConstants.DOC_STANDARD_GOODS);
        builder.withTypes(EsConstants.DOC_STANDARD_GOODS);
        builder.withQuery(getWhereCriteria(request));

        builder.withPageable(request.getPageable());
        List<SortBuilder> sortBuilders = getSorts(request);
        if (CollectionUtils.isNotEmpty(sortBuilders)) {
            sortBuilders.forEach(builder::withSort);
        }

        System.out.println("where3===>" + getWhereCriteria(request).toString());
        System.out.println("sort3===>" + sortBuilders.toString());

        return builder.build();
    }
}
