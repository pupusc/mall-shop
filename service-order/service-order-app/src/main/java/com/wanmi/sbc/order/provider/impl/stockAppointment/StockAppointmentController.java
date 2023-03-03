package com.wanmi.sbc.order.provider.impl.stockAppointment;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.provider.stockAppointment.StockAppointmentProvider;
import com.wanmi.sbc.order.api.request.appointmentrecord.AppointmentRecordRequest;
import com.wanmi.sbc.order.api.request.stockAppointment.AppointmentRequest;
import com.wanmi.sbc.order.stockAppointment.service.StockAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Validated
public class StockAppointmentController implements StockAppointmentProvider {

    @Autowired
    StockAppointmentService appointmentService;

    @Override
    public BaseResponse add(@RequestBody @Valid AppointmentRequest request) {
        Integer integer = appointmentService.saveAppointment(request.getAppointmentList());
        if(null!=integer&&integer>0) {
            return BaseResponse.SUCCESSFUL();
        }else {
            return BaseResponse.FAILED();
        }
    }

    @Override
    public BaseResponse<AppointmentRequest> finAll() {
        return appointmentService.findAllAppointment();
    }

    @Override
    public void delete(Integer id) {
        appointmentService.delete(id);
    }
}