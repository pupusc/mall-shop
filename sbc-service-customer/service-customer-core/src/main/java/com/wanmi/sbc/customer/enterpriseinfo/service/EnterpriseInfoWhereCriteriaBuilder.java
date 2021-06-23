package com.wanmi.sbc.customer.enterpriseinfo.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.customer.api.request.enterpriseinfo.EnterpriseInfoQueryRequest;
import com.wanmi.sbc.customer.enterpriseinfo.model.root.EnterpriseInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;
import org.apache.commons.lang3.StringUtils;
import com.wanmi.sbc.common.util.XssUtils;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>企业信息表动态查询条件构建器</p>
 * @author TangLian
 * @date 2020-03-02 19:05:06
 */
public class EnterpriseInfoWhereCriteriaBuilder {
    public static Specification<EnterpriseInfo> build(EnterpriseInfoQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-企业IdList
            if (CollectionUtils.isNotEmpty(queryRequest.getEnterpriseIdList())) {
                predicates.add(root.get("enterpriseId").in(queryRequest.getEnterpriseIdList()));
            }

            // 企业Id
            if (StringUtils.isNotEmpty(queryRequest.getEnterpriseId())) {
                predicates.add(cbuild.equal(root.get("enterpriseId"), queryRequest.getEnterpriseId()));
            }

            // 模糊查询 - 企业名称
            if (StringUtils.isNotEmpty(queryRequest.getEnterpriseName())) {
                predicates.add(cbuild.like(root.get("enterpriseName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getEnterpriseName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 统一社会信用代码
            if (StringUtils.isNotEmpty(queryRequest.getSocialCreditCode())) {
                predicates.add(cbuild.like(root.get("socialCreditCode"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getSocialCreditCode()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 公司性质
            if (queryRequest.getBusinessNatureType() != null) {
                predicates.add(cbuild.equal(root.get("businessNatureType"), queryRequest.getBusinessNatureType()));
            }

            // 公司行业
            if (queryRequest.getBusinessIndustryType() != null) {
                predicates.add(cbuild.equal(root.get("businessIndustryType"), queryRequest.getBusinessIndustryType()));
            }

            // 模糊查询 - 营业执照地址
            if (StringUtils.isNotEmpty(queryRequest.getBusinessLicenseUrl())) {
                predicates.add(cbuild.like(root.get("businessLicenseUrl"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getBusinessLicenseUrl()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 企业会员id
            if (StringUtils.isNotEmpty(queryRequest.getCustomerId())) {
                predicates.add(cbuild.like(root.get("customerId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCustomerId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 创建人
            if (StringUtils.isNotEmpty(queryRequest.getCreatePerson())) {
                predicates.add(cbuild.like(root.get("createPerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCreatePerson()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 大于或等于 搜索条件:参加时间开始
            if (queryRequest.getCreateTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("createTime"),
                        queryRequest.getCreateTimeBegin()));
            }
            // 小于或等于 搜索条件:参加时间截止
            if (queryRequest.getCreateTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("createTime"),
                        queryRequest.getCreateTimeEnd()));
            }

            // 模糊查询 - 更新人
            if (StringUtils.isNotEmpty(queryRequest.getUpdatePerson())) {
                predicates.add(cbuild.like(root.get("updatePerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getUpdatePerson()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
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

            // 删除标志
            if (queryRequest.getDelFlag() != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), queryRequest.getDelFlag()));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
