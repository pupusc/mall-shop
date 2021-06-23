package com.wanmi.sbc.goods.goodsrestrictedcustomerrela.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.goods.api.request.goodsrestrictedcustomerrela.GoodsRestrictedCustomerRelaQueryRequest;
import com.wanmi.sbc.goods.goodsrestrictedcustomerrela.model.root.GoodsRestrictedCustomerRela;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>限售配置动态查询条件构建器</p>
 * @author baijz
 * @date 2020-04-08 11:32:28
 */
public class GoodsRestrictedCustomerRelaWhereCriteriaBuilder {
    public static Specification<GoodsRestrictedCustomerRela> build(GoodsRestrictedCustomerRelaQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-限售会员的关系主键List
            if (CollectionUtils.isNotEmpty(queryRequest.getRelaIdList())) {
                predicates.add(root.get("relaId").in(queryRequest.getRelaIdList()));
            }

            // 限售会员的关系主键
            if (queryRequest.getRelaId() != null) {
                predicates.add(cbuild.equal(root.get("relaId"), queryRequest.getRelaId()));
            }

            // 限售主键
            if (queryRequest.getRestrictedSaleId() != null) {
                predicates.add(cbuild.equal(root.get("restrictedSaleId"), queryRequest.getRestrictedSaleId()));
            }

            // 特定会员的限售类型 0: 会员等级  1：指定会员
            if (queryRequest.getAssignPersonRestrictedType() != null) {
                predicates.add(cbuild.equal(root.get("personRestrictedType"), queryRequest.getAssignPersonRestrictedType()));
            }

            // 模糊查询 - 会员ID
            if (StringUtils.isNotEmpty(queryRequest.getCustomerId())) {
                predicates.add(cbuild.like(root.get("customerId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCustomerId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
