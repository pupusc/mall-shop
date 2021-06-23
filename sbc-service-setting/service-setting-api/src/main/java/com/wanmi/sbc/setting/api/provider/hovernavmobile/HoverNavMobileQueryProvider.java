package com.wanmi.sbc.setting.api.provider.hovernavmobile;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.hovernavmobile.HoverNavMobileByIdRequest;
import com.wanmi.sbc.setting.api.response.hovernavmobile.HoverNavMobileByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>移动端悬浮导航栏查询服务Provider</p>
 * @author dyt
 * @date 2020-04-29 14:28:21
 */
@FeignClient(value = "${application.setting.name}", contextId = "HoverNavMobileQueryProvider")
public interface HoverNavMobileQueryProvider {

	/**
	 * 单个查询移动端悬浮导航栏API
	 *
	 * @author dyt
	 * @param hoverNavMobileByIdRequest 单个查询移动端悬浮导航栏请求参数 {@link HoverNavMobileByIdRequest}
	 * @return 移动端悬浮导航栏详情 {@link HoverNavMobileByIdResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/hover-nav-mobile/get-by-id")
	BaseResponse<HoverNavMobileByIdResponse> getById(@RequestBody @Valid HoverNavMobileByIdRequest hoverNavMobileByIdRequest);

}

