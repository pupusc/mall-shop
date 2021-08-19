package com.wanmi.sbc.setting.umengpushconfig.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.setting.api.request.umengpushconfig.UmengPushConfigQueryRequest;
import com.wanmi.sbc.setting.umengpushconfig.model.root.UmengPushConfig;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>友盟push接口配置动态查询条件构建器</p>
 * @author bob
 * @date 2020-01-07 10:34:04
 */
public class UmengPushConfigWhereCriteriaBuilder {
    public static Specification<UmengPushConfig> build(UmengPushConfigQueryRequest queryRequest) {
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

            // 模糊查询 - androidKeyId
            if (StringUtils.isNotEmpty(queryRequest.getAndroidKeyId())) {
                predicates.add(cbuild.like(root.get("androidKeyId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getAndroidKeyId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - androidMsgSecret
            if (StringUtils.isNotEmpty(queryRequest.getAndroidMsgSecret())) {
                predicates.add(cbuild.like(root.get("androidMsgSecret"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getAndroidMsgSecret()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - androidKeySecret
            if (StringUtils.isNotEmpty(queryRequest.getAndroidKeySecret())) {
                predicates.add(cbuild.like(root.get("androidKeySecret"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getAndroidKeySecret()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - iosKeyId
            if (StringUtils.isNotEmpty(queryRequest.getIosKeyId())) {
                predicates.add(cbuild.like(root.get("iosKeyId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getIosKeyId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - iosKeySecret
            if (StringUtils.isNotEmpty(queryRequest.getIosKeySecret())) {
                predicates.add(cbuild.like(root.get("iosKeySecret"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getIosKeySecret()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
