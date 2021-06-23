package com.wanmi.sbc.customer.customersignrecord.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.customer.api.request.customersignrecord.CustomerSignRecordQueryRequest;
import com.wanmi.sbc.customer.customersignrecord.model.root.CustomerSignRecord;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>用户签到记录动态查询条件构建器</p>
 * @author wangtao
 * @date 2019-10-05 16:13:04
 */
public class CustomerSignRecordWhereCriteriaBuilder {
    public static Specification<CustomerSignRecord> build(CustomerSignRecordQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-用户签到记录表idList
            if (CollectionUtils.isNotEmpty(queryRequest.getSignRecordIdList())) {
                predicates.add(root.get("signRecordId").in(queryRequest.getSignRecordIdList()));
            }

            // 用户签到记录表id
            if (StringUtils.isNotEmpty(queryRequest.getSignRecordId())) {
                predicates.add(cbuild.equal(root.get("signRecordId"), queryRequest.getSignRecordId()));
            }

            // 模糊查询 - 用户id
            if (StringUtils.isNotEmpty(queryRequest.getCustomerId())) {
                predicates.add(cbuild.like(root.get("customerId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCustomerId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 大于或等于 搜索条件:签到日期记录开始
            if (queryRequest.getSignRecordBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("signRecord"),
                        queryRequest.getSignRecordBegin()));
            }
            // 小于或等于 搜索条件:签到日期记录截止
            if (queryRequest.getSignRecordEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("signRecord"),
                        queryRequest.getSignRecordEnd()));
            }

            // 删除区分：0 未删除，1 已删除
            if (queryRequest.getDelFlag() != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), queryRequest.getDelFlag()));
            }
            // 模糊查询 - 签到ip
            if (StringUtils.isNotEmpty(queryRequest.getSignIp())) {
                predicates.add(cbuild.like(root.get("signIp"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getSignIp()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }
            // 精确查询 - 签到终端
            if (StringUtils.isNotEmpty(queryRequest.getSignTerminal())) {
                predicates.add(cbuild.equal(root.get("signTerminal"), queryRequest.getSignTerminal()));
            }
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
