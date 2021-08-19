package com.wanmi.sbc.setting.provider.impl.storeresource;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.storeresource.StoreResourceSaveProvider;
import com.wanmi.sbc.setting.api.request.storeresource.*;
import com.wanmi.sbc.setting.api.response.storeresource.StoreResourceAddResponse;
import com.wanmi.sbc.setting.api.response.storeresource.StoreResourceModifyResponse;
import com.wanmi.sbc.setting.storeresource.model.root.StoreResource;
import com.wanmi.sbc.setting.storeresource.service.StoreResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>店铺资源库保存服务接口实现</p>
 * @author lq
 * @date 2019-11-05 16:12:49
 */
@RestController
@Validated
public class StoreResourceSaveController implements StoreResourceSaveProvider {
	@Autowired
	private StoreResourceService storeResourceService;

	@Override
	public BaseResponse<StoreResourceAddResponse> add(@RequestBody @Valid StoreResourceAddRequest storeResourceAddRequest) {
		StoreResource storeResource = new StoreResource();
		KsBeanUtil.copyPropertiesThird(storeResourceAddRequest, storeResource);
		return BaseResponse.success(new StoreResourceAddResponse(
				storeResourceService.wrapperVo(storeResourceService.add(storeResource))));
	}

	@Override
	public BaseResponse<StoreResourceModifyResponse> modify(@RequestBody @Valid StoreResourceModifyRequest storeResourceModifyRequest) {
		StoreResource storeResource = new StoreResource();
		KsBeanUtil.copyPropertiesThird(storeResourceModifyRequest, storeResource);
		return BaseResponse.success(new StoreResourceModifyResponse(
				storeResourceService.wrapperVo(storeResourceService.modify(storeResource))));
	}


	@Override
	public BaseResponse move(@RequestBody @Valid StoreResourceMoveRequest
									 moveRequest) {
		storeResourceService.updateCateByIds(moveRequest.getCateId(), moveRequest.getResourceIds(),moveRequest.getStoreId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid StoreResourceDelByIdRequest storeResourceDelByIdRequest) {
		storeResourceService.deleteById(storeResourceDelByIdRequest.getResourceId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid StoreResourceDelByIdListRequest storeResourceDelByIdListRequest) {
		storeResourceService.delete(storeResourceDelByIdListRequest.getResourceIds(),
				storeResourceDelByIdListRequest.getStoreId());
		return BaseResponse.SUCCESSFUL();
	}

}

