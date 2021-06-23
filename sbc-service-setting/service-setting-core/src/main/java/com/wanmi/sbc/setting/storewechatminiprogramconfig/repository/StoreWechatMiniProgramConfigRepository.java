package com.wanmi.sbc.setting.storewechatminiprogramconfig.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.setting.storewechatminiprogramconfig.model.root.StoreWechatMiniProgramConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * <p>门店微信小程序配置DAO</p>
 * @author tangLian
 * @date 2020-01-16 11:47:15
 */
@Repository
public interface StoreWechatMiniProgramConfigRepository extends JpaRepository<StoreWechatMiniProgramConfig, String>,
        JpaSpecificationExecutor<StoreWechatMiniProgramConfig> {

    /**
     * 批量删除门店微信小程序配置
     * @author tangLian
     */
    @Modifying
    @Query("update StoreWechatMiniProgramConfig set delFlag = 1 where id in ?1")
    int deleteByIdList(List<String> idList);

    /**
     * 门店id查询
     * @param storeId
     * @param delFlag
     * @return
     */
    Optional<StoreWechatMiniProgramConfig> findByStoreIdAndDelFlag(Long storeId, DeleteFlag delFlag);

}

