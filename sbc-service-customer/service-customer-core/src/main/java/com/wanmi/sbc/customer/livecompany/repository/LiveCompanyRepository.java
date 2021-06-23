package com.wanmi.sbc.customer.livecompany.repository;

import com.wanmi.sbc.customer.livecompany.model.root.LiveCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.wanmi.sbc.common.enums.DeleteFlag;
import java.util.Optional;
import java.util.List;

/**
 * <p>直播商家DAO</p>
 * @author zwb
 * @date 2020-06-06 18:06:59
 */
@Repository
public interface LiveCompanyRepository extends JpaRepository<LiveCompany, Long>,
        JpaSpecificationExecutor<LiveCompany> {

    /**
     * 单个删除直播商家
     * @author zwb
     */
    @Modifying
    @Query("update LiveCompany set delFlag = 1 where id = ?1")
    void deleteById(Long id);

    /**
     * 批量删除直播商家
     * @author zwb
     */
    @Modifying
    @Query("update LiveCompany set delFlag = 1 where id in ?1")
    void deleteByIdList(List<Long> idList);

    Optional<LiveCompany> findByIdAndDelFlagAndStoreId(Long id, DeleteFlag delFlag, Long storeId);


    Optional<LiveCompany> findByStoreIdAndDelFlag(Long id, DeleteFlag delFlag);


    /**
     * 修改审核状态
     * @author zwb
     */
    @Modifying
    @Query("update LiveCompany set liveBroadcastStatus = ?1,auditReason=?2 where storeId = ?3")
    void updateLiveBroadcastStatusByStoreId(Integer liveBroadcastStatus, String auditReason, Long storeId);

}
