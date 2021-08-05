package com.wanmi.sbc.crm.preferencetagdetail.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.crm.api.request.preferencetagdetail.PreferenceTagDetailQueryRequest;
import com.wanmi.sbc.crm.preferencetagdetail.model.root.PreferenceTagDetail;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>偏好标签明细动态查询条件构建器</p>
 * @author dyt
 * @date 2020-03-11 14:58:07
 */
public class PreferenceTagDetailWhereCriteriaBuilder {
    public static Specification<PreferenceTagDetail> build(PreferenceTagDetailQueryRequest queryRequest) {
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

            // 标签id
            if (queryRequest.getTagId() != null) {
                predicates.add(cbuild.equal(root.get("tagId"), queryRequest.getTagId()));
            }

            // 模糊查询 - 偏好类标签名称
            if (StringUtils.isNotEmpty(queryRequest.getDetailName())) {
                predicates.add(cbuild.like(root.get("detailName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getDetailName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 会员人数
            if (queryRequest.getCustomerCount() != null) {
                predicates.add(cbuild.equal(root.get("customerCount"), queryRequest.getCustomerCount()));
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

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
