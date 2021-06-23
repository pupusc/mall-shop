package com.wanmi.sbc.elastic.api.request.customerFunds;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.StoreType;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.EsConstants;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * 会员资金查询请求
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class EsCustomerFundsPageRequest extends BaseQueryRequest {

    private static final long serialVersionUID = -5115285012717445946L;

    /**
     * 批量查询-会员资金主键List
     */
    @ApiModelProperty(value = "批量查询-会员资金主键List")
    private List<String> customerFundsIdList;

    /**
     * 会员账号
     */
    @ApiModelProperty(value = "会员账号")
    private String customerAccount;

    /**
     * 会员名称
     */
    @ApiModelProperty(value = "会员名称")
    private String customerName;

    /**
     * 是否分销员，-1：全部，0：否，1：是
     */
    @ApiModelProperty(value = "是否分销员")
    private Integer distributor;

    /**
     * 账户余额开始
     */
    @ApiModelProperty(value = "账户余额开始")
    private BigDecimal startAccountBalance;

    /**
     * 账户余额结束
     */
    @ApiModelProperty(value = "账户余额结束")
    private BigDecimal endAccountBalance;

    /**
     * 冻结余额开始
     */
    @ApiModelProperty(value = "冻结余额开始")
    private BigDecimal startBlockedBalance;

    /**
     * 冻结余额结束
     */
    @ApiModelProperty(value = "冻结余额结束")
    private BigDecimal endBlockedBalance;

    /**
     * 可提现金额开始
     */
    @ApiModelProperty(value = "可提现金额开始")
    private BigDecimal startWithdrawAmount;

    /**
     * 可提现金额结束
     */
    @ApiModelProperty(value = "可提现金额结束")
    private BigDecimal endWithdrawAmount;



    /**
     * 封装公共条件
     * @return
     */
    public SearchQuery getSearchCriteria() {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withIndices( EsConstants.DOC_CUSTOMER_FUNDS);
        builder.withQuery(this.getWhereCriteria());
        builder.withPageable(this.getPageable());
        if(StringUtils.equals("asc",this.getSortRole())){
            builder.withSort(SortBuilders.fieldSort(this.getSortColumn()).order(SortOrder.ASC));
        }else{
            builder.withSort(SortBuilders.fieldSort(this.getSortColumn()).order(SortOrder.DESC));
        }

        return builder.build();
    }




    public QueryBuilder getWhereCriteria() {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //会员账号模糊查询
        if (StringUtils.isNotBlank(customerAccount)) {
            boolQueryBuilder.must(wildcardQuery("customerAccount", StringUtil.ES_LIKE_CHAR.concat(XssUtils.replaceEsLikeWildcard(customerAccount.trim())).concat(StringUtil.ES_LIKE_CHAR)));
        }
        //会员名称模糊查询
        if (StringUtils.isNotBlank(customerName)) {
            boolQueryBuilder.must(wildcardQuery("customerName", StringUtil.ES_LIKE_CHAR.concat(XssUtils.replaceEsLikeWildcard(customerName.trim())).concat(StringUtil.ES_LIKE_CHAR)));
        }
        //是否分销员
        if(Objects.nonNull(distributor) &&  distributor != -1){
            boolQueryBuilder.must(termQuery("distributor",distributor));
        }


        /**
         *  账户余额开始
         */
        if (Objects.nonNull(startAccountBalance)) {
            boolQueryBuilder.must(rangeQuery("accountBalance").gte(startAccountBalance.doubleValue()));
        }

        /**
         *  账户余额结束
         */
        if (Objects.nonNull(endAccountBalance)) {
            boolQueryBuilder.must(rangeQuery("accountBalance").lte(endAccountBalance.doubleValue()));
        }

        /**
         *  冻结余额开始
         */
        if (Objects.nonNull(startBlockedBalance)) {
            boolQueryBuilder.must(rangeQuery("blockedBalance").gte(startBlockedBalance.doubleValue()));
        }

        /**
         *  冻结余额结束
         */
        if (Objects.nonNull(endBlockedBalance)) {
            boolQueryBuilder.must(rangeQuery("blockedBalance").lte(endBlockedBalance.doubleValue()));
        }

        /**
         *  可提现余额开始
         */
        if (Objects.nonNull(startWithdrawAmount)) {
            boolQueryBuilder.must(rangeQuery("withdrawAmount").gte(startWithdrawAmount.doubleValue()));
        }

        /**
         *  可提现余额结束
         */
        if (Objects.nonNull(endWithdrawAmount)) {
            boolQueryBuilder.must(rangeQuery("withdrawAmount").lte(endWithdrawAmount.doubleValue()));
        }
        return boolQueryBuilder;
    }


}
