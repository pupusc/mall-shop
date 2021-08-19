package com.wanmi.sbc.setting.api.provider.platformaddress;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.platformaddress.PlatformAddressAddRequest;
import com.wanmi.sbc.setting.api.request.platformaddress.PlatformAddressDelByIdListRequest;
import com.wanmi.sbc.setting.api.request.platformaddress.PlatformAddressDelByIdRequest;
import com.wanmi.sbc.setting.api.request.platformaddress.PlatformAddressModifyRequest;
import com.wanmi.sbc.setting.api.response.platformaddress.PlatformAddressAddResponse;
import com.wanmi.sbc.setting.api.response.platformaddress.PlatformAddressModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>平台地址信息保存服务Provider</p>
 * @author dyt
 * @date 2020-03-30 14:39:57
 */
@FeignClient(value = "${application.setting.name}", contextId = "PlatformAddressProvider")
public interface PlatformAddressSaveProvider {

	/**
	 * 新增平台地址信息API
	 *
	 * @author dyt
	 * @param platformAddressAddRequest 平台地址信息新增参数结构 {@link PlatformAddressAddRequest}
	 * @return 新增的平台地址信息信息 {@link PlatformAddressAddResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/platformaddress/add")
    BaseResponse<PlatformAddressAddResponse> add(@RequestBody @Valid PlatformAddressAddRequest
                                                         platformAddressAddRequest);

	/**
	 * 修改平台地址信息API
	 *
	 * @author dyt
	 * @param platformAddressModifyRequest 平台地址信息修改参数结构 {@link PlatformAddressModifyRequest}
	 * @return 修改的平台地址信息信息 {@link PlatformAddressModifyResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/platformaddress/modify")
    BaseResponse<PlatformAddressModifyResponse> modify(@RequestBody @Valid PlatformAddressModifyRequest platformAddressModifyRequest);

	/**
	 * 单个删除平台地址信息API
	 *
	 * @author dyt
	 * @param platformAddressDelByIdRequest 单个删除参数结构 {@link PlatformAddressDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/platformaddress/delete-by-id")
    BaseResponse deleteById(@RequestBody @Valid PlatformAddressDelByIdRequest platformAddressDelByIdRequest);

	/**
	 * 批量删除平台地址信息API
	 *
	 * @author dyt
	 * @param platformAddressDelByIdListRequest 批量删除参数结构 {@link PlatformAddressDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/platformaddress/delete-by-id-list")
    BaseResponse deleteByIdList(@RequestBody @Valid PlatformAddressDelByIdListRequest platformAddressDelByIdListRequest);

}

