package com.wanmi.sbc.marketing.markup.service;

import com.wanmi.sbc.marketing.api.request.markuplevel.MarkupLevelQueryRequest;
import com.wanmi.sbc.marketing.markup.model.root.MarkupLevel;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>加价购活动动态查询条件构建器</p>
 * @author he
 * @date 2021-02-04 16:11:01
 */
public class MarkupLevelWhereCriteriaBuilder {
    public static Specification<MarkupLevel> build(MarkupLevelQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-加价购阶梯idList
            if (CollectionUtils.isNotEmpty(queryRequest.getIdList())) {
                predicates.add(root.get("id").in(queryRequest.getIdList()));
            }
            if (CollectionUtils.isNotEmpty(queryRequest.getMarkupIds())) {
                predicates.add(root.get("markupId").in(queryRequest.getMarkupIds()));
            }

            // 加价购阶梯id
            if (queryRequest.getId() != null) {
                predicates.add(cbuild.equal(root.get("id"), queryRequest.getId()));
            }

            // 加价购id
            if (queryRequest.getMarkupId() != null) {
                predicates.add(cbuild.equal(root.get("markupId"), queryRequest.getMarkupId()));
            }

            // 加价购阶梯满足金额
            if (queryRequest.getLevelAmount() != null) {
                predicates.add(cbuild.equal(root.get("levelAmount"), queryRequest.getLevelAmount()));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
