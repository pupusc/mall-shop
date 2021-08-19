package com.wanmi.sbc.message.smssetting.repository;

import com.wanmi.sbc.message.smssetting.model.root.SmsSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>短信配置DAO</p>
 * @author lvzhenwei
 * @date 2019-12-03 15:15:28
 */
@Repository
public interface SmsSettingRepository extends JpaRepository<SmsSetting, Long>,
        JpaSpecificationExecutor<SmsSetting> {

    /**
     * 单个删除短信配置
     * @author lvzhenwei
     */
    @Modifying
    @Query("update SmsSetting set delFlag = 1 where id = ?1")
    int deleteByBeanId(Long id);

    /**
     * 批量删除短信配置
     * @author lvzhenwei
     */
    @Modifying
    @Query("update SmsSetting set delFlag = 1 where id in ?1")
    int deleteByIdList(List<Long> idList);

}
