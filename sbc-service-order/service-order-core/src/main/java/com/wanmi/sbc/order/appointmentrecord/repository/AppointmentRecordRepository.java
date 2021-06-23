package com.wanmi.sbc.order.appointmentrecord.repository;

import com.wanmi.sbc.order.appointmentrecord.model.root.AppointmentRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>预约</p>
 *
 * @author zxd
 * @date 2020-05-21 10:32:23
 */
@Repository
public interface AppointmentRecordRepository extends MongoRepository<AppointmentRecord, Long>{



    AppointmentRecord findByAppointmentSaleIdAndBuyerIdAndGoodsInfoId(Long appointmentSaleId,String buyerId,String goodsInfoId);


    List<AppointmentRecord> findByAppointmentSaleInfo_snapUpStartTime(LocalDateTime appointmentSaleInfo_snapUpStartTime);
}
