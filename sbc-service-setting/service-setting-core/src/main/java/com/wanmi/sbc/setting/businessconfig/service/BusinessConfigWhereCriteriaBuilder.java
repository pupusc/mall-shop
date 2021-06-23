package com.wanmi.sbc.setting.businessconfig.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.setting.api.request.businessconfig.BusinessConfigQueryRequest;
import com.wanmi.sbc.setting.businessconfig.model.root.BusinessConfig;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>招商页设置动态查询条件构建器</p>
 * @author lq
 * @date 2019-11-05 16:09:10
 */
public class BusinessConfigWhereCriteriaBuilder {
    public static Specification<BusinessConfig> build(BusinessConfigQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-招商页设置主键List
            if (CollectionUtils.isNotEmpty(queryRequest.getBusinessConfigIdList())) {
                predicates.add(root.get("businessConfigId").in(queryRequest.getBusinessConfigIdList()));
            }

            // 招商页设置主键
            if (queryRequest.getBusinessConfigId() != null) {
                predicates.add(cbuild.equal(root.get("businessConfigId"), queryRequest.getBusinessConfigId()));
            }

            // 模糊查询 - 招商页banner
            if (StringUtils.isNotEmpty(queryRequest.getBusinessBanner())) {
                predicates.add(cbuild.like(root.get("businessBanner"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getBusinessBanner()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 招商页自定义
            if (StringUtils.isNotEmpty(queryRequest.getBusinessCustom())) {
                predicates.add(cbuild.like(root.get("businessCustom"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getBusinessCustom()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 招商页注册协议
            if (StringUtils.isNotEmpty(queryRequest.getBusinessRegister())) {
                predicates.add(cbuild.like(root.get("businessRegister"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getBusinessRegister()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 招商页入驻协议
            if (StringUtils.isNotEmpty(queryRequest.getBusinessEnter())) {
                predicates.add(cbuild.like(root.get("businessEnter"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getBusinessEnter()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
