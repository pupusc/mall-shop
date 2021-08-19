package com.wanmi.sbc.customer.storelevel.model.entity;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.customer.storelevel.model.root.StoreLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinxianzhi on 2019/03/07.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreLevelQueryRequest extends BaseQueryRequest {

    private static final long serialVersionUID = 749964193420662659L;

    /**
     * 批量店铺等级id
     */
    private List<Long> storeLevelIds;

    /**
     * 封装公共条件
     *
     * @return
     */
    public Specification<StoreLevel> getWhereCriteria() {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(storeLevelIds)) {
                predicates.add(root.get("storeLevelId").in(storeLevelIds));
            }
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
