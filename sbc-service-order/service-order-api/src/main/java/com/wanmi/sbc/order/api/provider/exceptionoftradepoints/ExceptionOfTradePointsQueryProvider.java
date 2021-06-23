package com.wanmi.sbc.order.api.provider.exceptionoftradepoints;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.request.exceptionoftradepoints.ExceptionOfTradePointsPageRequest;
import com.wanmi.sbc.order.api.response.exceptionoftradepoints.ExceptionOfTradePointsPageResponse;
import com.wanmi.sbc.order.api.request.exceptionoftradepoints.ExceptionOfTradePointsListRequest;
import com.wanmi.sbc.order.api.response.exceptionoftradepoints.ExceptionOfTradePointsListResponse;
import com.wanmi.sbc.order.api.request.exceptionoftradepoints.ExceptionOfTradePointsByIdRequest;
import com.wanmi.sbc.order.api.response.exceptionoftradepoints.ExceptionOfTradePointsByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>积分订单抵扣异常表查询服务Provider</p>
 * @author caofang
 * @date 2021-03-10 18:54:25
 */
@FeignClient(value = "${application.order.name}", contextId = "ExceptionOfTradePointsQueryProvider")
public interface ExceptionOfTradePointsQueryProvider {

	/**
	 * 分页查询积分订单抵扣异常表API
	 *
	 * @author caofang
	 * @param exceptionOfTradePointsPageReq 分页请求参数和筛选对象 {@link ExceptionOfTradePointsPageRequest}
	 * @return 积分订单抵扣异常表分页列表信息 {@link ExceptionOfTradePointsPageResponse}
	 */
	@PostMapping("/order/${application.order.version}/exceptionoftradepoints/page")
	BaseResponse<ExceptionOfTradePointsPageResponse> page(@RequestBody @Valid ExceptionOfTradePointsPageRequest exceptionOfTradePointsPageReq);

	/**
	 * 列表查询积分订单抵扣异常表API
	 *
	 * @author caofang
	 * @param exceptionOfTradePointsListReq 列表请求参数和筛选对象 {@link ExceptionOfTradePointsListRequest}
	 * @return 积分订单抵扣异常表的列表信息 {@link ExceptionOfTradePointsListResponse}
	 */
	@PostMapping("/order/${application.order.version}/exceptionoftradepoints/list")
	BaseResponse<ExceptionOfTradePointsListResponse> list(@RequestBody @Valid ExceptionOfTradePointsListRequest exceptionOfTradePointsListReq);

	/**
	 * 单个查询积分订单抵扣异常表API
	 *
	 * @author caofang
	 * @param exceptionOfTradePointsByIdRequest 单个查询积分订单抵扣异常表请求参数 {@link ExceptionOfTradePointsByIdRequest}
	 * @return 积分订单抵扣异常表详情 {@link ExceptionOfTradePointsByIdResponse}
	 */
	@PostMapping("/order/${application.order.version}/exceptionoftradepoints/get-by-id")
	BaseResponse<ExceptionOfTradePointsByIdResponse> getById(@RequestBody @Valid ExceptionOfTradePointsByIdRequest exceptionOfTradePointsByIdRequest);

}

