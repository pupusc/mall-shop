package com.wanmi.sbc.marketing.markup.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.marketing.api.request.markupleveldetail.MarkupLevelDetailQueryRequest;
import com.wanmi.sbc.marketing.markup.model.root.MarkupLevelDetail;
import com.wanmi.sbc.marketing.markup.model.root.MarkupLevelDetail;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>加价购活动动态查询条件构建器</p>
 * @author he
 * @date 2021-02-04 16:11:24
 */
public class MarkupLevelDetailWhereCriteriaBuilder {
    public static Specification<MarkupLevelDetail> build(MarkupLevelDetailQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-加价购阶梯详情idList
            if (CollectionUtils.isNotEmpty(queryRequest.getIdList())) {
                predicates.add(root.get("id").in(queryRequest.getIdList()));
            }

            // 加价购阶梯详情id
            if (queryRequest.getId() != null) {
                predicates.add(cbuild.equal(root.get("id"), queryRequest.getId()));
            }

            // 加价购活动关联id
            if (queryRequest.getMarkupId() != null) {
                predicates.add(cbuild.equal(root.get("markupId"), queryRequest.getMarkupId()));
            }

            // 加价购阶梯关联id
            if (queryRequest.getMarkupLevelId() != null) {
                predicates.add(cbuild.equal(root.get("markupLevelId"), queryRequest.getMarkupLevelId()));
            }

            // 加购商品加购价格
            if (queryRequest.getMarkupPrice() != null) {
                predicates.add(cbuild.equal(root.get("markupPrice"), queryRequest.getMarkupPrice()));
            }

            // 模糊查询 - 加购商品关联sku 
            if (StringUtils.isNotEmpty(queryRequest.getGoodsInfoId())) {
                predicates.add(cbuild.like(root.get("goodsInfoId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getGoodsInfoId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
