package com.wanmi.sbc.elastic.api.request.customerFunds;

import com.wanmi.sbc.common.util.EsConstants;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.io.Serializable;
import java.math.BigDecimal;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

/**
 * @Author yangzhen
 * @Description //会员资金信息
 * @Date 14:29 2020/12/11
 * @Param
 * @return
 **/
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EsCustomerFundsGrantAmountModifyRequest implements Serializable {

    private static final long serialVersionUID = 4555211803309442026L;

    /**
     * 主键
     */
    private String customerFundsId;

    /**
     * 会员ID
     */
    private String customerId;

    /**
     * 账户余额
     */
    private BigDecimal accountBalance;

    /**
     * 冻结余额
     */
    private BigDecimal blockedBalance;

    /**
     * 可提现金额
     */
    private BigDecimal withdrawAmount;

    /**
     * 是否分销员，0：否，1：是
     */
    private Integer distributor;


    /**
     * 变更金额
     */
    private BigDecimal amount;

    /**
     * 封装公共条件
     * @return
     */
    public SearchQuery getSearchCriteria() {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withIndices( EsConstants.DOC_CUSTOMER_FUNDS);
        builder.withQuery(this.getWhereCriteria());
        return builder.build();
    }


    public QueryBuilder getWhereCriteria() {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if (StringUtils.isNotEmpty(customerId)) {
            boolQueryBuilder.must(termQuery("customerId", customerId));

        }

        if (StringUtils.isNotEmpty(customerFundsId)) {
            boolQueryBuilder.must(termQuery("customerFundsId", customerFundsId));
        }

        return boolQueryBuilder;
    }


}
