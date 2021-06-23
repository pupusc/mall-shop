package com.wanmi.sbc.customer.liveroom.repository;

import com.wanmi.sbc.customer.bean.enums.LiveRoomStatus;
import com.wanmi.sbc.customer.liveroom.model.root.LiveRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.wanmi.sbc.common.enums.DeleteFlag;
import java.util.Optional;
import java.util.List;

/**
 * <p>直播间DAO</p>
 * @author zwb
 * @date 2020-06-06 18:28:57
 */
@Repository
public interface LiveRoomRepository extends JpaRepository<LiveRoom, Long>,
        JpaSpecificationExecutor<LiveRoom> {

    /**
     * 单个删除直播间
     * @author zwb
     */
    @Modifying
    @Query("update LiveRoom set delFlag = 1 where id = ?1")
    void deleteById(Long id);

    /**
     * 批量删除直播间
     * @author zwb
     */
    @Modifying
    @Query("update LiveRoom set delFlag = 1 where id in ?1")
    void deleteByIdList(List<Long> idList);

    Optional<LiveRoom> findByIdAndStoreIdAndDelFlag(Long id, Long storeId, DeleteFlag delFlag);

    /**
     * 批量修改
     * @author zwb
     */
    @Modifying
    @Query("update LiveRoom set liveStatus =?1 where roomId in ?2 and delFlag = 0")
    void updateStatusByRoomIdList(LiveRoomStatus liveStatus, List<Long> roomIdList);


    /**
     * 单个修改直播间推荐状态
     * @author zwb
     */
    @Modifying
    @Query("update LiveRoom set recommend =?1 where roomId = ?2")
    void updateRecommendByRoomId(Integer recommend, Long roomId);

    Optional<LiveRoom> findByRoomIdAndDelFlag(Long roomId,DeleteFlag delFlag);
}
