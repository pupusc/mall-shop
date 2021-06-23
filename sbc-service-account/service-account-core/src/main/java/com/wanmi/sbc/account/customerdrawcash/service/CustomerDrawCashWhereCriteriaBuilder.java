package com.wanmi.sbc.account.customerdrawcash.service;

import com.wanmi.sbc.account.bean.enums.AuditStatus;
import com.wanmi.sbc.account.bean.enums.DrawCashStatus;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.account.api.request.customerdrawcash.CustomerDrawCashQueryRequest;
import com.wanmi.sbc.account.customerdrawcash.model.root.CustomerDrawCash;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.apache.commons.lang3.StringUtils;
import com.wanmi.sbc.common.util.XssUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>会员提现管理动态查询条件构建器</p>
 * @author chenyufei
 * @date 2019-02-25 17:22:24
 */
public class CustomerDrawCashWhereCriteriaBuilder {
    public static Specification<CustomerDrawCash> build(CustomerDrawCashQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-提现idList
            if (CollectionUtils.isNotEmpty(queryRequest.getDrawCashIdList())) {
                predicates.add(root.get("drawCashId").in(queryRequest.getDrawCashIdList()));
            }

            // 提现id
            if (StringUtils.isNotEmpty(queryRequest.getDrawCashId())) {
                predicates.add(cbuild.equal(root.get("drawCashId"), queryRequest.getDrawCashId()));
            }

            // 模糊查询 - 提现单号(订单编号)
            if (StringUtils.isNotEmpty(queryRequest.getDrawCashNo())) {
                predicates.add(cbuild.like(root.get("drawCashNo"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getDrawCashNo()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 大于或等于 搜索条件:申请时间开始
            if (queryRequest.getApplyTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("applyTime"),
                        queryRequest.getApplyTimeBegin()));
            }
            // 小于或等于 搜索条件:申请时间截止
            if (queryRequest.getApplyTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("applyTime"),
                        queryRequest.getApplyTimeEnd()));
            }

            // 模糊查询 - 会员id
            if (StringUtils.isNotEmpty(queryRequest.getCustomerId())) {
                predicates.add(cbuild.like(root.get("customerId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCustomerId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 会员名称
            if (StringUtils.isNotEmpty(queryRequest.getCustomerName())) {
                predicates.add(cbuild.like(root.get("customerName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCustomerName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 会员账号
            if (StringUtils.isNotEmpty(queryRequest.getCustomerAccount())) {
                predicates.add(cbuild.like(root.get("customerAccount"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCustomerAccount()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 提现渠道 0:微信 1:支付宝
            if (queryRequest.getDrawCashChannel() != null) {
                predicates.add(cbuild.equal(root.get("drawCashChannel"), queryRequest.getDrawCashChannel()));
            }

            // 模糊查询 - 提现账户名称
            if (StringUtils.isNotEmpty(queryRequest.getDrawCashAccountName())) {
                predicates.add(cbuild.like(root.get("drawCashAccountName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getDrawCashAccountName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 提现账户账号
            if (StringUtils.isNotEmpty(queryRequest.getDrawCashAccount())) {
                predicates.add(cbuild.like(root.get("drawCashAccount"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getDrawCashAccount()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 本次提现金额
            if (queryRequest.getDrawCashSum() != null) {
                predicates.add(cbuild.equal(root.get("drawCashSum"), queryRequest.getDrawCashSum()));
            }

            // 模糊查询 - 提现备注
            if (StringUtils.isNotEmpty(queryRequest.getDrawCashRemark())) {
                predicates.add(cbuild.like(root.get("drawCashRemark"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getDrawCashRemark()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }


            // C端请求，待审核，包括后台审核后提现失败的单子
            if (!queryRequest.getSourceFromPlatForm() && queryRequest.getAuditStatus() != null && queryRequest.getAuditStatus().equals(AuditStatus.WAIT)) {
                // 运营端审核状态(0:待审核,1:审核不通过,2:审核通过)
                Predicate p1 = cbuild.equal(root.get("auditStatus"), AuditStatus.WAIT);
                // 提现状态(0:未提现,1:提现失败,2:提现成功)
                Predicate p2 = cbuild.equal(root.get("drawCashStatus"), DrawCashStatus.FAIL);
                Predicate p3 = cbuild.or(p1,p2);
                predicates.add(p3);
            } else {
                // 运营端审核状态(0:待审核,1:审核不通过,2:审核通过)
                if (queryRequest.getAuditStatus() != null) {
                    predicates.add(cbuild.equal(root.get("auditStatus"), queryRequest.getAuditStatus()));
                }

                // 提现状态(0:未提现,1:提现失败,2:提现成功)
                if (queryRequest.getDrawCashStatus() != null) {
                    predicates.add(cbuild.equal(root.get("drawCashStatus"), queryRequest.getDrawCashStatus()));
                }
            }

            // 模糊查询 - 运营端驳回原因
            if (StringUtils.isNotEmpty(queryRequest.getRejectReason())) {
                predicates.add(cbuild.like(root.get("rejectReason"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getRejectReason()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 提现失败原因
            if (StringUtils.isNotEmpty(queryRequest.getDrawCashFailedReason())) {
                predicates.add(cbuild.like(root.get("drawCashFailedReason"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getDrawCashFailedReason()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 用户操作状态(0:已申请,1:已取消)
            if (queryRequest.getCustomerOperateStatus() != null) {
                predicates.add(cbuild.equal(root.get("customerOperateStatus"), queryRequest.getCustomerOperateStatus()));
            }

            // 提现单完成状态(0:未完成,1:已完成)
            if (queryRequest.getFinishStatus() != null) {
                predicates.add(cbuild.equal(root.get("finishStatus"), queryRequest.getFinishStatus()));
            }

            // 大于或等于 搜索条件:提现单完成时间开始
            if (queryRequest.getFinishTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("finishTime"),
                        queryRequest.getFinishTimeBegin()));
            }
            // 小于或等于 搜索条件:提现单完成时间截止
            if (queryRequest.getFinishTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("finishTime"),
                        queryRequest.getFinishTimeEnd()));
            }

            // 模糊查询 - 操作人
            if (StringUtils.isNotEmpty(queryRequest.getSupplierOperateId())) {
                predicates.add(cbuild.like(root.get("supplierOperateId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getSupplierOperateId()))
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

            // 删除标志(0:未删除,1:已删除)
            if (queryRequest.getDelFlag() != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), queryRequest.getDelFlag()));
            }

            //按支付时间倒序排列
            cquery.orderBy(cbuild.desc(root.get("applyTime")));

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
