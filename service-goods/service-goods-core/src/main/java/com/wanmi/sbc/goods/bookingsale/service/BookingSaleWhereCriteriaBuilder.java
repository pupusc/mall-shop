package com.wanmi.sbc.goods.bookingsale.service;

import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.goods.api.request.bookingsale.BookingSaleQueryRequest;
import com.wanmi.sbc.goods.bean.enums.PresellSaleStatus;
import com.wanmi.sbc.goods.bookingsale.model.root.BookingSale;
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
 * <p>预售信息动态查询条件构建器</p>
 *
 * @author dany
 * @date 2020-06-05 10:47:21
 */
public class BookingSaleWhereCriteriaBuilder {
    public static Specification<BookingSale> build(BookingSaleQueryRequest queryRequest) {
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

            // 0:全部 1:进行中，2 已暂停 3 未开始 4. 已结束
            if (queryRequest.getQueryTab() != null) {
                if (queryRequest.getQueryTab().equals(PresellSaleStatus.NOT_START)) {
                    predicates.add(cbuild.greaterThan(root.get("startTime"), LocalDateTime.now()));
                    predicates.add(cbuild.equal(root.get("pauseFlag"), 0));
                } else if (queryRequest.getQueryTab().equals(PresellSaleStatus.STARTED)) {
                    predicates.add(cbuild.lessThan(root.get("startTime"), LocalDateTime.now()));
                    predicates.add(cbuild.greaterThan(root.get("endTime"), LocalDateTime.now()));
                    predicates.add(cbuild.equal(root.get("pauseFlag"), 0));
                } else if (queryRequest.getQueryTab().equals(PresellSaleStatus.ENDED)) {
                    predicates.add(cbuild.lessThan(root.get("endTime"), LocalDateTime.now()));
                } else if (queryRequest.getQueryTab().equals(PresellSaleStatus.PAUSED)) {
                    predicates.add(cbuild.equal(root.get("pauseFlag"), 1));
                } else if (queryRequest.getQueryTab().equals(PresellSaleStatus.S_NS)) {
                    predicates.add(cbuild.greaterThan(root.get("endTime"), LocalDateTime.now()));
                    predicates.add(cbuild.equal(root.get("pauseFlag"), 0));
                }
            }

            // 批量查询-storeIdList
            if (CollectionUtils.isNotEmpty(queryRequest.getStoreIds())) {
                predicates.add(root.get("storeId").in(queryRequest.getStoreIds()));
            }

            // 商户id
            if (queryRequest.getStoreId() != null) {
                predicates.add(cbuild.equal(root.get("storeId"), queryRequest.getStoreId()));
            }

            // 预售类型 0：全款预售  1：定金预售
            if (queryRequest.getBookingType() != null) {
                predicates.add(cbuild.equal(root.get("bookingType"), queryRequest.getBookingType()));
            }

            // 大于或等于 搜索条件:定金支付开始时间开始
            if (queryRequest.getHandSelStartTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("handSelStartTime"),
                        queryRequest.getHandSelStartTimeBegin()));
            }
            // 小于或等于 搜索条件:定金支付开始时间截止
            if (queryRequest.getHandSelStartTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("handSelStartTime"),
                        queryRequest.getHandSelStartTimeEnd()));
            }

            // 大于或等于 搜索条件:定金支付结束时间开始
            if (queryRequest.getHandSelEndTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("handSelEndTime"),
                        queryRequest.getHandSelEndTimeBegin()));
            }
            // 小于或等于 搜索条件:定金支付结束时间截止
            if (queryRequest.getHandSelEndTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("handSelEndTime"),
                        queryRequest.getHandSelEndTimeEnd()));
            }

            // 大于或等于 搜索条件:尾款支付开始时间开始
            if (queryRequest.getTailStartTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("tailStartTime"),
                        queryRequest.getTailStartTimeBegin()));
            }
            // 小于或等于 搜索条件:尾款支付开始时间截止
            if (queryRequest.getTailStartTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("tailStartTime"),
                        queryRequest.getTailStartTimeEnd()));
            }

            // 大于或等于 搜索条件:尾款支付结束时间开始
            if (queryRequest.getTailEndTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("tailEndTime"),
                        queryRequest.getTailEndTimeBegin()));
            }
            // 小于或等于 搜索条件:尾款支付结束时间截止
            if (queryRequest.getTailEndTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("tailEndTime"),
                        queryRequest.getTailEndTimeEnd()));
            }

            // 大于或等于 搜索条件:预售开始时间开始
            if (queryRequest.getBookingStartTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("bookingStartTime"),
                        queryRequest.getBookingStartTimeBegin()));
            }
            // 小于或等于 搜索条件:预售开始时间截止
            if (queryRequest.getBookingStartTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("bookingStartTime"),
                        queryRequest.getBookingStartTimeEnd()));
            }

            // 大于或等于 搜索条件:预售结束时间开始
            if (queryRequest.getBookingEndTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("bookingEndTime"),
                        queryRequest.getBookingEndTimeBegin()));
            }
            // 小于或等于 搜索条件:预售结束时间截止
            if (queryRequest.getBookingEndTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("bookingEndTime"),
                        queryRequest.getBookingEndTimeEnd()));
            }

            // 发货日期 2020-01-10
            if (StringUtils.isNotEmpty(queryRequest.getDeliverTime())) {
                predicates.add(cbuild.equal(root.get("deliverTime"), queryRequest.getDeliverTime()));
            }

            // 大于或等于 发货开始日期 2020-01-10
            if (StringUtils.isNotEmpty(queryRequest.getDeliverStartTime())) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("deliverTime"), queryRequest.getDeliverStartTime()));
            }
            // 小于或等于 发货结束日期 2020-01-10
            if (StringUtils.isNotEmpty(queryRequest.getDeliverEndTime())) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("deliverTime"), queryRequest.getDeliverEndTime()));
            }

            if(Objects.nonNull(queryRequest.getPlatform())){
                // 模糊查询 - 参加会员  -3企业会员 -2付费会员 -1:全部客户 0:全部等级 other:其他等级
                if (Platform.SUPPLIER.equals(queryRequest.getPlatform()) && StringUtils.isNotEmpty(queryRequest.getJoinLevel())) {
                    Expression<Integer> expression = cbuild.function("FIND_IN_SET", Integer.class, cbuild.literal(queryRequest.getJoinLevel()), root.get("joinLevel"));
                    predicates.add(cbuild.greaterThan(expression, 0));
                } else if (Platform.BOSS.equals(queryRequest.getPlatform()) && StringUtils.isNotEmpty(queryRequest.getJoinLevel())) {
                    //boss端搜索项 -3企业会员 -2付费会员 -1 全部客户 0 部分客户
                    if (queryRequest.getJoinLevel().equals("0")) {
                        predicates.add(cbuild.notLike(root.get("joinLevel"), "-%"));
                    } else {
                        predicates.add(cbuild.equal(root.get("joinLevel"), queryRequest.getJoinLevel()));
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
