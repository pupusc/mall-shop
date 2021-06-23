package com.wanmi.sbc.message.pushdetail.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.message.api.request.pushdetail.PushDetailQueryRequest;
import com.wanmi.sbc.message.pushdetail.model.root.PushDetail;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>推送详情动态查询条件构建器</p>
 * @author Bob
 * @date 2020-01-08 17:16:17
 */
public class PushDetailWhereCriteriaBuilder {
    public static Specification<PushDetail> build(PushDetailQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-友盟推送任务IDList
            if (CollectionUtils.isNotEmpty(queryRequest.getTaskIdList())) {
                predicates.add(root.get("taskId").in(queryRequest.getTaskIdList()));
            }

            // 友盟推送任务ID
            if (StringUtils.isNotEmpty(queryRequest.getTaskId())) {
                predicates.add(cbuild.equal(root.get("taskId"), queryRequest.getTaskId()));
            }

            // 运营计划ID
            if (queryRequest.getPlanId() != null) {
                predicates.add(cbuild.equal(root.get("planId"), queryRequest.getPlanId()));
            }

            if (queryRequest.getPlatform() != null) {
                predicates.add(cbuild.equal(root.get("platform"), queryRequest.getPlatform()));
            }

            if (queryRequest.getNodeId() != null) {
                predicates.add(cbuild.equal(root.get("nodeId"), queryRequest.getNodeId()));
            }

            // 实际发送
            if (queryRequest.getSendSum() != null) {
                predicates.add(cbuild.equal(root.get("sendSum"), queryRequest.getSendSum()));
            }

            // 打开数
            if (queryRequest.getOpenSum() != null) {
                predicates.add(cbuild.equal(root.get("openSum"), queryRequest.getOpenSum()));
            }

            // 发送状态 0:未开始 1:进行中 2:已结束 3:失败
            if (queryRequest.getSendStatus() != null) {
                predicates.add(cbuild.equal(root.get("sendStatus"), queryRequest.getSendStatus()));
            }

            // 模糊查询 - 失败信息
            if (StringUtils.isNotEmpty(queryRequest.getFailRemark())) {
                predicates.add(cbuild.like(root.get("failRemark"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getFailRemark()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 创建人ID
            if (StringUtils.isNotEmpty(queryRequest.getCreatePerson())) {
                predicates.add(cbuild.like(root.get("createPerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCreatePerson()))
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

            // 模糊查询 - 更新人ID
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

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
