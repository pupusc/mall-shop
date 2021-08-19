package com.wanmi.sbc.order.api.provider.paycallbackresult;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.request.paycallbackresult.PayCallBackResultAddRequest;
import com.wanmi.sbc.order.api.request.paycallbackresult.PayCallBackResultDelByIdRequest;
import com.wanmi.sbc.order.api.request.paycallbackresult.PayCallBackResultModifyRequest;
import com.wanmi.sbc.order.api.request.paycallbackresult.PayCallBackResultModifyResultStatusRequest;
import com.wanmi.sbc.order.api.response.paycallbackresult.PayCallBackResultAddResponse;
import com.wanmi.sbc.order.api.response.paycallbackresult.PayCallBackResultModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>支付回调结果保存服务Provider</p>
 * @author lvzhenwei
 * @date 2020-07-01 17:34:23
 */
@FeignClient(value = "${application.order.name}", contextId = "PayCallBackResultProvider")
public interface PayCallBackResultProvider {

	/**
	 * 新增支付回调结果API
	 *
	 * @author lvzhenwei
	 * @param payCallBackResultAddRequest 支付回调结果新增参数结构 {@link PayCallBackResultAddRequest}
	 * @return 新增的支付回调结果信息 {@link PayCallBackResultAddResponse}
	 */
	@PostMapping("/order/${application.order.version}/paycallbackresult/add")
    BaseResponse<PayCallBackResultAddResponse> add(@RequestBody @Valid PayCallBackResultAddRequest payCallBackResultAddRequest);

	/**
	 * 修改支付回调结果API
	 *
	 * @author lvzhenwei
	 * @param payCallBackResultModifyRequest 支付回调结果修改参数结构 {@link PayCallBackResultModifyRequest}
	 * @return 修改的支付回调结果信息 {@link PayCallBackResultModifyResponse}
	 */
	@PostMapping("/order/${application.order.version}/paycallbackresult/modify")
    BaseResponse<PayCallBackResultModifyResponse> modify(@RequestBody @Valid PayCallBackResultModifyRequest payCallBackResultModifyRequest);

	/**
	 * 单个删除支付回调结果API
	 *
	 * @author lvzhenwei
	 * @param payCallBackResultDelByIdRequest 单个删除参数结构 {@link PayCallBackResultDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/order/${application.order.version}/paycallbackresult/delete-by-id")
    BaseResponse deleteById(@RequestBody @Valid PayCallBackResultDelByIdRequest payCallBackResultDelByIdRequest);

	/**
	 * 根据businessId更新支付回调结果状态
	 *
	 * @author lvzhenwei
	 * @param payCallBackResultModifyResultStatusRequest 根据businessId更新支付回调结果状态参数结构 {@link PayCallBackResultModifyResultStatusRequest}
	 * @return 根据businessId更新支付回调结果状态 {@link PayCallBackResultModifyResponse}
	 */
	@PostMapping("/order/${application.order.version}/paycallbackresult/modify_result_status_by_businessId")
	BaseResponse modifyResultStatusByBusinessId(@RequestBody @Valid PayCallBackResultModifyResultStatusRequest payCallBackResultModifyResultStatusRequest);

}

