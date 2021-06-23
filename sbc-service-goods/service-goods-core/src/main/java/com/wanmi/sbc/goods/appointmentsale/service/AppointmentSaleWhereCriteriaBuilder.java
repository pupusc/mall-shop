package com.wanmi.sbc.goods.appointmentsale.service;

import com.wanmi.sbc.common.enums.AppointmentStatus;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.goods.api.request.appointmentsale.AppointmentSaleQueryRequest;
import com.wanmi.sbc.goods.appointmentsale.model.root.AppointmentSale;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>预约抢购动态查询条件构建器</p>
 *
 * @author zxd
 * @date 2020-05-21 10:32:23
 */
public class AppointmentSaleWhereCriteriaBuilder {
    public static Specification<AppointmentSale> build(AppointmentSaleQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-idList
            if (CollectionUtils.isNotEmpty(queryRequest.getIdList())) {
                predicates.add(root.get("id").in(queryRequest.getIdList()));
            }

            // id
            if (queryRequest.getId() != null) {
                predicates.add(cbuild.equal(root.get("id"), queryRequest.getId()));
            }

            // 模糊查询 - 活动名称
            if (StringUtils.isNotEmpty(queryRequest.getActivityName())) {
                predicates.add(cbuild.like(root.get("activityName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getActivityName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 非模糊查询 - 活动名称
            if (StringUtils.isNotEmpty(queryRequest.getActivityNameEqual())) {
                predicates.add(cbuild.equal(root.get("activityName"), queryRequest.getActivityNameEqual()));
            }

            // 批量查询-storeIdList
            if (CollectionUtils.isNotEmpty(queryRequest.getStoreIds())) {
                predicates.add(root.get("storeId").in(queryRequest.getStoreIds()));
            }

            // 商户id
            if (queryRequest.getStoreId() != null) {
                predicates.add(cbuild.equal(root.get("storeId"), queryRequest.getStoreId()));
            }

            // 0:全部 1:进行中，2 已暂停 3 未开始 4. 已结束
            if (queryRequest.getStatus() != null) {
                if (queryRequest.getStatus().equals(AppointmentStatus.NO_START)) {
                    predicates.add(cbuild.greaterThan(root.get("appointmentStartTime"), LocalDateTime.now()));
                    predicates.add(cbuild.equal(root.get("pauseFlag"), 0));
                } else if (queryRequest.getStatus().equals(AppointmentStatus.RUNNING)) {
                    predicates.add(cbuild.lessThan(root.get("appointmentStartTime"), LocalDateTime.now()));
                    predicates.add(cbuild.greaterThan(root.get("snapUpEndTime"), LocalDateTime.now()));
                    predicates.add(cbuild.equal(root.get("pauseFlag"), 0));
                } else if (queryRequest.getStatus().equals(AppointmentStatus.END)) {
                    predicates.add(cbuild.lessThan(root.get("snapUpEndTime"), LocalDateTime.now()));
                } else if (queryRequest.getStatus().equals(AppointmentStatus.SUSPENDED)) {
                    predicates.add(cbuild.equal(root.get("pauseFlag"), 1));
                    predicates.add(cbuild.greaterThan(root.get("snapUpEndTime"), LocalDateTime.now()));
                } else if (queryRequest.getStatus().equals(AppointmentStatus.NO_START_AND_RUNNING)) {
                    predicates.add(cbuild.greaterThan(root.get("snapUpEndTime"), LocalDateTime.now()));
                    predicates.add(cbuild.equal(root.get("pauseFlag"), 0));
                }
            }

            // 预约类型 0：不预约不可购买  1：不预约可购买
            if (queryRequest.getAppointmentType() != null) {
                predicates.add(cbuild.equal(root.get("appointmentType"), queryRequest.getAppointmentType()));
            }

            // 大于或等于 搜索条件:预约开始时间开始
            if (queryRequest.getAppointmentStartTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("appointmentStartTime"),
                        queryRequest.getAppointmentStartTimeBegin()));
            }
            // 小于或等于 搜索条件:预约开始时间截止
            if (queryRequest.getAppointmentStartTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("appointmentStartTime"),
                        queryRequest.getAppointmentStartTimeEnd()));
            }

            // 大于或等于 搜索条件:预约结束时间开始
            if (queryRequest.getAppointmentEndTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("appointmentEndTime"),
                        queryRequest.getAppointmentEndTimeBegin()));
            }
            // 小于或等于 搜索条件:预约结束时间截止
            if (queryRequest.getAppointmentEndTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("appointmentEndTime"),
                        queryRequest.getAppointmentEndTimeEnd()));
            }

            // 大于或等于 搜索条件:抢购开始时间开始
            if (queryRequest.getSnapUpStartTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("snapUpStartTime"),
                        queryRequest.getSnapUpStartTimeBegin()));
            }
            // 小于或等于 搜索条件:抢购开始时间截止
            if (queryRequest.getSnapUpStartTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("snapUpStartTime"),
                        queryRequest.getSnapUpStartTimeEnd()));
            }

            // 大于或等于 搜索条件:抢购结束时间开始
            if (queryRequest.getSnapUpEndTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("snapUpEndTime"),
                        queryRequest.getSnapUpEndTimeBegin()));
            }
            // 小于或等于 搜索条件:抢购结束时间截止
            if (queryRequest.getSnapUpEndTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("snapUpEndTime"),
                        queryRequest.getSnapUpEndTimeEnd()));
            }

            // 模糊查询 - 发货日期 2020-01-10
            if (StringUtils.isNotEmpty(queryRequest.getDeliverTime())) {
                predicates.add(cbuild.like(root.get("deliverTime"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getDeliverTime()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 大于或等于 发货开始日期 2020-01-10
            if (StringUtils.isNotEmpty(queryRequest.getDeliverStartTime())) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("deliverTime"), queryRequest.getDeliverStartTime()));
            }
            // 小于或等于 发货结束日期 2020-01-10
            if (StringUtils.isNotEmpty(queryRequest.getDeliverEndTime())) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("deliverTime"), queryRequest.getDeliverEndTime()));
            }

            // 模糊查询 - 参加会员  -2 指定用户 -1:全部客户 0:全部等级 other:其他等级 -3：指定人群 -4：企业会员
            if(Objects.nonNull(queryRequest.getPlatform())){
                if (Platform.SUPPLIER.equals(queryRequest.getPlatform()) && StringUtils.isNotEmpty(queryRequest.getJoinLevel())) {
                    Expression<Integer> expression = cbuild.function("FIND_IN_SET", Integer.class, cbuild.literal(queryRequest.getJoinLevel()), root.get("joinLevel"));
                    predicates.add(cbuild.greaterThan(expression, 0));
                } else if (Platform.BOSS.equals(queryRequest.getPlatform()) && StringUtils.isNotEmpty(queryRequest.getJoinLevel())) {
                    //boss端搜索项 -1 全部客户 0 部分客户
                    if (queryRequest.getJoinLevel().equals("-1")) {
                        predicates.add(cbuild.equal(root.get("joinLevel"), queryRequest.getJoinLevel()));
                    } else {
                        predicates.add(cbuild.notEqual(root.get("joinLevel"), "-1"));
                    }
                }
            }

            // 是否平台等级 （1平台（自营店铺属于平台等级） 0店铺）
            if (queryRequest.getJoinLevelType() != null) {
                predicates.add(cbuild.equal(root.get("joinLevelType"), queryRequest.getJoinLevelType()));
            }

            // 是否删除标志 0：否，1：是
            if (queryRequest.getDelFlag() != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), queryRequest.getDelFlag()));
            }

            // 是否暂停 0:否 1:是
            if (queryRequest.getPauseFlag() != null) {
                predicates.add(cbuild.equal(root.get("pauseFlag"), queryRequest.getPauseFlag()));
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

            // 模糊查询 - 创建人
            if (StringUtils.isNotEmpty(queryRequest.getCreatePerson())) {
                predicates.add(cbuild.like(root.get("createPerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCreatePerson()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 更新人
            if (StringUtils.isNotEmpty(queryRequest.getUpdatePerson())) {
                predicates.add(cbuild.like(root.get("updatePerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getUpdatePerson()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
