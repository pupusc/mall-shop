package com.wanmi.sbc.order.stockAppointment.service;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.order.api.request.stockAppointment.AppointmentRequest;
import com.wanmi.sbc.order.stockAppointment.model.root.GoodsStockAppointment;
import com.wanmi.sbc.order.stockAppointment.repository.GoodsStockAppointmentRepository;
import com.wanmi.sbc.order.api.request.stockAppointment.StockAppointmentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StockAppointmentService {

    @Autowired
    GoodsStockAppointmentRepository repository;

    @Transactional
    public BaseResponse saveAppointment(List<StockAppointmentRequest> request){
        GoodsStockAppointment stockAppointments = KsBeanUtil.convert(request.get(0), GoodsStockAppointment.class);
        if(isAppointmented(stockAppointments)){
            return BaseResponse.error("商品已预约！");
        }
        return BaseResponse.success(repository.save(stockAppointments).getId());
    }

    public BaseResponse<AppointmentRequest> findAllAppointment(){
        List<StockAppointmentRequest> requests = KsBeanUtil.convertList(repository.findAll(), StockAppointmentRequest.class);
        AppointmentRequest appointmentRequest=new AppointmentRequest();
        appointmentRequest.setAppointmentList(requests);
        return BaseResponse.success(appointmentRequest);
    }

    public BaseResponse<AppointmentRequest> findCustomerAppointment(AppointmentRequest request){
        StockAppointmentRequest stockAppointmentRequest = request.getAppointmentList().get(0);
        List<StockAppointmentRequest> requests = KsBeanUtil.convertList(repository.findByAccountAndCustomer(stockAppointmentRequest.getAccount(),stockAppointmentRequest.getCustomer()), StockAppointmentRequest.class);
        AppointmentRequest appointmentRequest=new AppointmentRequest();
        appointmentRequest.setAppointmentList(requests);
        return BaseResponse.success(appointmentRequest);
    }

    public boolean isAppointmented(GoodsStockAppointment stockAppointments){
        int i = repository.countByAccountAndCustomerAndGoodsIdAndGoodsInfo(stockAppointments.getAccount(), stockAppointments.getCustomer(), stockAppointments.getGoodsId(), stockAppointments.getGoodsInfo());
        if(i>0){
            return true;
        }else {
            return false;
        }
    }

    @Transactional
    public BaseResponse delete(AppointmentRequest request){
        try {
            GoodsStockAppointment appointment=KsBeanUtil.convert(request.getAppointmentList().get(0), GoodsStockAppointment.class);
            repository.deleteByCustomerAndAccountAndGoodsIdAndGoodsInfo(appointment.getCustomer(),appointment.getAccount(), appointment.getGoodsId(),appointment.getGoodsInfo());
        }catch (Exception e){
            e.printStackTrace();
            return BaseResponse.error("取消预约失败！");
        }
        return BaseResponse.success("取消预约成功！");
    }

    @Transactional
    public BaseResponse deleteById(Integer id) {
        try {
            repository.deleteById(id);
            return BaseResponse.success("删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return BaseResponse.error("删除失败");
        }
    }
}
