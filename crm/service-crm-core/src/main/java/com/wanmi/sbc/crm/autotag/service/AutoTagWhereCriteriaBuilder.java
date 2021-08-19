package com.wanmi.sbc.crm.autotag.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.crm.api.request.autotag.AutoTagQueryRequest;
import com.wanmi.sbc.crm.autotag.model.root.AutoTag;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>自动标签动态查询条件构建器</p>
 * @author dyt
 * @date 2020-03-11 14:47:32
 */
public class AutoTagWhereCriteriaBuilder {
    public static Specification<AutoTag> build(AutoTagQueryRequest queryRequest) {
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

            // 模糊查询 - 自动标签名称
            if (StringUtils.isNotEmpty(queryRequest.getTagName())) {
                predicates.add(cbuild.like(root.get("tagName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getTagName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 强匹配标签名称 - 自动标签名称
            if (StringUtils.isNotEmpty(queryRequest.getCheckTagName())) {
                predicates.add(cbuild.equal(root.get("tagName"), queryRequest.getCheckTagName()));
            }

            // 会员人数
            if (queryRequest.getCustomerCount() != null) {
                predicates.add(cbuild.equal(root.get("customerCount"), queryRequest.getCustomerCount()));
            }

            // 标签类型，0：偏好标签组，1：指标值标签，2：指标值范围标签，3、综合类标签
            if (queryRequest.getType() != null) {
                predicates.add(cbuild.equal(root.get("type"), queryRequest.getType()));
            }

            // 一级维度且或关系，0：且，1：或
            if (queryRequest.getRelationType() != null) {
                predicates.add(cbuild.equal(root.get("relationType"), queryRequest.getRelationType()));
            }

            // 系统标识，0：非系统，1：系统
            if (queryRequest.getSystemFlag() != null) {
                predicates.add(cbuild.equal(root.get("systemFlag"), queryRequest.getSystemFlag()));
            }

            // 删除标识，0：未删除，1：已删除
            if (queryRequest.getDelFlag() != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), queryRequest.getDelFlag()));
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

            // 模糊查询 - 创建人
            if (StringUtils.isNotEmpty(queryRequest.getCreatePerson())) {
                predicates.add(cbuild.like(root.get("createPerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCreatePerson()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 大于或等于 搜索条件:更新时间开始
            if (queryRequest.getUpdateTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("updateTime"),
                        queryRequest.getUpdateTimeBegin()));
            }
            // 小于或等于 搜索条件:更新时间截止
            if (queryRequest.getUpdateTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("updateTime"),
                        queryRequest.getUpdateTimeEnd()));
            }

            // 模糊查询 - 更新人
            if (StringUtils.isNotEmpty(queryRequest.getUpdatePerson())) {
                predicates.add(cbuild.like(root.get("updatePerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getUpdatePerson()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
