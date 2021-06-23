package com.wanmi.sbc.crm.api.provider.autotagOther;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.request.autotagother.AutoTagOtherPageRequest;
import com.wanmi.sbc.crm.api.response.autotagother.AutotagOtherPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(value = "${application.crm.name}", contextId = "AutoTagOtherQueryProvider")
public interface AutoTagOtherQueryProvider {

	/**
	 * 分页查询自动标签API
	 * @param autoTagOtherPageRequest 分页请求参数和筛选对象 {@link AutoTagOtherPageRequest}
	 * @return 自动标签分页列表信息 {@link AutotagOtherPageResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/autotagother/page")
	BaseResponse<AutotagOtherPageResponse> page(@RequestBody @Valid AutoTagOtherPageRequest autoTagOtherPageRequest);

}

