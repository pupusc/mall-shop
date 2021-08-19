package com.wanmi.sbc.crm.tagparam.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.crm.api.request.tagparam.TagParamQueryRequest;
import com.wanmi.sbc.crm.tagparam.model.root.TagParam;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>标签参数动态查询条件构建器</p>
 * @author dyt
 * @date 2020-03-12 15:59:49
 */
public class TagParamWhereCriteriaBuilder {
    public static Specification<TagParam> build(TagParamQueryRequest queryRequest) {
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

            // 标签维度id
            if (queryRequest.getTagDimensionId() != null) {
                predicates.add(cbuild.equal(root.get("tagDimensionId"), queryRequest.getTagDimensionId()));
            }

            // 批量查询-idList
            if (CollectionUtils.isNotEmpty(queryRequest.getTagDimensionIdList())) {
                predicates.add(root.get("tagDimensionId").in(queryRequest.getTagDimensionIdList()));
            }

            // 模糊查询 - 维度配置名称
            if (StringUtils.isNotEmpty(queryRequest.getName())) {
                predicates.add(cbuild.like(root.get("name"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 标签参数类型
            if(Objects.nonNull(queryRequest.getTagType())){
                predicates.add(cbuild.equal(root.get("tagType"), queryRequest.getTagType()));
            }

            // 模糊查询 - 字段名称
            if (StringUtils.isNotEmpty(queryRequest.getColumnName())) {
                predicates.add(cbuild.like(root.get("columnName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getColumnName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 维度配置类型，0：top类型，1：聚合结果类型，2：查询条件类型
            if (queryRequest.getType() != null) {
                predicates.add(cbuild.equal(root.get("type"), queryRequest.getType()));
            }

            if(CollectionUtils.isNotEmpty(queryRequest.getTypeList())){
                predicates.add(root.get("type").in(queryRequest.getTypeList()));
            }

            // 标签维度类型，0：top类型，1：count计数类型，2：sum求和，3：avg平均值，4：max最大值，5：min最小值，6：in包含类型，7：等于，8、区间类，9：多重期间or关系
            if (queryRequest.getTagDimensionType() != null) {
                predicates.add(cbuild.equal(root.get("tagDimensionType"), queryRequest.getTagDimensionType()));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
