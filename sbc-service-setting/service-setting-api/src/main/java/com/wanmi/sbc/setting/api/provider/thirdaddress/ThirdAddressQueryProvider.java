package com.wanmi.sbc.setting.api.provider.thirdaddress;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.thirdaddress.ThirdAddressListRequest;
import com.wanmi.sbc.setting.api.request.thirdaddress.ThirdAddressPageRequest;
import com.wanmi.sbc.setting.api.response.thirdaddress.ThirdAddressListResponse;
import com.wanmi.sbc.setting.api.response.thirdaddress.ThirdAddressPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>第三方地址映射表查询服务Provider</p>
 * @author dyt
 * @date 2020-08-14 13:41:44
 */
@FeignClient(value = "${application.setting.name}", contextId = "ThirdAddressQueryProvider")
public interface ThirdAddressQueryProvider {

	/**
	 * 分页查询第三方地址映射表API
	 *
	 * @author dyt
	 * @param thirdAddressPageReq 分页请求参数和筛选对象 {@link ThirdAddressPageRequest}
	 * @return 第三方地址映射表分页列表信息 {@link ThirdAddressPageResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/thirdaddress/page")
	BaseResponse<ThirdAddressPageResponse> page(@RequestBody @Valid ThirdAddressPageRequest thirdAddressPageReq);

	/**
	 * 分第三方地址映射列表API
	 *
	 * @author dyt
	 * @param request 分页请求参数和筛选对象 {@link ThirdAddressListRequest}
	 * @return 第三方地址映射列表信息 {@link ThirdAddressListResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/thirdaddress/list")
	BaseResponse<ThirdAddressListResponse> list(@RequestBody @Valid ThirdAddressListRequest request);
}

