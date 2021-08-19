package com.wanmi.sbc.marketing.marketingsuits.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.marketing.api.request.marketingsuits.MarketingSuitsQueryRequest;
import com.wanmi.sbc.marketing.marketingsuits.model.root.MarketingSuits;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>组合商品主表动态查询条件构建器</p>
 * @author zhk
 * @date 2020-04-02 10:39:15
 */
public class MarketingSuitsWhereCriteriaBuilder {
    public static Specification<MarketingSuits> build(MarketingSuitsQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-主键idList
            if (CollectionUtils.isNotEmpty(queryRequest.getIdList())) {
                predicates.add(root.get("id").in(queryRequest.getIdList()));
            }

            // 主键id
            if (queryRequest.getId() != null) {
                predicates.add(cbuild.equal(root.get("id"), queryRequest.getId()));
            }

            // 促销id
            if (queryRequest.getMarketingId() != null) {
                predicates.add(cbuild.equal(root.get("marketingId"), queryRequest.getMarketingId()));
            }

            // 模糊查询 - 套餐主图（图片url全路径）
            if (StringUtils.isNotEmpty(queryRequest.getMainImage())) {
                predicates.add(cbuild.like(root.get("mainImage"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getMainImage()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 套餐价格
            if (queryRequest.getSuitsPrice() != null) {
                predicates.add(cbuild.equal(root.get("suitsPrice"), queryRequest.getSuitsPrice()));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
