package com.wanmi.sbc.marketing.pointscoupon.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.marketing.api.request.pointscoupon.PointsCouponQueryRequest;
import com.wanmi.sbc.marketing.coupon.model.root.CouponInfo;
import com.wanmi.sbc.marketing.pointscoupon.model.root.PointsCoupon;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>积分兑换券表动态查询条件构建器</p>
 *
 * @author yang
 * @date 2019-06-11 10:07:09
 */
public class PointsCouponWhereCriteriaBuilder {
    public static Specification<PointsCoupon> build(PointsCouponQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<PointsCoupon, CouponInfo> pointsCouponCouponInfoJoin = root.join("couponInfo");
            // 批量查询-积分兑换券idList
            if (CollectionUtils.isNotEmpty(queryRequest.getPointsCouponIdList())) {
                predicates.add(root.get("pointsCouponId").in(queryRequest.getPointsCouponIdList()));
            }

            // 积分兑换券id
            if (queryRequest.getPointsCouponId() != null) {
                predicates.add(cbuild.equal(root.get("pointsCouponId"), queryRequest.getPointsCouponId()));
            }

            // 模糊查询 - 优惠券id
            if (StringUtils.isNotEmpty(queryRequest.getCouponId())) {
                predicates.add(cbuild.like(root.get("couponId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCouponId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 兑换积分（用于查询用户可兑换优惠券）
            if (queryRequest.getPoints() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("points"), queryRequest.getPoints()));
            }
            // 大于或等于积分区间查询开始积分
            if (queryRequest.getPointsSectionStart() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("points"), queryRequest.getPointsSectionStart()));
            }

            // 小于或等于积分区间查询截止时间
            if (queryRequest.getPointsSectionEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("points"), queryRequest.getPointsSectionEnd()));
            }

            // 是否启用 0：停用，1：启用
            if (queryRequest.getStatus() != null) {
                predicates.add(cbuild.equal(root.get("status"), queryRequest.getStatus()));
            }

            // 优惠券名称模糊查询
            if (StringUtils.isNotEmpty(queryRequest.getCouponName())) {
                predicates.add(cbuild.like(pointsCouponCouponInfoJoin.get("couponName"), new StringBuffer().append("%")
                        .append(XssUtils.replaceLikeWildcard(queryRequest.getCouponName().trim())).append("%").toString()));
            }

            // 大于或等于 搜索条件:兑换开始时间开始
            if (queryRequest.getBeginTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("beginTime"),
                        queryRequest.getBeginTimeBegin()));
            }
            // 小于或等于 搜索条件:兑换开始时间截止
            if (queryRequest.getBeginTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("beginTime"),
                        queryRequest.getBeginTimeEnd()));
            }

            // 大于或等于 搜索条件:兑换结束时间开始
            if (queryRequest.getEndTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("endTime"),
                        queryRequest.getEndTimeBegin()));
            }
            // 小于或等于 搜索条件:兑换结束时间截止
            if (queryRequest.getEndTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("endTime"),
                        queryRequest.getEndTimeEnd()));
            }

            // 删除标识,0: 未删除 1: 已删除
            if (queryRequest.getDelFlag() != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), queryRequest.getDelFlag()));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
