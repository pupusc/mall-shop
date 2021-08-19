package com.wanmi.sbc.customer.distribution.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerQueryRequest;
import com.wanmi.sbc.customer.distribution.model.root.DistributionCustomer;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>分销员动态查询条件构建器</p>
 *
 * @author lq
 * @date 2019-02-19 10:13:15
 */
public class DistributionCustomerWhereCriteriaBuilder {
    public static Specification<DistributionCustomer> build(DistributionCustomerQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-分销员标识UUIDList
            if (CollectionUtils.isNotEmpty(queryRequest.getDistributionIdList())) {
                predicates.add(root.get("distributionId").in(queryRequest.getDistributionIdList()));
            }

            // 分销员等级ID
            if (StringUtils.isNotEmpty(queryRequest.getDistributorLevelId())) {
                predicates.add(cbuild.equal(root.get("distributorLevelId"), queryRequest.getDistributorLevelId()));
            }


            // 分销员标识UUID
            if (StringUtils.isNotEmpty(queryRequest.getDistributionId())) {
                predicates.add(cbuild.equal(root.get("distributionId"), queryRequest.getDistributionId()));
            }

            // 模糊查询 - 会员ID
            if (StringUtils.isNotEmpty(queryRequest.getCustomerId())) {
                predicates.add(cbuild.equal(root.get("customerId"), queryRequest.getCustomerId()));
            }

            // 模糊查询 - 会员名称
            if (StringUtils.isNotEmpty(queryRequest.getCustomerName())) {
                predicates.add(cbuild.like(root.get("customerName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCustomerName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 会员登录账号|手机号
            if (StringUtils.isNotEmpty(queryRequest.getCustomerAccount())) {
                predicates.add(cbuild.like(root.get("customerAccount"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCustomerAccount()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 大于或等于 搜索条件:创建时间开始
            if (queryRequest.getCreateTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("createTime"),
                        queryRequest.getCreateTimeBegin()));
            }
            // 小于或等于 搜索条件:创建时间截止
            if (queryRequest.getCreateTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("createTime"),
                        queryRequest.getCreateTimeEnd()));
            }


            // 是否删除标志 0：否，1：是
            if (queryRequest.getDelFlag() != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), queryRequest.getDelFlag()));
            }

            // 是否禁止分销 0: 启用中  1：禁用中
            if (queryRequest.getForbiddenFlag() != null) {
                predicates.add(cbuild.equal(root.get("forbiddenFlag"), queryRequest.getForbiddenFlag()));
            }

            // 是否有分销员资格0：否，1：是
            if (queryRequest.getDistributorFlag() != null) {
                predicates.add(cbuild.equal(root.get("distributorFlag"), queryRequest.getDistributorFlag()));
            }

            // 邀新人数-从
            if (Objects.nonNull(queryRequest.getInviteCountStart()) && queryRequest.getInviteCountStart().compareTo(0) > -1) {
                predicates.add(cbuild.ge(root.get("inviteCount"), queryRequest.getInviteCountStart()));
            }

            // 邀新人数-至
            if (Objects.nonNull(queryRequest.getInviteCountEnd()) && queryRequest.getInviteCountEnd().compareTo(0) > -1) {
                predicates.add(cbuild.le(root.get("inviteCount"), queryRequest.getInviteCountEnd()));
            }

            // 有效邀新人数-从
            if (Objects.nonNull(queryRequest.getInviteAvailableCountStart()) && queryRequest.getInviteAvailableCountStart().compareTo(0) > -1) {
                predicates.add(cbuild.ge(root.get("inviteAvailableCount"), queryRequest.getInviteAvailableCountStart()));
            }

            // 有效邀新人数-至
            if (Objects.nonNull(queryRequest.getInviteAvailableCountEnd()) && queryRequest.getInviteAvailableCountEnd().compareTo(0) > -1) {
                predicates.add(cbuild.le(root.get("inviteAvailableCount"), queryRequest.getInviteAvailableCountEnd()));
            }

            // 邀新奖金(元)-从
            if (Objects.nonNull(queryRequest.getRewardCashStart()) && queryRequest.getRewardCashStart().compareTo(BigDecimal.ZERO) > -1) {
                predicates.add(cbuild.ge(root.get("rewardCash"), queryRequest.getRewardCashStart()));
            }

            // 邀新奖金(元)-至
            if (Objects.nonNull(queryRequest.getRewardCashEnd()) && queryRequest.getRewardCashEnd().compareTo(BigDecimal.ZERO) > -1) {
                predicates.add(cbuild.le(root.get("rewardCash"), queryRequest.getRewardCashEnd()));
            }

            // 分销订单(笔)-从
            if (Objects.nonNull(queryRequest.getDistributionTradeCountStart()) && queryRequest.getDistributionTradeCountStart().compareTo(0) > -1) {
                predicates.add(cbuild.ge(root.get("distributionTradeCount"), queryRequest.getDistributionTradeCountStart()));
            }

            // 分销订单(笔)-至
            if (Objects.nonNull(queryRequest.getDistributionTradeCountEnd()) && queryRequest.getDistributionTradeCountEnd().compareTo(0) > -1) {
                predicates.add(cbuild.le(root.get("distributionTradeCount"), queryRequest.getDistributionTradeCountEnd()));
            }

            // 销售额(元)-从
            if (Objects.nonNull(queryRequest.getSalesStart()) && queryRequest.getSalesStart().compareTo(BigDecimal.ZERO) > -1) {
                predicates.add(cbuild.ge(root.get("sales"), queryRequest.getSalesStart()));
            }

            // 销售额(元)-至
            if (Objects.nonNull(queryRequest.getSalesEnd()) && queryRequest.getSalesEnd().compareTo(BigDecimal.ZERO) > -1) {
                predicates.add(cbuild.le(root.get("sales"), queryRequest.getSalesEnd()));
            }

            // 分销佣金(元)-从
            if (Objects.nonNull(queryRequest.getCommissionStart()) && queryRequest.getCommissionStart().compareTo(BigDecimal.ZERO) > -1) {
                predicates.add(cbuild.ge(root.get("commission"), queryRequest.getCommissionStart()));
            }

            // 分销佣金(元)-至
            if (Objects.nonNull(queryRequest.getCommissionEnd()) && queryRequest.getCommissionEnd().compareTo(BigDecimal.ZERO) > -1) {
                predicates.add(cbuild.le(root.get("commission"), queryRequest.getCommissionEnd()));
            }


            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
