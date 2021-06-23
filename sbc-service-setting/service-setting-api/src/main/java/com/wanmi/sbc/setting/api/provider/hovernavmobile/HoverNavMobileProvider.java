package com.wanmi.sbc.setting.api.provider.hovernavmobile;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.hovernavmobile.HoverNavMobileModifyRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>移动端悬浮导航栏保存服务Provider</p>
 * @author dyt
 * @date 2020-04-29 14:28:21
 */
@FeignClient(value = "${application.setting.name}", contextId = "HoverNavMobileProvider")
public interface HoverNavMobileProvider {

	/**
	 * 修改移动端悬浮导航栏API
	 *
	 * @author dyt
	 * @param hoverNavMobileModifyRequest 移动端悬浮导航栏修改参数结构 {@link HoverNavMobileModifyRequest}
	 * @return 修改结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/hover-nav-mobile/modify")
	BaseResponse modify(@RequestBody @Valid HoverNavMobileModifyRequest hoverNavMobileModifyRequest);
}

