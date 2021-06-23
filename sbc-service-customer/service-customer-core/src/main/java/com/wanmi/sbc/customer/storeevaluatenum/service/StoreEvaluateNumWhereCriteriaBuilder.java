package com.wanmi.sbc.customer.storeevaluatenum.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.customer.api.request.storeevaluatenum.StoreEvaluateNumQueryRequest;
import com.wanmi.sbc.customer.storeevaluatenum.model.root.StoreEvaluateNum;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>店铺统计评分等级人数统计动态查询条件构建器</p>
 * @author liutao
 * @date 2019-03-04 10:55:28
 */
public class StoreEvaluateNumWhereCriteriaBuilder {
    public static Specification<StoreEvaluateNum> build(StoreEvaluateNumQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-id 主键List
            if (CollectionUtils.isNotEmpty(queryRequest.getNumIdList())) {
                predicates.add(root.get("numId").in(queryRequest.getNumIdList()));
            }

            // id 主键
            if (StringUtils.isNotEmpty(queryRequest.getNumId())) {
                predicates.add(cbuild.equal(root.get("numId"), queryRequest.getNumId()));
            }

            // 店铺id
            if (queryRequest.getStoreId() != null) {
                predicates.add(cbuild.equal(root.get("storeId"), queryRequest.getStoreId()));
            }

            // 模糊查询 - 店铺名称
            if (StringUtils.isNotEmpty(queryRequest.getStoreName())) {
                predicates.add(cbuild.like(root.get("storeName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getStoreName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 优秀评分数（5星-4星）
            if (queryRequest.getExcellentNum() != null) {
                predicates.add(cbuild.equal(root.get("excellentNum"), queryRequest.getExcellentNum()));
            }

            // 中等评分数（3星）
            if (queryRequest.getMediumNum() != null) {
                predicates.add(cbuild.equal(root.get("mediumNum"), queryRequest.getMediumNum()));
            }

            // 差的评分数（1星-2星）
            if (queryRequest.getDifferenceNum() != null) {
                predicates.add(cbuild.equal(root.get("differenceNum"), queryRequest.getDifferenceNum()));
            }

            // 综合评分
            if (queryRequest.getSumCompositeScore() != null) {
                predicates.add(cbuild.equal(root.get("sumCompositeScore"), queryRequest.getSumCompositeScore()));
            }

            // 评分周期 0：30天，1：90天，2：180天
            if (queryRequest.getScoreCycle() != null) {
                predicates.add(cbuild.equal(root.get("scoreCycle"), queryRequest.getScoreCycle()));
            }

            // 统计类型 0：商品评分，1：服务评分，2：物流评分
            if (queryRequest.getNumType() != null) {
                predicates.add(cbuild.equal(root.get("numType"), queryRequest.getNumType()));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
