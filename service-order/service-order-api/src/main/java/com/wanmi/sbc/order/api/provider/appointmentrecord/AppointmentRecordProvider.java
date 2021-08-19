package com.wanmi.sbc.order.api.provider.appointmentrecord;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.request.appointmentrecord.AppointmentRecordRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>预约抢购记录Provider</p>
 * @author zxd
 * @date 2020-05-21 10:32:23
 */
@FeignClient(value = "${application.order.name}", contextId = "AppointmentRecordProvider")
public interface AppointmentRecordProvider {

	/**
	 * 预约
	 *
	 * @author zxd
	 * @param request 预约参数结构 {@link AppointmentRecordRequest}
	 * @return
	 */
	@PostMapping("/order/${application.order.version}/appointmentrecord/add")
	BaseResponse add(@RequestBody @Valid AppointmentRecordRequest request);

}

