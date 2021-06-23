package com.wanmi.sbc.setting.email;

import com.wanmi.sbc.common.enums.DeleteFlag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 客户邮箱配置数据源
 */
@Repository
public interface EmailConfigRepository extends JpaRepository<EmailConfig, Long>,
        JpaSpecificationExecutor<EmailConfig> {

    /**
     * 查询BOSS管理后台邮箱接口配置
     *
     * @param deleteFlag
     * @return
     */
    List<EmailConfig> findEmailConfigByDelFlag(DeleteFlag deleteFlag);

    /**
     * 根据配置id查询邮箱接口配置详情
     *
     * @param emailConfigId
     * @param deleteFlag
     * @return
     */
    EmailConfig findEmailConfigByEmailConfigIdAndDelFlag(String emailConfigId, DeleteFlag deleteFlag);

}
