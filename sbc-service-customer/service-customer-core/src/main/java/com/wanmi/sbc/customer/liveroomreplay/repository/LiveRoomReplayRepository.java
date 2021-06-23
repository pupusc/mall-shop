package com.wanmi.sbc.customer.liveroomreplay.repository;

import com.wanmi.sbc.customer.liveroomreplay.model.root.LiveRoomReplay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.wanmi.sbc.common.enums.DeleteFlag;
import java.util.Optional;
import java.util.List;

/**
 * <p>直播回放DAO</p>
 * @author zwb
 * @date 2020-06-17 09:24:26
 */
@Repository
public interface LiveRoomReplayRepository extends JpaRepository<LiveRoomReplay, Long>,
        JpaSpecificationExecutor<LiveRoomReplay> {

    /**
     * 单个删除直播回放
     * @author zwb
     */
    @Modifying
    @Query("update LiveRoomReplay set delFlag = 1 where id = ?1")
    void deleteById(Long id);

    /**
     * 批量删除直播回放
     * @author zwb
     */
    @Modifying
    @Query("update LiveRoomReplay set delFlag = 1 where id in ?1")
    void deleteByIdList(List<Long> idList);

    Optional<LiveRoomReplay> findByIdAndDelFlag(Long id, DeleteFlag delFlag);

    List<LiveRoomReplay> findByRoomIdAndDelFlag(Long roomId ,DeleteFlag delFlag);

    Optional<LiveRoomReplay> findByMediaUrlAndDelFlag(String mediaUrl, DeleteFlag delFlag);
}
