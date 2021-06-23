package com.wanmi.sbc.setting.provider.impl.thirdaddress;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.thirdaddress.ThirdAddressProvider;
import com.wanmi.sbc.setting.api.request.thirdaddress.ThirdAddressBatchMergeRequest;
import com.wanmi.sbc.setting.api.request.thirdaddress.ThirdAddressMappingRequest;
import com.wanmi.sbc.setting.api.request.thirdaddress.ThirdAddressModifyRequest;
import com.wanmi.sbc.setting.thirdaddress.service.ThirdAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>第三方地址映射表保存服务接口实现</p>
 * @author dyt
 * @date 2020-08-14 13:41:44
 */
@RestController
@Validated
public class ThirdAddressController implements ThirdAddressProvider {
	@Autowired
	private ThirdAddressService thirdAddressService;

	@Override
	public BaseResponse modify(@RequestBody @Valid ThirdAddressModifyRequest thirdAddressModifyRequest) {
		thirdAddressService.modify(thirdAddressModifyRequest);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse mapping(@RequestBody @Valid ThirdAddressMappingRequest request) {
		thirdAddressService.mapping(request.getThirdPlatformType());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse batchMerge(@RequestBody @Valid ThirdAddressBatchMergeRequest request) {
		thirdAddressService.batchMerge(request.getThirdAddressList());
		return BaseResponse.SUCCESSFUL();
	}
}

