package com.wanmi.sbc.elastic.api.request.customer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EsDistributionCustomerPageRequest extends BaseQueryRequest {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "token")
    private String token;

    /**
     * 批量查询-分销员标识UUIDList
     */
    @ApiModelProperty(value = "批量查询-分销员标识")
    private List<String> distributionIdList;

    /**
     * 分销员标识UUID
     */
    @ApiModelProperty(value = "分销员标识UUID")
    private String distributionId;

    /**
     * 会员ID
     */
    @ApiModelProperty(value = "会员ID")
    private String customerId;

    /**
     * 会员名称
     */
    @ApiModelProperty(value = "会员名称")
    private String customerName;

    /**
     * 会员登录账号|手机号
     */
    @ApiModelProperty(value = "会员登录账号|手机号")
    private String customerAccount;

    /**
     * 搜索条件:创建时间开始
     */
    @ApiModelProperty(value = "搜索条件:创建时间开始")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTimeBegin;
    /**
     * 搜索条件:创建时间截止
     */
    @ApiModelProperty(value = "搜索条件:创建时间截止")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTimeEnd;


    /**
     * 是否禁止分销 0: 启用中  1：禁用中
     */
    @ApiModelProperty(value = "是否禁止分销")
    private DefaultFlag forbiddenFlag;


    /**
     * 是否有分销员资格0：否，1：是
     */
    @ApiModelProperty(value = "是否有分销员资格")
    private DefaultFlag distributorFlag;

    /**
     * 邀新人数-从
     */
    @ApiModelProperty(value = "邀新人数-从")
    private Integer inviteCountStart;

    /**
     * 邀新人数-至
     */
    @ApiModelProperty(value = "邀新人数-至")
    private Integer inviteCountEnd;

    /**
     * 有效邀新人数-从
     */
    @ApiModelProperty(value = "有效邀新人数-从")
    private Integer inviteAvailableCountStart;

    /**
     * 有效邀新人数-至
     */
    @ApiModelProperty(value = "有效邀新人数-至")
    private Integer inviteAvailableCountEnd;

    /**
     * 邀新奖金(元)-从
     */
    @ApiModelProperty(value = "邀新奖金(元)-从")
    private BigDecimal rewardCashStart;
    /**
     * 邀新奖金(元)-至
     */
    @ApiModelProperty(value = "邀新奖金(元)-至")
    private BigDecimal rewardCashEnd;

    /**
     * 分销订单(笔)-从
     */
    @ApiModelProperty(value = "分销订单(笔)-从")
    private Integer distributionTradeCountStart;

    /**
     * 分销订单(笔)-至
     */
    @ApiModelProperty(value = "分销订单(笔)-至")
    private Integer distributionTradeCountEnd;

    /**
     * 销售额(元)-从
     */
    @ApiModelProperty(value = "销售额(元)-从")
    private BigDecimal salesStart;

    /**
     * 销售额(元)-至
     */
    @ApiModelProperty(value = "销售额(元)-至")
    private BigDecimal salesEnd;

    /**
     * 分销佣金(元)-从
     */
    @ApiModelProperty(value = "分销佣金(元)-从")
    private BigDecimal commissionStart;

    /**
     * 分销佣金(元)-至
     */
    @ApiModelProperty(value = "分销佣金(元)-至")
    private BigDecimal commissionEnd;

    /**
     * 未入账分销佣金(元)
     */
    @ApiModelProperty(value = "未入账分销佣金(元)")
    private BigDecimal commissionNotRecorded;

    /**
     * 分销员等级ID
     */
    @ApiModelProperty(value = "分销员等级ID ")
    private String distributorLevelId;

    /**
     * 是否删除标志 0：否，1：是
     */

    private DeleteFlag delFlag = DeleteFlag.NO;

    /**
     * 分销员列表查询条件
     *
     * @return
     */
    public NativeSearchQuery esCriteria() {

        BoolQueryBuilder bool = QueryBuilders.boolQuery();


        if (CollectionUtils.isNotEmpty(this.getDistributionIdList())) {
            bool.must(QueryBuilders.termsQuery("distributionId", this.getDistributionIdList()));
        }

        // 分销员等级ID
        if (StringUtils.isNotEmpty(this.getDistributorLevelId())) {
            bool.must(QueryBuilders.termQuery("distributorLevelId", this.getDistributorLevelId()));
        }

        // 分销员标识UUID
        if (StringUtils.isNotEmpty(this.getDistributionId())) {
            bool.must(QueryBuilders.termQuery("distributionId", this.getDistributionId()));
        }

        // 模糊查询 - 会员ID
        if (StringUtils.isNotEmpty(this.getCustomerId())) {
            bool.must(QueryBuilders.wildcardQuery("customerId", "*" + this.getCustomerId() + "*"));
        }

        // 模糊查询 - 会员名称
        if (StringUtils.isNotEmpty(this.getCustomerName())) {
            bool.must(QueryBuilders.wildcardQuery("customerName", "*" + this.getCustomerName() + "*"));
        }

        // 模糊查询 - 会员登录账号|手机号
        if (StringUtils.isNotEmpty(this.getCustomerAccount())) {
            bool.must(QueryBuilders.wildcardQuery("customerAccount", "*" + this.getCustomerAccount() + "*"));
        }

        // 大于或等于 搜索条件:创建时间开始
        // 小于或等于 搜索条件:创建时间截止
        if (ObjectUtils.allNotNull(this.getCreateTimeEnd(), this.getCreateTimeEnd())) {
            bool.must(QueryBuilders.rangeQuery("createTime")
                    .gte(DateUtil.format(this.getCreateTimeBegin(), DateUtil.FMT_TIME_4))
                    .lte(DateUtil.format(this.getCreateTimeEnd(), DateUtil.FMT_TIME_4)));

        }

        // 是否删除标志 0：否，1：是
        if (Objects.nonNull(this.getDelFlag())) {
            bool.must(QueryBuilders.termQuery("delFlag", this.getDelFlag().toValue()));
        }

        // 是否禁止分销 0: 启用中  1：禁用中
        if (Objects.nonNull(this.getForbiddenFlag())) {
            bool.must(QueryBuilders.termQuery("forbiddenFlag", this.getForbiddenFlag().toValue()));
        }

        // 是否有分销员资格0：否，1：是
        if (Objects.nonNull(this.getDistributorFlag())) {
            bool.must(QueryBuilders.termQuery("distributorFlag", this.getDistributorFlag().toValue()));
        }

        // 邀新人数-从
        if (Objects.nonNull(this.getInviteCountStart()) && this.getInviteCountStart().compareTo(0) > -1) {
            bool.must(QueryBuilders.rangeQuery("inviteCount").gt(this.getInviteCountStart()));
        }

        // 邀新人数-至
        if (Objects.nonNull(this.getInviteCountEnd()) && this.getInviteCountEnd().compareTo(0) > -1) {
            bool.must(QueryBuilders.rangeQuery("inviteCount").lt(this.getInviteCountEnd()));
        }

        // 有效邀新人数-从
        if (Objects.nonNull(this.getInviteAvailableCountStart()) && this.getInviteAvailableCountStart().compareTo(0) > -1) {
            bool.must(QueryBuilders.rangeQuery("inviteAvailableCount").gt(this.getInviteAvailableCountStart()));
        }

        // 有效邀新人数-至
        if (Objects.nonNull(this.getInviteAvailableCountEnd()) && this.getInviteAvailableCountEnd().compareTo(0) > -1) {
            bool.must(QueryBuilders.rangeQuery("inviteAvailableCount").lt(this.getInviteAvailableCountEnd()));
        }

        // 邀新奖金(元)-从
        if (Objects.nonNull(this.getRewardCashStart()) && this.getRewardCashStart().compareTo(BigDecimal.ZERO) > -1) {
            bool.must(QueryBuilders.rangeQuery("rewardCash").gt(this.getRewardCashStart().doubleValue()));
        }

        // 邀新奖金(元)-至
        if (Objects.nonNull(this.getRewardCashEnd()) && this.getRewardCashEnd().compareTo(BigDecimal.ZERO) > -1) {
            bool.must(QueryBuilders.rangeQuery("rewardCash").lt(this.getRewardCashEnd().doubleValue()));
        }

        // 分销订单(笔)-从
        if (Objects.nonNull(this.getDistributionTradeCountStart()) && this.getDistributionTradeCountStart().compareTo(0) > -1) {
            bool.must(QueryBuilders.rangeQuery("distributionTradeCount").gt(this.getDistributionTradeCountStart()));
        }

        // 分销订单(笔)-至
        if (Objects.nonNull(this.getDistributionTradeCountEnd()) && this.getDistributionTradeCountEnd().compareTo(0) > -1) {
            bool.must(QueryBuilders.rangeQuery("distributionTradeCount").gt(this.getDistributionTradeCountEnd()));
        }

        // 销售额(元)-从
        if (Objects.nonNull(this.getSalesStart()) && this.getSalesStart().compareTo(BigDecimal.ZERO) > -1) {
            bool.must(QueryBuilders.rangeQuery("sales").gt(this.getSalesStart().doubleValue()));
        }

        // 销售额(元)-至
        if (Objects.nonNull(this.getSalesEnd()) && this.getSalesEnd().compareTo(BigDecimal.ZERO) > -1) {
            bool.must(QueryBuilders.rangeQuery("sales").lt(this.getSalesStart().doubleValue()));
        }

        // 分销佣金(元)-从
        if (Objects.nonNull(this.getCommissionStart()) && this.getCommissionStart().compareTo(BigDecimal.ZERO) > -1) {
            bool.must(QueryBuilders.rangeQuery("commission").gt(this.getCommissionStart().doubleValue()));
        }

        // 分销佣金(元)-至
        if (Objects.nonNull(this.getCommissionEnd()) && this.getCommissionEnd().compareTo(BigDecimal.ZERO) > -1) {
            bool.must(QueryBuilders.rangeQuery("commission").lt(this.getCommissionEnd().doubleValue()));
        }

        SortOrder sortOrder = StringUtils.equals(this.getSortRole(), "asc") ? SortOrder.ASC : SortOrder.DESC;
        FieldSortBuilder order = SortBuilders.fieldSort(this.getSortColumn()).order(sortOrder);
        return new NativeSearchQueryBuilder()
                .withQuery(bool)
                .withPageable(this.getPageable())
                .withSort(order)
                .build();
    }

}