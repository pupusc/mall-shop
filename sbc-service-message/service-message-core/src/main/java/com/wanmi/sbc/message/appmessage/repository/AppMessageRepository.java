package com.wanmi.sbc.message.appmessage.repository;

import com.wanmi.sbc.message.appmessage.model.root.AppMessage;
import com.wanmi.sbc.message.bean.enums.MessageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.wanmi.sbc.common.enums.DeleteFlag;
import java.util.Optional;
import java.util.List;

/**
 * <p>App站内信消息发送表DAO</p>
 * @author xuyunpeng
 * @date 2020-01-06 10:53:00
 */
@Repository
public interface AppMessageRepository extends JpaRepository<AppMessage, String>,
        JpaSpecificationExecutor<AppMessage> {

    /**
     * 单个删除App站内信消息发送表
     * @author xuyunpeng
     */
    @Modifying
    @Query("update AppMessage set delFlag = 1 where appMessageId = ?1 and customerId = ?2")
    void deleteById(String appMessageId, String customerId);

    Optional<AppMessage> findByAppMessageIdAndDelFlagAndCustomerId(String id, DeleteFlag delFlag, String customerId);

    /**
     * 查询未读消息数量
     * @param customerId
     * @param messageType
     * @return
     */
    @Query("select count(appMessageId) from AppMessage where customerId = ?1 and messageType = ?2 and isRead = 0 and delFlag = 0 ")
    int getMessageCount(String customerId, MessageType messageType);

    @Modifying
    @Query("update AppMessage set isRead = 1 where customerId = ?1 and appMessageId in ?2")
    void setMessageRead(String customerId, List<String> messageIds);

    /**
     * 查询所有未读消息
     * @param customerId
     * @return
     */
    @Query("select appMessageId from AppMessage where customerId = ?1 and delFlag = 0 and isRead = 0")
    List<String> findAllAppMessage(String customerId);


}
