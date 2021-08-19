package com.wanmi.sbc.message.messagesend.repository;

import com.wanmi.sbc.message.messagesend.model.root.MessageSendCustomerScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>站内信消息会员关联表DAO</p>
 * @author xuyunpeng
 * @date 2020-01-06 11:16:04
 */
@Repository
public interface MessageSendCustomerScopeRepository extends JpaRepository<MessageSendCustomerScope, Long>,
        JpaSpecificationExecutor<MessageSendCustomerScope> {

    void deleteByMessageId(Long messageId);

    List<MessageSendCustomerScope> findByMessageId(Long messageId);

}
