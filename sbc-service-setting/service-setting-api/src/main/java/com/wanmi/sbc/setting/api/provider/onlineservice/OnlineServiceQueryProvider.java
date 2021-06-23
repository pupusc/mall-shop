package com.wanmi.sbc.setting.api.provider.onlineservice;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.onlineservice.OnlineServiceListRequest;
import com.wanmi.sbc.setting.api.response.onlineservice.OnlineServiceListResponse;
import com.wanmi.sbc.setting.api.request.onlineservice.OnlineServiceByIdRequest;
import com.wanmi.sbc.setting.api.response.onlineservice.OnlineServiceByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>onlineService查询服务Provider</p>
 * @author lq
 * @date 2019-11-05 16:10:28
 */
@FeignClient(value = "${application.setting.name}", contextId = "OnlineServiceQueryProvider")
public interface OnlineServiceQueryProvider {

	/**
	 * 座席列表查询onlineServiceAPI
	 *
	 * @author lq
	 * @param onlineServiceListReq 座席列表请求参数和筛选对象 {@link OnlineServiceListRequest}
	 * @return onlineService座席的列表信息 {@link OnlineServiceListResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/onlineservice/list")
	BaseResponse<OnlineServiceListResponse> list(@RequestBody @Valid OnlineServiceListRequest onlineServiceListReq);

	/**
	 * 单个查询onlineServiceAPI
	 *
	 * @author lq
	 * @param onlineServiceByIdRequest 单个查询onlineService请求参数 {@link OnlineServiceByIdRequest}
	 * @return onlineService详情 {@link OnlineServiceByIdResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/onlineservice/get-by-id")
	BaseResponse<OnlineServiceByIdResponse> getById(@RequestBody @Valid OnlineServiceByIdRequest
                                                            onlineServiceByIdRequest);

}

