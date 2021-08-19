package com.wanmi.sbc.message.smssend.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.message.api.request.smssend.SmsSendQueryRequest;
import com.wanmi.sbc.message.smssend.model.root.SmsSend;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>短信发送动态查询条件构建器</p>
 * @author zgl
 * @date 2019-12-03 15:36:05
 */
public class SmsSendWhereCriteriaBuilder {
    public static Specification<SmsSend> build(SmsSendQueryRequest queryRequest) {
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

            // 模糊查询 - 短信内容
            if (StringUtils.isNotEmpty(queryRequest.getContext())) {
                predicates.add(cbuild.like(root.get("context"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getContext()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模板code
            if (queryRequest.getTemplateCode() != null) {
                predicates.add(cbuild.equal(root.get("templateCode"), queryRequest.getTemplateCode()));
            }

            // 签名id
            if (queryRequest.getSignId() != null) {
                predicates.add(cbuild.equal(root.get("signId"), queryRequest.getSignId()));
            }

            // 模糊查询 - 接收人描述
            if (StringUtils.isNotEmpty(queryRequest.getReceiveContext())) {
                predicates.add(cbuild.like(root.get("receiveContext"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getReceiveContext()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 接收类型（0-全部，1-会员等级，2-会员人群，3-自定义）
            if (queryRequest.getReceiveType() != null) {
                predicates.add(cbuild.equal(root.get("receiveType"), queryRequest.getReceiveType()));
            }

            // 模糊查询 - 接收人明细
            if (StringUtils.isNotEmpty(queryRequest.getReceiveValue())) {
                predicates.add(cbuild.like(root.get("receiveValue"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getReceiveValue()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 手工添加的号码
            if (StringUtils.isNotEmpty(queryRequest.getManualAdd())) {
                predicates.add(cbuild.like(root.get("manualAdd"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getManualAdd()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 状态（0-未开始，1-进行中，2-已结束，3-任务失败）
            if (queryRequest.getStatus() != null) {
                predicates.add(cbuild.equal(root.get("status"), queryRequest.getStatus()));
            }

            // 非当前状态（0-未开始，1-进行中，2-已结束，3-任务失败）
            if (queryRequest.getNoStatus() != null) {
                predicates.add(cbuild.notEqual(root.get("status"), queryRequest.getNoStatus()));
            }

            // 模糊查询 - 任务执行信息
            if (StringUtils.isNotEmpty(queryRequest.getMessage())) {
                predicates.add(cbuild.like(root.get("message"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getMessage()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 发送类型（0-立即发送，1-定时发送）
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

            // 预计发送条数
            if (queryRequest.getRowCount() != null) {
                predicates.add(cbuild.equal(root.get("rowCount"), queryRequest.getRowCount()));
            }

            // 模糊查询 - 创建人
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

            // 模糊查询 - 更新人
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
