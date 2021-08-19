package com.wanmi.sbc.setting.provider.impl.wechatloginset;

import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.setting.api.request.wechatloginset.WechatLoginSetByStoreIdRequest;
import com.wanmi.sbc.setting.api.request.wechatloginset.WechatServiceStatusByStoreIdRequest;
import com.wanmi.sbc.setting.api.response.wechatloginset.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.wechatloginset.WechatLoginSetQueryProvider;
import com.wanmi.sbc.setting.api.request.wechatloginset.WechatLoginSetByIdRequest;
import com.wanmi.sbc.setting.wechatloginset.service.WechatLoginSetService;
import com.wanmi.sbc.setting.wechatloginset.model.root.WechatLoginSet;
import javax.validation.Valid;

/**
 * <p>微信授权登录配置查询服务接口实现</p>
 * @author lq
 * @date 2019-11-05 16:15:25
 */
@RestController
@Validated
public class WechatLoginSetQueryController implements WechatLoginSetQueryProvider {
	@Autowired
	private WechatLoginSetService wechatLoginSetService;

	@Override
	public BaseResponse<WechatLoginSetByIdResponse> getById(@RequestBody @Valid WechatLoginSetByIdRequest wechatLoginSetByIdRequest) {
		WechatLoginSet wechatLoginSet = wechatLoginSetService.getById(wechatLoginSetByIdRequest.getWechatSetId());
		return BaseResponse.success(new WechatLoginSetByIdResponse(wechatLoginSetService.wrapperVo(wechatLoginSet)));
	}

	/**
	 * 单个查询微信授权登录配置API
	 *
	 * @return 微信授权登录配置详情 {@link WechatLoginSetByIdResponse}
	 * @author lq
	 */
	@Override
	public BaseResponse<WechatLoginSetResponse> getInfo() {
		WechatLoginSet wechatLoginSet = wechatLoginSetService.getLoginSet(WechatLoginSet.builder()
				.storeId(Constants.BOSS_DEFAULT_STORE_ID)
				.build());
		return BaseResponse.success(wechatLoginSetService.wrapperInfoVo(wechatLoginSet));
	}

	@Override
	public BaseResponse<WechatLoginSetResponse> getInfoByStoreId(@RequestBody @Valid WechatLoginSetByStoreIdRequest wechatLoginSetByStoreIdRequest) {
		WechatLoginSet wechatLoginSet = wechatLoginSetService.getLoginSetByStoreId(wechatLoginSetByStoreIdRequest.getStoreId());
		return BaseResponse.success(wechatLoginSetService.wrapperInfoVo(wechatLoginSet));
	}

	@Override
	public BaseResponse<WechatLoginSetServerStatusResponse> getWechatServerStatus() {
		WechatLoginSet wechatLoginSet = wechatLoginSetService.getLoginSet(WechatLoginSet.builder()
				.storeId(Constants.BOSS_DEFAULT_STORE_ID)
				.build());
		return BaseResponse.success(WechatLoginSetServerStatusResponse.builder()
				.mobileStatus(wechatLoginSet.getMobileServerStatus())
				.appStatus(wechatLoginSet.getAppServerStatus())
				.pcStatus(wechatLoginSet.getPcServerStatus())
				.build());
	}

	@Override
	public BaseResponse<WechatLoginSetServerStatusResponse> getWechatServerStatusByStoreId(@RequestBody @Valid WechatServiceStatusByStoreIdRequest wechatServiceStatusByStoreIdRequest) {
		WechatLoginSet wechatLoginSet = wechatLoginSetService.getLoginSetByStoreId(wechatServiceStatusByStoreIdRequest.getStoreId());
		return BaseResponse.success(WechatLoginSetServerStatusResponse.builder()
				.mobileStatus(wechatLoginSet.getMobileServerStatus())
				.appStatus(wechatLoginSet.getAppServerStatus())
				.pcStatus(wechatLoginSet.getPcServerStatus())
				.build());
	}
}

