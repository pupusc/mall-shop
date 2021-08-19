package com.wanmi.sbc.message.smssenddetail.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.message.api.request.smssenddetail.SmsSendDetailQueryRequest;
import com.wanmi.sbc.message.smssenddetail.model.root.SmsSendDetail;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>短信发送动态查询条件构建器</p>
 * @author zgl
 * @date 2019-12-03 15:43:37
 */
public class SmsSendDetailWhereCriteriaBuilder {
    public static Specification<SmsSendDetail> build(SmsSendDetailQueryRequest queryRequest) {
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

            // 发送任务id
            if (queryRequest.getSendId() != null) {
                predicates.add(cbuild.equal(root.get("sendId"), queryRequest.getSendId()));
            }

            // 模糊查询 - 接收短信的号码
            if (StringUtils.isNotEmpty(queryRequest.getPhoneNumbers())) {
                predicates.add(cbuild.like(root.get("phoneNumbers"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getPhoneNumbers()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 回执id
            if (StringUtils.isNotEmpty(queryRequest.getBizId())) {
                predicates.add(cbuild.like(root.get("bizId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getBizId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 状态（0-失败，1-成功）
            if (queryRequest.getStatus() != null) {
                predicates.add(cbuild.equal(root.get("status"), queryRequest.getStatus()));
            }

            // 不是该状态（0-失败，1-成功）
            if (queryRequest.getNotStatus() != null) {
                predicates.add(cbuild.or(root.get("status").isNull(),cbuild.notEqual(root.get("status"), queryRequest.getNotStatus())));
            }

            // 模糊查询 - 请求状态码。
            if (StringUtils.isNotEmpty(queryRequest.getCode())) {
                predicates.add(cbuild.like(root.get("code"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCode()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 任务执行信息
            if (StringUtils.isNotEmpty(queryRequest.getMessage())) {
                predicates.add(cbuild.like(root.get("message"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getMessage()))
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

            // 大于或等于 搜索条件:sendTime开始
            if (queryRequest.getSendTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("sendTime"),
                        queryRequest.getSendTimeBegin()));
            }
            // 小于或等于 搜索条件:sendTime截止
            if (queryRequest.getSendTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("sendTime"),
                        queryRequest.getSendTimeEnd()));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
