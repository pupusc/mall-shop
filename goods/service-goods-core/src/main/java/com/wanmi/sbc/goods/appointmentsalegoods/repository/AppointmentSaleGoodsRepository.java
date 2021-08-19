package com.wanmi.sbc.goods.appointmentsalegoods.repository;

import com.wanmi.sbc.goods.appointmentsalegoods.model.root.AppointmentSaleGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * <p>预约抢购DAO</p>
 *
 * @author zxd
 * @date 2020-05-21 13:47:11
 */
@Repository
public interface AppointmentSaleGoodsRepository extends JpaRepository<AppointmentSaleGoods, Long>,
        JpaSpecificationExecutor<AppointmentSaleGoods> {

    Optional<AppointmentSaleGoods> findByIdAndStoreId(Long id, Long storeId);

    List<AppointmentSaleGoods> findByAppointmentSaleIdAndStoreId(Long id, Long storeId);

    void deleteByIdIn(List<Long> idList);

    void deleteByAppointmentSaleId(Long appointmentSaleId);


    @Modifying
    @Query("update AppointmentSaleGoods a set a.appointmentCount = a.appointmentCount + 1,updateTime = now() where a.appointmentSaleId = ?1 and a.goodsInfoId = ?2")
    int updateAppointmentCount(Long appointmentSaleId, String goodsInfoId);


    @Modifying
    @Query("update AppointmentSaleGoods a set a.buyerCount = a.buyerCount + :stock,updateTime = now() where a.appointmentSaleId = :appointmentSaleId and a.goodsInfoId = :goodsInfoId")
    int updateBuyCount(@Param("appointmentSaleId") Long appointmentSaleId, @Param("goodsInfoId") String goodsInfoId, @Param("stock") Integer stock);

    @Query("from AppointmentSale a inner join AppointmentSaleGoods b on a.id = b.appointmentSaleId " +
            "and b.goodsInfoId in :goodsInfoIds and ((a.appointmentStartTime <= :startTime and a.snapUpEndTime >= :startTime) or " +
            "(a.appointmentStartTime <= :endTime and a.snapUpEndTime >= :endTime)) " +
            " and a.delFlag = 0")
    List existAppointmentActivity(@Param("goodsInfoIds") List<String> goodsInfoIds,
                                    @Param("startTime") LocalDateTime startTime,
                                    @Param("endTime") LocalDateTime endTime);
}
