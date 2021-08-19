package com.wanmi.sbc.crm.planstatisticsmessage.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.crm.api.request.planstatisticsmessage.PlanStatisticsMessageQueryRequest;
import com.wanmi.sbc.crm.planstatisticsmessage.model.root.PlanStatisticsMessage;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>运营计划效果统计站内信收到人/次统计数据动态查询条件构建器</p>
 * @author lvzhenwei
 * @date 2020-02-05 15:08:00
 */
public class PlanStatisticsMessageWhereCriteriaBuilder {
    public static Specification<PlanStatisticsMessage> build(PlanStatisticsMessageQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-运营计划idList
            if (CollectionUtils.isNotEmpty(queryRequest.getPlanIdList())) {
                predicates.add(root.get("planId").in(queryRequest.getPlanIdList()));
            }

            // 运营计划id
            if (queryRequest.getPlanId() != null) {
                predicates.add(cbuild.equal(root.get("planId"), queryRequest.getPlanId()));
            }

            // 站内信收到人数
            if (queryRequest.getMessageReceiveNum() != null) {
                predicates.add(cbuild.equal(root.get("messageReceiveNum"), queryRequest.getMessageReceiveNum()));
            }

            // 站内信收到人次
            if (queryRequest.getMessageReceiveTotal() != null) {
                predicates.add(cbuild.equal(root.get("messageReceiveTotal"), queryRequest.getMessageReceiveTotal()));
            }

            // 大于或等于 搜索条件:统计日期开始
            if (queryRequest.getStatisticsDateBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("statisticsDate"),
                        queryRequest.getStatisticsDateBegin()));
            }
            // 小于或等于 搜索条件:统计日期截止
            if (queryRequest.getStatisticsDateEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("statisticsDate"),
                        queryRequest.getStatisticsDateEnd()));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
