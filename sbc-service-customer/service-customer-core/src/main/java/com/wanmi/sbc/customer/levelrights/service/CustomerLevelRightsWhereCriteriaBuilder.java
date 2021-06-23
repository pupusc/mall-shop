package com.wanmi.sbc.customer.levelrights.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.customer.api.request.levelrights.CustomerLevelRightsQueryRequest;
import com.wanmi.sbc.customer.levelrights.model.root.CustomerLevelRights;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>会员等级权益表动态查询条件构建器</p>
 */
public class CustomerLevelRightsWhereCriteriaBuilder {
    public static Specification<CustomerLevelRights> build(CustomerLevelRightsQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-主键idList
            if (CollectionUtils.isNotEmpty(queryRequest.getRightsIdList())) {
                predicates.add(root.get("rightsId").in(queryRequest.getRightsIdList()));
            }

            // 主键id
            if (queryRequest.getRightsId() != null) {
                predicates.add(cbuild.equal(root.get("rightsId"), queryRequest.getRightsId()));
            }

            // 模糊查询 - 权益名称
            if (StringUtils.isNotEmpty(queryRequest.getRightsName())) {
                predicates.add(cbuild.like(root.get("rightsName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getRightsName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 权益类型 0等级徽章 1专属客服 2会员折扣 3券礼包 4返积分
            if (queryRequest.getRightsType() != null) {
                predicates.add(cbuild.equal(root.get("rightsType"), queryRequest.getRightsType()));
            }

            // 模糊查询 - logo地址
            if (StringUtils.isNotEmpty(queryRequest.getRightsLogo())) {
                predicates.add(cbuild.like(root.get("rightsLogo"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getRightsLogo()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 权益介绍
            if (StringUtils.isNotEmpty(queryRequest.getRightsDescription())) {
                predicates.add(cbuild.like(root.get("rightsDescription"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getRightsDescription()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 权益规则(JSON)
            if (StringUtils.isNotEmpty(queryRequest.getRightsRule())) {
                predicates.add(cbuild.like(root.get("rightsRule"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getRightsRule()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 是否开启 0:关闭 1:开启
            if (queryRequest.getStatus() != null) {
                predicates.add(cbuild.equal(root.get("status"), queryRequest.getStatus()));
            }

            // 删除标识 0:未删除1:已删除
            if (queryRequest.getDelFlag() != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), queryRequest.getDelFlag()));
            }

            cquery.orderBy(cbuild.asc(root.get("sort")));

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
