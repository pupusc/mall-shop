package com.wanmi.sbc.setting.storeexpresscompanyrela.service;

import com.wanmi.sbc.setting.api.request.storeexpresscompanyrela.StoreExpressCompanyRelaQueryRequest;
import com.wanmi.sbc.setting.storeexpresscompanyrela.model.root.StoreExpressCompanyRela;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>店铺快递公司关联表动态查询条件构建器</p>
 * @author lq
 * @date 2019-11-05 16:12:13
 */
public class StoreExpressCompanyRelaWhereCriteriaBuilder {
    public static Specification<StoreExpressCompanyRela> build(StoreExpressCompanyRelaQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-主键UUIDList
            if (CollectionUtils.isNotEmpty(queryRequest.getIdList())) {
                predicates.add(root.get("id").in(queryRequest.getIdList()));
            }

            // 主键UUID
            if (queryRequest.getId() != null) {
                predicates.add(cbuild.equal(root.get("id"), queryRequest.getId()));
            }

            // 主键ID,自增
            if (queryRequest.getExpressCompanyId() != null) {
                predicates.add(cbuild.equal(root.get("expressCompanyId"), queryRequest.getExpressCompanyId()));
            }

            // 店铺标识
            if (queryRequest.getStoreId() != null) {
                predicates.add(cbuild.equal(root.get("storeId"), queryRequest.getStoreId()));
            }

            // 商家标识
            if (queryRequest.getCompanyInfoId() != null) {
                predicates.add(cbuild.equal(root.get("companyInfoId"), queryRequest.getCompanyInfoId()));
            }

            // 模糊查询 - 应用唯一标识
//            if (StringUtils.isNotEmpty(queryRequest.getAppKey())) {
//                predicates.add(cbuild.like(root.get("appKey"), StringUtil.SQL_LIKE_CHAR
//                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getAppKey()))
//                        .concat(StringUtil.SQL_LIKE_CHAR)));
//            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
