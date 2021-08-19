package com.wanmi.sbc.setting.api.provider.platformaddress;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.platformaddress.PlatformAddressByIdRequest;
import com.wanmi.sbc.setting.api.request.platformaddress.PlatformAddressListRequest;
import com.wanmi.sbc.setting.api.request.platformaddress.PlatformAddressPageRequest;
import com.wanmi.sbc.setting.api.request.platformaddress.PlatformAddressVerifyRequest;
import com.wanmi.sbc.setting.api.response.platformaddress.PlatformAddressByIdResponse;
import com.wanmi.sbc.setting.api.response.platformaddress.PlatformAddressListResponse;
import com.wanmi.sbc.setting.api.response.platformaddress.PlatformAddressPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>平台地址信息查询服务Provider</p>
 * @author dyt
 * @date 2020-03-30 14:39:57
 */
@FeignClient(value = "${application.setting.name}", contextId = "PlatformAddressQueryProvider")
public interface PlatformAddressQueryProvider {

	/**
	 * 分页查询平台地址信息API
	 *
	 * @author dyt
	 * @param platformAddressPageReq 分页请求参数和筛选对象 {@link PlatformAddressPageRequest}
	 * @return 平台地址信息分页列表信息 {@link PlatformAddressPageResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/platformaddress/page")
    BaseResponse<PlatformAddressPageResponse> page(@RequestBody @Valid PlatformAddressPageRequest
                                                           platformAddressPageReq);

	/**
	 * 列表查询平台地址信息API
	 *
	 * @author dyt
	 * @param platformAddressListReq 列表请求参数和筛选对象 {@link PlatformAddressListRequest}
	 * @return 平台地址信息的列表信息 {@link PlatformAddressListResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/platformaddress/list")
    BaseResponse<PlatformAddressListResponse> list(@RequestBody @Valid PlatformAddressListRequest platformAddressListReq);

	/**
	 * 单个查询平台地址信息API
	 *
	 * @author dyt
	 * @param platformAddressByIdRequest 单个查询平台地址信息请求参数 {@link PlatformAddressByIdRequest}
	 * @return 平台地址信息详情 {@link PlatformAddressByIdResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/platformaddress/get-by-id")
    BaseResponse<PlatformAddressByIdResponse> getById(@RequestBody @Valid PlatformAddressByIdRequest platformAddressByIdRequest);

	/**
	 * 列表查询平台地址信息API
	 *
	 * @author dyt
	 * @param platformAddressListReq 列表请求参数和筛选对象 {@link PlatformAddressListRequest}
	 * @return 平台地址信息的列表信息 {@link PlatformAddressListResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/platformaddress/list-province-or-city")
    BaseResponse<PlatformAddressListResponse> provinceOrCitylist(@RequestBody @Valid PlatformAddressListRequest platformAddressListReq);

	/**
	 * 校验是否需要完善地址信息API
	 *
	 * @author yhy
	 * @param platformAddressVerifyRequest 需要校验的地址信息 {@link PlatformAddressVerifyRequest}
	 * @return
	 */
	@PostMapping("/setting/${application.setting.version}/platformaddress/verify-address")
	BaseResponse<Boolean> verifyAddress(@RequestBody @Valid PlatformAddressVerifyRequest platformAddressVerifyRequest);
}

