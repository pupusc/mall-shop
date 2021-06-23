package com.wanmi.sbc.setting.companyinfo.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.setting.api.request.companyinfo.CompanyInfoQueryRequest;
import com.wanmi.sbc.setting.companyinfo.model.root.CompanyInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>公司信息动态查询条件构建器</p>
 * @author lq
 * @date 2019-11-05 16:09:36
 */
public class CompanyInfoWhereCriteriaBuilder {
    public static Specification<CompanyInfo> build(CompanyInfoQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-公司信息IDList
            if (CollectionUtils.isNotEmpty(queryRequest.getCompanyInfoIdList())) {
                predicates.add(root.get("companyInfoId").in(queryRequest.getCompanyInfoIdList()));
            }

            // 公司信息ID
            if (queryRequest.getCompanyInfoId() != null) {
                predicates.add(cbuild.equal(root.get("companyInfoId"), queryRequest.getCompanyInfoId()));
            }

            // 模糊查询 - 公司名称
            if (StringUtils.isNotEmpty(queryRequest.getCompanyName())) {
                predicates.add(cbuild.like(root.get("companyName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCompanyName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 法人身份证反面
            if (StringUtils.isNotEmpty(queryRequest.getBackIdcard())) {
                predicates.add(cbuild.like(root.get("backIdcard"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getBackIdcard()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 省
            if (queryRequest.getProvinceId() != null) {
                predicates.add(cbuild.equal(root.get("provinceId"), queryRequest.getProvinceId()));
            }

            // 市
            if (queryRequest.getCityId() != null) {
                predicates.add(cbuild.equal(root.get("cityId"), queryRequest.getCityId()));
            }

            // 区
            if (queryRequest.getAreaId() != null) {
                predicates.add(cbuild.equal(root.get("areaId"), queryRequest.getAreaId()));
            }

            // 模糊查询 - 详细地址
            if (StringUtils.isNotEmpty(queryRequest.getDetailAddress())) {
                predicates.add(cbuild.like(root.get("detailAddress"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getDetailAddress()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 联系人
            if (StringUtils.isNotEmpty(queryRequest.getContactName())) {
                predicates.add(cbuild.like(root.get("contactName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getContactName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 联系方式
            if (StringUtils.isNotEmpty(queryRequest.getContactPhone())) {
                predicates.add(cbuild.like(root.get("contactPhone"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getContactPhone()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 版权信息
            if (StringUtils.isNotEmpty(queryRequest.getCopyright())) {
                predicates.add(cbuild.like(root.get("copyright"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCopyright()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 公司简介
            if (StringUtils.isNotEmpty(queryRequest.getCompanyDescript())) {
                predicates.add(cbuild.like(root.get("companyDescript"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCompanyDescript()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 操作人
            if (StringUtils.isNotEmpty(queryRequest.getOperator())) {
                predicates.add(cbuild.like(root.get("operator"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getOperator()))
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

            // 大于或等于 搜索条件:修改时间开始
            if (queryRequest.getUpdateTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("updateTime"),
                        queryRequest.getUpdateTimeBegin()));
            }
            // 小于或等于 搜索条件:修改时间截止
            if (queryRequest.getUpdateTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("updateTime"),
                        queryRequest.getUpdateTimeEnd()));
            }

            // 商家类型 0、平台自营 1、第三方商家
            if (queryRequest.getCompanyType() != null) {
                predicates.add(cbuild.equal(root.get("companyType"), queryRequest.getCompanyType()));
            }

            // 账户是否全部收到打款 0、否 1、是
            if (queryRequest.getIsAccountChecked() != null) {
                predicates.add(cbuild.equal(root.get("isAccountChecked"), queryRequest.getIsAccountChecked()));
            }

            // 模糊查询 - 社会信用代码
            if (StringUtils.isNotEmpty(queryRequest.getSocialCreditCode())) {
                predicates.add(cbuild.like(root.get("socialCreditCode"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getSocialCreditCode()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 住所
            if (StringUtils.isNotEmpty(queryRequest.getAddress())) {
                predicates.add(cbuild.like(root.get("address"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getAddress()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 法定代表人
            if (StringUtils.isNotEmpty(queryRequest.getLegalRepresentative())) {
                predicates.add(cbuild.like(root.get("legalRepresentative"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getLegalRepresentative()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 注册资本
            if (queryRequest.getRegisteredCapital() != null) {
                predicates.add(cbuild.equal(root.get("registeredCapital"), queryRequest.getRegisteredCapital()));
            }

            // 大于或等于 搜索条件: 成立日期开始
            if (queryRequest.getFoundDateBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("foundDate"),
                        queryRequest.getFoundDateBegin()));
            }
            // 小于或等于 搜索条件: 成立日期截止
            if (queryRequest.getFoundDateEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("foundDate"),
                        queryRequest.getFoundDateEnd()));
            }

            // 大于或等于 搜索条件:营业期限自开始
            if (queryRequest.getBusinessTermStartBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("businessTermStart"),
                        queryRequest.getBusinessTermStartBegin()));
            }
            // 小于或等于 搜索条件:营业期限自截止
            if (queryRequest.getBusinessTermStartEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("businessTermStart"),
                        queryRequest.getBusinessTermStartEnd()));
            }

            // 大于或等于 搜索条件:营业期限至开始
            if (queryRequest.getBusinessTermEndBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("businessTermEnd"),
                        queryRequest.getBusinessTermEndBegin()));
            }
            // 小于或等于 搜索条件:营业期限至截止
            if (queryRequest.getBusinessTermEndEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("businessTermEnd"),
                        queryRequest.getBusinessTermEndEnd()));
            }

            // 模糊查询 - 经营范围
            if (StringUtils.isNotEmpty(queryRequest.getBusinessScope())) {
                predicates.add(cbuild.like(root.get("businessScope"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getBusinessScope()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 营业执照副本电子版
            if (StringUtils.isNotEmpty(queryRequest.getBusinessLicence())) {
                predicates.add(cbuild.like(root.get("businessLicence"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getBusinessLicence()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 法人身份证正面
            if (StringUtils.isNotEmpty(queryRequest.getFrontIdcard())) {
                predicates.add(cbuild.like(root.get("frontIdcard"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getFrontIdcard()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 商家编号
            if (StringUtils.isNotEmpty(queryRequest.getCompanyCode())) {
                predicates.add(cbuild.like(root.get("companyCode"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCompanyCode()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 商家名称
            if (StringUtils.isNotEmpty(queryRequest.getSupplierName())) {
                predicates.add(cbuild.like(root.get("supplierName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getSupplierName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 是否删除 0 否  1 是
            if (queryRequest.getDelFlag() != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), queryRequest.getDelFlag()));
            }

            // 模糊查询 - 应用唯一key
//            if (StringUtils.isNotEmpty(queryRequest.getAppKey())) {
//                predicates.add(cbuild.like(root.get("appKey"), StringUtil.SQL_LIKE_CHAR
//                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getAppKey()))
//                        .concat(StringUtil.SQL_LIKE_CHAR)));
//            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
