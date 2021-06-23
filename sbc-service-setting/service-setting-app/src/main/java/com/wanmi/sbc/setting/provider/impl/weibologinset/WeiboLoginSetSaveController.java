package com.wanmi.sbc.setting.provider.impl.weibologinset;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.weibologinset.WeiboLoginSetSaveProvider;
import com.wanmi.sbc.setting.api.request.weibologinset.WeiboLoginSetAddRequest;
import com.wanmi.sbc.setting.api.response.weibologinset.WeiboLoginSetAddResponse;
import com.wanmi.sbc.setting.api.request.weibologinset.WeiboLoginSetModifyRequest;
import com.wanmi.sbc.setting.api.response.weibologinset.WeiboLoginSetModifyResponse;
import com.wanmi.sbc.setting.api.request.weibologinset.WeiboLoginSetDelByIdRequest;
import com.wanmi.sbc.setting.api.request.weibologinset.WeiboLoginSetDelByIdListRequest;
import com.wanmi.sbc.setting.weibologinset.service.WeiboLoginSetService;
import com.wanmi.sbc.setting.weibologinset.model.root.WeiboLoginSet;
import javax.validation.Valid;

/**
 * <p>微信登录配置保存服务接口实现</p>
 * @author lq
 * @date 2019-11-05 16:17:06
 */
@RestController
@Validated
public class WeiboLoginSetSaveController implements WeiboLoginSetSaveProvider {
	@Autowired
	private WeiboLoginSetService weiboLoginSetService;

	@Override
	public BaseResponse<WeiboLoginSetAddResponse> add(@RequestBody @Valid WeiboLoginSetAddRequest weiboLoginSetAddRequest) {
		WeiboLoginSet weiboLoginSet = new WeiboLoginSet();
		KsBeanUtil.copyPropertiesThird(weiboLoginSetAddRequest, weiboLoginSet);
		return BaseResponse.success(new WeiboLoginSetAddResponse(
				weiboLoginSetService.wrapperVo(weiboLoginSetService.add(weiboLoginSet))));
	}

	@Override
	public BaseResponse<WeiboLoginSetModifyResponse> modify(@RequestBody @Valid WeiboLoginSetModifyRequest weiboLoginSetModifyRequest) {
		WeiboLoginSet weiboLoginSet = new WeiboLoginSet();
		KsBeanUtil.copyPropertiesThird(weiboLoginSetModifyRequest, weiboLoginSet);
		return BaseResponse.success(new WeiboLoginSetModifyResponse(
				weiboLoginSetService.wrapperVo(weiboLoginSetService.modify(weiboLoginSet))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid WeiboLoginSetDelByIdRequest weiboLoginSetDelByIdRequest) {
		weiboLoginSetService.deleteById(weiboLoginSetDelByIdRequest.getWeiboSetId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid WeiboLoginSetDelByIdListRequest weiboLoginSetDelByIdListRequest) {
		weiboLoginSetService.deleteByIdList(weiboLoginSetDelByIdListRequest.getWeiboSetIdList());
		return BaseResponse.SUCCESSFUL();
	}

}

