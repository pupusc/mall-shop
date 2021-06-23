package com.wanmi.sbc.setting.provider.impl.qqloginset;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.qqloginset.QqLoginSetSaveProvider;
import com.wanmi.sbc.setting.api.request.qqloginset.QqLoginSetAddRequest;
import com.wanmi.sbc.setting.api.response.qqloginset.QqLoginSetAddResponse;
import com.wanmi.sbc.setting.api.request.qqloginset.QqLoginSetModifyRequest;
import com.wanmi.sbc.setting.api.response.qqloginset.QqLoginSetModifyResponse;
import com.wanmi.sbc.setting.api.request.qqloginset.QqLoginSetDelByIdRequest;
import com.wanmi.sbc.setting.api.request.qqloginset.QqLoginSetDelByIdListRequest;
import com.wanmi.sbc.setting.qqloginset.service.QqLoginSetService;
import com.wanmi.sbc.setting.qqloginset.model.root.QqLoginSet;
import javax.validation.Valid;

/**
 * <p>qq登录信息保存服务接口实现</p>
 * @author lq
 * @date 2019-11-05 16:11:28
 */
@RestController
@Validated
public class QqLoginSetSaveController implements QqLoginSetSaveProvider {
	@Autowired
	private QqLoginSetService qqLoginSetService;

	@Override
	public BaseResponse<QqLoginSetAddResponse> add(@RequestBody @Valid QqLoginSetAddRequest qqLoginSetAddRequest) {
		QqLoginSet qqLoginSet = new QqLoginSet();
		KsBeanUtil.copyPropertiesThird(qqLoginSetAddRequest, qqLoginSet);
		return BaseResponse.success(new QqLoginSetAddResponse(
				qqLoginSetService.wrapperVo(qqLoginSetService.add(qqLoginSet))));
	}

	@Override
	public BaseResponse<QqLoginSetModifyResponse> modify(@RequestBody @Valid QqLoginSetModifyRequest qqLoginSetModifyRequest) {
		QqLoginSet qqLoginSet = new QqLoginSet();
		KsBeanUtil.copyPropertiesThird(qqLoginSetModifyRequest, qqLoginSet);
		return BaseResponse.success(new QqLoginSetModifyResponse(
				qqLoginSetService.wrapperVo(qqLoginSetService.modify(qqLoginSet))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid QqLoginSetDelByIdRequest qqLoginSetDelByIdRequest) {
		qqLoginSetService.deleteById(qqLoginSetDelByIdRequest.getQqSetId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid QqLoginSetDelByIdListRequest qqLoginSetDelByIdListRequest) {
		qqLoginSetService.deleteByIdList(qqLoginSetDelByIdListRequest.getQqSetIdList());
		return BaseResponse.SUCCESSFUL();
	}

}

