package com.wanmi.sbc.setting.systemliveconfig.repository;

import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.setting.growthValue.model.root.SystemGrowthValueConfig;
import com.wanmi.sbc.setting.systemliveconfig.model.root.SystemLiveConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.wanmi.sbc.common.enums.DeleteFlag;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

/**
 * <p>小程序直播设置DAO</p>
 * @author zwb
 * @date 2020-06-19 16:11:36
 */
@Repository
public interface SystemLiveConfigRepository extends JpaRepository<SystemLiveConfig, String>,
        JpaSpecificationExecutor<SystemLiveConfig> {

    /**
     * 查询直播设置
     *
     * @param deleteFlag
     * @return
     */
    List<SystemLiveConfig> findByDelFlag(DeleteFlag deleteFlag);


    /**
     * 修改成长值开关
     * @param id
     * @param status
     * @param currentTime
     * @return
     */
    @Modifying
    @Query("update SystemLiveConfig s set s.status = :status ,s.updateTime = :currentTime where s.systemLiveConfigId = :id")
    int updateStatusById(@Param("id") String id, @Param("status") EnableStatus status,
                         @Param("currentTime") LocalDateTime currentTime);
}
