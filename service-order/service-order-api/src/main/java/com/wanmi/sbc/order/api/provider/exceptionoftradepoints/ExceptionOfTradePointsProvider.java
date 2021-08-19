package com.wanmi.sbc.order.api.provider.exceptionoftradepoints;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.request.exceptionoftradepoints.ExceptionOfTradePointsAddRequest;
import com.wanmi.sbc.order.api.response.exceptionoftradepoints.ExceptionOfTradePointsAddResponse;
import com.wanmi.sbc.order.api.request.exceptionoftradepoints.ExceptionOfTradePointsModifyRequest;
import com.wanmi.sbc.order.api.response.exceptionoftradepoints.ExceptionOfTradePointsModifyResponse;
import com.wanmi.sbc.order.api.request.exceptionoftradepoints.ExceptionOfTradePointsDelByIdRequest;
import com.wanmi.sbc.order.api.request.exceptionoftradepoints.ExceptionOfTradePointsDelByIdListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>积分订单抵扣异常表保存服务Provider</p>
 * @author caofang
 * @date 2021-03-10 18:54:25
 */
@FeignClient(value = "${application.order.name}", contextId = "ExceptionOfTradePointsProvider")
public interface ExceptionOfTradePointsProvider {

	/**
	 * 新增积分订单抵扣异常表API
	 *
	 * @author caofang
	 * @param exceptionOfTradePointsAddRequest 积分订单抵扣异常表新增参数结构 {@link ExceptionOfTradePointsAddRequest}
	 * @return 新增的积分订单抵扣异常表信息 {@link ExceptionOfTradePointsAddResponse}
	 */
	@PostMapping("/order/${application.order.version}/exceptionoftradepoints/add")
	BaseResponse<ExceptionOfTradePointsAddResponse> add(@RequestBody @Valid ExceptionOfTradePointsAddRequest exceptionOfTradePointsAddRequest);

	/**
	 * 修改积分订单抵扣异常表API
	 *
	 * @author caofang
	 * @param exceptionOfTradePointsModifyRequest 积分订单抵扣异常表修改参数结构 {@link ExceptionOfTradePointsModifyRequest}
	 * @return 修改的积分订单抵扣异常表信息 {@link ExceptionOfTradePointsModifyResponse}
	 */
	@PostMapping("/order/${application.order.version}/exceptionoftradepoints/modify")
	BaseResponse<ExceptionOfTradePointsModifyResponse> modify(@RequestBody @Valid ExceptionOfTradePointsModifyRequest exceptionOfTradePointsModifyRequest);

	/**
	 * 单个删除积分订单抵扣异常表API
	 *
	 * @author caofang
	 * @param exceptionOfTradePointsDelByIdRequest 单个删除参数结构 {@link ExceptionOfTradePointsDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/order/${application.order.version}/exceptionoftradepoints/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid ExceptionOfTradePointsDelByIdRequest exceptionOfTradePointsDelByIdRequest);

	/**
	 * 批量删除积分订单抵扣异常表API
	 *
	 * @author caofang
	 * @param exceptionOfTradePointsDelByIdListRequest 批量删除参数结构 {@link ExceptionOfTradePointsDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/order/${application.order.version}/exceptionoftradepoints/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid ExceptionOfTradePointsDelByIdListRequest exceptionOfTradePointsDelByIdListRequest);

}

