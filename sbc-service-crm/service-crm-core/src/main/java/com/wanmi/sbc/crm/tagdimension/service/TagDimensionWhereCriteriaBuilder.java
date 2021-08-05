package com.wanmi.sbc.crm.tagdimension.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.crm.api.request.tagdimension.TagDimensionQueryRequest;
import com.wanmi.sbc.crm.tagdimension.model.root.TagDimension;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>标签维度动态查询条件构建器</p>
 * @author dyt
 * @date 2020-03-12 16:00:30
 */
public class TagDimensionWhereCriteriaBuilder {
    public static Specification<TagDimension> build(TagDimensionQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-idList
            if (CollectionUtils.isNotEmpty(queryRequest.getIdList())) {
                predicates.add(root.get("id").in(queryRequest.getIdList()));
            }

            // id
            if (queryRequest.getId() != null) {
                predicates.add(cbuild.equal(root.get("id"), queryRequest.getId()));
            }

            // 模糊查询 - 维度名
            if (StringUtils.isNotEmpty(queryRequest.getName())) {
                predicates.add(cbuild.like(root.get("name"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 维度对应表明
            if (StringUtils.isNotEmpty(queryRequest.getTableName())) {
                predicates.add(cbuild.like(root.get("tableName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getTableName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            //first_last_type
            if(queryRequest.getFirstLastType()!= null){
                predicates.add(cbuild.equal(root.get("firstLastType"),queryRequest.getFirstLastType()));
            }


            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
