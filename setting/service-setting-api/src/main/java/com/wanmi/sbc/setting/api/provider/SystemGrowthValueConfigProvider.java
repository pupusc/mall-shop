package com.wanmi.sbc.setting.api.provider;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.SystemGrowthValueConfigModifyRequest;
import com.wanmi.sbc.setting.api.request.SystemGrowthValueStatusModifyRequest;
import com.wanmi.sbc.setting.api.request.SystemPointsConfigModifyRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>系统成长值设置保存服务Provider</p>
 * @author yxz
 * @date 2019-04-03 11:43:28
 */
@FeignClient(value = "${application.setting.name}", contextId = "SystemGrowthValueConfigProvider")
public interface SystemGrowthValueConfigProvider {

	/**
	 * 修改成长值设置API
	 *
	 * @author yxz
	 * @param systemGrowthValueConfigModifyRequest 成长值设置修改参数结构 {@link SystemPointsConfigModifyRequest}
	 */
	@PostMapping("/setting/${application.setting.version}/system-growth-value-config/modify-system-growth-value-config")
	BaseResponse modifySystemGrowthValueConfig(@RequestBody @Valid SystemGrowthValueConfigModifyRequest systemGrowthValueConfigModifyRequest);

	/**
	 * 修改成长值开关API
	 *
	 * @author yxz
	 * @param systemGrowthValueStatusModifyRequest 成长值设置修改参数结构 {@link SystemGrowthValueStatusModifyRequest}
	 */
	@PostMapping("/setting/${application.setting.version}/system-growth-value-config/modify-system-growth-value-status")
	BaseResponse modifySystemGrowthValueStatus(@RequestBody @Valid SystemGrowthValueStatusModifyRequest systemGrowthValueStatusModifyRequest);
}

