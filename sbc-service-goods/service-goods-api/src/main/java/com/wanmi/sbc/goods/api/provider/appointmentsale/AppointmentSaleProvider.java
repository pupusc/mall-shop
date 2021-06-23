package com.wanmi.sbc.goods.api.provider.appointmentsale;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.appointmentsale.*;
import com.wanmi.sbc.goods.api.response.appointmentsale.AppointmentSaleAddResponse;
import com.wanmi.sbc.goods.api.response.appointmentsale.AppointmentSaleModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>预约抢购保存服务Provider</p>
 * @author zxd
 * @date 2020-05-21 10:32:23
 */
@FeignClient(value = "${application.goods.name}", contextId = "AppointmentSaleProvider")
public interface AppointmentSaleProvider {

	/**
	 * 新增预约抢购API
	 *
	 * @author zxd
	 * @param appointmentSaleAddRequest 预约抢购新增参数结构 {@link AppointmentSaleAddRequest}
	 * @return 新增的预约抢购信息 {@link AppointmentSaleAddResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/appointmentsale/add")
	BaseResponse<AppointmentSaleAddResponse> add(@RequestBody @Valid AppointmentSaleAddRequest appointmentSaleAddRequest);

	/**
	 * 修改预约抢购API
	 *
	 * @author zxd
	 * @param appointmentSaleModifyRequest 预约抢购修改参数结构 {@link AppointmentSaleModifyRequest}
	 * @return 修改的预约抢购信息 {@link AppointmentSaleModifyResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/appointmentsale/modify")
	BaseResponse<AppointmentSaleModifyResponse> modify(@RequestBody @Valid AppointmentSaleModifyRequest appointmentSaleModifyRequest);

	/**
	 * 单个删除预约抢购API
	 *
	 * @author zxd
	 * @param appointmentSaleDelByIdRequest 单个删除参数结构 {@link AppointmentSaleDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/appointmentsale/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid AppointmentSaleDelByIdRequest appointmentSaleDelByIdRequest);

	/**
	 * 批量删除预约抢购API
	 *
	 * @author zxd
	 * @param appointmentSaleDelByIdListRequest 批量删除参数结构 {@link AppointmentSaleDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/appointmentsale/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid AppointmentSaleDelByIdListRequest appointmentSaleDelByIdListRequest);

	/**
	 * 暂停活动/开启活动
	 *
	 * @param request
	 * @return
	 */
	@PostMapping("/goods/${application.goods.version}/appointmentsale/modify-status")
    BaseResponse modifyStatus(@RequestBody @Valid AppointmentSaleStatusRequest request);
}

