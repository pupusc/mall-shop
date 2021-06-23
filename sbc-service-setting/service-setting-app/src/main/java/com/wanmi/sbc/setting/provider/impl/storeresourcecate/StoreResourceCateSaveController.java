package com.wanmi.sbc.setting.provider.impl.storeresourcecate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.storeresourcecate.StoreResourceCateSaveProvider;
import com.wanmi.sbc.setting.api.request.storeresourcecate.*;
import com.wanmi.sbc.setting.api.response.storeresourcecate.StoreResourceCateAddResponse;
import com.wanmi.sbc.setting.api.response.storeresourcecate.StoreResourceCateModifyResponse;
import com.wanmi.sbc.setting.storeresourcecate.model.root.StoreResourceCate;
import com.wanmi.sbc.setting.storeresourcecate.service.StoreResourceCateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>店铺资源资源分类表保存服务接口实现</p>
 * @author lq
 * @date 2019-11-05 16:13:19
 */
@RestController
@Validated
public class StoreResourceCateSaveController implements StoreResourceCateSaveProvider {
	@Autowired
	private StoreResourceCateService storeResourceCateService;

	@Override
	public BaseResponse<StoreResourceCateAddResponse> add(@RequestBody @Valid StoreResourceCateAddRequest storeResourceCateAddRequest) {
		StoreResourceCate storeResourceCate = new StoreResourceCate();
		KsBeanUtil.copyPropertiesThird(storeResourceCateAddRequest, storeResourceCate);
		return BaseResponse.success(new StoreResourceCateAddResponse(
				storeResourceCateService.wrapperVo(storeResourceCateService.add(storeResourceCate))));
	}

	@Override
	public BaseResponse<StoreResourceCateModifyResponse> modify(@RequestBody @Valid StoreResourceCateModifyRequest storeResourceCateModifyRequest) {
		StoreResourceCate storeResourceCate = new StoreResourceCate();
		KsBeanUtil.copyPropertiesThird(storeResourceCateModifyRequest, storeResourceCate);
		return BaseResponse.success(new StoreResourceCateModifyResponse(
				storeResourceCateService.wrapperVo(storeResourceCateService.modify(storeResourceCate))));
	}

	@Override
	public BaseResponse delete(@RequestBody @Valid StoreResourceCateDelByIdRequest storeResourceCateDelByIdRequest) {
		storeResourceCateService.delete(storeResourceCateDelByIdRequest.getCateId(),storeResourceCateDelByIdRequest.getStoreId());
		return BaseResponse.SUCCESSFUL();
	}


	@Override
	public BaseResponse init(@RequestBody @Valid StoreResourceCateInitRequest storeResourceCate) {
		storeResourceCateService.init(storeResourceCate);
		return BaseResponse.SUCCESSFUL();
	}



}

