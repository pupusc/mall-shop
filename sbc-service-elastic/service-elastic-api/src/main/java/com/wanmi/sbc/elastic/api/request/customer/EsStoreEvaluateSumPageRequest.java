package com.wanmi.sbc.elastic.api.request.customer;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>店铺评价分页查询请求参数</p>
 *
 * @author liutao
 * @date 2019-02-23 10:59:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EsStoreEvaluateSumPageRequest extends BaseQueryRequest {
    private static final long serialVersionUID = 1L;

    /**
     * 批量查询-id 主键List
     */
    private List<Long> sumIdList;

    /**
     * id 主键
     */
    private Long sumId;

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 服务综合评分
     */
    private BigDecimal sumServerScore;

    /**
     * 商品质量综合评分
     */
    private BigDecimal sumGoodsScore;

    /**
     * 物流综合评分
     */
    private BigDecimal sumLogisticsScoreScore;

    /**
     * 订单数
     */
    private Integer orderNum;

    /**
     * 评分周期 0：30天，1：90天，2：180天
     */
    private Integer scoreCycle;

    /**
     * 综合评分
     */
    private BigDecimal sumCompositeScore;

    /**
     * 商家评价查询条件
     *
     * @return
     */
    public NativeSearchQuery esCriteria() {

        BoolQueryBuilder bool = QueryBuilders.boolQuery();
        // 批量查询-id 主键List
        if (CollectionUtils.isNotEmpty(this.getSumIdList())) {
            bool.must(QueryBuilders.termsQuery("sumId", this.getSumIdList()));
        }

        // id 主键
        if (this.getSumId() != null) {
            bool.must(QueryBuilders.termQuery("sumId", this.getSumId()));
        }

        // 店铺id
        if (this.getStoreId() != null) {
            bool.must(QueryBuilders.termQuery("storeId", this.getStoreId()));
        }

        // 模糊查询 - 店铺名称
        if (StringUtils.isNotEmpty(this.getStoreName())) {
            bool.must(QueryBuilders.wildcardQuery("storeName", "*" + this.getStoreName() + "*"));
        }

        // 服务综合评分
        if (this.getSumServerScore() != null) {
            bool.must(QueryBuilders.termQuery("sumServerScore", this.getSumServerScore().doubleValue()));
        }

        // 商品质量综合评分
        if (this.getSumGoodsScore() != null) {
            bool.must(QueryBuilders.termQuery("sumGoodsScore", this.getSumGoodsScore().doubleValue()));
        }

        // 物流综合评分
        if (this.getSumLogisticsScoreScore() != null) {
            bool.must(QueryBuilders.termQuery("sumLogisticsScoreScore", this.getSumLogisticsScoreScore().doubleValue()));
        }

        // 订单数
        if (this.getOrderNum() != null) {
            bool.must(QueryBuilders.termQuery("orderNum", this.getOrderNum()));
        }

        // 评分周期 0：30天，1：90天，2：180天
        if (this.getScoreCycle() != null) {
            bool.must(QueryBuilders.termQuery("scoreCycle", this.getScoreCycle()));
        }

        // 综合评分
        if (this.getSumCompositeScore() != null) {
            bool.must(QueryBuilders.termQuery("sumCompositeScore", this.getSumCompositeScore().doubleValue()));
        }

        SortOrder sortOrder = StringUtils.equalsIgnoreCase(this.getSortRole(), "ASC") ? SortOrder.ASC : SortOrder.DESC;
        FieldSortBuilder order = SortBuilders.fieldSort(this.getSortColumn()).order(sortOrder);
        return new NativeSearchQueryBuilder()
                .withQuery(bool)
                .withPageable(this.getPageable())
                .withSort(order)
                .build();
    }

}