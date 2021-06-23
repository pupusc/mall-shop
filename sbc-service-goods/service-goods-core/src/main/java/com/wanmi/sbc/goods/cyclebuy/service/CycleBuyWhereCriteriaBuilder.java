package com.wanmi.sbc.goods.cyclebuy.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.goods.api.request.cyclebuy.CycleBuyQueryRequest;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.cyclebuy.model.root.CycleBuy;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>周期购活动动态查询条件构建器</p>
 * @author weiwenhao
 * @date 2021-01-21 09:15:37
 */
public class CycleBuyWhereCriteriaBuilder {
    public static Specification<CycleBuy> build(CycleBuyQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-周期购IdList
            if (CollectionUtils.isNotEmpty(queryRequest.getIdList())) {
                predicates.add(root.get("id").in(queryRequest.getIdList()));
            }

            // 周期购Id
            if (queryRequest.getId() != null) {
                predicates.add(cbuild.equal(root.get("id"), queryRequest.getId()));
            }

            // 关联商品Id
            if (StringUtils.isNotEmpty(queryRequest.getGoodsId())) {
                predicates.add(cbuild.equal(root.get("goodsId"), queryRequest.getGoodsId()));
            }

            // 创建商品ID
            if (StringUtils.isNotEmpty(queryRequest.getOriginGoodsId())) {
                predicates.add(cbuild.equal(root.get("originGoodsId"), queryRequest.getOriginGoodsId()));
            }

            // 模糊查询 - 周期购活动名称
            if (StringUtils.isNotEmpty(queryRequest.getActivityName())) {
                predicates.add(cbuild.like(root.get("activityName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getActivityName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 上下架 0：下架 1:上架
            if (queryRequest.getAddedFlag() != null) {
                predicates.add(cbuild.equal(root.get("addedFlag"), queryRequest.getAddedFlag()));
            }

            // 商铺Id
            if (queryRequest.getStoreId() != null) {
                predicates.add(cbuild.equal(root.get("storeId"), queryRequest.getStoreId()));
            }

            predicates.add(cbuild.equal(root.get("delFlag"), 0));

            cquery.orderBy(cbuild.desc(root.get("createTime")),cbuild.desc(root.get("createTime")));

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
