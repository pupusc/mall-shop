package com.wanmi.sbc.setting.api.provider.thirdplatformconfig;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.thirdplatformconfig.ThirdPlatformConfigByTypeRequest;
import com.wanmi.sbc.setting.api.response.thirdplatformconfig.ThirdPlatformConfigQueryResponse;
import com.wanmi.sbc.setting.api.response.thirdplatformconfig.ThirdPlatformConfigResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>第三方平台配置查询服务Provider</p>
 * @author dyt
 * @date 2020-08-14 13:41:44
 */
@FeignClient(value = "${application.setting.name}", contextId = "ThirdPlatformConfigQueryProvider")
public interface ThirdPlatformConfigQueryProvider {

	/**
	 * 分页查询第三方平台配置API
	 *
	 * @author dyt
	 * @return 第三方平台配置列表信息 {@link ThirdPlatformConfigQueryResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/thirdplatformconfig/list")
	BaseResponse<ThirdPlatformConfigQueryResponse> list();

	/**
	 * 获取第三方平台配置API
	 *
	 * @author dyt
	 * @param request 请求参数 {@link ThirdPlatformConfigByTypeRequest}
	 * @return 第三方平台配置信息 {@link ThirdPlatformConfigResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/thirdplatformconfig/get-by-type")
	BaseResponse<ThirdPlatformConfigResponse> get(@RequestBody @Valid ThirdPlatformConfigByTypeRequest request);
}

