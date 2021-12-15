package com.wanmi.sbc.customer.paidcardcustomerrel.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelQueryRequest;
import com.wanmi.sbc.customer.paidcardcustomerrel.model.root.PaidCardCustomerRel;
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
 * @date 2021-01-29 14:03:59
 */
public class PaidCardCustomerRelWhereCriteriaBuilder {
    public static Specification<PaidCardCustomerRel> build(PaidCardCustomerRelQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-主键List
            if (CollectionUtils.isNotEmpty(queryRequest.getIdList())) {
                predicates.add(root.get("id").in(queryRequest.getIdList()));
            }
            if (CollectionUtils.isNotEmpty(queryRequest.getIdList())) {
                predicates.add(root.get("sendMsgFlag").in(queryRequest.getSendMsgFlag()));
            }
            if (CollectionUtils.isNotEmpty(queryRequest.getPaidCardIdList())) {
                predicates.add(root.get("paidCardId").in(queryRequest.getPaidCardIdList()));
            }

            if (queryRequest.getCurrentTime() != null) {
                predicates.add(cbuild.lessThan(root.get("beginTime"), queryRequest.getCurrentTime()));
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("endTime"), queryRequest.getCurrentTime()));
            }

            if (queryRequest.getMaxTmpId() != null) {
                predicates.add(cbuild.greaterThan(root.get("tmpId"), queryRequest.getMaxTmpId()));
            }

            // 主键
            if (StringUtils.isNotEmpty(queryRequest.getId())) {
                predicates.add(cbuild.equal(root.get("id"), queryRequest.getId()));
            }
//            做一次 优化 like 更改为 equals by duanlongshan
//            // 模糊查询 - 会员id
//            if (StringUtils.isNotEmpty(queryRequest.getCustomerId())) {
//                predicates.add(cbuild.like(root.get("customerId"), StringUtil.SQL_LIKE_CHAR
//                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCustomerId()))
//                        .concat(StringUtil.SQL_LIKE_CHAR)));
//            }
//
//            // 模糊查询 - 付费会员类型ID
//            if (StringUtils.isNotEmpty(queryRequest.getPaidCardId())) {
//                predicates.add(cbuild.like(root.get("paidCardId"), StringUtil.SQL_LIKE_CHAR
//                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getPaidCardId()))
//                        .concat(StringUtil.SQL_LIKE_CHAR)));
//            }

            // 模糊查询 - 会员id
            if (StringUtils.isNotEmpty(queryRequest.getCustomerId())) {
                predicates.add(cbuild.equal(root.get("customerId"), queryRequest.getCustomerId()));
            }

            // 模糊查询 - 付费会员类型ID
            if (StringUtils.isNotEmpty(queryRequest.getPaidCardId())) {
                predicates.add(cbuild.equal(root.get("paidCardId"), queryRequest.getPaidCardId()));
            }

            // 大于或等于 搜索条件:开始时间开始
            if (queryRequest.getBeginTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("beginTime"),
                        queryRequest.getBeginTimeBegin()));
            }
            // 小于或等于 搜索条件:开始时间截止
            if (queryRequest.getBeginTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("beginTime"),
                        queryRequest.getBeginTimeEnd()));
            }

            // 大于或等于 搜索条件:结束时间开始
            if (queryRequest.getEndTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("endTime"),
                        queryRequest.getEndTimeBegin()));
            }
            // 小于或等于 搜索条件:结束时间截止
            if (queryRequest.getEndTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("endTime"),
                        queryRequest.getEndTimeEnd()));
            }

            // 大于或等于 (用于筛选过期会员卡)
            if (queryRequest.getEndTimeFlag() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("endTime"),
                        queryRequest.getEndTimeFlag()));
            }

            // 状态： 0：未删除 1：删除
            if (queryRequest.getDelFlag() != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), queryRequest.getDelFlag()));
            }

            if (queryRequest.getSendMsgFlag() != null) {
                predicates.add(cbuild.equal(root.get("sendMsgFlag"), queryRequest.getSendMsgFlag()));
            }

            if (queryRequest.getSendExpireMsgFlag() != null) {
                predicates.add(cbuild.equal(root.get("sendExpireMsgFlag"), queryRequest.getSendExpireMsgFlag()));
            }

            // 模糊查询 - 卡号
            if (StringUtils.isNotEmpty(queryRequest.getCardNo())) {
                predicates.add(cbuild.like(root.get("cardNo"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCardNo()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            if(CollectionUtils.isNotEmpty(queryRequest.getCustomerIdList())) {
                predicates.add(root.get("customerId").in(queryRequest.getCustomerIdList()));
            }
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
