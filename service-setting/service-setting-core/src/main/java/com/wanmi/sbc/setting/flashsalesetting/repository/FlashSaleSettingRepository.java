package com.wanmi.sbc.setting.flashsalesetting.repository;

import com.wanmi.sbc.setting.flashsalesetting.model.root.FlashSaleSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>秒杀设置DAO</p>
 * @author yxz
 * @date 2019-06-11 13:48:53
 */
@Repository
public interface FlashSaleSettingRepository extends JpaRepository<FlashSaleSetting, Long>,
        JpaSpecificationExecutor<FlashSaleSetting> {

    /**
     * 单个删除秒杀设置
     * @author yxz
     */
    @Modifying
    @Query("update FlashSaleSetting set delFlag = 1 where id = ?1")
    void deleteById(Long id);

    /**
     * 批量删除秒杀设置
     * @author yxz
     */
    @Modifying
    @Query("update FlashSaleSetting set delFlag = 1 where id in ?1")
    int deleteByIdList(List<Long> idList);

}
