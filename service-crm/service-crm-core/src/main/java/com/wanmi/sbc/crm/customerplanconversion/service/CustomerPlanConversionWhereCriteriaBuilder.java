package com.wanmi.sbc.crm.customerplanconversion.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.crm.api.request.customerplanconversion.CustomerPlanConversionQueryRequest;
import com.wanmi.sbc.crm.customerplanconversion.model.root.CustomerPlanConversion;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>运营计划转化效果动态查询条件构建器</p>
 * @author zhangwenchang
 * @date 2020-02-12 00:16:50
 */
public class CustomerPlanConversionWhereCriteriaBuilder {
    public static Specification<CustomerPlanConversion> build(CustomerPlanConversionQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-主键IDList
            if (CollectionUtils.isNotEmpty(queryRequest.getIdList())) {
                predicates.add(root.get("id").in(queryRequest.getIdList()));
            }

            // 主键ID
            if (queryRequest.getId() != null) {
                predicates.add(cbuild.equal(root.get("id"), queryRequest.getId()));
            }

            // 运营计划id
            if (queryRequest.getPlanId() != null) {
                predicates.add(cbuild.equal(root.get("planId"), queryRequest.getPlanId()));
            }

            // 访客数UV
            if (queryRequest.getVisitorsUvCount() != null) {
                predicates.add(cbuild.equal(root.get("visitorsUvCount"), queryRequest.getVisitorsUvCount()));
            }

            // 下单人数
            if (queryRequest.getOrderPersonCount() != null) {
                predicates.add(cbuild.equal(root.get("orderPersonCount"), queryRequest.getOrderPersonCount()));
            }

            // 下单笔数
            if (queryRequest.getOrderCount() != null) {
                predicates.add(cbuild.equal(root.get("orderCount"), queryRequest.getOrderCount()));
            }

            // 付款人数
            if (queryRequest.getPayPersonCount() != null) {
                predicates.add(cbuild.equal(root.get("payPersonCount"), queryRequest.getPayPersonCount()));
            }

            // 付款笔数
            if (queryRequest.getPayCount() != null) {
                predicates.add(cbuild.equal(root.get("payCount"), queryRequest.getPayCount()));
            }

            // 付款金额
            if (queryRequest.getTotalPrice() != null) {
                predicates.add(cbuild.equal(root.get("totalPrice"), queryRequest.getTotalPrice()));
            }

            // 客单价
            if (queryRequest.getUnitPrice() != null) {
                predicates.add(cbuild.equal(root.get("unitPrice"), queryRequest.getUnitPrice()));
            }

            // 覆盖人数
            if (queryRequest.getCoversCount() != null) {
                predicates.add(cbuild.equal(root.get("coversCount"), queryRequest.getCoversCount()));
            }

            // 访客人数
            if (queryRequest.getVisitorsCount() != null) {
                predicates.add(cbuild.equal(root.get("visitorsCount"), queryRequest.getVisitorsCount()));
            }

            // 访客人数/覆盖人数转换率
            if (queryRequest.getCoversVisitorsRate() != null) {
                predicates.add(cbuild.equal(root.get("coversVisitorsRate"), queryRequest.getCoversVisitorsRate()));
            }

            // 付款人数/访客人数转换率
            if (queryRequest.getPayVisitorsRate() != null) {
                predicates.add(cbuild.equal(root.get("payVisitorsRate"), queryRequest.getPayVisitorsRate()));
            }

            // 付款人数/覆盖人数转换率
            if (queryRequest.getPayCoversRate() != null) {
                predicates.add(cbuild.equal(root.get("payCoversRate"), queryRequest.getPayCoversRate()));
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

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
