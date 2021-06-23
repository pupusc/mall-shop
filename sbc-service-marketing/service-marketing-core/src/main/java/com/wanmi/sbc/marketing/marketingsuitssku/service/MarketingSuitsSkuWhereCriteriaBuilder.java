package com.wanmi.sbc.marketing.marketingsuitssku.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.marketing.api.request.marketingsuitssku.MarketingSuitsSkuQueryRequest;
import com.wanmi.sbc.marketing.marketingsuitssku.model.root.MarketingSuitsSku;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>组合活动关联商品sku表动态查询条件构建器</p>
 * @author zhk
 * @date 2020-04-02 10:51:12
 */
public class MarketingSuitsSkuWhereCriteriaBuilder {
    public static Specification<MarketingSuitsSku> build(MarketingSuitsSkuQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 关联查询
            // Join<MarketingSuitsSku, Marketing> marketingJoin = root.join("marketing");
            // 批量查询-主键idList
            if (CollectionUtils.isNotEmpty(queryRequest.getIdList())) {
                predicates.add(root.get("id").in(queryRequest.getIdList()));
            }

            // 主键id
            if (queryRequest.getId() != null) {
                predicates.add(cbuild.equal(root.get("id"), queryRequest.getId()));
            }

            // 组合id
            if (queryRequest.getSuitsId() != null) {
                predicates.add(cbuild.equal(root.get("suitsId"), queryRequest.getSuitsId()));
            }

            // 促销活动id
            if (queryRequest.getMarketingId() != null) {
                predicates.add(cbuild.equal(root.get("marketingId"), queryRequest.getMarketingId()));
            }

            // 模糊查询 - skuId
            if (StringUtils.isNotEmpty(queryRequest.getSkuId())) {
                predicates.add(cbuild.like(root.get("skuId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getSkuId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 单个优惠价格（优惠多少）
            if (queryRequest.getDiscountPrice() != null) {
                predicates.add(cbuild.equal(root.get("discountPrice"), queryRequest.getDiscountPrice()));
            }

            // sku数量
            if (queryRequest.getNum() != null) {
                predicates.add(cbuild.equal(root.get("num"), queryRequest.getNum()));
            }
            //
            // // 精确查询-营销类型
            // if (Objects.nonNull(queryRequest.getMarketingType())) {
            //     predicates.add(cbuild.equal(marketingJoin.get("marketingType"), queryRequest.getMarketingType().toValue()));
            // }
            //
            // // 精确查询-删除标志
            // if(Objects.nonNull(queryRequest.getDelFlag())){
            //     predicates.add(cbuild.equal(marketingJoin.get("delFlag"), queryRequest.getDelFlag().toValue()));
            // }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
