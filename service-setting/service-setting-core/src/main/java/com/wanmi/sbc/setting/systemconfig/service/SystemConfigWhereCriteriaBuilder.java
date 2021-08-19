package com.wanmi.sbc.setting.systemconfig.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.setting.api.request.systemconfig.SystemConfigQueryRequest;
import com.wanmi.sbc.setting.systemconfig.model.root.SystemConfig;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>系统配置表动态查询条件构建器</p>
 * @author yang
 * @date 2019-11-05 18:33:04
 */
public class SystemConfigWhereCriteriaBuilder {
    public static Specification<SystemConfig> build(SystemConfigQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询- 编号List
            if (CollectionUtils.isNotEmpty(queryRequest.getIdList())) {
                predicates.add(root.get("id").in(queryRequest.getIdList()));
            }

            //  编号
            if (queryRequest.getId() != null) {
                predicates.add(cbuild.equal(root.get("id"), queryRequest.getId()));
            }

            // 查询 - 键
            if (StringUtils.isNotEmpty(queryRequest.getConfigKey())) {
                predicates.add(cbuild.equal(root.get("configKey"), queryRequest.getConfigKey()));
            }

            // 查询 - 类型
            if (StringUtils.isNotEmpty(queryRequest.getConfigType())) {
                predicates.add(cbuild.equal(root.get("configType"), queryRequest.getConfigType()));
            }

            // 查询 - 名称
            if (StringUtils.isNotEmpty(queryRequest.getConfigName())) {
                predicates.add(cbuild.equal(root.get("configName"), queryRequest.getConfigName()));
            }

            // 模糊查询 - 备注
            if (StringUtils.isNotEmpty(queryRequest.getRemark())) {
                predicates.add(cbuild.like(root.get("remark"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getRemark()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 状态,0:未启用1:已启用
            if (queryRequest.getStatus() != null) {
                predicates.add(cbuild.equal(root.get("status"), queryRequest.getStatus()));
            }

            // 模糊查询 - 配置内容，如JSON内容
            if (StringUtils.isNotEmpty(queryRequest.getContext())) {
                predicates.add(cbuild.like(root.get("context"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getContext()))
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

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
