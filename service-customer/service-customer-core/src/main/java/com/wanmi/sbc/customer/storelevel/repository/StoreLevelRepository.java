package com.wanmi.sbc.customer.storelevel.repository;

import com.wanmi.sbc.customer.storelevel.model.root.StoreLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>商户客户等级表DAO</p>
 *
 * @author yang
 * @date 2019-02-27 19:51:30
 */
@Repository
public interface StoreLevelRepository extends JpaRepository<StoreLevel, Long>,
        JpaSpecificationExecutor<StoreLevel> {

    /**
     * 删除会员等级
     */
    @Modifying
    @Query("update StoreLevel l set l.delFlag = 1 where l.storeLevelId = :storeLevelId")
    void deleteStoreLevel(@Param("storeLevelId") Long storeLevelId);

    /**
     * 查询某商户会员等级
     *
     * @return
     */
    @Query("from StoreLevel c where c.delFlag = 0 and c.storeId = :storeId order by c.createTime asc")
    List<StoreLevel> findByStoreIdOrderByCreateTimeAsc(@Param("storeId") Long storeId);

    /**
     * 根据等级名称查询
     *
     * @param levelName
     * @return
     */
    @Query("from StoreLevel c where c.delFlag = 0 and c.storeId = :storeId and c.levelName = :levelName")
    List<StoreLevel> findByStoreIdAndLevelName(@Param("storeId") Long storeId, @Param("levelName") String levelName);

    /**
     * 查询满足升级条件的店铺等级列表
     */
    @Query("FROM StoreLevel s WHERE s.storeId = :storeId and s.delFlag=0 and (:amountConditions >= s.amountConditions " +
            "or :orderConditions >= s.orderConditions) ORDER BY s.createTime desc")
    List<StoreLevel> queryByLevelUpCondition(@Param("storeId") Long storeId,
                                       @Param("amountConditions") BigDecimal amountConditions,
                                       @Param("orderConditions") Integer orderConditions);

    /**
     * 查询某商户会员等级
     *
     * @return
     */
    @Query("from StoreLevel c where c.storeId in :storeIds and c.delFlag = 0  ")
    List<StoreLevel> findByStoreIdOrderByCreateTimeAsc(@Param("storeIds") List<Long> storeIds);
}
