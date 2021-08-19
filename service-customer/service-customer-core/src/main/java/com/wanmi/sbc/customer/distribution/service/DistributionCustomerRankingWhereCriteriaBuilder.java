package com.wanmi.sbc.customer.distribution.service;

import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerRankingQueryRequest;
import com.wanmi.sbc.customer.distribution.model.root.DistributionCustomerRanking;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>用户分销排行榜动态查询条件构建器</p>
 * @author lq
 * @date 2019-04-19 10:05:05
 */
public class DistributionCustomerRankingWhereCriteriaBuilder {
    public static Specification<DistributionCustomerRanking> build(DistributionCustomerRankingQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-会员IDList
            if (CollectionUtils.isNotEmpty(queryRequest.getCustomerIdList())) {
                predicates.add(root.get("customerId").in(queryRequest.getCustomerIdList()));
            }

            // 会员id
            if (queryRequest.getCustomerId() != null) {
                predicates.add(cbuild.equal(root.get("customerId"), queryRequest.getCustomerId()));
            }


            // type 对应数值>0
            if (queryRequest.getType() != null) {
                predicates.add(cbuild.gt(root.get(queryRequest.getType() ), 0));
            }

            // 目标日期
            if (queryRequest.getTargetDate() != null) {
                predicates.add(cbuild.equal(root.get("targetDate"), queryRequest.getTargetDate()));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
