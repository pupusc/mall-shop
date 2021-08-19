package com.wanmi.sbc.message.messagesend.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.message.api.request.messagesend.MessageSendQueryRequest;
import com.wanmi.sbc.message.messagesend.model.root.MessageSend;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>站内信任务表动态查询条件构建器</p>
 * @author xuyunpeng
 * @date 2020-01-06 11:12:11
 */
public class MessageSendWhereCriteriaBuilder {
    public static Specification<MessageSend> build(MessageSendQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-主键idList
            if (CollectionUtils.isNotEmpty(queryRequest.getMessageIdList())) {
                predicates.add(root.get("messageId").in(queryRequest.getMessageIdList()));
            }

            // 主键id
            if (queryRequest.getMessageId() != null) {
                predicates.add(cbuild.equal(root.get("messageId"), queryRequest.getMessageId()));
            }

            // 模糊查询 - 任务名称
            if (StringUtils.isNotEmpty(queryRequest.getName())) {
                predicates.add(cbuild.like(root.get("name"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 消息类型 0优惠促销
            if (queryRequest.getMessageType() != null) {
                predicates.add(cbuild.equal(root.get("messageType"), queryRequest.getMessageType()));
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

            // 模糊查询 - 封面图
            if (StringUtils.isNotEmpty(queryRequest.getImgUrl())) {
                predicates.add(cbuild.like(root.get("imgUrl"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getImgUrl()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 推送类型 0：全部会员、1：按会员等级、2：按标签、3：按人群、4：指定会员
            if (queryRequest.getSendType() != null) {
                predicates.add(cbuild.equal(root.get("sendType"), queryRequest.getSendType()));
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

            // 模糊查询 - 删除标识 0：未删除 1：删除
            if (queryRequest.getDelFlag() != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), queryRequest.getDelFlag()));
            }

            // 是否push消息标识 0：否、1：是
            if (queryRequest.getIsPush() != null) {
                predicates.add(cbuild.equal(root.get("isPush"), queryRequest.getIsPush()));
            }

            // 发送数
            if (queryRequest.getSendSum() != null) {
                predicates.add(cbuild.equal(root.get("sendSum"), queryRequest.getSendSum()));
            }

            // 打开数
            if (queryRequest.getOpenSum() != null) {
                predicates.add(cbuild.equal(root.get("openSum"), queryRequest.getOpenSum()));
            }

            if(queryRequest.getSendStatus() != null){
                switch(queryRequest.getSendStatus()){
                    case NO_BEGIN:
                        predicates.add(cbuild.lessThanOrEqualTo(root.get("sendTime"),LocalDateTime.now()));
                        break;
                    case END:
                        predicates.add(cbuild.greaterThanOrEqualTo(root.get("sendTime"),LocalDateTime.now()));
                        break;
                }
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
