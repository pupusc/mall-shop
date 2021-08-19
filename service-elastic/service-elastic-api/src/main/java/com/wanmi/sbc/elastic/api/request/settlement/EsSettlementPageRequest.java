package com.wanmi.sbc.elastic.api.request.settlement;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.StoreType;
import com.wanmi.sbc.common.util.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.util.List;
import java.util.Objects;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * 结算分页查询请求
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class EsSettlementPageRequest extends BaseQueryRequest {

    private static final long serialVersionUID = -5115285012717445946L;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private String startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private String endTime;

    /**
     * 店铺Id
     */
    @ApiModelProperty(value = "店铺Id")
    private Long storeId;

    /**
     * 结算状态
     */
    @ApiModelProperty(value = "结算状态")
    private Integer settleStatus;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    /**
     * 批量店铺ID
     */
    @ApiModelProperty(value = "批量店铺ID")
    private List<Long> storeListId;

    /**
     * 商家类型 0供应商 1商家
     */
    @ApiModelProperty(value = "商家类型")
    private StoreType storeType;



    /**
     * 封装公共条件
     * @return
     */
    public SearchQuery getSearchCriteria() {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withIndices( EsConstants.DOC_SETTLEMENT);
        builder.withQuery(this.getWhereCriteria());
        builder.withPageable(this.getPageable());
        builder.withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC));

        return builder.build();
    }




    public QueryBuilder getWhereCriteria() {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //店铺名称模糊查询
        if (StringUtils.isNotBlank(storeName)) {
            boolQueryBuilder.must(wildcardQuery("storeName", ElasticCommonUtil.replaceEsLikeWildcard(storeName)));
        }
        //结算状态
        if(Objects.nonNull(settleStatus) ){
            boolQueryBuilder.must(termQuery("settleStatus",settleStatus));
        }

        //店铺类型
        if(Objects.nonNull(storeType) ){
            boolQueryBuilder.must(termQuery("storeType",storeType.toValue()));
        }

        //店铺id
        if(Objects.nonNull(storeId) ){
            boolQueryBuilder.must(termQuery("storeId",storeId));
        }

        /**
         *  开始
         */
        if (StringUtils.isNotEmpty(startTime)) {
            boolQueryBuilder.must(rangeQuery("createTime").gte(ElasticCommonUtil.localDateFormat(startTime)));
        }

        /**
         *  结束
         */
        if (StringUtils.isNotEmpty(endTime)) {
            boolQueryBuilder.must(rangeQuery("createTime").lte(ElasticCommonUtil.plusOneDayLocalDateFormat(endTime)));
        }
        return boolQueryBuilder;
    }


}
