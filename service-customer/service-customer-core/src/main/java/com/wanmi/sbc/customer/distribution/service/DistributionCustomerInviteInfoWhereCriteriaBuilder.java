package com.wanmi.sbc.customer.distribution.service;

import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerInviteInfoQueryRequest;
import com.wanmi.sbc.customer.distribution.model.root.DistributionCustomerInviteInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>分销员邀新信息动态查询条件构建器</p>
 * @author lq
 * @date 2019-02-19 10:13:15
 */
public class DistributionCustomerInviteInfoWhereCriteriaBuilder {
    public static Specification<DistributionCustomerInviteInfo> build(DistributionCustomerInviteInfoQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-分销员标识UUIDList
            if (CollectionUtils.isNotEmpty(queryRequest.getDistributionIdList())) {
                predicates.add(root.get("distributionId").in(queryRequest.getDistributionIdList()));
            }

            // 分销员标识UUID
            if (StringUtils.isNotEmpty(queryRequest.getDistributionId())) {
                predicates.add(cbuild.equal(root.get("distributionId"), queryRequest.getDistributionId()));
            }

            // 已发放邀新奖励现金人数-至
            if (Objects.nonNull(queryRequest.getRewardCashCountEnd()) && queryRequest.getRewardCashCountEnd().compareTo(0) > -1) {
                predicates.add(cbuild.lt(root.get("rewardCashCount"), queryRequest.getRewardCashCountEnd()));
            }

            // 达到上限未发放奖励现金人数-从
            if (Objects.nonNull(queryRequest.getRewardCashLimitCountStart()) && queryRequest.getRewardCashLimitCountStart().compareTo(0) > -1) {
                predicates.add(cbuild.gt(root.get("rewardCashLimitCount"), queryRequest.getRewardCashLimitCountStart()));
            }

            // 达到上限未发放奖励现金有效邀新人数-从
            if (Objects.nonNull(queryRequest.getRewardCashAvailableLimitCountStart()) && queryRequest.getRewardCashAvailableLimitCountStart().compareTo(0) > -1) {
                predicates.add(cbuild.gt(root.get("rewardCashAvailableLimitCount"), queryRequest.getRewardCashAvailableLimitCountStart()));
            }

            // 已发放邀新奖励优惠券人数-至
            if (Objects.nonNull(queryRequest.getRewardCouponCountEnd()) && queryRequest.getRewardCouponCountEnd().compareTo(0) > -1) {
                predicates.add(cbuild.lt(root.get("rewardCouponCount"), queryRequest.getRewardCouponCountEnd()));
            }

            // 达到上限未发放奖励优惠券人数-从
            if (Objects.nonNull(queryRequest.getRewardCouponLimitCountStart()) && queryRequest.getRewardCouponLimitCountStart().compareTo(0) > -1) {
                predicates.add(cbuild.gt(root.get("rewardCouponLimitCount"), queryRequest.getRewardCouponLimitCountStart()));
            }

            // 达到上限未发放奖励优惠券有效邀新人数-从
            if (Objects.nonNull(queryRequest.getRewardCouponAvailableLimitCountStart()) && queryRequest.getRewardCouponAvailableLimitCountStart().compareTo(0) > -1) {
                predicates.add(cbuild.gt(root.get("rewardCouponAvailableLimitCount"), queryRequest.getRewardCouponAvailableLimitCountStart()));
            }


            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
