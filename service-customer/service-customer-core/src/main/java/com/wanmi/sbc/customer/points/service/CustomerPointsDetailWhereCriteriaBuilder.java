package com.wanmi.sbc.customer.points.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.customer.api.request.points.CustomerPointsDetailQueryRequest;
import com.wanmi.sbc.customer.points.model.root.CustomerPointsDetail;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>会员积分明细动态查询条件构建器</p>
 */
public class CustomerPointsDetailWhereCriteriaBuilder {
    public static Specification<CustomerPointsDetail> build(CustomerPointsDetailQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 用户编号
            if (queryRequest.getCustomerId() != null) {
                predicates.add(cbuild.equal(root.get("customerId"), queryRequest.getCustomerId()));
            }

            // 用户编号列表
            if (CollectionUtils.isNotEmpty(queryRequest.getCustomerIdList())) {
                predicates.add(root.get("customerId").in(queryRequest.getCustomerIdList()));
            }

            // 模糊查询 - 用户账号
            if (StringUtils.isNotEmpty(queryRequest.getCustomerAccount())) {
                predicates.add(cbuild.like(root.get("customerAccount"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCustomerAccount()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 用户名
            if (StringUtils.isNotEmpty(queryRequest.getCustomerName())) {
                predicates.add(cbuild.like(root.get("customerName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCustomerName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 操作类型 0:扣除 1:增长
            if (queryRequest.getType() != null) {
                predicates.add(cbuild.equal(root.get("type"), queryRequest.getType()));
            }

            // 业务类型 0签到 1注册 2分享商品 3分享注册 4分享购买  5评论商品 6晒单 7上传头像/完善个人信息 8绑定微信
            // 9添加收货地址 10关注店铺 11订单完成 12订单抵扣 13优惠券兑换 14积分兑换 15退单返还 16订单取消返还 17过期扣除
            if (queryRequest.getServiceType() != null) {
                predicates.add(cbuild.equal(root.get("serviceType"), queryRequest.getServiceType()));
            }

            // 内容备注
            if (queryRequest.getContent() != null) {
                predicates.add(cbuild.equal(root.get("content"), queryRequest.getContent()));
            }

            // 操作开始时间
            if (queryRequest.getOpTimeBegin() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String time = formatter.format(queryRequest.getOpTimeBegin());
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("opTime"), LocalDateTime.of(LocalDate
                        .parse(time, formatter), LocalTime.MIN)));
            }

            // 操作结束时间
            if (queryRequest.getOpTimeEnd() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String time = formatter.format(queryRequest.getOpTimeEnd());
                predicates.add(cbuild.lessThanOrEqualTo(root.get("opTime"), LocalDateTime.of(LocalDate
                        .parse(time, formatter), LocalTime.MAX)));
            }

            cquery.orderBy(cbuild.desc(root.get("opTime")),cbuild.desc(root.get("id")));

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
