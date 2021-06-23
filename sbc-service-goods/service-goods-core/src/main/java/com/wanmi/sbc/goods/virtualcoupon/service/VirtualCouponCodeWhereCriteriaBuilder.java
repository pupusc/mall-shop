package com.wanmi.sbc.goods.virtualcoupon.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.goods.api.request.virtualcoupon.VirtualCouponCodeQueryRequest;
import com.wanmi.sbc.goods.util.XssUtils;
import com.wanmi.sbc.goods.virtualcoupon.model.root.VirtualCouponCode;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>券码动态查询条件构建器</p>
 * @author 梁善
 * @date 2021-01-25 16:14:42
 */
public class VirtualCouponCodeWhereCriteriaBuilder {
    public static Specification<VirtualCouponCode> build(VirtualCouponCodeQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-券码IDList
            if (CollectionUtils.isNotEmpty(queryRequest.getIdList())) {
                predicates.add(root.get("id").in(queryRequest.getIdList()));
            }

            // 券码ID
            if (queryRequest.getId() != null) {
                predicates.add(cbuild.equal(root.get("id"), queryRequest.getId()));
            }

            // 电子卡券ID
            if (queryRequest.getCouponId() != null) {
                predicates.add(cbuild.equal(root.get("couponId"), queryRequest.getCouponId()));
            }

            // 模糊查询 - 批次号
            if (StringUtils.isNotEmpty(queryRequest.getBatchNo())) {
                predicates.add(cbuild.like(root.get("batchNo"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getBatchNo()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 0:兑换码 1:券码+密钥 2:链接
            if (queryRequest.getProvideType() != null) {
                predicates.add(cbuild.equal(root.get("provideType"), queryRequest.getProvideType()));
            }

            // 模糊查询 - 兑换码/券码/链接
            if (StringUtils.isNotEmpty(queryRequest.getCouponNo())) {
                predicates.add(cbuild.like(root.get("couponNo"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCouponNo()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 大于或等于 搜索条件:领取结束时间开始
            if (queryRequest.getReceiveEndTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("receiveEndTime"),
                        queryRequest.getReceiveEndTimeBegin()));
            }
            // 小于或等于 搜索条件:领取结束时间截止
            if (queryRequest.getReceiveEndTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("receiveEndTime"),
                        queryRequest.getReceiveEndTimeEnd()));
            }

            // 0:未发放 1:已发放 2:已过期
            if (queryRequest.getStatus() != null) {
                predicates.add(cbuild.equal(root.get("status"), queryRequest.getStatus()));
            }

            // 模糊查询 - 订单号
            if (StringUtils.isNotEmpty(queryRequest.getTid())) {
                predicates.add(cbuild.like(root.get("tid"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getTid()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 删除标识;0:未删除1:已删除
            if (queryRequest.getDelFlag() != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), queryRequest.getDelFlag()));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
