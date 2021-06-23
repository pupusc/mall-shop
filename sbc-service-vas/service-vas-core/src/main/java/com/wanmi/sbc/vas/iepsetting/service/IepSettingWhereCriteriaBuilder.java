package com.wanmi.sbc.vas.iepsetting.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.vas.api.request.iepsetting.IepSettingQueryRequest;
import com.wanmi.sbc.vas.iepsetting.model.root.IepSetting;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;
import org.apache.commons.lang3.StringUtils;
import com.wanmi.sbc.common.util.XssUtils;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>企业购设置动态查询条件构建器</p>
 * @author 宋汉林
 * @date 2020-03-02 20:15:04
 */
public class IepSettingWhereCriteriaBuilder {
    public static Specification<IepSetting> build(IepSettingQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询- id List
            if (CollectionUtils.isNotEmpty(queryRequest.getIdList())) {
                predicates.add(root.get("id").in(queryRequest.getIdList()));
            }

            //  id 
            if (StringUtils.isNotEmpty(queryRequest.getId())) {
                predicates.add(cbuild.equal(root.get("id"), queryRequest.getId()));
            }

            // 模糊查询 -  企业会员名称 
            if (StringUtils.isNotEmpty(queryRequest.getEnterpriseCustomerName())) {
                predicates.add(cbuild.like(root.get("enterpriseCustomerName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getEnterpriseCustomerName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 -  企业价名称 
            if (StringUtils.isNotEmpty(queryRequest.getEnterprisePriceName())) {
                predicates.add(cbuild.like(root.get("enterprisePriceName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getEnterprisePriceName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 -  企业会员logo 
            if (StringUtils.isNotEmpty(queryRequest.getEnterpriseCustomerLogo())) {
                predicates.add(cbuild.like(root.get("enterpriseCustomerLogo"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getEnterpriseCustomerLogo()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            //  企业会员审核 0: 不需要审核 1: 需要审核 
            if (queryRequest.getEnterpriseCustomerAuditFlag() != null) {
                predicates.add(cbuild.equal(root.get("enterpriseCustomerAuditFlag"), queryRequest.getEnterpriseCustomerAuditFlag()));
            }

            //  企业商品审核 0: 不需要审核 1: 需要审核 
            if (queryRequest.getEnterpriseGoodsAuditFlag() != null) {
                predicates.add(cbuild.equal(root.get("enterpriseGoodsAuditFlag"), queryRequest.getEnterpriseGoodsAuditFlag()));
            }

            // 模糊查询 -  企业会员注册协议 
            if (StringUtils.isNotEmpty(queryRequest.getEnterpriseCustomerRegisterContent())) {
                predicates.add(cbuild.like(root.get("enterpriseCustomerRegisterContent"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getEnterpriseCustomerRegisterContent()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 -  创建人 
            if (StringUtils.isNotEmpty(queryRequest.getCreatePerson())) {
                predicates.add(cbuild.like(root.get("createPerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCreatePerson()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 大于或等于 搜索条件: 创建时间 开始
            if (queryRequest.getCreateTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("createTime"),
                        queryRequest.getCreateTimeBegin()));
            }
            // 小于或等于 搜索条件: 创建时间 截止
            if (queryRequest.getCreateTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("createTime"),
                        queryRequest.getCreateTimeEnd()));
            }

            // 模糊查询 -  修改人 
            if (StringUtils.isNotEmpty(queryRequest.getUpdatePerson())) {
                predicates.add(cbuild.like(root.get("updatePerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getUpdatePerson()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 大于或等于 搜索条件: 修改时间 开始
            if (queryRequest.getUpdateTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("updateTime"),
                        queryRequest.getUpdateTimeBegin()));
            }
            // 小于或等于 搜索条件: 修改时间 截止
            if (queryRequest.getUpdateTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("updateTime"),
                        queryRequest.getUpdateTimeEnd()));
            }

            //  是否删除标志 0：否，1：是 
            if (queryRequest.getDelFlag() != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), queryRequest.getDelFlag()));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
