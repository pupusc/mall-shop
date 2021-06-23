package com.wanmi.sbc.setting.operatedatalog.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.setting.api.request.operatedatalog.OperateDataLogQueryRequest;
import com.wanmi.sbc.setting.operatedatalog.model.root.OperateDataLog;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>系统日志动态查询条件构建器</p>
 * @author guanfl
 * @date 2020-04-21 14:57:15
 */
public class OperateDataLogWhereCriteriaBuilder {
    public static Specification<OperateDataLog> build(OperateDataLogQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-自增主键List
            if (CollectionUtils.isNotEmpty(queryRequest.getIdList())) {
                predicates.add(root.get("id").in(queryRequest.getIdList()));
            }

            // 自增主键
            if (queryRequest.getId() != null) {
                predicates.add(cbuild.equal(root.get("id"), queryRequest.getId()));
            }

            // 模糊查询 - 操作内容
            if (StringUtils.isNotEmpty(queryRequest.getOperateContent())) {
                predicates.add(cbuild.like(root.get("operateContent"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getOperateContent()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 操作标识
            if (StringUtils.isNotEmpty(queryRequest.getOperateId())) {
                predicates.add(cbuild.like(root.get("operateId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getOperateId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 操作前数据
            if (StringUtils.isNotEmpty(queryRequest.getOperateBeforeData())) {
                predicates.add(cbuild.like(root.get("operateBeforeData"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getOperateBeforeData()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 操作后数据
            if (StringUtils.isNotEmpty(queryRequest.getOperateAfterData())) {
                predicates.add(cbuild.like(root.get("operateAfterData"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getOperateAfterData()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 操作人账号
            if (StringUtils.isNotEmpty(queryRequest.getOperateAccount())) {
                predicates.add(cbuild.like(root.get("operateAccount"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getOperateAccount()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 操作人名称
            if (StringUtils.isNotEmpty(queryRequest.getOperateName())) {
                predicates.add(cbuild.like(root.get("operateName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getOperateName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 大于或等于 搜索条件:操作时间开始
            if (queryRequest.getOperateTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("operateTime"),
                        queryRequest.getOperateTimeBegin()));
            }
            // 小于或等于 搜索条件:操作时间截止
            if (queryRequest.getOperateTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("operateTime"),
                        queryRequest.getOperateTimeEnd()));
            }

            // 删除标记
            if (queryRequest.getDelFlag() != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), queryRequest.getDelFlag()));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
