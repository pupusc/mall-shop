package com.wanmi.sbc.setting.growthValue.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.setting.growthValue.model.root.SystemGrowthValueConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>系统成长值设置DAO</p>
 * @author yxz
 * @date 2019-04-03 11:43:28
 */
@Repository
public interface SystemGrowthValueConfigRepository extends JpaRepository<SystemGrowthValueConfig, String>,
        JpaSpecificationExecutor<SystemGrowthValueConfig> {

    /**
     * 查询成长值设置
     *
     * @param deleteFlag
     * @return
     */
    List<SystemGrowthValueConfig> findByDelFlag(DeleteFlag deleteFlag);

    /**
     * 根据配置id查询成长值设置详情
     *
     * @param growthValueConfigId
     * @param deleteFlag
     * @return
     */
    SystemGrowthValueConfig findByGrowthValueConfigIdAndDelFlag(String growthValueConfigId, DeleteFlag deleteFlag);

    /**
     * 修改成长值开关
     * @param id
     * @param status
     * @param currentTime
     * @return
     */
    @Modifying
    @Query("update SystemGrowthValueConfig s set s.status = :status ,s.updateTime = :currentTime where s.growthValueConfigId = :id")
    int updateStatusById(@Param("id") String id, @Param("status") EnableStatus status,
                         @Param("currentTime") LocalDateTime currentTime);

}
