package com.wanmi.sbc.message.appmessage.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.message.api.request.appmessage.AppMessageQueryRequest;
import com.wanmi.sbc.message.appmessage.model.root.AppMessage;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;
import org.apache.commons.lang3.StringUtils;
import com.wanmi.sbc.common.util.XssUtils;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>App站内信消息发送表动态查询条件构建器</p>
 * @author xuyunpeng
 * @date 2020-01-06 10:53:00
 */
public class AppMessageWhereCriteriaBuilder {
    public static Specification<AppMessage> build(AppMessageQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-主键idList
            if (CollectionUtils.isNotEmpty(queryRequest.getAppMessageIdList())) {
                predicates.add(root.get("appMessageId").in(queryRequest.getAppMessageIdList()));
            }

            // 主键id
            if (StringUtils.isNotEmpty(queryRequest.getAppMessageId())) {
                predicates.add(cbuild.equal(root.get("appMessageId"), queryRequest.getAppMessageId()));
            }

            // 消息一级类型：0优惠促销、1服务通知
            if (queryRequest.getMessageType() != null) {
                predicates.add(cbuild.equal(root.get("messageType"), queryRequest.getMessageType()));
            }

            // 消息二级类型
            if (queryRequest.getMessageTypeDetail() != null) {
                predicates.add(cbuild.equal(root.get("messageTypeDetail"), queryRequest.getMessageTypeDetail()));
            }

            // 模糊查询 - 封面图
            if (StringUtils.isNotEmpty(queryRequest.getImgUrl())) {
                predicates.add(cbuild.like(root.get("imgUrl"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getImgUrl()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 消息标题
            if (StringUtils.isNotEmpty(queryRequest.getTitle())) {
                predicates.add(cbuild.like(root.get("title"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getTitle()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 消息内容
            if (StringUtils.isNotEmpty(queryRequest.getContent())) {
                predicates.add(cbuild.like(root.get("content"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getContent()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 跳转路由
            if (StringUtils.isNotEmpty(queryRequest.getRouteName())) {
                predicates.add(cbuild.like(root.get("routeName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getRouteName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 路由参数
            if (StringUtils.isNotEmpty(queryRequest.getRouteParam())) {
                predicates.add(cbuild.like(root.get("routeParam"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getRouteParam()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 大于或等于 搜索条件:发送时间开始
            if (queryRequest.getSendTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("sendTime"),
                        queryRequest.getSendTimeBegin()));
            }
            // 小于或等于 搜索条件:发送时间截止
            if (queryRequest.getSendTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("sendTime"),
                        queryRequest.getSendTimeEnd()));
            }

            // 是否已读 0：未读、1：已读
            if (queryRequest.getIsRead() != null) {
                predicates.add(cbuild.equal(root.get("isRead"), queryRequest.getIsRead()));
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

            // 模糊查询 - 创建人
            if (StringUtils.isNotEmpty(queryRequest.getCreatePerson())) {
                predicates.add(cbuild.like(root.get("createPerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCreatePerson()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
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

            // 模糊查询 - 修改人
            if (StringUtils.isNotEmpty(queryRequest.getUpdatePerson())) {
                predicates.add(cbuild.like(root.get("updatePerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getUpdatePerson()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 删除标识 0：未删除、1：删除
            if (queryRequest.getDelFlag() != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), queryRequest.getDelFlag()));
            }

            // 模糊查询 - 会员id
            if (StringUtils.isNotEmpty(queryRequest.getCustomerId())) {
                predicates.add(cbuild.equal(root.get("customerId"), queryRequest.getCustomerId()));
            }

            // 消息任务id
            if (queryRequest.getMessageSendId() != null) {
                predicates.add(cbuild.equal(root.get("messageSendId"), queryRequest.getMessageSendId()));
            }

            // 节点id
            if (queryRequest.getNodeId() != null) {
                predicates.add(cbuild.equal(root.get("nodeId"), queryRequest.getNodeId()));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
