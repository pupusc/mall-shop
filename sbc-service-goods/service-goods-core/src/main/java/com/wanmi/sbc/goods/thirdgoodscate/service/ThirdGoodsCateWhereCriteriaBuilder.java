package com.wanmi.sbc.goods.thirdgoodscate.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.goods.api.request.thirdgoodscate.ThirdGoodsCateQueryRequest;
import com.wanmi.sbc.goods.thirdgoodscate.model.root.ThirdGoodsCate;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>第三方平台类目动态查询条件构建器</p>
 * @author 
 * @date 2020-08-29 13:35:42
 */
public class ThirdGoodsCateWhereCriteriaBuilder {
    public static Specification<ThirdGoodsCate> build(ThirdGoodsCateQueryRequest queryRequest) {
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

            // 三方商品分类主键
            if (queryRequest.getCateId() != null) {
                predicates.add(cbuild.equal(root.get("cateId"), queryRequest.getCateId()));
            }

            // 模糊查询 - 分类名称
            if (StringUtils.isNotEmpty(queryRequest.getCateName())) {
                predicates.add(cbuild.like(root.get("cateName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCateName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 父分类ID
            if (queryRequest.getCateParentId() != null) {
                predicates.add(cbuild.equal(root.get("cateParentId"), queryRequest.getCateParentId()));
            }

            // 模糊查询 - 分类层次路径,例0|01|001
            if (StringUtils.isNotEmpty(queryRequest.getCatePath())) {
                predicates.add(cbuild.like(root.get("catePath"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCatePath()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 分类层级
            if (queryRequest.getCateGrade() != null) {
                predicates.add(cbuild.equal(root.get("cateGrade"), queryRequest.getCateGrade()));
            }

            // 大于或等于 搜索条件:创建时间开始
            if (queryRequest.getCreateTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("createTime"),
                        queryRequest.getCreateTimeBegin()));
            }
            // 小于或等于 搜索条件:创建时间截止
            if (queryRequest.getCreateTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("createTime"),
                        queryRequest.getCreateTimeEnd()));
            }

            // 大于或等于 搜索条件:更新时间开始
            if (queryRequest.getUpdateTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("updateTime"),
                        queryRequest.getUpdateTimeBegin()));
            }
            // 小于或等于 搜索条件:更新时间截止
            if (queryRequest.getUpdateTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("updateTime"),
                        queryRequest.getUpdateTimeEnd()));
            }

            // 第三方平台来源(0,linkedmall)
            if (queryRequest.getThirdPlatformType() != null) {
                predicates.add(cbuild.equal(root.get("thirdPlatformType"), queryRequest.getThirdPlatformType()));
            }

            // 删除标识,0:未删除1:已删除
            if (queryRequest.getDelFlag() != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), queryRequest.getDelFlag()));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
