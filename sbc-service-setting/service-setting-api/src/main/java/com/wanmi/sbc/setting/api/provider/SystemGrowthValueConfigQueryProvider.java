package com.wanmi.sbc.setting.api.provider;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.response.SystemGrowthValueConfigQueryResponse;
import com.wanmi.sbc.setting.api.response.SystemGrowthValueOpenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * <p>系统成长值设置查询服务Provider</p>
 * @author yxz
 * @date 2019-04-03 11:43:28
 */
@FeignClient(value = "${application.setting.name}", contextId = "SystemGrowthValueConfigQueryProvider")
public interface SystemGrowthValueConfigQueryProvider {

	/**
	 * 查询成长值设置API
	 */
	@PostMapping("/setting/${application.setting.version}/system-growth-value-config/query-system-growth-value-config")
	BaseResponse<SystemGrowthValueConfigQueryResponse> querySystemGrowthValueConfig();

	/**
	 * 查询成长值开关
	 *
	 * @return {@link SystemGrowthValueOpenResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/system-growth-value-config/is-growth-value-open")
	BaseResponse<SystemGrowthValueOpenResponse> isGrowthValueOpen();
}

