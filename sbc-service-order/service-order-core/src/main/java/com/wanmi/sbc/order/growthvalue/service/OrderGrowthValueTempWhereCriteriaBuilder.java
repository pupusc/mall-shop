package com.wanmi.sbc.order.growthvalue.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.order.api.request.growthvalue.OrderGrowthValueTempQueryRequest;
import com.wanmi.sbc.order.growthvalue.model.root.OrderGrowthValueTemp;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>会员权益处理订单成长值 临时表动态查询条件构建器</p>
 */
public class OrderGrowthValueTempWhereCriteriaBuilder {
    public static Specification<OrderGrowthValueTemp> build(OrderGrowthValueTempQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-主键idList
            if (CollectionUtils.isNotEmpty(queryRequest.getIdList())) {
                predicates.add(root.get("id").in(queryRequest.getIdList()));
            }

            // 主键id
            if (queryRequest.getId() != null) {
                predicates.add(cbuild.equal(root.get("id"), queryRequest.getId()));
            }

            // 模糊查询 - 订单号
            if (StringUtils.isNotEmpty(queryRequest.getOrderNo())) {
                predicates.add(cbuild.like(root.get("orderNo"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getOrderNo()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 大于或等于可退货截止时间
            if (queryRequest.getReturnEndTime() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("returnEndTime"),
                        queryRequest.getReturnEndTime()));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
