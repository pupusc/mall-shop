package com.wanmi.sbc.order.paycallbackresult.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.order.api.request.paycallbackresult.PayCallBackResultQueryRequest;
import com.wanmi.sbc.order.paycallbackresult.model.root.PayCallBackResult;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>支付回调结果动态查询条件构建器</p>
 * @author lvzhenwei
 * @date 2020-07-01 17:34:23
 */
public class PayCallBackResultWhereCriteriaBuilder {
    public static Specification<PayCallBackResult> build(PayCallBackResultQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-主键List
            if (CollectionUtils.isNotEmpty(queryRequest.getIdList())) {
                predicates.add(root.get("id").in(queryRequest.getIdList()));
            }

            // 主键
            if (queryRequest.getId() != null) {
                predicates.add(cbuild.equal(root.get("id"), queryRequest.getId()));
            }

            // 订单号
            if (StringUtils.isNotEmpty(queryRequest.getBusinessId())) {
                predicates.add(cbuild.equal(root.get("businessId"), queryRequest.getBusinessId()));
            }

            //批量查询--订单号
            if (CollectionUtils.isNotEmpty(queryRequest.getBusinessIds())) {
                predicates.add(root.get("businessId").in(queryRequest.getBusinessIds()));
            }

            // 模糊查询 - 回调结果内容
            if (StringUtils.isNotEmpty(queryRequest.getResultContext())) {
                predicates.add(cbuild.like(root.get("resultContext"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getResultContext()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 结果状态，0：待处理；1:处理中 2：处理成功；3：处理失败
            if (queryRequest.getResultStatus() != null) {
                predicates.add(cbuild.equal(root.get("resultStatus"), queryRequest.getResultStatus()));
            }

            // 处理失败次数
            if (queryRequest.getErrorNum() != null) {
                predicates.add(cbuild.le(root.get("errorNum"), queryRequest.getErrorNum()));
            }

            // 支付方式，0：微信；1：支付宝；2：银联
            if (queryRequest.getPayType() != null) {
                predicates.add(cbuild.equal(root.get("payType"), queryRequest.getPayType()));
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

            // 模糊查询 - 创建人
            if (StringUtils.isNotEmpty(queryRequest.getCreatePerson())) {
                predicates.add(cbuild.like(root.get("createPerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCreatePerson()))
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

            // 模糊查询 - 更新人
            if (StringUtils.isNotEmpty(queryRequest.getUpdatePerson())) {
                predicates.add(cbuild.like(root.get("updatePerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getUpdatePerson()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }
            cquery.orderBy(cbuild.desc(root.get("createTime")));
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
