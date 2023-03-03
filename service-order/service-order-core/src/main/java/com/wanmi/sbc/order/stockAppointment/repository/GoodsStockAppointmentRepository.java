package com.wanmi.sbc.order.stockAppointment.repository;

import com.wanmi.sbc.order.stockAppointment.model.root.GoodsStockAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GoodsStockAppointmentRepository extends JpaRepository<GoodsStockAppointment, Integer>, JpaSpecificationExecutor<GoodsStockAppointment> {

    @Modifying
    @Query
    int deleteByCustomerAndAccountAndGoodsIdAndGoodsInfo(String customer,String account,String goodsId,String goodsInfo);

    @Query
    List<GoodsStockAppointment> findByAccountAndCustomer(String account,String customer);

    @Query
    int countByAccountAndCustomerAndGoodsIdAndGoodsInfo(String account,String customer,String goodsId,String goodsInfo);

}
