package com.wanmi.sbc.message.api.provider.pushcustomerenable;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.message.api.request.pushcustomerenable.PushCustomerEnableByIdRequest;
import com.wanmi.sbc.message.api.request.pushcustomerenable.PushCustomerEnableListRequest;
import com.wanmi.sbc.message.api.request.pushcustomerenable.PushCustomerEnablePageRequest;
import com.wanmi.sbc.message.api.response.pushcustomerenable.PushCustomerEnableByIdResponse;
import com.wanmi.sbc.message.api.response.pushcustomerenable.PushCustomerEnableListResponse;
import com.wanmi.sbc.message.api.response.pushcustomerenable.PushCustomerEnablePageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>会员推送通知开关查询服务Provider</p>
 * @author Bob
 * @date 2020-01-07 15:31:47
 */
@FeignClient(value = "${application.message.name}", contextId = "PushCustomerEnableQueryProvider")
public interface PushCustomerEnableQueryProvider {

	/**
	 * 分页查询会员推送通知开关API
	 *
	 * @author Bob
	 * @param pushCustomerEnablePageReq 分页请求参数和筛选对象 {@link PushCustomerEnablePageRequest}
	 * @return 会员推送通知开关分页列表信息 {@link PushCustomerEnablePageResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/pushcustomerenable/page")
	BaseResponse<PushCustomerEnablePageResponse> page(@RequestBody @Valid PushCustomerEnablePageRequest pushCustomerEnablePageReq);

	/**
	 * 列表查询会员推送通知开关API
	 *
	 * @author Bob
	 * @param pushCustomerEnableListReq 列表请求参数和筛选对象 {@link PushCustomerEnableListRequest}
	 * @return 会员推送通知开关的列表信息 {@link PushCustomerEnableListResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/pushcustomerenable/list")
	BaseResponse<PushCustomerEnableListResponse> list(@RequestBody @Valid PushCustomerEnableListRequest pushCustomerEnableListReq);

	/**
	 * 单个查询会员推送通知开关API
	 *
	 * @author Bob
	 * @param pushCustomerEnableByIdRequest 单个查询会员推送通知开关请求参数 {@link PushCustomerEnableByIdRequest}
	 * @return 会员推送通知开关详情 {@link PushCustomerEnableByIdResponse}
	 */
	@PostMapping("/sms/${application.sms.version}/pushcustomerenable/get-by-id")
	BaseResponse<PushCustomerEnableByIdResponse> getById(@RequestBody @Valid PushCustomerEnableByIdRequest pushCustomerEnableByIdRequest);

}

