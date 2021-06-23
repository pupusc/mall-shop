package com.wanmi.sbc.message.pushsendnode.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.message.api.request.pushsendnode.PushSendNodeQueryRequest;
import com.wanmi.sbc.message.pushsendnode.model.root.PushSendNode;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>会员推送通知节点动态查询条件构建器</p>
 * @author Bob
 * @date 2020-01-13 10:47:41
 */
public class PushSendNodeWhereCriteriaBuilder {
    public static Specification<PushSendNode> build(PushSendNodeQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-idList
            if (CollectionUtils.isNotEmpty(queryRequest.getIdList())) {
                predicates.add(root.get("id").in(queryRequest.getIdList()));
            }

            // id
            if (queryRequest.getId() != null) {
                predicates.add(cbuild.equal(root.get("id"), queryRequest.getId()));
            }

            // 模糊查询 - 节点名称
            if (StringUtils.isNotEmpty(queryRequest.getNodeName())) {
                predicates.add(cbuild.like(root.get("nodeName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getNodeName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 节点code
            if (StringUtils.isNotEmpty(queryRequest.getNodeCode())) {
                predicates.add(cbuild.equal(root.get("nodeCode"), queryRequest.getNodeCode()));
            }

            // 节点类型
            if (queryRequest.getNodeType() != null) {
                predicates.add(cbuild.equal(root.get("nodeType"), queryRequest.getNodeType()));
            }

            // 模糊查询 - 节点标题
            if (StringUtils.isNotEmpty(queryRequest.getNodeTitle())) {
                predicates.add(cbuild.like(root.get("nodeTitle"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getNodeTitle()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 通知内容
            if (StringUtils.isNotEmpty(queryRequest.getNodeContext())) {
                predicates.add(cbuild.like(root.get("nodeContext"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getNodeContext()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 预计发送总数
            if (queryRequest.getExpectedSendCount() != null) {
                predicates.add(cbuild.equal(root.get("expectedSendCount"), queryRequest.getExpectedSendCount()));
            }

            // 实际发送总数
            if (queryRequest.getActuallySendCount() != null) {
                predicates.add(cbuild.equal(root.get("actuallySendCount"), queryRequest.getActuallySendCount()));
            }

            // 打开总数
            if (queryRequest.getOpenCount() != null) {
                predicates.add(cbuild.equal(root.get("openCount"), queryRequest.getOpenCount()));
            }

            // 状态 0:未启用 1:启用
            if (queryRequest.getStatus() != null) {
                predicates.add(cbuild.equal(root.get("status"), queryRequest.getStatus()));
            }

            // 删除标志 0:未删除 1:删除
            if (queryRequest.getDelFlag() != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), queryRequest.getDelFlag()));
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
