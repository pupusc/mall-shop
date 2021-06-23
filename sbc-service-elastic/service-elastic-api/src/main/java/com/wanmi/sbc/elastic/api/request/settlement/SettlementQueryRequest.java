package com.wanmi.sbc.elastic.api.request.settlement;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.SettleStatus;
import com.wanmi.sbc.common.enums.StoreType;
import com.wanmi.sbc.common.util.*;
import com.wanmi.sbc.customer.bean.enums.AccountState;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.enums.StoreState;
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
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.elasticsearch.index.query.QueryBuilders.*;

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
public class SettlementQueryRequest {




    /**
     * 结算单号id列表
     */
    @ApiModelProperty(value = "结算单号id列表")
    private List<Long> settleIdLists;



    /**
     * 结算状态 {@link SettleStatus}
     */
    @ApiModelProperty(value = "结算状态")
    @NotNull
    private Integer status;

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

        if (CollectionUtils.isNotEmpty(settleIdLists)) {
            boolQueryBuilder.must(termsQuery("settleId", settleIdLists));
        }

        return boolQueryBuilder;
    }



}
