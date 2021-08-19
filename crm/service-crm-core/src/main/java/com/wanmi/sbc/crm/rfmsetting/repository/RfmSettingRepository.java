package com.wanmi.sbc.crm.rfmsetting.repository;

import com.wanmi.sbc.crm.bean.enums.RFMType;
import com.wanmi.sbc.crm.rfmsetting.model.root.RfmSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>rfm参数配置DAO</p>
 * @author zhanglingke
 * @date 2019-10-14 14:33:42
 */
@Repository
public interface RfmSettingRepository extends JpaRepository<RfmSetting, Long>,
        JpaSpecificationExecutor<RfmSetting> {

    /**
     * 单个删除rfm参数配置
     * @author zhanglingke
     */
    @Modifying
    @Query("update RfmSetting set delFlag = 1 where id = ?1")
    int deleteByBeanId(Long id);

    /**
     * 批量删除rfm参数配置
     * @author zhanglingke
     */
    @Modifying
    @Query("update RfmSetting set delFlag = 1 where id in ?1")
    int deleteByIdList(List<Long> idList);

    /**
     * 查询指定rfm模型中param最大的项
     * @param type
     * @return
     */
    @Query(value = "SELECT * FROM `rfm_setting` WHERE `type` = ?1 ORDER BY param DESC LIMIT 0,1 ",nativeQuery = true)
    RfmSetting getMaxParamSetting(int type);

    /**
     * 查询指定rfm模型中score最大的项
     * @param type
     * @return
     */
    @Query(value = "SELECT * FROM `rfm_setting` WHERE `type` = ?1 ORDER BY score DESC LIMIT 0,1 ",nativeQuery = true)
    RfmSetting getMaxScoreSetting(int type);
}
