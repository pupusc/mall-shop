package com.wanmi.sbc.setting.api.provider.push;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.push.AppPushConfigByIdRequest;
import com.wanmi.sbc.setting.api.response.push.AppPushConfigByIdResponse;
import com.wanmi.sbc.setting.api.response.push.AppPushConfigDetailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>消息推送查询服务Provider</p>
 * @author chenyufei
 * @date 2019-05-10 14:39:59
 */
@FeignClient(value = "${application.setting.name}", contextId = "AppPushConfigQueryProvider")
public interface AppPushConfigQueryProvider {

	/**
	 * 单个查询消息推送API
	 *
	 * @author chenyufei
	 * @param appPushConfigByIdRequest 单个查询消息推送请求参数 {@link AppPushConfigByIdRequest}
	 * @return 消息推送详情 {@link AppPushConfigByIdResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/apppushconfig/get-by-id")
	BaseResponse<AppPushConfigByIdResponse> getById(@RequestBody @Valid AppPushConfigByIdRequest appPushConfigByIdRequest);


	/**
	 * 查询友盟推送API
	 *
	 * @author chenyufei
	 * @return 消息推送详情
	 */
	@PostMapping("/setting/${application.setting.version}/apppushconfig/detail")
	BaseResponse<AppPushConfigDetailResponse> detail();

}

