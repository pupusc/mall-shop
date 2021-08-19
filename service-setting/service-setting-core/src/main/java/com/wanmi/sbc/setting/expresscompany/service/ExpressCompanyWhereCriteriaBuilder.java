package com.wanmi.sbc.setting.expresscompany.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.setting.api.request.expresscompany.ExpressCompanyQueryRequest;
import com.wanmi.sbc.setting.expresscompany.model.root.ExpressCompany;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>物流公司动态查询条件构建器</p>
 * @author lq
 * @date 2019-11-05 16:10:00
 */
public class ExpressCompanyWhereCriteriaBuilder {
    public static Specification<ExpressCompany> build(ExpressCompanyQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-主键ID,自增List
            if (CollectionUtils.isNotEmpty(queryRequest.getExpressCompanyIdList())) {
                predicates.add(root.get("expressCompanyId").in(queryRequest.getExpressCompanyIdList()));
            }

            // 主键ID,自增
            if (queryRequest.getExpressCompanyId() != null) {
                predicates.add(cbuild.equal(root.get("expressCompanyId"), queryRequest.getExpressCompanyId()));
            }

            // 模糊查询 - 物流公司名称
            if (StringUtils.isNotEmpty(queryRequest.getExpressName())) {
                predicates.add(cbuild.like(root.get("expressName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getExpressName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 物流公司代码
            if (StringUtils.isNotEmpty(queryRequest.getExpressCode())) {
                predicates.add(cbuild.like(root.get("expressCode"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getExpressCode()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 是否是常用物流公司 0：否 1：是
            if (queryRequest.getIsChecked() != null) {
                predicates.add(cbuild.equal(root.get("isChecked"), queryRequest.getIsChecked()));
            }

            // 是否是用户新增 0：否 1：是
            if (queryRequest.getIsAdd() != null) {
                predicates.add(cbuild.equal(root.get("isAdd"), queryRequest.getIsAdd()));
            }

            // 删除标志 默认0：未删除 1：删除
            if (queryRequest.getDelFlag() != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), queryRequest.getDelFlag()));
            }


            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
