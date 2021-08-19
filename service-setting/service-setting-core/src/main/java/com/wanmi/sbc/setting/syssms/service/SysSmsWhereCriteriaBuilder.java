package com.wanmi.sbc.setting.syssms.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.setting.api.request.syssms.SysSmsQueryRequest;
import com.wanmi.sbc.setting.syssms.model.root.SysSms;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.apache.commons.lang3.StringUtils;
import com.wanmi.sbc.common.util.XssUtils;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>系统短信配置动态查询条件构建器</p>
 * @author lq
 * @date 2019-11-05 16:13:47
 */
public class SysSmsWhereCriteriaBuilder {
    public static Specification<SysSms> build(SysSmsQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-主键List
            if (CollectionUtils.isNotEmpty(queryRequest.getSmsIdList())) {
                predicates.add(root.get("smsId").in(queryRequest.getSmsIdList()));
            }

            // 主键
            if (StringUtils.isNotEmpty(queryRequest.getSmsId())) {
                predicates.add(cbuild.equal(root.get("smsId"), queryRequest.getSmsId()));
            }

            // 模糊查询 - 接口地址
            if (StringUtils.isNotEmpty(queryRequest.getSmsUrl())) {
                predicates.add(cbuild.like(root.get("smsUrl"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getSmsUrl()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 名称
            if (StringUtils.isNotEmpty(queryRequest.getSmsName())) {
                predicates.add(cbuild.like(root.get("smsName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getSmsName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - SMTP密码
            if (StringUtils.isNotEmpty(queryRequest.getSmsPass())) {
                predicates.add(cbuild.like(root.get("smsPass"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getSmsPass()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 网关
            if (StringUtils.isNotEmpty(queryRequest.getSmsGateway())) {
                predicates.add(cbuild.like(root.get("smsGateway"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getSmsGateway()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 是否开启(0未开启 1已开启)
            if (queryRequest.getIsOpen() != null) {
                predicates.add(cbuild.equal(root.get("isOpen"), queryRequest.getIsOpen()));
            }

            // 大于或等于 搜索条件:createTime开始
            if (queryRequest.getCreateTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("createTime"),
                        queryRequest.getCreateTimeBegin()));
            }
            // 小于或等于 搜索条件:createTime截止
            if (queryRequest.getCreateTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("createTime"),
                        queryRequest.getCreateTimeEnd()));
            }

            // 大于或等于 搜索条件:modifyTime开始
            if (queryRequest.getModifyTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("modifyTime"),
                        queryRequest.getModifyTimeBegin()));
            }
            // 小于或等于 搜索条件:modifyTime截止
            if (queryRequest.getModifyTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("modifyTime"),
                        queryRequest.getModifyTimeEnd()));
            }

            // 模糊查询 - smsAddress
            if (StringUtils.isNotEmpty(queryRequest.getSmsAddress())) {
                predicates.add(cbuild.like(root.get("smsAddress"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getSmsAddress()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - smsProvider
            if (StringUtils.isNotEmpty(queryRequest.getSmsProvider())) {
                predicates.add(cbuild.like(root.get("smsProvider"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getSmsProvider()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - smsContent
            if (StringUtils.isNotEmpty(queryRequest.getSmsContent())) {
                predicates.add(cbuild.like(root.get("smsContent"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getSmsContent()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }


            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
