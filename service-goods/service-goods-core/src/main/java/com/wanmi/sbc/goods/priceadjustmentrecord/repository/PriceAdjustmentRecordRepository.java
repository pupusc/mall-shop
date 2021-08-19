package com.wanmi.sbc.goods.priceadjustmentrecord.repository;

import com.wanmi.sbc.goods.priceadjustmentrecord.model.root.PriceAdjustmentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>调价记录表DAO</p>
 *
 * @author chenli
 * @date 2020-12-09 19:57:21
 */
@Repository
public interface PriceAdjustmentRecordRepository extends JpaRepository<PriceAdjustmentRecord, String>,
        JpaSpecificationExecutor<PriceAdjustmentRecord> {

    /**
     * 单个查询调价记录表
     *
     * @param id
     * @param storeId
     * @return
     */
    PriceAdjustmentRecord findByIdAndStoreId(String id, Long storeId);


    @Modifying
    @Query(value = "delete from PriceAdjustmentRecord r where r.id in ?1 and r.confirmFlag = 0")
    void deleteByIds(List<String> ids);

    @Query("select r.id from PriceAdjustmentRecord r where r.createTime < ?1 and r.confirmFlag = 0")
    List<String> findByTime(LocalDateTime time);


    /**
     * 确认改价记录
     *
     * @param id
     * @param storeId
     */
    @Modifying
    @Query("update PriceAdjustmentRecord a set a.confirmFlag = '1',a.effectiveTime=?3 where a.id = ?1 and a.storeId=?2")
    void confirmAdjustRecord(String id, Long storeId, LocalDateTime effectiveTime);

    @Modifying
    @Query("update PriceAdjustmentRecord set goodsNum = goodsNum - 1 where id = ?1")
    void reduceGoodsNum(String id);

}
