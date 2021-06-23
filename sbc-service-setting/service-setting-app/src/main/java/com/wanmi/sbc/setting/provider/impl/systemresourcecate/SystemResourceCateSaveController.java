package com.wanmi.sbc.setting.provider.impl.systemresourcecate;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.systemresourcecate.SystemResourceCateSaveProvider;
import com.wanmi.sbc.setting.api.request.systemresourcecate.SystemResourceCateAddRequest;
import com.wanmi.sbc.setting.api.response.systemresourcecate.SystemResourceCateAddResponse;
import com.wanmi.sbc.setting.api.request.systemresourcecate.SystemResourceCateModifyRequest;
import com.wanmi.sbc.setting.api.response.systemresourcecate.SystemResourceCateModifyResponse;
import com.wanmi.sbc.setting.api.request.systemresourcecate.SystemResourceCateDelByIdRequest;
import com.wanmi.sbc.setting.api.request.systemresourcecate.SystemResourceCateDelByIdListRequest;
import com.wanmi.sbc.setting.systemresourcecate.service.SystemResourceCateService;
import com.wanmi.sbc.setting.systemresourcecate.model.root.SystemResourceCate;
import javax.validation.Valid;

/**
 * <p>平台素材资源分类保存服务接口实现</p>
 * @author lq
 * @date 2019-11-05 16:14:55
 */
@RestController
@Validated
public class SystemResourceCateSaveController implements SystemResourceCateSaveProvider {
	@Autowired
	private SystemResourceCateService systemResourceCateService;

	@Override
	public BaseResponse<SystemResourceCateAddResponse> add(@RequestBody @Valid SystemResourceCateAddRequest systemResourceCateAddRequest) {
		SystemResourceCate systemResourceCate = new SystemResourceCate();
		KsBeanUtil.copyPropertiesThird(systemResourceCateAddRequest, systemResourceCate);
		return BaseResponse.success(new SystemResourceCateAddResponse(
				systemResourceCateService.wrapperVo(systemResourceCateService.add(systemResourceCate))));
	}

	@Override
	public BaseResponse<SystemResourceCateModifyResponse> modify(@RequestBody @Valid SystemResourceCateModifyRequest systemResourceCateModifyRequest) {
		SystemResourceCate systemResourceCate = new SystemResourceCate();
		KsBeanUtil.copyPropertiesThird(systemResourceCateModifyRequest, systemResourceCate);
		return BaseResponse.success(new SystemResourceCateModifyResponse(
				systemResourceCateService.wrapperVo(systemResourceCateService.edit(systemResourceCate))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid SystemResourceCateDelByIdRequest systemResourceCateDelByIdRequest) {
		systemResourceCateService.delete(systemResourceCateDelByIdRequest.getCateId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid SystemResourceCateDelByIdListRequest systemResourceCateDelByIdListRequest) {
		systemResourceCateService.deleteByIdList(systemResourceCateDelByIdListRequest.getCateIdList());
		return BaseResponse.SUCCESSFUL();
	}

}

