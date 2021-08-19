package com.wanmi.sbc.message.pushcustomerenable.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.message.api.request.pushcustomerenable.PushCustomerEnableQueryRequest;
import com.wanmi.sbc.message.pushcustomerenable.model.root.PushCustomerEnable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>会员推送通知开关动态查询条件构建器</p>
 * @author Bob
 * @date 2020-01-07 15:31:47
 */
public class PushCustomerEnableWhereCriteriaBuilder {
    public static Specification<PushCustomerEnable> build(PushCustomerEnableQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-会员IDList
            if (CollectionUtils.isNotEmpty(queryRequest.getCustomerIdList())) {
                predicates.add(root.get("customerId").in(queryRequest.getCustomerIdList()));
            }

            // 会员ID
            if (StringUtils.isNotEmpty(queryRequest.getCustomerId())) {
                predicates.add(cbuild.equal(root.get("customerId"), queryRequest.getCustomerId()));
            }

            // 开启状态 0:未开启 1:启用
            if (queryRequest.getEnableStatus() != null) {
                predicates.add(cbuild.equal(root.get("enableStatus"), queryRequest.getEnableStatus()));
            }

            // 账号安全通知 0:未启用 1:启用
            if (queryRequest.getAccountSecurity() != null) {
                predicates.add(cbuild.equal(root.get("accountSecurity"), queryRequest.getAccountSecurity()));
            }

            // 账户资产通知 0:未启用 1:启用
            if (queryRequest.getAccountAssets() != null) {
                predicates.add(cbuild.equal(root.get("accountAssets"), queryRequest.getAccountAssets()));
            }

            // 订单进度通知 0:未启用 1:启用
            if (queryRequest.getOrderProgressRate() != null) {
                predicates.add(cbuild.equal(root.get("orderProgressRate"), queryRequest.getOrderProgressRate()));
            }

            // 退单进度通知 0:未启用 1:启用
            if (queryRequest.getReturnOrderProgressRate() != null) {
                predicates.add(cbuild.equal(root.get("returnOrderProgressRate"), queryRequest.getReturnOrderProgressRate()));
            }

            // 分销业务通知 0:未启用 1:启用
            if (queryRequest.getDistribution() != null) {
                predicates.add(cbuild.equal(root.get("distribution"), queryRequest.getDistribution()));
            }

            // 删除标志 0:未删除 1:删除
            if (queryRequest.getDelFlag() != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), queryRequest.getDelFlag()));
            }

            // 模糊查询 - 创建人ID
            if (StringUtils.isNotEmpty(queryRequest.getCreatePerson())) {
                predicates.add(cbuild.like(root.get("createPerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCreatePerson()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
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

            // 模糊查询 - 更新人ID
            if (StringUtils.isNotEmpty(queryRequest.getUpdatePerson())) {
                predicates.add(cbuild.like(root.get("updatePerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getUpdatePerson()))
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

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
