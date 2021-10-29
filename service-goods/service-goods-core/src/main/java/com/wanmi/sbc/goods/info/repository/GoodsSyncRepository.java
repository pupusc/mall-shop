package com.wanmi.sbc.goods.info.repository;

import com.wanmi.sbc.goods.info.model.root.GoodsSync;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface GoodsSyncRepository extends JpaRepository<GoodsSync, Long>, JpaSpecificationExecutor<GoodsSync> {

//    @Query
    List<GoodsSync> findByStatus(Integer status);
    @Modifying
    @Query("update GoodsSync w set w.status = ?2, w.updateTime = now() where w.goodsNo = ?1")
    int updateStatus(String goodsNo,Integer status);

    @Modifying
    @Transactional
    @Query("update GoodsSync w set w.adAuditStatus = ?2, w.updateTime = now() where w.id in ?1 and adAuditStatus = ?3")
    int batchUpdateAdStatus(List<Long> idList,Integer status,Integer originStatus);

    @Modifying
    @Transactional
    @Query("update GoodsSync w set w.adManualAuditStatus = 1, w.updateTime = now() where w.id in ?1 and adManualAuditStatus = 0")
    int batchApprove(List<Long> idList);

    @Modifying
    @Transactional
    @Query("update GoodsSync w set w.adManualAuditStatus = 2, w.updateTime = now(),adManualRejectReason = ?2 where w.id in ?1 and adManualAuditStatus = 0")
    int batchReject(List<Long> idList,String rejectReason);

    @Modifying
    @Transactional
    @Query("update GoodsSync w set w.launchStatus = 1,w.status = 1, w.updateTime = now() where w.id in ?1 and launchStatus = 0")
    int batchApproveLaunch(List<Long> idList);

    @Modifying
    @Transactional
    @Query("update GoodsSync w set w.launchStatus = 2, w.updateTime = now(),launchRejectReason = ?2 where w.id in ?1 and launchStatus = 0")
    int batchRejectLaunch(List<Long> idList,String rejectReason);

    @Modifying
    @Transactional
    @Query("update GoodsSync w set w.status = 2, w.updateTime = now() where w.id in ?1 and w.status = 1")
    int batchPublish(List<Long> idList);
}



