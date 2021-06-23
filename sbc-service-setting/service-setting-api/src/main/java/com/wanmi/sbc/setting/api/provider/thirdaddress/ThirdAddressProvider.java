package com.wanmi.sbc.setting.api.provider.thirdaddress;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.thirdaddress.ThirdAddressBatchMergeRequest;
import com.wanmi.sbc.setting.api.request.thirdaddress.ThirdAddressMappingRequest;
import com.wanmi.sbc.setting.api.request.thirdaddress.ThirdAddressModifyRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>第三方地址映射表保存服务Provider</p>
 * @author dyt
 * @date 2020-08-14 13:41:44
 */
@FeignClient(value = "${application.setting.name}", contextId = "ThirdAddressProvider")
public interface ThirdAddressProvider {

	/**
	 * 修改第三方地址映射表API
	 *
	 * @author dyt
	 * @param thirdAddressModifyRequest 第三方地址映射表修改参数结构 {@link ThirdAddressModifyRequest}
	 * @return 操作结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/thirdaddress/modify")
	BaseResponse modify(@RequestBody @Valid ThirdAddressModifyRequest thirdAddressModifyRequest);

	/**
	 * 映射第三方地址映射表API
	 *
	 * @author dyt
	 * @return 操作结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/thirdaddress/mapping-by-type")
	BaseResponse mapping(@RequestBody @Valid ThirdAddressMappingRequest request);

	/**
	 * 初始化第三方地址映射表API
	 *
	 * @author dyt
	 * @return 操作结果 {@link BaseResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/thirdaddress/batch-merge")
	BaseResponse batchMerge(@RequestBody @Valid ThirdAddressBatchMergeRequest request);
}

