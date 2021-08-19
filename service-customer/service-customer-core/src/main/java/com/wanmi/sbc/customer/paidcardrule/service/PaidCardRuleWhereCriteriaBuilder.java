package com.wanmi.sbc.customer.paidcardrule.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.customer.api.request.paidcardrule.PaidCardRuleQueryRequest;
import com.wanmi.sbc.customer.paidcardrule.model.root.PaidCardRule;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.apache.commons.lang3.StringUtils;
import com.wanmi.sbc.common.util.XssUtils;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>付费会员动态查询条件构建器</p>
 * @author xuhai
 * @date 2021-01-29 14:04:01
 */
public class PaidCardRuleWhereCriteriaBuilder {
    public static Specification<PaidCardRule> build(PaidCardRuleQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-主键List
            if (CollectionUtils.isNotEmpty(queryRequest.getIdList())) {
                predicates.add(root.get("id").in(queryRequest.getIdList()));
            }

            if (CollectionUtils.isNotEmpty(queryRequest.getPaidCardIdList())) {
                predicates.add(root.get("paidCardId").in(queryRequest.getPaidCardIdList()));
            }

            // 主键
            if (StringUtils.isNotEmpty(queryRequest.getId())) {
                predicates.add(cbuild.equal(root.get("id"), queryRequest.getId()));
            }

            // 配置类型 0：付费配置；1：续费配置
            if (queryRequest.getType() != null) {
                predicates.add(cbuild.equal(root.get("type"), queryRequest.getType()));
            }

            // 模糊查询 - 名称
            if (StringUtils.isNotEmpty(queryRequest.getName())) {
                predicates.add(cbuild.like(root.get("name"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 价格
            if (queryRequest.getPrice() != null) {
                predicates.add(cbuild.equal(root.get("price"), queryRequest.getPrice()));
            }

            // 0:禁用；1：启用
            if (queryRequest.getStatus() != null) {
                predicates.add(cbuild.equal(root.get("status"), queryRequest.getStatus()));
            }

            // 模糊查询 - 付费会员类型id
            if (StringUtils.isNotEmpty(queryRequest.getPaidCardId())) {
                predicates.add(cbuild.like(root.get("paidCardId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getPaidCardId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 时间单位：0天，1月，2年
            if (queryRequest.getTimeUnit() != null) {
                predicates.add(cbuild.equal(root.get("timeUnit"), queryRequest.getTimeUnit()));
            }

            // 时间（数值）
            if (queryRequest.getTimeVal() != null) {
                predicates.add(cbuild.equal(root.get("timeVal"), queryRequest.getTimeVal()));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
