package com.wanmi.sbc.setting.storeresource.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.setting.api.request.storeresource.StoreResourceQueryRequest;
import com.wanmi.sbc.setting.storeresource.model.root.StoreResource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>店铺资源库动态查询条件构建器</p>
 * @author lq
 * @date 2019-11-05 16:12:49
 */
public class StoreResourceWhereCriteriaBuilder {
    public static Specification<StoreResource> build(StoreResourceQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-素材IDList
            if (CollectionUtils.isNotEmpty(queryRequest.getResourceIdList())) {
                predicates.add(root.get("resourceId").in(queryRequest.getResourceIdList()));
            }

            // 素材ID
            if (queryRequest.getResourceId() != null) {
                predicates.add(cbuild.equal(root.get("resourceId"), queryRequest.getResourceId()));
            }

            // 资源类型(0:图片,1:视频)
            if (queryRequest.getResourceType() != null) {
                predicates.add(cbuild.equal(root.get("resourceType"), queryRequest.getResourceType()));
            }

            // 素材分类ID
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

            // 模糊查询 - 素材KEY
            if (StringUtils.isNotEmpty(queryRequest.getResourceKey())) {
                predicates.add(cbuild.like(root.get("resourceKey"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getResourceKey()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 素材名称
            if (StringUtils.isNotEmpty(queryRequest.getResourceName())) {
                predicates.add(cbuild.like(root.get("resourceName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getResourceName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 素材地址
            if (StringUtils.isNotEmpty(queryRequest.getArtworkUrl())) {
                predicates.add(cbuild.like(root.get("artworkUrl"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getArtworkUrl()))
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
            // 0：待审核，1：审核通过，2：审核不通过
            if (queryRequest.getAuditStatus() != null ) {
                predicates.add(cbuild.equal(root.get("auditStatus"), queryRequest.getAuditStatus()));
            }

            // 模糊查询 - oss服务器类型，对应system_config的config_type
            if (StringUtils.isNotEmpty(queryRequest.getServerType())) {
                predicates.add(cbuild.like(root.get("serverType"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getServerType()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }


            //批量分类编号
            if(org.apache.commons.collections.CollectionUtils.isNotEmpty(queryRequest.getCateIds())){
                predicates.add(root.get("cateId").in(queryRequest.getCateIds()));
            }


            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
