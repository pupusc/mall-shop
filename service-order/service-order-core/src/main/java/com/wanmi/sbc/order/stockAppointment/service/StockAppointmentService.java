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
    public Integer saveAppointment(List<StockAppointmentRequest> request){
        return repository.saveAll(KsBeanUtil.convertList(request, GoodsStockAppointment.class)).size();
    }

    public BaseResponse<AppointmentRequest> findAllAppointment(){
        List<StockAppointmentRequest> requests = KsBeanUtil.convertList(repository.findAll(), StockAppointmentRequest.class);
        AppointmentRequest appointmentRequest=new AppointmentRequest();
        appointmentRequest.setAppointmentList(requests);
        return BaseResponse.success(appointmentRequest);
    }

    @Transactional
    public void delete(Integer id){
        repository.deleteById(id);
    }
}
