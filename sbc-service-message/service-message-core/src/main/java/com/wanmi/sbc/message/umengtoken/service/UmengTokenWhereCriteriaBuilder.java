package com.wanmi.sbc.message.umengtoken.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.message.api.request.umengtoken.UmengTokenQueryRequest;
import com.wanmi.sbc.message.umengtoken.model.root.UmengToken;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>友盟推送设备与会员关系动态查询条件构建器</p>
 * @author bob
 * @date 2020-01-06 11:36:26
 */
public class UmengTokenWhereCriteriaBuilder {
    public static Specification<UmengToken> build(UmengTokenQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-idList
            if (CollectionUtils.isNotEmpty(queryRequest.getIdList())) {
                predicates.add(root.get("id").in(queryRequest.getIdList()));
            }

            // id
            if (queryRequest.getId() != null) {
                predicates.add(cbuild.equal(root.get("id"), queryRequest.getId()));
            }

            // 模糊查询 - 会员ID
            if (StringUtils.isNotEmpty(queryRequest.getCustomerId())) {
                predicates.add(cbuild.like(root.get("customerId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCustomerId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 友盟推送会员设备token
            if (StringUtils.isNotEmpty(queryRequest.getDevlceToken())) {
                predicates.add(cbuild.like(root.get("devlceToken"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getDevlceToken()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 大于或等于 搜索条件:绑定时间开始
            if (queryRequest.getBindingTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("bindingTime"),
                        queryRequest.getBindingTimeBegin()));
            }
            // 小于或等于 搜索条件:绑定时间截止
            if (queryRequest.getBindingTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("bindingTime"),
                        queryRequest.getBindingTimeEnd()));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
