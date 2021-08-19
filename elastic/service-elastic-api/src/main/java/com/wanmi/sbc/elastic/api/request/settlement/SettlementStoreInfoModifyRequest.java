package com.wanmi.sbc.elastic.api.request.settlement;

import com.wanmi.sbc.common.enums.SettleStatus;
import com.wanmi.sbc.common.util.EsConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

/**
 * @Author yangzhen
 * @Description // 商家店铺信息
 * @Date 18:30 2020/12/7
 * @Param
 * @return
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class SettlementStoreInfoModifyRequest {




    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long storeId;



    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    /**
     * 封装公共条件
     * @return
     */
    public SearchQuery getSearchCriteria() {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withIndices( EsConstants.DOC_SETTLEMENT);
        builder.withQuery(this.getWhereCriteria());
        return builder.build();
    }




    public QueryBuilder getWhereCriteria() {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if (Objects.nonNull(storeId)) {
            boolQueryBuilder.must(termQuery("storeId",storeId));
        }

        return boolQueryBuilder;
    }



}
