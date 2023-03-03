package com.wanmi.sbc.order.api.provider.stockAppointment;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.request.appointmentrecord.AppointmentRecordRequest;
import com.wanmi.sbc.order.api.request.stockAppointment.AppointmentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * <p>预约抢购记录Provider</p>
 * @author zxd
 * @date 2020-05-21 10:32:23
 */
@FeignClient(value = "${application.order.name}", contextId = "StockAppointmentProvider")
public interface StockAppointmentProvider {

	/**
	 * 预约
	 *
	 * @author zxd
	 * @param request 预约参数结构 {@link AppointmentRecordRequest}
	 * @return
	 */
	@PostMapping("/order/${application.order.version}/stockappointment/add")
	BaseResponse add(@RequestBody @Valid AppointmentRequest request);

	@PostMapping("/order/${application.order.version}/stockappointment/findAll")
	BaseResponse<AppointmentRequest> finAll();

	@PostMapping("/order/${application.order.version}/stockappointment/findCustomerAppointment")
	BaseResponse<AppointmentRequest> findCustomerAppointment(@RequestBody AppointmentRequest request);

	@PostMapping("/order/${application.order.version}/stockappointment/delete")
	BaseResponse delete(@RequestBody AppointmentRequest request);

}

