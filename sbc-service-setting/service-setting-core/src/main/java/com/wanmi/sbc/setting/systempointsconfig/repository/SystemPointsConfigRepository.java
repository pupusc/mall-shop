package com.wanmi.sbc.setting.systempointsconfig.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.setting.systempointsconfig.model.root.SystemPointsConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>积分设置DAO</p>
 * @author yxz
 * @date 2019-03-28 16:24:21
 */
@Repository
public interface SystemPointsConfigRepository extends JpaRepository<SystemPointsConfig, String>,
        JpaSpecificationExecutor<SystemPointsConfig> {

    /**
     * 查询积分设置
     *
     * @param deleteFlag
     * @return
     */
    List<SystemPointsConfig> findByDelFlag(DeleteFlag deleteFlag);

    /**
     * 根据配置id查询积分设置详情
     *
     * @param pointsConfigId
     * @param deleteFlag
     * @return
     */
    SystemPointsConfig findByPointsConfigIdAndDelFlag(String pointsConfigId, DeleteFlag deleteFlag);

}
