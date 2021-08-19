package com.wanmi.sbc.customer.growthvalue.service;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.customer.api.request.growthvalue.CustomerGrowthValueQueryRequest;
import com.wanmi.sbc.customer.growthvalue.model.root.CustomerGrowthValue;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>客户成长值明细表动态查询条件构建器</p>
 *
 * @author yang
 * @since 2019/2/25
 */
public class CustomerGrowthValueWhereCriteriaBuilder {
    public static Specification<CustomerGrowthValue> build(CustomerGrowthValueQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 用户编号
            if (queryRequest.getCustomerId() != null) {
                predicates.add(cbuild.equal(root.get("customerId"), queryRequest.getCustomerId()));
            }

            // 获取操作类型 扣除/增长
            if (queryRequest.getType() != null) {
                predicates.add(cbuild.equal(root.get("type"), queryRequest.getType()));
            }

            // 获取业务类型
            if (queryRequest.getGrowthValueServiceType() != null) {
                predicates.add(cbuild.equal(root.get("serviceType"), queryRequest.getGrowthValueServiceType()));
            }

            // 获取内容备注
            if (queryRequest.getContent() != null) {
                predicates.add(cbuild.equal(root.get("content"), queryRequest.getContent()));
            }

            // 操作开始时间
            if (queryRequest.getGteGainStartDate() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String time = formatter.format(queryRequest.getGteGainStartDate());
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("opTime"), LocalDateTime.of(LocalDate
                        .parse(time, formatter), LocalTime.MIN)));
            }

            // 操作结束时间
            if (queryRequest.getLteGainEndDate() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String time = formatter.format(queryRequest.getLteGainEndDate());
                predicates.add(cbuild.lessThanOrEqualTo(root.get("opTime"), LocalDateTime.of(LocalDate
                        .parse(time, formatter), LocalTime.MAX)));
            }

            // 排除成长值为0的记录
            if (DefaultFlag.YES.equals(queryRequest.getExcludeZeroFlag())) {
                predicates.add(cbuild.notEqual(root.get("growthValue"), 0L));
            }

            cquery.orderBy(cbuild.desc(root.get("opTime")));

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
