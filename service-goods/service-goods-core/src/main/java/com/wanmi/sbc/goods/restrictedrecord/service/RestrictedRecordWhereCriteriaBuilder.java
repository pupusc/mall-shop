package com.wanmi.sbc.goods.restrictedrecord.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.goods.api.request.restrictedrecord.RestrictedRecordQueryRequest;
import com.wanmi.sbc.goods.restrictedrecord.model.root.RestrictedRecord;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>限售动态查询条件构建器</p>
 * @author 限售记录
 * @date 2020-04-11 15:59:01
 */
public class RestrictedRecordWhereCriteriaBuilder {
    public static Specification<RestrictedRecord> build(RestrictedRecordQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-记录主键List
            if (CollectionUtils.isNotEmpty(queryRequest.getRecordIdList())) {
                predicates.add(root.get("recordId").in(queryRequest.getRecordIdList()));
            }

            // 记录主键
            if (queryRequest.getRecordId() != null) {
                predicates.add(cbuild.equal(root.get("recordId"), queryRequest.getRecordId()));
            }

            // 模糊查询 - 会员的主键
            if (StringUtils.isNotEmpty(queryRequest.getCustomerId())) {
                predicates.add(cbuild.like(root.get("customerId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCustomerId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 货品主键
            if (StringUtils.isNotEmpty(queryRequest.getGoodsInfoId())) {
                predicates.add(cbuild.like(root.get("goodsInfoId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getGoodsInfoId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 购买的数量
            if (queryRequest.getPurchaseNum() != null) {
                predicates.add(cbuild.equal(root.get("purchaseNum"), queryRequest.getPurchaseNum()));
            }

            // 周期类型（0: 终生，1:周  2:月  3:年）
            if (queryRequest.getRestrictedCycleType() != null) {
                predicates.add(cbuild.equal(root.get("restrictedCycleType"), queryRequest.getRestrictedCycleType()));
            }

            // 大于或等于 搜索条件:开始时间开始
            if (queryRequest.getStartDateBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("startDate"),
                        queryRequest.getStartDateBegin()));
            }
            // 小于或等于 搜索条件:开始时间截止
            if (queryRequest.getStartDateEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("startDate"),
                        queryRequest.getStartDateEnd()));
            }

            // 大于或等于 搜索条件:结束时间开始
            if (queryRequest.getEndDateBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("endDate"),
                        queryRequest.getEndDateBegin()));
            }
            // 小于或等于 搜索条件:结束时间截止
            if (queryRequest.getEndDateEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("endDate"),
                        queryRequest.getEndDateEnd()));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
