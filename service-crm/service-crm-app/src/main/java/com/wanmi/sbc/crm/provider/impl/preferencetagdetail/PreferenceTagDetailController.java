package com.wanmi.sbc.crm.provider.impl.preferencetagdetail;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.api.provider.preferencetagdetail.PreferenceTagDetailProvider;
import com.wanmi.sbc.crm.api.request.preferencetagdetail.PreferenceTagDetailAddRequest;
import com.wanmi.sbc.crm.api.response.preferencetagdetail.PreferenceTagDetailAddResponse;
import com.wanmi.sbc.crm.api.request.preferencetagdetail.PreferenceTagDetailModifyRequest;
import com.wanmi.sbc.crm.api.response.preferencetagdetail.PreferenceTagDetailModifyResponse;
import com.wanmi.sbc.crm.api.request.preferencetagdetail.PreferenceTagDetailDelByIdRequest;
import com.wanmi.sbc.crm.api.request.preferencetagdetail.PreferenceTagDetailDelByIdListRequest;
import com.wanmi.sbc.crm.preferencetagdetail.service.PreferenceTagDetailService;
import com.wanmi.sbc.crm.preferencetagdetail.model.root.PreferenceTagDetail;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

/**
 * <p>偏好标签明细保存服务接口实现</p>
 * @author dyt
 * @date 2020-03-11 14:58:07
 */
@RestController
@Validated
public class PreferenceTagDetailController implements PreferenceTagDetailProvider {
	@Autowired
	private PreferenceTagDetailService preferenceTagDetailService;

	@Override
	public BaseResponse<PreferenceTagDetailAddResponse> add(@RequestBody @Valid PreferenceTagDetailAddRequest preferenceTagDetailAddRequest) {
		PreferenceTagDetail preferenceTagDetail = KsBeanUtil.convert(preferenceTagDetailAddRequest, PreferenceTagDetail.class);
		return BaseResponse.success(new PreferenceTagDetailAddResponse(
				preferenceTagDetailService.wrapperVo(preferenceTagDetailService.add(preferenceTagDetail))));
	}

	@Override
	public BaseResponse<PreferenceTagDetailModifyResponse> modify(@RequestBody @Valid PreferenceTagDetailModifyRequest preferenceTagDetailModifyRequest) {
		PreferenceTagDetail preferenceTagDetail = KsBeanUtil.convert(preferenceTagDetailModifyRequest, PreferenceTagDetail.class);
		return BaseResponse.success(new PreferenceTagDetailModifyResponse(
				preferenceTagDetailService.wrapperVo(preferenceTagDetailService.modify(preferenceTagDetail))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid PreferenceTagDetailDelByIdRequest preferenceTagDetailDelByIdRequest) {
		preferenceTagDetailService.deleteById(preferenceTagDetailDelByIdRequest.getId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid PreferenceTagDetailDelByIdListRequest preferenceTagDetailDelByIdListRequest) {
		preferenceTagDetailService.deleteByIdList(preferenceTagDetailDelByIdListRequest.getIdList());
		return BaseResponse.SUCCESSFUL();
	}

}

