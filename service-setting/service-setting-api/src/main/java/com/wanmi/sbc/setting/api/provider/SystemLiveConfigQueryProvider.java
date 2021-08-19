package com.wanmi.sbc.setting.api.provider;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.response.SystemGrowthValueOpenResponse;
import com.wanmi.sbc.setting.api.response.SystemLiveOpenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;


/**
 * <p>小程序直播设置查询服务Provider</p>
 * @author zwb
 * @date 2020-06-19 16:11:36
 */
@FeignClient(value = "${application.setting.name}", contextId = "SystemLiveConfigQueryProvider")
public interface SystemLiveConfigQueryProvider {


	/**
	 * 查询小程序直播开关
	 *
	 * @return {@link SystemGrowthValueOpenResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/system-live-config/isOpen")
	BaseResponse<SystemLiveOpenResponse> isLiveOpen();

}

