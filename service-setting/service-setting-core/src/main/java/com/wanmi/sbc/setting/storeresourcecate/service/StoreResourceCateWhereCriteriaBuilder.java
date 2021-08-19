package com.wanmi.sbc.setting.storeresourcecate.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.setting.api.request.storeresourcecate.StoreResourceCateQueryRequest;
import com.wanmi.sbc.setting.storeresourcecate.model.root.StoreResourceCate;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>店铺资源资源分类表动态查询条件构建器</p>
 * @author lq
 * @date 2019-11-05 16:13:19
 */
public class StoreResourceCateWhereCriteriaBuilder {
    public static Specification<StoreResourceCate> build(StoreResourceCateQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-素材分类idList
            if (CollectionUtils.isNotEmpty(queryRequest.getCateIdList())) {
                predicates.add(root.get("cateId").in(queryRequest.getCateIdList()));
            }

            // 素材分类id
            if (queryRequest.getCateId() != null) {
                predicates.add(cbuild.equal(root.get("cateId"), queryRequest.getCateId()));
            }

            // 店铺标识
            if (queryRequest.getStoreId() != null) {
                predicates.add(cbuild.equal(root.get("storeId"), queryRequest.getStoreId()));
            }

            // 商家标识
            if (queryRequest.getCompanyInfoId() != null) {
                predicates.add(cbuild.equal(root.get("companyInfoId"), queryRequest.getCompanyInfoId()));
            }


            // 分类名称
            if (StringUtils.isNotEmpty(queryRequest.getCateName())) {
                predicates.add(cbuild.equal(root.get("cateName"), queryRequest.getCateName()));
            }

            // 父分类ID
            if (queryRequest.getCateParentId() != null) {
                predicates.add(cbuild.equal(root.get("cateParentId"), queryRequest.getCateParentId()));
            }

            // 模糊查询 - 分类图片
            if (StringUtils.isNotEmpty(queryRequest.getCateImg())) {
                predicates.add(cbuild.like(root.get("cateImg"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCateImg()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 分类层次路径,例1|01|001
            if (StringUtils.isNotEmpty(queryRequest.getCatePath())) {
                predicates.add(cbuild.like(root.get("catePath"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCatePath()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 分类层级
            if (queryRequest.getCateGrade() != null) {
                predicates.add(cbuild.equal(root.get("cateGrade"), queryRequest.getCateGrade()));
            }

            // 模糊查询 - 拼音
            if (StringUtils.isNotEmpty(queryRequest.getPinYin())) {
                predicates.add(cbuild.like(root.get("pinYin"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getPinYin()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 简拼
            if (StringUtils.isNotEmpty(queryRequest.getSpinYin())) {
                predicates.add(cbuild.like(root.get("spinYin"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getSpinYin()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
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

            // 删除标识,0:未删除1:已删除
            if (queryRequest.getDelFlag() != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), queryRequest.getDelFlag()));
            }

            // 是否默认,0:否1:是
            if (queryRequest.getIsDefault() != null) {
                predicates.add(cbuild.equal(root.get("isDefault"), queryRequest.getIsDefault()));
            }
            //批量分类编号
            if (CollectionUtils.isNotEmpty(queryRequest.getCateIdList())) {
                predicates.add(root.get("cateId").in(queryRequest.getCateIdList()));
            }
            //批量分类父编号
            if (CollectionUtils.isNotEmpty(queryRequest.getCateParentIds())) {
                predicates.add(root.get("cateParentId").in(queryRequest.getCateParentIds()));
            }
            //模糊查询，分类路径做为前缀
            if (StringUtils.isNotEmpty(queryRequest.getLikeCatePath())) {
                predicates.add(cbuild.like(root.get("catePath"), queryRequest.getLikeCatePath().concat(StringUtil
                        .SQL_LIKE_CHAR)));
            }
            //非分类编号
            if (queryRequest.getNotCateId() != null) {
                predicates.add(cbuild.notEqual(root.get("cateId"), queryRequest.getNotCateId()));
            }
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
