package com.wanmi.sbc.customer.paidcardrightsrel.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.customer.api.request.paidcardrightsrel.PaidCardRightsRelQueryRequest;
import com.wanmi.sbc.customer.paidcardrightsrel.model.root.PaidCardRightsRel;
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
 * @date 2021-01-29 14:04:00
 */
public class PaidCardRightsRelWhereCriteriaBuilder {
    public static Specification<PaidCardRightsRel> build(PaidCardRightsRelQueryRequest queryRequest) {
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

            // 模糊查询 - 所属会员权益id
            if (StringUtils.isNotEmpty(queryRequest.getPaidCardId())) {
                predicates.add(cbuild.like(root.get("paidCardId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getPaidCardId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 权益id
            if (queryRequest.getRightsId() != null) {
                predicates.add(cbuild.equal(root.get("rightsId"), queryRequest.getRightsId()));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
