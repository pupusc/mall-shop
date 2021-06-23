package com.wanmi.sbc.marketing.grouponactivity.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.marketing.api.request.grouponactivity.GrouponActivityQueryRequest;
import com.wanmi.sbc.marketing.bean.enums.AuditStatus;
import com.wanmi.sbc.marketing.grouponactivity.model.root.GrouponActivity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>拼团活动信息表动态查询条件构建器</p>
 *
 * @author groupon
 * @date 2019-05-15 14:02:38
 */
public class GrouponActivityWhereCriteriaBuilder {
    public static Specification<GrouponActivity> build(GrouponActivityQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-活动IDList
            if (CollectionUtils.isNotEmpty(queryRequest.getGrouponActivityIdList())) {
                predicates.add(root.get("grouponActivityId").in(queryRequest.getGrouponActivityIdList()));
            }

            // 活动ID
            if (StringUtils.isNotEmpty(queryRequest.getGrouponActivityId())) {
                predicates.add(cbuild.equal(root.get("grouponActivityId"), queryRequest.getGrouponActivityId()));
            }

            // 拼团人数
            if (queryRequest.getGrouponNum() != null) {
                predicates.add(cbuild.equal(root.get("grouponNum"), queryRequest.getGrouponNum()));
            }

            // 大于或等于 搜索条件:开始时间开始
            if (queryRequest.getStartTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("startTime"),
                        queryRequest.getStartTimeBegin()));
            }
            // 小于或等于 搜索条件:开始时间截止
            if (queryRequest.getStartTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("startTime"),
                        queryRequest.getStartTimeEnd()));
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

            // 模糊查询 - 拼团分类ID
            if (StringUtils.isNotEmpty(queryRequest.getGrouponCateId())) {
                predicates.add(cbuild.like(root.get("grouponCateId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getGrouponCateId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 是否自动成团，0：否，1：是
            if (Objects.nonNull(queryRequest.getAutoGroupon())) {
                predicates.add(cbuild.equal(root.get("autoGroupon"), queryRequest.getAutoGroupon()));
            }

            // 是否包邮，0：否，1：是
            if (Objects.nonNull(queryRequest.getFreeDelivery() )) {
                predicates.add(cbuild.equal(root.get("freeDelivery"), queryRequest.getFreeDelivery()));
            }

            // 模糊查询 - spu编号
            if (StringUtils.isNotEmpty(queryRequest.getGoodsId())) {
                predicates.add(cbuild.like(root.get("goodsId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getGoodsId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            //模糊查询 - spu编码
            if (StringUtils.isNotEmpty(queryRequest.getGoodsNo())) {
                predicates.add(cbuild.like(root.get("goodsNo"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getGoodsNo()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - spu商品名称
            if (StringUtils.isNotEmpty(queryRequest.getGoodsName())) {
                predicates.add(cbuild.like(root.get("goodsName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getGoodsName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 店铺ID
            if (StringUtils.isNotEmpty(queryRequest.getStoreId())) {
                predicates.add(cbuild.equal(root.get("storeId"), queryRequest.getStoreId()));
            }

            // 是否精选，0：否，1：是
            if (Objects.nonNull(queryRequest.getSticky())) {
                predicates.add(cbuild.equal(root.get("sticky"), queryRequest.getSticky()));
            }

            // 是否审核通过，0：待审核，1：审核通过，2：审核不通过
            if (Objects.nonNull(queryRequest.getAuditStatus())) {
                predicates.add(cbuild.equal(root.get("auditStatus"), queryRequest.getAuditStatus()));
            }


            // 是否删除，0：否，1：是
            if (Objects.nonNull(queryRequest.getDelFlag())) {
                predicates.add(cbuild.equal(root.get("delFlag"), queryRequest.getDelFlag()));
            }

            if (Objects.nonNull(queryRequest.getStartTime())) {
                //大于或等于传入时间
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("startTime"), queryRequest.getStartTime()));
            }

            if (Objects.nonNull(queryRequest.getEndTime())) {
                //小y于或等于传入时间
                predicates.add(cbuild.lessThanOrEqualTo(root.get("endTime"), queryRequest.getEndTime()));

            }
            if(Objects.nonNull(queryRequest.getTabType())){
                switch (queryRequest.getTabType()) {
                    //进行中
                    case STARTED:
                        predicates.add(cbuild.lessThanOrEqualTo(root.get("startTime"), LocalDateTime.now()));
                        predicates.add(cbuild.greaterThanOrEqualTo(root.get("endTime"), LocalDateTime.now()));
                        predicates.add(cbuild.equal(root.get("auditStatus"), AuditStatus.CHECKED));
                        break;
                    //未生效
                    case NOT_START:
                        predicates.add(cbuild.greaterThanOrEqualTo(root.get("startTime"), LocalDateTime.now()));
                        predicates.add(cbuild.equal(root.get("auditStatus"), AuditStatus.CHECKED));
                        break;
                    //已结束
                    case ENDED:
                        predicates.add(cbuild.lessThan(root.get("endTime"), LocalDateTime.now()));
                        predicates.add(cbuild.equal(root.get("auditStatus"), AuditStatus.CHECKED));
                        break;
                    //待审核
                    case WAIT_CHECK:
                        predicates.add(cbuild.equal(root.get("auditStatus"), AuditStatus.WAIT_CHECK));
                        break;
                    //审核失败
                    case NOT_PASS:
                        predicates.add(cbuild.equal(root.get("auditStatus"), AuditStatus.NOT_PASS));
                        break;
                    case WILL_AND_ALREADY_START:
                        predicates.add(cbuild.greaterThanOrEqualTo(root.get("endTime"), LocalDateTime.now()));
                        predicates.add(cbuild.equal(root.get("auditStatus"), AuditStatus.CHECKED));
                    default:
                        break;
                }
            }

            // 批量查询-spuIDList
            if (CollectionUtils.isNotEmpty(queryRequest.getSpuIdList())) {
                predicates.add(root.get("goodsId").in(queryRequest.getSpuIdList()));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
