package com.wanmi.sbc.marketing.grouponrecord.repository;

import com.wanmi.sbc.marketing.grouponrecord.model.root.GrouponRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * <p>拼团活动参团信息表DAO</p>
 *
 * @author groupon
 * @date 2019-05-17 16:17:44
 */
@Repository
public interface GrouponRecordRepository extends JpaRepository<GrouponRecord, String>,
        JpaSpecificationExecutor<GrouponRecord> {

    /**
     * 单个查询C端用户参团情况
     * @param grouponActivityId
     * @param customerId
     * @param goodsInfoId
     * @return
     */
    GrouponRecord findTopByGrouponActivityIdAndCustomerIdAndGoodsInfoId(String grouponActivityId, String customerId,
                                                                        String goodsInfoId);

    /**
     * 根据活动ID、会员ID、SKU编号更新已购买数量（增加操作）
     * @param grouponActivityId
     * @param customerId
     * @param goodsInfoId
     * @param buyNum
     * @param updateTime
     * @return
     */
    @Modifying
    @Query("update GrouponRecord g set g.buyNum = g.buyNum + :buyNum,g.updateTime = :updateTime where g.grouponActivityId = :grouponActivityId " +
            " and g.customerId = :customerId and g.goodsInfoId = :goodsInfoId")
    int incrBuyNumByGrouponActivityIdAndCustomerIdAndGoodsInfoId(@Param("grouponActivityId") String grouponActivityId,
                                                @Param("customerId") String customerId,
                                                @Param("goodsInfoId") String goodsInfoId,
                                                @Param("buyNum") Integer buyNum,
                                                @Param("updateTime") LocalDateTime updateTime);

    /**
     * 根据活动ID、会员ID、SKU编号更新已购买数量（减少操作）
     * @param grouponActivityId
     * @param customerId
     * @param goodsInfoId
     * @param buyNum
     * @param updateTime
     * @return
     */
    @Modifying
    @Query("update GrouponRecord g set g.buyNum = g.buyNum - :buyNum,g.updateTime = :updateTime where g.grouponActivityId = :grouponActivityId " +
            " and g.customerId = :customerId and g.goodsInfoId = :goodsInfoId")
    int decrBuyNumByGrouponActivityIdAndCustomerIdAndGoodsInfoId(@Param("grouponActivityId") String grouponActivityId,
                                                @Param("customerId") String customerId,
                                                @Param("goodsInfoId") String goodsInfoId,
                                                @Param("buyNum") Integer buyNum,
                                                @Param("updateTime") LocalDateTime updateTime);
}
