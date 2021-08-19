package com.wanmi.sbc.message.smssetting.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.message.api.request.smssetting.SmsSettingQueryRequest;
import com.wanmi.sbc.message.smssetting.model.root.SmsSetting;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>短信配置动态查询条件构建器</p>
 * @author lvzhenwei
 * @date 2019-12-03 15:15:28
 */
public class SmsSettingWhereCriteriaBuilder {
    public static Specification<SmsSetting> build(SmsSettingQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-主键List
            if (CollectionUtils.isNotEmpty(queryRequest.getIdList())) {
                predicates.add(root.get("id").in(queryRequest.getIdList()));
            }

            // 主键
            if (queryRequest.getId() != null) {
                predicates.add(cbuild.equal(root.get("id"), queryRequest.getId()));
            }

            // 模糊查询 - 调用api参数key
            if (StringUtils.isNotEmpty(queryRequest.getAccessKeyId())) {
                predicates.add(cbuild.like(root.get("accessKeyId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getAccessKeyId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 调用api参数secret
            if (StringUtils.isNotEmpty(queryRequest.getAccessKeySecret())) {
                predicates.add(cbuild.like(root.get("accessKeySecret"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getAccessKeySecret()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 短信平台类型：0：阿里云短信平台
            if (queryRequest.getType() != null) {
                predicates.add(cbuild.equal(root.get("type"), queryRequest.getType()));
            }

            // 是否启用：0：未启用；1：启用
            if (queryRequest.getStatus() != null) {
                predicates.add(cbuild.equal(root.get("status"), queryRequest.getStatus()));
            }

            // 删除标识：0：未删除；1：已删除
            if (queryRequest.getDelFlag() != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), queryRequest.getDelFlag()));
            }

            // 大于或等于 搜索条件:创建时间开始
            if (queryRequest.getCreatTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("creatTime"),
                        queryRequest.getCreatTimeBegin()));
            }
            // 小于或等于 搜索条件:创建时间截止
            if (queryRequest.getCreatTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("creatTime"),
                        queryRequest.getCreatTimeEnd()));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
