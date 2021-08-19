package com.wanmi.sbc.setting.baseconfig.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.setting.api.request.baseconfig.BaseConfigQueryRequest;
import com.wanmi.sbc.setting.baseconfig.model.root.BaseConfig;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>基本设置动态查询条件构建器</p>
 * @author lq
 * @date 2019-11-05 16:08:31
 */
public class BaseConfigWhereCriteriaBuilder {
    public static Specification<BaseConfig> build(BaseConfigQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-基本设置IDList
            if (CollectionUtils.isNotEmpty(queryRequest.getBaseConfigIdList())) {
                predicates.add(root.get("baseConfigId").in(queryRequest.getBaseConfigIdList()));
            }

            // 基本设置ID
            if (queryRequest.getBaseConfigId() != null) {
                predicates.add(cbuild.equal(root.get("baseConfigId"), queryRequest.getBaseConfigId()));
            }

            // 模糊查询 - PC端商城网址
            if (StringUtils.isNotEmpty(queryRequest.getPcWebsite())) {
                predicates.add(cbuild.like(root.get("pcWebsite"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getPcWebsite()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 移动端商城网址
            if (StringUtils.isNotEmpty(queryRequest.getMobileWebsite())) {
                predicates.add(cbuild.like(root.get("mobileWebsite"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getMobileWebsite()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - PC商城logo
            if (StringUtils.isNotEmpty(queryRequest.getPcLogo())) {
                predicates.add(cbuild.like(root.get("pcLogo"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getPcLogo()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - PC商城banner,最多可添加5个,多个图片间以'|'隔开
            if (StringUtils.isNotEmpty(queryRequest.getPcBanner())) {
                predicates.add(cbuild.like(root.get("pcBanner"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getPcBanner()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 移动商城banner,最多可添加5个,多个图片间以'|'隔开
            if (StringUtils.isNotEmpty(queryRequest.getMobileBanner())) {
                predicates.add(cbuild.like(root.get("mobileBanner"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getMobileBanner()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - PC商城首页banner,最多可添加5个,多个图片间以'|'隔开
            if (StringUtils.isNotEmpty(queryRequest.getPcMainBanner())) {
                predicates.add(cbuild.like(root.get("pcMainBanner"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getPcMainBanner()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 网页ico
            if (StringUtils.isNotEmpty(queryRequest.getPcIco())) {
                predicates.add(cbuild.like(root.get("pcIco"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getPcIco()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - pc商城标题
            if (StringUtils.isNotEmpty(queryRequest.getPcTitle())) {
                predicates.add(cbuild.like(root.get("pcTitle"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getPcTitle()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 商家后台登录网址
            if (StringUtils.isNotEmpty(queryRequest.getSupplierWebsite())) {
                predicates.add(cbuild.like(root.get("supplierWebsite"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getSupplierWebsite()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 会员注册协议
            if (StringUtils.isNotEmpty(queryRequest.getRegisterContent())) {
                predicates.add(cbuild.like(root.get("registerContent"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getRegisterContent()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
