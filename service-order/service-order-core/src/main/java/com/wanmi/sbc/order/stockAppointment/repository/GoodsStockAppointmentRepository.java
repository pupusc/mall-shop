package com.wanmi.sbc.order.stockAppointment.repository;

import com.wanmi.sbc.order.stockAppointment.model.root.GoodsStockAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GoodsStockAppointmentRepository extends JpaRepository<GoodsStockAppointment, Integer>, JpaSpecificationExecutor<GoodsStockAppointment> {


}
