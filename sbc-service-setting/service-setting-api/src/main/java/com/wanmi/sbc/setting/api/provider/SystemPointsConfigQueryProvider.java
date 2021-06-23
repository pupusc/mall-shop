package com.wanmi.sbc.setting.api.provider;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.response.SystemPointsConfigQueryResponse;
import com.wanmi.sbc.setting.api.response.SystemPointsOpenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * <p>积分设置查询服务Provider</p>
 * @author yxz
 * @date 2019-03-28 16:24:21
 */
@FeignClient(value = "${application.setting.name}", contextId = "SystemPointsConfigQueryProvider")
public interface SystemPointsConfigQueryProvider {

	/**
	 * 查询积分设置API
	 */
	@PostMapping("/setting/${application.setting.version}/system-points-config/query-system-points-config")
	BaseResponse<SystemPointsConfigQueryResponse> querySystemPointsConfig();

	/**
	 * 查询成长值开关
	 *
	 * @return {@link SystemPointsOpenResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/system-points-config/is-points-open")
	BaseResponse<SystemPointsOpenResponse> isPointsOpen();
}

