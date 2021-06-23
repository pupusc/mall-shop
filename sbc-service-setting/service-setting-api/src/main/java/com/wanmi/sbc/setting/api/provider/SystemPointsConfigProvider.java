package com.wanmi.sbc.setting.api.provider;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.SystemPointsConfigModifyRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>积分设置保存服务Provider</p>
 * @author yxz
 * @date 2019-03-28 16:24:21
 */
@FeignClient(value = "${application.setting.name}", contextId = "SystemPointsConfigProvider")
public interface SystemPointsConfigProvider {

	/**
	 * 修改积分设置API
	 *
	 * @author yxz
	 * @param systemPointsConfigModifyRequest 积分设置修改参数结构 {@link SystemPointsConfigModifyRequest}
	 */
	@PostMapping("/setting/${application.setting.version}/system-points-config/modify-system-points-config")
	BaseResponse modifySystemPointsConfig(@RequestBody @Valid SystemPointsConfigModifyRequest systemPointsConfigModifyRequest);

}

