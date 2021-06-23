package com.wanmi.sbc.order.api.provider.paycallbackresult;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.request.paycallbackresult.PayCallBackResultByIdRequest;
import com.wanmi.sbc.order.api.request.paycallbackresult.PayCallBackResultListRequest;
import com.wanmi.sbc.order.api.request.paycallbackresult.PayCallBackResultPageRequest;
import com.wanmi.sbc.order.api.response.paycallbackresult.PayCallBackResultByIdResponse;
import com.wanmi.sbc.order.api.response.paycallbackresult.PayCallBackResultListResponse;
import com.wanmi.sbc.order.api.response.paycallbackresult.PayCallBackResultPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>支付回调结果查询服务Provider</p>
 * @author lvzhenwei
 * @date 2020-07-01 17:34:23
 */
@FeignClient(value = "${application.order.name}", contextId = "PayCallBackResultQueryProvider")
public interface PayCallBackResultQueryProvider {

	/**
	 * 分页查询支付回调结果API
	 *
	 * @author lvzhenwei
	 * @param payCallBackResultPageReq 分页请求参数和筛选对象 {@link PayCallBackResultPageRequest}
	 * @return 支付回调结果分页列表信息 {@link PayCallBackResultPageResponse}
	 */
	@PostMapping("/order/${application.order.version}/paycallbackresult/page")
    BaseResponse<PayCallBackResultPageResponse> page(@RequestBody @Valid PayCallBackResultPageRequest payCallBackResultPageReq);

	/**
	 * 列表查询支付回调结果API
	 *
	 * @author lvzhenwei
	 * @param payCallBackResultListReq 列表请求参数和筛选对象 {@link PayCallBackResultListRequest}
	 * @return 支付回调结果的列表信息 {@link PayCallBackResultListResponse}
	 */
	@PostMapping("/order/${application.order.version}/paycallbackresult/list")
    BaseResponse<PayCallBackResultListResponse> list(@RequestBody @Valid PayCallBackResultListRequest payCallBackResultListReq);

	/**
	 * 单个查询支付回调结果API
	 *
	 * @author lvzhenwei
	 * @param payCallBackResultByIdRequest 单个查询支付回调结果请求参数 {@link PayCallBackResultByIdRequest}
	 * @return 支付回调结果详情 {@link PayCallBackResultByIdResponse}
	 */
	@PostMapping("/order/${application.order.version}/paycallbackresult/get-by-id")
    BaseResponse<PayCallBackResultByIdResponse> getById(@RequestBody @Valid PayCallBackResultByIdRequest payCallBackResultByIdRequest);

}

