package com.wanmi.sbc.message.pushsend.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.message.api.request.pushsend.PushSendQueryRequest;
import com.wanmi.sbc.message.pushsend.model.root.PushSend;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>会员推送信息动态查询条件构建器</p>
 * @author Bob
 * @date 2020-01-08 17:15:32
 */
public class PushSendWhereCriteriaBuilder {
    public static Specification<PushSend> build(PushSendQueryRequest queryRequest) {
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

            // 模糊查询 - 友盟安卓任务ID
            if (StringUtils.isNotEmpty(queryRequest.getAndroidTaskId())) {
                predicates.add(cbuild.like(root.get("androidTaskId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getAndroidTaskId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 友盟iOS任务ID
            if (StringUtils.isNotEmpty(queryRequest.getIosTaskId())) {
                predicates.add(cbuild.like(root.get("iosTaskId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getIosTaskId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 消息名称
            if (StringUtils.isNotEmpty(queryRequest.getMsgName())) {
                predicates.add(cbuild.like(root.get("msgName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getMsgName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 消息标题
            if (StringUtils.isNotEmpty(queryRequest.getMsgTitle())) {
                predicates.add(cbuild.like(root.get("msgTitle"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getMsgTitle()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 消息内容
            if (StringUtils.isNotEmpty(queryRequest.getMsgContext())) {
                predicates.add(cbuild.like(root.get("msgContext"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getMsgContext()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 消息封面图片
            if (StringUtils.isNotEmpty(queryRequest.getMsgImg())) {
                predicates.add(cbuild.like(root.get("msgImg"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getMsgImg()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 点击消息跳转的页面路由
            if (StringUtils.isNotEmpty(queryRequest.getMsgRouter())) {
                predicates.add(cbuild.like(root.get("msgRouter"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getMsgRouter()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 消息接受人 0:全部会员 1:按会员等级 2:按标签 3:按人群 4:指定会员
            if (queryRequest.getMsgRecipient() != null) {
                predicates.add(cbuild.equal(root.get("msgRecipient"), queryRequest.getMsgRecipient()));
            }

            // 模糊查询 - 等级、标签、人群ID。逗号分割
            if (StringUtils.isNotEmpty(queryRequest.getMsgRecipientDetail())) {
                predicates.add(cbuild.like(root.get("msgRecipientDetail"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getMsgRecipientDetail()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 大于或等于 搜索条件:推送时间开始
            if (queryRequest.getPushTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("pushTime"),
                        queryRequest.getPushTimeBegin()));
            }
            // 小于或等于 搜索条件:推送时间截止
            if (queryRequest.getPushTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("pushTime"),
                        queryRequest.getPushTimeEnd()));
            }

            // 预计发送人数
            if (queryRequest.getExpectedSendCount() != null) {
                predicates.add(cbuild.equal(root.get("expectedSendCount"), queryRequest.getExpectedSendCount()));
            }

            // 删除标志 0:未删除 1:已删除
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
