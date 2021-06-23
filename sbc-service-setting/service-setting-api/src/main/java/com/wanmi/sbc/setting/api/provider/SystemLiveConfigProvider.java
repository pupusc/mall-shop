package com.wanmi.sbc.setting.api.provider;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.SystemGrowthValueStatusModifyRequest;
import com.wanmi.sbc.setting.api.request.SystemLiveStatusModifyRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>小程序直播设置保存服务Provider</p>
 * @author zwb
 * @date 2020-06-19 16:11:36
 */
@FeignClient(value = "${application.setting.name}", contextId = "SystemLiveConfigProvider")
public interface SystemLiveConfigProvider {


	/**
	 * 修改直播值开关API
	 *
	 * @author zwb
	 * @param systemLiveStatusModifyRequest  {@link SystemGrowthValueStatusModifyRequest}
	 */
	@PostMapping("/setting/${application.setting.version}/system-live-config/modify-system-live-status")
	BaseResponse modifySystemLiveStatus(@RequestBody @Valid SystemLiveStatusModifyRequest systemLiveStatusModifyRequest);

}

