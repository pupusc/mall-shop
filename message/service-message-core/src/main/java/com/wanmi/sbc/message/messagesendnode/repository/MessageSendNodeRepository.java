package com.wanmi.sbc.message.messagesendnode.repository;

import com.wanmi.sbc.message.bean.enums.NodeType;
import com.wanmi.sbc.message.bean.enums.SwitchFlag;
import com.wanmi.sbc.message.messagesendnode.model.root.MessageSendNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.wanmi.sbc.common.enums.DeleteFlag;
import java.util.Optional;
import java.util.List;

/**
 * <p>站内信通知节点表DAO</p>
 * @author xuyunpeng
 * @date 2020-01-09 11:45:53
 */
@Repository
public interface MessageSendNodeRepository extends JpaRepository<MessageSendNode, Long>,
        JpaSpecificationExecutor<MessageSendNode> {

    /**
     * 单个删除站内信通知节点表
     * @author xuyunpeng
     */
    @Modifying
    @Query("update MessageSendNode set delFlag = 1 where id = ?1")
    void deleteById(Long id);

    /**
     * 批量删除站内信通知节点表
     * @author xuyunpeng
     */
    @Modifying
    @Query("update MessageSendNode set delFlag = 1 where id in ?1")
    void deleteByIdList(List<Long> idList);

    Optional<MessageSendNode> findByIdAndDelFlag(Long id, DeleteFlag delFlag);

    @Modifying
    @Query("update MessageSendNode set status = ?1 where id = ?2")
    void updateStatus(SwitchFlag status, Long id);

    @Modifying
    @Query("update MessageSendNode set sendSum = sendSum + 1 where id = ?1")
    void addSendSum(Long id);

    @Modifying
    @Query("update MessageSendNode set openSum = openSum + 1 where id = ?1")
    void addOpenSum(Long id);

    MessageSendNode findByNodeTypeAndNodeCode(NodeType nodeType, String nodeCode);

}
