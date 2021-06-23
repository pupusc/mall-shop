package com.wanmi.sbc.goods.appointmentsale.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.appointmentsale.model.root.AppointmentSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * <p>预约抢购DAO</p>
 *
 * @author zxd
 * @date 2020-05-21 10:32:23
 */
@Repository
public interface AppointmentSaleRepository extends JpaRepository<AppointmentSale, Long>,
        JpaSpecificationExecutor<AppointmentSale> {

    /**
     * 单个删除预约抢购
     *
     * @author zxd
     */
    @Modifying
    @Query("update AppointmentSale set delFlag = 1 where id = ?1")
    void deleteById(Long id);

    /**
     * 批量删除预约抢购
     *
     * @author zxd
     */
    @Modifying
    @Query("update AppointmentSale set delFlag = 1 where id in ?1")
    void deleteByIdList(List<Long> idList);

    Optional<AppointmentSale> findByIdAndDelFlag(Long id, DeleteFlag delFlag);


    Optional<AppointmentSale> findByIdAndStoreIdAndDelFlag(Long id, Long storeId, DeleteFlag delFlag);

    /**
     * 根据skuid获取正在进行中的预购活动
     *
     * @author zxd
     */
    @Query("from AppointmentSale a inner join AppointmentSaleGoods b on a.id = b.appointmentSaleId " +
            "and b.goodsInfoId = ?1 and a.appointmentStartTime <= now() and a.snapUpEndTime >= now() and a.pauseFlag = 0 and a.delFlag = 0")
    Object findByGoodsInfoIdInProcess(String goodsInfoId);


    /**
     * 根据skuid列表获取正在进行中的预购活动列表
     *
     * @author zxd
     */
    @Query("from AppointmentSale a inner join AppointmentSaleGoods b on a.id = b.appointmentSaleId " +
            "and b.goodsInfoId in ?1 and a.appointmentStartTime <= now() and a.snapUpEndTime >= now() and a.pauseFlag = 0 and a.delFlag = 0")
    List findByGoodsInfoIdInProcess(List<String> goodsInfoIdList);

    /**
     * 根据skuid列表获取正在进行中的预购活动列表
     *
     */
    @Query("from AppointmentSale a inner join AppointmentSaleGoods b on a.id = b.appointmentSaleId " +
            "and b.goodsInfoId in ?1 " +
            "and a.appointmentStartTime <= now() and a.appointmentEndTime >= now() and a.pauseFlag = 0 and a.delFlag = 0")
    List findByGoodsInfoIdInAppointment(List<String> goodsInfoIdList);

    /**
     * 根据spuid列表获取未结束的预约活动
     *
     * @author zxd
     */
    @Query("from AppointmentSale a inner join AppointmentSaleGoods b on a.id = b.appointmentSaleId " +
            "and b.goodsId = ?1 and a.snapUpEndTime >= now() and a.delFlag = 0")
    List getNotEndActivity(String goodsId);
}
