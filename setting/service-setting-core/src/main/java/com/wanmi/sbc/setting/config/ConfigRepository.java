package com.wanmi.sbc.setting.config;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.setting.bean.enums.ConfigKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统配置数据源
 * Created by daiyitian on 2017/04/11.
 */
@Repository
public interface ConfigRepository extends JpaRepository<Config, Long>, JpaSpecificationExecutor<Config> {

    /**
     * 根据configKey查询config
     * @param configKey configKey
     * @param delFlag delFlag
     * @return Stream<Config>
     */
    List<Config> findByConfigKeyAndDelFlagAndStatus(String configKey, DeleteFlag delFlag,Integer status);

    /**
     * 根据type查询config
     * @param configType configType
     * @param delFlag delFlag
     * @return config对象
     */
    Config findByConfigTypeAndDelFlag(String configType, DeleteFlag delFlag);

    /**
     * 根据type查询config
     * @param configType configType
     * @param configKey configKey
     * @param delFlag delFlag
     * @return config对象
     */
    Config findByConfigTypeAndConfigKeyAndDelFlag(String configType, String configKey, DeleteFlag delFlag);


    Config findByIdAndDelFlag(Long id,DeleteFlag deleteFlag);



    /**
     * 根据type查询configList
     * @param configKey configKey
     * @param delFlag delFlag
     * @return Stream<Config>
     */
    List<Config> findByConfigKeyAndDelFlag(String configKey, DeleteFlag delFlag);


    @Modifying
    @Query("update Config c set c.status = :status ,c.updateTime = :currentTime where c.configType = :configType and c.delFlag = 0")
    int updateStatusByType(@Param("configType") String configType, @Param("status") Integer status, @Param("currentTime") LocalDateTime currentTime);

    /**
     * 修改设置
     * @param configType configType
     * @param configKey configKey
     * @param status configKey
     * @return rows
     */
    @Modifying
    @Query("update Config c set c.status = :status ,c.updateTime = sysdate(), c.context= :context where c.configKey = :configKey and c.configType = :configType and c.delFlag = 0")
    int updateStatusByTypeAndConfigKey(@Param("configType") String configType, @Param("configKey") String configKey
            , @Param("status") Integer status, @Param("context") String context);


    /**
     * 直播开关
     * @param configKey
     * @param status
     * @return
     */
    @Transactional
    @Modifying
    @Query("update Config c set c.status = :status  where c.configKey = :configKey and c.delFlag = 0")
    int updateStatusConfigKey(@Param("configKey") String configKey ,@Param("status")Integer status);
}
