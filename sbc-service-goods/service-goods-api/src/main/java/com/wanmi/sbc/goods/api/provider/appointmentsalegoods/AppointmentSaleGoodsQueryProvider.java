package com.wanmi.sbc.goods.api.provider.appointmentsalegoods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.appointmentsale.AppointmentGoodsInfoSimplePageRequest;
import com.wanmi.sbc.goods.api.request.appointmentsalegoods.AppointmentSaleGoodsByIdRequest;
import com.wanmi.sbc.goods.api.request.appointmentsalegoods.AppointmentSaleGoodsListRequest;
import com.wanmi.sbc.goods.api.request.appointmentsalegoods.AppointmentSaleGoodsPageRequest;
import com.wanmi.sbc.goods.api.response.appointmentsalegoods.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>预约抢购查询服务Provider</p>
 * @author zxd
 * @date 2020-05-21 13:47:11
 */
@FeignClient(value = "${application.goods.name}", contextId = "AppointmentSaleGoodsQueryProvider")
public interface AppointmentSaleGoodsQueryProvider {

	/**
	 * 分页查询预约抢购API
	 *
	 * @author zxd
	 * @param appointmentSaleGoodsPageReq 分页请求参数和筛选对象 {@link AppointmentSaleGoodsPageRequest}
	 * @return 预约抢购分页列表信息 {@link AppointmentSaleGoodsPageResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/appointmentsalegoods/page")
	BaseResponse<AppointmentSaleGoodsPageResponse> page(@RequestBody @Valid AppointmentSaleGoodsPageRequest appointmentSaleGoodsPageReq);

	/**
	 * 列表查询预约抢购API
	 *
	 * @author zxd
	 * @param appointmentSaleGoodsListReq 列表请求参数和筛选对象 {@link AppointmentSaleGoodsListRequest}
	 * @return 预约抢购的列表信息 {@link AppointmentSaleGoodsListResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/appointmentsalegoods/list")
	BaseResponse<AppointmentSaleGoodsListResponse> list(@RequestBody @Valid AppointmentSaleGoodsListRequest appointmentSaleGoodsListReq);

	/**
	 * 单个查询预约抢购API
	 *
	 * @author zxd
	 * @param appointmentSaleGoodsByIdRequest 单个查询预约抢购请求参数 {@link AppointmentSaleGoodsByIdRequest}
	 * @return 预约抢购详情 {@link AppointmentSaleGoodsByIdResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/appointmentsalegoods/get-by-id")
	BaseResponse<AppointmentSaleGoodsByIdResponse> getById(@RequestBody @Valid AppointmentSaleGoodsByIdRequest appointmentSaleGoodsByIdRequest);


	/**
	 * 获取魔方预约活动商品列表分页信息
	 * @param request
	 * @return
	 */
	@PostMapping("/goods/${application.goods.version}/appointmentsalegoods/page-boss")
    BaseResponse<AppointmentResponse> pageBoss(@RequestBody @Valid AppointmentGoodsInfoSimplePageRequest request);


	/**
	 * 获取魔方H5预约活动商品列表分页信息
	 * @param request
	 * @return
	 */
	@PostMapping("/goods/${application.goods.version}/appointmentsalegoods/pageAppointmentGoodsInfo")
	BaseResponse<AppointmentGoodsResponse> pageAppointmentGoodsInfo(@RequestBody @Valid AppointmentGoodsInfoSimplePageRequest request);
}

