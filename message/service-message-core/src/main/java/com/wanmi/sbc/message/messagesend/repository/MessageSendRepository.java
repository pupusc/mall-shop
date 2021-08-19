package com.wanmi.sbc.message.messagesend.repository;

import com.wanmi.sbc.message.messagesend.model.root.MessageSend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.wanmi.sbc.common.enums.DeleteFlag;
import java.util.Optional;
import java.util.List;

/**
 * <p>站内信任务表DAO</p>
 * @author xuyunpeng
 * @date 2020-01-06 11:12:11
 */
@Repository
public interface MessageSendRepository extends JpaRepository<MessageSend, Long>,
        JpaSpecificationExecutor<MessageSend> {

    /**
     * 单个删除站内信任务表
     * @author xuyunpeng
     */
    @Modifying
    @Query("update MessageSend set delFlag = 1 where messageId = ?1")
    void deleteById(Long messageId);

    /**
     * 批量删除站内信任务表
     * @author xuyunpeng
     */
    @Modifying
    @Query("update MessageSend set delFlag = 1 where messageId in ?1")
    void deleteByIdList(List<Long> messageIdList);

    Optional<MessageSend> findByMessageIdAndDelFlag(Long id, DeleteFlag delFlag);

    @Modifying
    @Query("update MessageSend set sendSum = sendSum + ?1 where messageId = ?2")
    void addSendSum(Integer sendSum, Long messageId);

    @Modifying
    @Query("update MessageSend set openSum = openSum + 1 where messageId = ?1")
    void addOpenSum(Long messageId);

    MessageSend findByPushId(String pushId);

    @Modifying
    @Query("update MessageSend set delFlag = 1 where pushId = ?1")
    void deleteByPushId(String pushId);

}
