package com.wanmi.sbc.elastic.spu.serivce;

import com.wanmi.sbc.common.util.ElasticCommonUtil;
import com.wanmi.sbc.common.util.EsConstants;
import com.wanmi.sbc.elastic.api.request.spu.EsSpuPageRequest;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.enums.GoodsSelectStatus;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortMode;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
public class EsSpuSearchCriteriaBuilder {

    /**
     * 封装公共条件
     *
     * @return
     */
    private static QueryBuilder getWhereCriteria(EsSpuPageRequest request) {
        BoolQueryBuilder boolQb = QueryBuilders.boolQuery();

        if (Objects.nonNull(request.getGoodsChannelType())) {
            boolQb.must(QueryBuilders.termQuery("goodsChannelTypeList", request.getGoodsChannelType()));
        }

        //批量商品编号
        if (CollectionUtils.isNotEmpty(request.getGoodsIds())) {
            boolQb.must(idsQuery().addIds(request.getGoodsIds().toArray(new String[]{})));
        }
        //批量店铺分类关联商品编号
        if (CollectionUtils.isNotEmpty(request.getStoreCateGoodsIds())) {
            boolQb.must(idsQuery().addIds(request.getStoreCateGoodsIds().toArray(new String[]{})));
        }
        if (Objects.nonNull(request.getGroupSearch())) {

            boolQb.mustNot(QueryBuilders.termQuery("goodsType", GoodsType.VIRTUAL_COUPON.toValue()));

            boolQb.mustNot(QueryBuilders.termQuery("goodsType", GoodsType.VIRTUAL_GOODS.toValue()));

        }
        //查询SPU编码
        if (StringUtils.isNotBlank(request.getGoodsNo())) {
            boolQb.must(termQuery("goodsNo", request.getGoodsNo()));
        }
        //批量查询SPU编码
        if (CollectionUtils.isNotEmpty(request.getGoodsNos())) {
            boolQb.must(termsQuery("goodsNo", request.getGoodsNos()));
        }
        //查询品牌编号
        if (request.getBrandId() != null && request.getBrandId() > 0) {
            boolQb.must(termQuery("goodsBrand.brandId", request.getBrandId()));
        }
        //查询分类编号
        if (request.getCateId() != null && request.getCateId() > 0) {
            boolQb.must(termQuery("goodsCate.cateId", request.getCateId()));
        }
        //批量查询分类编号
        if (CollectionUtils.isNotEmpty(request.getCateIds())) {
            boolQb.must(termsQuery("goodsCate.cateId", request.getCateIds()));
        }
        //批量查询分类编号
        if (CollectionUtils.isNotEmpty(request.getBrandIds())) {
            boolQb.must(termsQuery("goodsBrand.brandId", request.getBrandIds()));
        }
        //公司信息ID
        if (request.getCompanyInfoId() != null) {
            boolQb.must(termQuery("companyInfoId", request.getCompanyInfoId()));
        }
        //店铺ID
        if (Objects.nonNull(request.getStoreId())) {
            boolQb.must(termQuery("storeId", request.getStoreId()));
        }
        //模糊查询SPU编码
        if (StringUtils.isNotEmpty(request.getLikeGoodsNo())) {
            boolQb.must(wildcardQuery("goodsNo", ElasticCommonUtil.replaceEsLikeWildcard(request.getLikeGoodsNo().trim())));
        }
        //模糊查询SKU编码
        if (StringUtils.isNotEmpty(request.getLikeGoodsInfoNo())) {
            boolQb.must(wildcardQuery("goodsInfos.goodsInfoNo", ElasticCommonUtil.replaceEsLikeWildcard(request.getLikeGoodsInfoNo().trim())));
        }
        //模糊查询名称
        if (StringUtils.isNotEmpty(request.getLikeGoodsName())) {
            boolQb.must(wildcardQuery("goodsName", ElasticCommonUtil.replaceEsLikeWildcard(request.getLikeGoodsName().trim())));
        }
        //模糊查询商家名称
        if (StringUtils.isNotBlank(request.getLikeSupplierName())) {
            boolQb.must(wildcardQuery("supplierName", ElasticCommonUtil.replaceEsLikeWildcard(request.getLikeSupplierName().trim())));
        }
        //模糊查询供应商名称
        if (StringUtils.isNotBlank(request.getLikeProviderName())) {
            boolQb.must(wildcardQuery("providerName", ElasticCommonUtil.replaceEsLikeWildcard(request.getLikeProviderName().trim())));
        }

        //关键词搜索
        if (StringUtils.isNotBlank(request.getKeyword())) {
            String str = ElasticCommonUtil.replaceEsLikeWildcard(request.getKeyword().trim());
            BoolQueryBuilder bq = QueryBuilders.boolQuery();
            bq.should(wildcardQuery("goodsName", str));
            bq.should(wildcardQuery("goodsNo", str));
            boolQb.must(bq);
        }

        //审核状态
        if (request.getAuditStatus() != null) {
            boolQb.must(termQuery("auditStatus", request.getAuditStatus().toValue()));
        }

        //上下架状态
        if (request.getAddedFlag() != null) {
            boolQb.must(termQuery("addedFlag", request.getAddedFlag()));
        }

        //多个上下架状态
        if (CollectionUtils.isNotEmpty(request.getAddedFlags())) {
            boolQb.must(termsQuery("addedFlag", request.getAddedFlags()));
        }

        //批量审核状态
        if (CollectionUtils.isNotEmpty(request.getAuditStatusList())) {
            boolQb.must(termsQuery("auditStatus", request.getAuditStatusList().stream().map(CheckStatus::toValue).collect(Collectors.toList())));
        }

        //查询标签关联
        if (Objects.nonNull(request.getLabelId())) {
            BoolQueryBuilder bq = QueryBuilders.boolQuery();
            bq.must(termQuery("goodsLabelList.goodsLabelId", request.getLabelId()));
            boolQb.must(nestedQuery("goodsLabelList", bq, ScoreMode.None));
        }

        //非商品编号
        if (StringUtils.isNotBlank(request.getNotGoodsId())) {
            boolQb.mustNot(idsQuery().addIds(request.getNotGoodsId()));
        }

        if (CollectionUtils.isNotEmpty(request.getNotGoodsIdList())) {
            boolQb.mustNot(idsQuery().addIds(request.getNotGoodsIdList().toArray(new String[]{})));
        }

        //销售类型
        if (!ObjectUtils.isEmpty(request.getSaleType())) {
            boolQb.must(termQuery("goodsInfos.saleType", request.getSaleType()));
        }

        //商品类型
        if (!ObjectUtils.isEmpty(request.getGoodsType())) {
            boolQb.must(termQuery("goodsType", request.getGoodsType()));
        }

        //商品来源
        if (!ObjectUtils.isEmpty(request.getGoodsSource())) {
            boolQb.must(termQuery("goodsSource", request.getGoodsSource()));
        }

        //ERP的SPU编码
        if (Objects.nonNull(request.getSpuErp())) {
            boolQb.must(termQuery("goodsInfos.erpGoodsNo",request.getSpuErp()));
        }

        //商品状态筛选
        if (CollectionUtils.isNotEmpty(request.getGoodsSelectStatuses())) {
            BoolQueryBuilder orBq = QueryBuilders.boolQuery();
            request.getGoodsSelectStatuses().forEach(goodsInfoSelectStatus -> {
                if (goodsInfoSelectStatus != null) {
                    if (goodsInfoSelectStatus == GoodsSelectStatus.ADDED) {
                        BoolQueryBuilder bq = QueryBuilders.boolQuery();
                        bq.should(termQuery("auditStatus", CheckStatus.CHECKED.toValue()));
                        bq.should(termQuery("addedFlag", AddedFlag.YES.toValue()));
                        orBq.should(bq);
                    } else if (goodsInfoSelectStatus == GoodsSelectStatus.NOT_ADDED) {
                        BoolQueryBuilder bq = QueryBuilders.boolQuery();
                        bq.should(termQuery("auditStatus", CheckStatus.CHECKED.toValue()));
                        bq.should(termQuery("addedFlag", AddedFlag.NO.toValue()));
                        orBq.should(bq);
                    } else if (goodsInfoSelectStatus == GoodsSelectStatus.PART_ADDED) {
                        BoolQueryBuilder bq = QueryBuilders.boolQuery();
                        bq.should(termQuery("auditStatus", CheckStatus.CHECKED.toValue()));
                        bq.should(termQuery("addedFlag", AddedFlag.PART.toValue()));
                        orBq.should(bq);
                    } else if (goodsInfoSelectStatus == GoodsSelectStatus.OTHER) {
                        List<Integer> status = Arrays.asList(CheckStatus.FORBADE.toValue(), CheckStatus.NOT_PASS.toValue(), CheckStatus.WAIT_CHECK.toValue());
                        BoolQueryBuilder bq = QueryBuilders.boolQuery();
                        bq.should(termsQuery("auditStatus", status));
                        orBq.should(bq);
                    }
                }
            });
            boolQb.must(orBq);
        }
        return boolQb;
    }

    private static List<SortBuilder> getSorts(EsSpuPageRequest request) {
        List<SortBuilder> sortBuilders = new ArrayList<>();
        SortOrder order = SortOrder.DESC;
        if (SortOrder.ASC.name().equalsIgnoreCase(request.getSortRole())) {
            order = SortOrder.ASC;
        }

        if (Objects.nonNull(request.getGoodsSortType())) {
            switch (request.getGoodsSortType()) {
                case MARKET_PRICE:
                    sortBuilders.add(new FieldSortBuilder("goodsInfos.marketPrice").order(order).sortMode(SortMode.MIN));
                    break;
                case STOCK:
                    sortBuilders.add(new FieldSortBuilder("stock").order(order));
                    break;
                case SALES_NUM:
                    sortBuilders.add(new FieldSortBuilder("realGoodsSalesNum").order(order));
                    break;
                default:
                    sortBuilders.add(new FieldSortBuilder("createTime").order(order));
                    break;//按创建时间倒序、ID升序
            }
        } else {
            sortBuilders.add(new FieldSortBuilder("createTime").order(order));
        }
        return sortBuilders;
    }



    public static SearchQuery getSearchCriteria(EsSpuPageRequest request) {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withIndices(EsConstants.DOC_GOODS_TYPE);
        builder.withQuery(getWhereCriteria(request));
        System.out.println("where===>" + getWhereCriteria(request).toString());
        builder.withPageable(request.getPageable());
        List<SortBuilder> sortBuilders = getSorts(request);
        if (CollectionUtils.isNotEmpty(sortBuilders)) {
            sortBuilders.forEach(builder::withSort);
        }
        return builder.build();
    }
}
