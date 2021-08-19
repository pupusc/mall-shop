package com.wanmi.sbc.message.pushsend.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.message.pushsend.model.root.PushSend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * <p>会员推送信息DAO</p>
 * @author Bob
 * @date 2020-01-08 17:15:32
 */
@Repository
public interface PushSendRepository extends JpaRepository<PushSend, Long>,
        JpaSpecificationExecutor<PushSend> {

    /**
     * 单个删除会员推送信息
     * @author Bob
     */
    @Modifying
    @Query("update PushSend set delFlag = 1 where id = ?1")
    void deleteById(Long id);

    /**
     * 批量删除会员推送信息
     * @author Bob
     */
    @Modifying
    @Query("update PushSend set delFlag = 1 where id in ?1")
    void deleteByIdList(List<Long> idList);

    Optional<PushSend> findByIdAndDelFlag(Long id, DeleteFlag delFlag);

}
