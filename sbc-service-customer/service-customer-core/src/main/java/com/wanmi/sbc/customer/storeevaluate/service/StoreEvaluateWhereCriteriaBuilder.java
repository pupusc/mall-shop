package com.wanmi.sbc.customer.storeevaluate.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.customer.api.request.storeevaluate.StoreEvaluateQueryRequest;
import com.wanmi.sbc.customer.storeevaluate.model.root.StoreEvaluate;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.apache.commons.lang3.StringUtils;
import com.wanmi.sbc.common.util.XssUtils;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>店铺评价动态查询条件构建器</p>
 * @author liutao
 * @date 2019-02-26 10:23:32
 */
public class StoreEvaluateWhereCriteriaBuilder {
    public static Specification<StoreEvaluate> build(StoreEvaluateQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-评价idList
            if (CollectionUtils.isNotEmpty(queryRequest.getEvaluateIdList())) {
                predicates.add(root.get("evaluateId").in(queryRequest.getEvaluateIdList()));
            }

            // 评价id
            if (StringUtils.isNotEmpty(queryRequest.getEvaluateId())) {
                predicates.add(cbuild.equal(root.get("evaluateId"), queryRequest.getEvaluateId()));
            }

            // 店铺Id
            if (queryRequest.getStoreId() != null) {
                predicates.add(cbuild.equal(root.get("storeId"), queryRequest.getStoreId()));
            }

            // 模糊查询 - 店铺名称
            if (StringUtils.isNotEmpty(queryRequest.getStoreName())) {
                predicates.add(cbuild.like(root.get("storeName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getStoreName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 订单号
            if (StringUtils.isNotEmpty(queryRequest.getOrderNo())) {
                predicates.add(cbuild.like(root.get("orderNo"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getOrderNo()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 会员Id
            if (StringUtils.isNotEmpty(queryRequest.getCustomerId())) {
                predicates.add(cbuild.like(root.get("customerId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCustomerId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 会员名称
            if (StringUtils.isNotEmpty(queryRequest.getCustomerName())) {
                predicates.add(cbuild.like(root.get("customerName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCustomerName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 会员登录账号|手机号
            if (StringUtils.isNotEmpty(queryRequest.getCustomerAccount())) {
                predicates.add(cbuild.like(root.get("customerAccount"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCustomerAccount()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 商品评分
            if (queryRequest.getGoodsScore() != null) {
                predicates.add(cbuild.equal(root.get("goodsScore"), queryRequest.getGoodsScore()));
            }

            // 服务评分
            if (queryRequest.getServerScore() != null) {
                predicates.add(cbuild.equal(root.get("serverScore"), queryRequest.getServerScore()));
            }

            // 物流评分
            if (queryRequest.getLogisticsScore() != null) {
                predicates.add(cbuild.equal(root.get("logisticsScore"), queryRequest.getLogisticsScore()));
            }

            // 综合评分（冗余字段看后面怎么做）
            if (queryRequest.getCompositeScore() != null) {
                predicates.add(cbuild.equal(root.get("compositeScore"), queryRequest.getCompositeScore()));
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

            // 模糊查询 - 创建人
            if (StringUtils.isNotEmpty(queryRequest.getCreatePerson())) {
                predicates.add(cbuild.like(root.get("createPerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCreatePerson()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 大于或等于 搜索条件:修改时间开始
            if (queryRequest.getUpdateTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("updateTime"),
                        queryRequest.getUpdateTimeBegin()));
            }
            // 小于或等于 搜索条件:修改时间截止
            if (queryRequest.getUpdateTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("updateTime"),
                        queryRequest.getUpdateTimeEnd()));
            }

            // 模糊查询 - 修改人
            if (StringUtils.isNotEmpty(queryRequest.getUpdatePerson())) {
                predicates.add(cbuild.like(root.get("updatePerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getUpdatePerson()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 大于或等于 搜索条件:删除时间开始
            if (queryRequest.getDelTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("delTime"),
                        queryRequest.getDelTimeBegin()));
            }
            // 小于或等于 搜索条件:删除时间截止
            if (queryRequest.getDelTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("delTime"),
                        queryRequest.getDelTimeEnd()));
            }

            // 模糊查询 - 删除人
            if (StringUtils.isNotEmpty(queryRequest.getDelPerson())) {
                predicates.add(cbuild.like(root.get("delPerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getDelPerson()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }
            cquery.orderBy(cbuild.desc(root.get("createTime")));
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
