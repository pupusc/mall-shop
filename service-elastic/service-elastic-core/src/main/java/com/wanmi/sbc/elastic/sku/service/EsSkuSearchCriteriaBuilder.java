package com.wanmi.sbc.elastic.sku.service;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.ElasticCommonUtil;
import com.wanmi.sbc.common.util.EsConstants;
import com.wanmi.sbc.elastic.api.request.sku.EsSkuPageRequest;
import com.wanmi.sbc.elastic.api.common.CommonEsSearchCriteriaBuilder;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.enums.GoodsInfoSelectStatus;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.existsQuery;
import static org.elasticsearch.index.query.QueryBuilders.idsQuery;
import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;
import static org.elasticsearch.index.query.QueryBuilders.wildcardQuery;

/**
 * <p>EsGoods表动态查询条件构建器</p>
 * @author dyt
 * @date 2020-12-04 10:39:15
 */
public class EsSkuSearchCriteriaBuilder {

    /**
     * 封装公共条件
     *
     * @return
     */
    private static QueryBuilder getWhereCriteria(EsSkuPageRequest request) {
        BoolQueryBuilder boolQb = CommonEsSearchCriteriaBuilder.getSkuCommonSearchCriterialBuilder(request);

        //批量SKU编号
        if(CollectionUtils.isNotEmpty(request.getGoodsInfoIds())){
            boolQb.must(idsQuery().addIds(request.getGoodsInfoIds().toArray(new String[]{})));
        }

        if(CollectionUtils.isNotEmpty(request.getNotGoodsInfoIds())){
            boolQb.mustNot(idsQuery().addIds(request.getNotGoodsInfoIds().toArray(new String[]{})));
        }

        //SPU编号
        if(StringUtils.isNotBlank(request.getGoodsId())){
            boolQb.must(termQuery("goodsInfo.goodsId", request.getGoodsId()));
        }

        //批量SPU编号
        if(CollectionUtils.isNotEmpty(request.getGoodsIds())){
            boolQb.must(termsQuery("goodsInfo.goodsId", request.getGoodsIds()));
        }

        //批量SKU No编号
        if(CollectionUtils.isNotEmpty(request.getGoodsInfoNos())){
            boolQb.must(termsQuery("goodsInfo.goodsInfoNo", request.getGoodsInfoNos()));
        }

        //批量SPU NO编号
        if(CollectionUtils.isNotEmpty(request.getGoodsNos())){
            boolQb.must(termsQuery("goodsNo", request.getGoodsNos()));
        }

        //SPU编码
        if(StringUtils.isNotEmpty(request.getLikeGoodsNo())){
            boolQb.must(wildcardQuery("goodsNo", ElasticCommonUtil.replaceEsLikeWildcard(request.getLikeGoodsNo().trim())));
        }

        //SKU编码
        if(StringUtils.isNotEmpty(request.getLikeGoodsInfoNo())){
            boolQb.must(wildcardQuery("goodsInfo.goodsInfoNo", ElasticCommonUtil.replaceEsLikeWildcard(request.getLikeGoodsInfoNo().trim())));
        }

        //店铺ID
        if(request.getStoreId() != null){
            boolQb.must(termQuery("goodsInfo.storeId", request.getStoreId()));
        }

        //分类ID
        if(request.getCateId() != null && request.getCateId() > 0){
            boolQb.must(termQuery("goodsInfo.cateId", request.getCateId()));
        }

        //分类ID
        if(CollectionUtils.isNotEmpty(request.getCateIds())){
            boolQb.must(termsQuery("goodsInfo.cateId", request.getCateIds()));
        }

        //公司信息ID
        if(request.getCompanyInfoId() != null){
            boolQb.must(termQuery("goodsInfo.companyInfoId", request.getCompanyInfoId()));
        }

        if (request.getNotThirdPlatformType() != null && request.getNotThirdPlatformType().size() > 0) {
            BoolQueryBuilder bq = QueryBuilders.boolQuery();
            bq.should(QueryBuilders.boolQuery().mustNot(termsQuery("thirdPlatformType", request.getNotThirdPlatformType())));
            bq.should(QueryBuilders.boolQuery().mustNot(existsQuery("thirdPlatformType")));
            boolQb.must(bq);
        }

        //批量店铺ID
        if(CollectionUtils.isNotEmpty(request.getStoreIds())){
            boolQb.must(termsQuery("goodsInfo.storeId", request.getStoreIds()));
        }

        //模糊查询名称
        if(StringUtils.isNotEmpty(request.getLikeGoodsName())){
            boolQb.must(wildcardQuery("goodsName", ElasticCommonUtil.replaceEsLikeWildcard(request.getLikeGoodsName().trim())));
        }

        //关键字搜索
        if (StringUtils.isNotBlank(request.getKeyword())) {
            String str = ElasticCommonUtil.replaceEsLikeWildcard(request.getKeyword().trim());
            BoolQueryBuilder bq = QueryBuilders.boolQuery();
            bq.should(wildcardQuery("goodsName", str));
            bq.should(wildcardQuery("goodsNo", str));
            boolQb.must(bq);
        }

        //上下架状态
        if (request.getAddedFlag() != null) {
            boolQb.must(termQuery("goodsInfo.addedFlag", request.getAddedFlag()));
        }

        //多个上下架状态
        if (CollectionUtils.isNotEmpty(request.getAddedFlags())) {
            boolQb.must(termsQuery("goodsInfo.addedFlag", request.getAddedFlags()));
        }

        //多个品牌
        if (request.getBrandId() != null && request.getBrandId() > 0) {
            boolQb.must(termQuery("goodsInfo.brandId", request.getBrandId()));
        }

        //审核状态
        if(request.getAuditStatus() != null){
            boolQb.must(termQuery("auditStatus", request.getAuditStatus().toValue()));
        }

        //多个审核状态
        if(CollectionUtils.isNotEmpty(request.getAuditStatuses())){
            boolQb.must(termsQuery("auditStatus", request.getAuditStatuses().stream().map(CheckStatus::toValue).collect(Collectors.toList())));
        }

        // 过滤积分价商品
        if (Objects.nonNull(request.getIntegralPriceFlag()) && Objects.equals(Boolean.TRUE, request.getIntegralPriceFlag())){
            boolQb.must(termQuery("goodsInfo.buyPoint", 0));
        }

        //删除标记
        if (request.getDelFlag() != null) {
            boolQb.must(termQuery("goodsInfo.delFlag", request.getDelFlag()));
        }

        //非商品编号
        if(StringUtils.isNotBlank(request.getNotGoodsId())){
            boolQb.mustNot(termQuery("goodsInfo.goodsId", request.getNotGoodsId()));
        }

        //非商品SKU编号
        if(StringUtils.isNotBlank(request.getNotGoodsInfoId())){
            boolQb.mustNot(idsQuery().addIds(request.getNotGoodsInfoId()));
        }

        // 商家类型
        if(Objects.nonNull(request.getCompanyType())){
            boolQb.must(termQuery("goodsInfo.companyType", request.getCompanyType().toValue()));
        }

        //查询标签关联
        if (Objects.nonNull(request.getLabelId())) {
            BoolQueryBuilder bq = QueryBuilders.boolQuery();
            bq.must(termQuery("goodsLabelList.goodsLabelId", request.getLabelId()));
            boolQb.must(nestedQuery("goodsLabelList", bq, ScoreMode.None));
        }

        //可售性
        if(request.getVendibility() != null) {
            boolQb.must(termQuery("goodsInfo.vendibilityStatus", DefaultFlag.YES.toValue()));
            boolQb.must(termQuery("goodsInfo.providerStatus", Constants.yes));
        }

        //业务员app商品状态筛选
        if(CollectionUtils.isNotEmpty(request.getGoodsSelectStatuses())){
            BoolQueryBuilder orBq = QueryBuilders.boolQuery();
            request.getGoodsSelectStatuses().forEach(goodsInfoSelectStatus -> {
                if(goodsInfoSelectStatus != null){
                    if(goodsInfoSelectStatus == GoodsInfoSelectStatus.ADDED){
                        BoolQueryBuilder bq = QueryBuilders.boolQuery();
                        bq.should(termQuery("auditStatus", CheckStatus.CHECKED.toValue()));
                        bq.should(termQuery("goodsInfo.addedFlag", AddedFlag.YES.toValue()));
                        orBq.should(bq);
                    } else if(goodsInfoSelectStatus == GoodsInfoSelectStatus.NOT_ADDED){
                        BoolQueryBuilder bq = QueryBuilders.boolQuery();
                        bq.should(termQuery("auditStatus", CheckStatus.CHECKED.toValue()));
                        bq.should(termQuery("goodsInfo.addedFlag", AddedFlag.NO.toValue()));
                        orBq.should(bq);
                    } else if(goodsInfoSelectStatus == GoodsInfoSelectStatus.OTHER){
                        List<Integer> status = Arrays.asList(CheckStatus.FORBADE.toValue(), CheckStatus.NOT_PASS.toValue(), CheckStatus.WAIT_CHECK.toValue());
                        orBq.should(termsQuery("auditStatus", status));
                    }
                }
            });
            boolQb.must(orBq);
        }

        if (request.getSaleType() != null){
            boolQb.must(termQuery("goodsInfo.saleType", request.getSaleType()));
        }


        // 查询商品类型
        if (request.getGoodsType()!=null){
            boolQb.must(termQuery("goodsInfo.goodsType",request.getGoodsType()));
        }


        //周期购关联赠品,过滤周期购商品
        if (request.getCycleGift()){
            boolQb.mustNot(termQuery("goodsInfo.goodsType",3));
        }

        // 商品来源
        if(Objects.nonNull(request.getGoodsSource())){
            boolQb.must(termQuery("goodsSource", request.getGoodsSource()));
        }
        return boolQb;
    }

    private static List<SortBuilder> getSorts(EsSkuPageRequest request) {
        List<SortBuilder> sortBuilders = new ArrayList<>();
        if(MapUtils.isNotEmpty(request.getSortMap())) {
            request.getSortMap().forEach((k, v) -> {
                SortOrder order = SortOrder.DESC;
                if (SortOrder.ASC.name().equalsIgnoreCase(v)) {
                    order = SortOrder.ASC;
                }
                sortBuilders.add(new FieldSortBuilder(k).order(order));
            });
        }
        return sortBuilders;
    }

    public static SearchQuery getSearchCriteria(EsSkuPageRequest request) {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withIndices(EsConstants.DOC_GOODS_INFO_TYPE);
        builder.withQuery(getWhereCriteria(request));

        if(CollectionUtils.isNotEmpty(request.getFilterCols())){
            builder.withSourceFilter(new FetchSourceFilter(request.getFilterCols().toArray(new String[]{}),null));
        }

        System.out.println("where===>" + getWhereCriteria(request).toString());
        builder.withPageable(request.getPageable());
        List<SortBuilder> sortBuilders = getSorts(request);
        if (CollectionUtils.isNotEmpty(sortBuilders)) {
            sortBuilders.forEach(builder::withSort);
        }
        return builder.build();
    }
}
