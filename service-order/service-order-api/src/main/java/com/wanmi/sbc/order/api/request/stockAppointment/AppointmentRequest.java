package com.wanmi.sbc.order.api.request.stockAppointment;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class AppointmentRequest implements Serializable {

    private List<StockAppointmentRequest> appointmentList;
}
