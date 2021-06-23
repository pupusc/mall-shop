package com.wanmi.sbc.message.messagesend.service;

import com.wanmi.sbc.message.api.request.appmessage.MessageSendCustomerScopeQueryRequest;
import com.wanmi.sbc.message.messagesend.model.root.MessageSendCustomerScope;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>站内信消息会员关联表动态查询条件构建器</p>
 * @author xuyunpeng
 * @date 2020-01-06 11:16:04
 */
public class MessageSendCustomerScopeWhereCriteriaBuilder {
    public static Specification<MessageSendCustomerScope> build(MessageSendCustomerScopeQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-主键idList
            if (CollectionUtils.isNotEmpty(queryRequest.getScopeIdList())) {
                predicates.add(root.get("scopeId").in(queryRequest.getScopeIdList()));
            }

            // 批量查询-messageIdList
            if (CollectionUtils.isNotEmpty(queryRequest.getMessageIds())) {
                predicates.add(root.get("messageId").in(queryRequest.getMessageIds()));
            }

            // 主键id
            if (queryRequest.getScopeId() != null) {
                predicates.add(cbuild.equal(root.get("scopeId"), queryRequest.getScopeId()));
            }

            // 消息id
            if (queryRequest.getMessageId() != null) {
                predicates.add(cbuild.equal(root.get("messageId"), queryRequest.getMessageId()));
            }

            // 关联的等级、人群、标签id
            if (StringUtils.isNotBlank(queryRequest.getJoinId())) {
                predicates.add(cbuild.equal(root.get("joinId"), queryRequest.getJoinId()));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
