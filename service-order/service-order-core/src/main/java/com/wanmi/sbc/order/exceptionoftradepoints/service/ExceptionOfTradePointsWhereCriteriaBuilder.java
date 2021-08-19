package com.wanmi.sbc.order.exceptionoftradepoints.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.order.api.request.exceptionoftradepoints.ExceptionOfTradePointsQueryRequest;
import com.wanmi.sbc.order.exceptionoftradepoints.model.root.ExceptionOfTradePoints;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;
import org.apache.commons.lang3.StringUtils;
import com.wanmi.sbc.common.util.XssUtils;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>积分订单抵扣异常表动态查询条件构建器</p>
 * @author caofang
 * @date 2021-03-10 18:54:25
 */
public class ExceptionOfTradePointsWhereCriteriaBuilder {
    public static Specification<ExceptionOfTradePoints> build(ExceptionOfTradePointsQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-异常标识IDList
            if (CollectionUtils.isNotEmpty(queryRequest.getIdList())) {
                predicates.add(root.get("id").in(queryRequest.getIdList()));
            }

            // 异常标识ID
            if (StringUtils.isNotEmpty(queryRequest.getId())) {
                predicates.add(cbuild.equal(root.get("id"), queryRequest.getId()));
            }

            // 模糊查询 - 订单id
            if (StringUtils.isNotEmpty(queryRequest.getTradeId())) {
                predicates.add(cbuild.like(root.get("tradeId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getTradeId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 使用积分
            if (queryRequest.getPoints() != null) {
                predicates.add(cbuild.equal(root.get("points"), queryRequest.getPoints()));
            }

            // 模糊查询 - 异常码
            if (StringUtils.isNotEmpty(queryRequest.getErrorCode())) {
                predicates.add(cbuild.like(root.get("errorCode"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getErrorCode()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 异常描述
            if (StringUtils.isNotEmpty(queryRequest.getErrorDesc())) {
                predicates.add(cbuild.like(root.get("errorDesc"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getErrorDesc()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 樊登积分抵扣码
            if (StringUtils.isNotEmpty(queryRequest.getDeductCode())) {
                predicates.add(cbuild.like(root.get("deductCode"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getDeductCode()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 处理状态,0：待处理，1：处理失败，2：处理成功
            if (queryRequest.getHandleStatus() != null) {
                predicates.add(cbuild.equal(root.get("handleStatus"), queryRequest.getHandleStatus()));
            }

            // 处理状态,0：待处理，1：处理失败，2：处理成功
            if (CollectionUtils.isNotEmpty(queryRequest.getHandleStatuses())) {
                predicates.add(root.get("handleStatus").in(queryRequest.getHandleStatuses()));
            }

            // 错误次数
            if (queryRequest.getErrorTime() != null) {
                predicates.add(cbuild.equal(root.get("errorTime"), queryRequest.getErrorTime()));
            }

            // 错误次数集合
            if (CollectionUtils.isNotEmpty(queryRequest.getErrorTimes())) {
                predicates.add(root.get("errorTime").in(queryRequest.getErrorTimes()));
            }

            // 是否删除标志 0：否，1：是
            if (queryRequest.getDelFlag() != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), queryRequest.getDelFlag()));
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

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
