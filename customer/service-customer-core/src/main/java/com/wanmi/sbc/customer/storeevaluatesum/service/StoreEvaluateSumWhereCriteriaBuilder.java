package com.wanmi.sbc.customer.storeevaluatesum.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.customer.api.request.storeevaluatesum.StoreEvaluateSumQueryRequest;
import com.wanmi.sbc.customer.storeevaluatesum.model.root.StoreEvaluateSum;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>店铺评价动态查询条件构建器</p>
 * @author liutao
 * @date 2019-02-23 10:59:09
 */
public class StoreEvaluateSumWhereCriteriaBuilder {
    public static Specification<StoreEvaluateSum> build(StoreEvaluateSumQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-id 主键List
            if (CollectionUtils.isNotEmpty(queryRequest.getSumIdList())) {
                predicates.add(root.get("sumId").in(queryRequest.getSumIdList()));
            }

            // id 主键
            if (queryRequest.getSumId() != null) {
                predicates.add(cbuild.equal(root.get("sumId"), queryRequest.getSumId()));
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

            // 服务综合评分
            if (queryRequest.getSumServerScore() != null) {
                predicates.add(cbuild.equal(root.get("sumServerScore"), queryRequest.getSumServerScore()));
            }

            // 商品质量综合评分
            if (queryRequest.getSumGoodsScore() != null) {
                predicates.add(cbuild.equal(root.get("sumGoodsScore"), queryRequest.getSumGoodsScore()));
            }

            // 物流综合评分
            if (queryRequest.getSumLogisticsScoreScore() != null) {
                predicates.add(cbuild.equal(root.get("sumLogisticsScoreScore"), queryRequest.getSumLogisticsScoreScore()));
            }

            // 订单数
            if (queryRequest.getOrderNum() != null) {
                predicates.add(cbuild.equal(root.get("orderNum"), queryRequest.getOrderNum()));
            }

            // 评分周期 0：30天，1：90天，2：180天
            if (queryRequest.getScoreCycle() != null) {
                predicates.add(cbuild.equal(root.get("scoreCycle"), queryRequest.getScoreCycle()));
            }

            // 综合评分
            if (queryRequest.getSumCompositeScore() != null) {
                predicates.add(cbuild.equal(root.get("sumCompositeScore"), queryRequest.getSumCompositeScore()));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
