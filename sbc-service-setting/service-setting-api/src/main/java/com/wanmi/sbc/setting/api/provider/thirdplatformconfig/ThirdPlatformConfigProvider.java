package com.wanmi.sbc.setting.api.provider.thirdplatformconfig;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.thirdplatformconfig.ThirdPlatformConfigModifyRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>第三方平台配置保存服务Provider</p>
 * @author dyt
 * @date 2020-08-14 13:41:44
 */
@FeignClient(value = "${application.setting.name}", contextId = "ThirdPlatformConfigProvider")
public interface ThirdPlatformConfigProvider {

	/**
	 * 修改第三方平台修改API
	 *
	 * @author dyt
	 * @param request 第三方平台配置修改参数结构 {@link ThirdPlatformConfigModifyRequest}
	 * @return 操作结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/thirdplatformconfig/modify")
	BaseResponse modify(@RequestBody @Valid ThirdPlatformConfigModifyRequest request);
}

