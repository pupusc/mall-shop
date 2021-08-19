package com.wanmi.sbc.crm.api.provider.autotagpreference;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.request.autotagpreference.AutoPreferencePageRequest;
import com.wanmi.sbc.crm.api.response.autotagpreference.AutotagPreferencePageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(value = "${application.crm.name}", contextId = "AutotagPreferenceQueryProvider")
public interface AutotagPreferenceQueryProvider {

	/**
	 * 分页查询标签明细API
	 * @param request 分页请求参数和筛选对象 {@link AutoPreferencePageRequest}
	 * @return 标签明细分页列表信息 {@link AutotagPreferencePageResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/autotagPreference/page")
	BaseResponse<AutotagPreferencePageResponse> page(@RequestBody @Valid AutoPreferencePageRequest request);
}

