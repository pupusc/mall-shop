package com.wanmi.sbc.order.provider.impl.stockAppointment;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.provider.stockAppointment.StockAppointmentProvider;
import com.wanmi.sbc.order.api.request.appointmentrecord.AppointmentRecordRequest;
import com.wanmi.sbc.order.api.request.stockAppointment.AppointmentRequest;
import com.wanmi.sbc.order.stockAppointment.service.StockAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Validated
public class StockAppointmentController implements StockAppointmentProvider {

    @Autowired
    StockAppointmentService appointmentService;

    @Override
    public BaseResponse add(@RequestBody @Valid AppointmentRequest request) {
        return appointmentService.saveAppointment(request.getAppointmentList());
    }
    @Override
    public BaseResponse<AppointmentRequest> findCustomerAppointment(AppointmentRequest request){
        return appointmentService.findCustomerAppointment(request);
    }

    @Override
    public BaseResponse<AppointmentRequest> finAll() {
        return appointmentService.findAllAppointment();
    }

    @Override
    public BaseResponse delete(AppointmentRequest request) {
        return appointmentService.delete(request);
    }

    @Override
    public BaseResponse deleteById(@RequestParam("id") Integer id){
        return appointmentService.deleteById(id);
    }
}
