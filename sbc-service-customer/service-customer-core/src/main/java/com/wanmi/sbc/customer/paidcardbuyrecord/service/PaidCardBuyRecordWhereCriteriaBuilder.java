package com.wanmi.sbc.customer.paidcardbuyrecord.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.customer.api.request.paidcardbuyrecord.PaidCardBuyRecordQueryRequest;
import com.wanmi.sbc.customer.paidcardbuyrecord.model.root.PaidCardBuyRecord;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.apache.commons.lang3.StringUtils;
import com.wanmi.sbc.common.util.XssUtils;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>付费会员动态查询条件构建器</p>
 * @author xuhai
 * @date 2021-01-29 14:03:58
 */
public class PaidCardBuyRecordWhereCriteriaBuilder {
    public static Specification<PaidCardBuyRecord> build(PaidCardBuyRecordQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-流水号List
            if (CollectionUtils.isNotEmpty(queryRequest.getPayCodeList())) {
                predicates.add(root.get("payCode").in(queryRequest.getPayCodeList()));
            }

            // 流水号
            if (StringUtils.isNotEmpty(queryRequest.getPayCode())) {
                predicates.add(cbuild.equal(root.get("payCode"), queryRequest.getPayCode()));
            }

            // 模糊查询 - 用户id
            if (StringUtils.isNotEmpty(queryRequest.getCustomerId())) {
                predicates.add(cbuild.like(root.get("customerId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCustomerId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 用户姓名
            if (StringUtils.isNotEmpty(queryRequest.getCustomerName())) {
                predicates.add(cbuild.like(root.get("customerName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCustomerName()))
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

            // 模糊查询 - 卡号
            if (StringUtils.isNotEmpty(queryRequest.getCardNo())) {
                predicates.add(cbuild.like(root.get("cardNo"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCardNo()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 付费会员类型id
            if (StringUtils.isNotEmpty(queryRequest.getPaidCardId())) {
                predicates.add(cbuild.like(root.get("paidCardId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getPaidCardId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 付费会员类型名称
            if (StringUtils.isNotEmpty(queryRequest.getPaidCardName())) {
                predicates.add(cbuild.like(root.get("paidCardName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getPaidCardName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 付费周期id
            if (StringUtils.isNotEmpty(queryRequest.getRuleId())) {
                predicates.add(cbuild.like(root.get("ruleId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getRuleId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 付费周期名称
            if (StringUtils.isNotEmpty(queryRequest.getRuleName())) {
                predicates.add(cbuild.like(root.get("ruleName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getRuleName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 价格
            if (queryRequest.getPrice() != null) {
                predicates.add(cbuild.equal(root.get("price"), queryRequest.getPrice()));
            }

            // 购买类型 0：购买 1：续费
            if (queryRequest.getType() != null) {
                predicates.add(cbuild.equal(root.get("type"), queryRequest.getType()));
            }

            // 模糊查询 - 用户账号
            if (StringUtils.isNotEmpty(queryRequest.getCustomerAccount())) {
                predicates.add(cbuild.like(root.get("customerAccount"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCustomerAccount()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }
            if (StringUtils.isNotEmpty(queryRequest.getCustomerPaidcardId())) {
                predicates.add(cbuild.equal(root.get("customerPaidcardId"),queryRequest.getCustomerPaidcardId()) );
            }

            // 大于或等于 搜索条件:失效时间开始
            if (queryRequest.getInvalidTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("invalidTime"),
                        queryRequest.getInvalidTimeBegin()));
            }
            // 小于或等于 搜索条件:失效时间截止
            if (queryRequest.getInvalidTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("invalidTime"),
                        queryRequest.getInvalidTimeEnd()));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
