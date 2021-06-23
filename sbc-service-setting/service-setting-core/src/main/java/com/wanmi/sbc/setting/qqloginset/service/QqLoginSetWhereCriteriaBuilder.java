package com.wanmi.sbc.setting.qqloginset.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.setting.api.request.qqloginset.QqLoginSetQueryRequest;
import com.wanmi.sbc.setting.qqloginset.model.root.QqLoginSet;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.apache.commons.lang3.StringUtils;
import com.wanmi.sbc.common.util.XssUtils;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>qq登录信息动态查询条件构建器</p>
 * @author lq
 * @date 2019-11-05 16:11:28
 */
public class QqLoginSetWhereCriteriaBuilder {
    public static Specification<QqLoginSet> build(QqLoginSetQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-qqSetIdList
            if (CollectionUtils.isNotEmpty(queryRequest.getQqSetIdList())) {
                predicates.add(root.get("qqSetId").in(queryRequest.getQqSetIdList()));
            }

            // qqSetId
            if (StringUtils.isNotEmpty(queryRequest.getQqSetId())) {
                predicates.add(cbuild.equal(root.get("qqSetId"), queryRequest.getQqSetId()));
            }

            // mobileServerStatus
            if (queryRequest.getMobileServerStatus() != null) {
                predicates.add(cbuild.equal(root.get("mobileServerStatus"), queryRequest.getMobileServerStatus()));
            }

            // 模糊查询 - mobileAppId
            if (StringUtils.isNotEmpty(queryRequest.getMobileAppId())) {
                predicates.add(cbuild.like(root.get("mobileAppId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getMobileAppId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - mobileAppSecret
            if (StringUtils.isNotEmpty(queryRequest.getMobileAppSecret())) {
                predicates.add(cbuild.like(root.get("mobileAppSecret"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getMobileAppSecret()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // pcServerStatus
            if (queryRequest.getPcServerStatus() != null) {
                predicates.add(cbuild.equal(root.get("pcServerStatus"), queryRequest.getPcServerStatus()));
            }

            // 模糊查询 - pcAppId
            if (StringUtils.isNotEmpty(queryRequest.getPcAppId())) {
                predicates.add(cbuild.like(root.get("pcAppId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getPcAppId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - pcAppSecret
            if (StringUtils.isNotEmpty(queryRequest.getPcAppSecret())) {
                predicates.add(cbuild.like(root.get("pcAppSecret"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getPcAppSecret()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // appServerStatus
            if (queryRequest.getAppServerStatus() != null) {
                predicates.add(cbuild.equal(root.get("appServerStatus"), queryRequest.getAppServerStatus()));
            }

            // 大于或等于 搜索条件:createTime开始
            if (queryRequest.getCreateTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("createTime"),
                        queryRequest.getCreateTimeBegin()));
            }
            // 小于或等于 搜索条件:createTime截止
            if (queryRequest.getCreateTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("createTime"),
                        queryRequest.getCreateTimeEnd()));
            }

            // 大于或等于 搜索条件:updateTime开始
            if (queryRequest.getUpdateTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("updateTime"),
                        queryRequest.getUpdateTimeBegin()));
            }
            // 小于或等于 搜索条件:updateTime截止
            if (queryRequest.getUpdateTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("updateTime"),
                        queryRequest.getUpdateTimeEnd()));
            }

            // 模糊查询 - operatePerson
            if (StringUtils.isNotEmpty(queryRequest.getOperatePerson())) {
                predicates.add(cbuild.like(root.get("operatePerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getOperatePerson()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
