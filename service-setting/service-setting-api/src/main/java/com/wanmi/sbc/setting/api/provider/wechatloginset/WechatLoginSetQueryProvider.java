package com.wanmi.sbc.setting.api.provider.wechatloginset;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.wechatloginset.WechatLoginSetByStoreIdRequest;
import com.wanmi.sbc.setting.api.request.wechatloginset.WechatServiceStatusByStoreIdRequest;
import com.wanmi.sbc.setting.api.response.wechatloginset.*;
import com.wanmi.sbc.setting.api.request.wechatloginset.WechatLoginSetByIdRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>微信授权登录配置查询服务Provider</p>
 * @author lq
 * @date 2019-11-05 16:15:25
 */
@FeignClient(value = "${application.setting.name}", contextId = "WechatLoginSetQueryProvider")
public interface WechatLoginSetQueryProvider {

	/**
	 * 单个查询微信授权登录配置API
	 *
	 * @author lq
	 * @param wechatLoginSetByIdRequest 单个查询微信授权登录配置请求参数 {@link WechatLoginSetByIdRequest}
	 * @return 微信授权登录配置详情 {@link WechatLoginSetByIdResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/wechatloginset/get-by-id")
	BaseResponse<WechatLoginSetByIdResponse> getById(@RequestBody @Valid WechatLoginSetByIdRequest
                                                             wechatLoginSetByIdRequest);

	/**
	 * 查询微信授权登录配置API
	 *
	 * @author lq
	 * @return 微信授权登录配置详情 {@link WechatLoginSetByIdResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/wechatloginset/get-info")
	BaseResponse<WechatLoginSetResponse> getInfo();

	/**
	 * 门店id查询平台微信登录配置
	 *
	 * @author lq
	 * @return 微信授权登录配置详情 {@link WechatLoginSetByIdResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/wechatloginset/getInfoByStoreId")
	BaseResponse<WechatLoginSetResponse> getInfoByStoreId(@RequestBody @Valid WechatLoginSetByStoreIdRequest wechatLoginSetByStoreIdRequest);

	/**
	 * 根据终端类型，获取授信开关状态
	 *
	 * @author lq
	 * @return 微信授权登录配置详情 {@link WechatLoginSetByIdResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/wechatloginset/get-server-status")
	BaseResponse<WechatLoginSetServerStatusResponse> getWechatServerStatus();

	/**
	 * 根据门店id查询终端类型，获取授信开关状态
	 *
	 * @author lq
	 * @return 微信授权登录配置详情 {@link WechatLoginSetByIdResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/wechatloginset/getWechatServerStatusByStoreId")
	BaseResponse<WechatLoginSetServerStatusResponse> getWechatServerStatusByStoreId(@RequestBody @Valid WechatServiceStatusByStoreIdRequest wechatServiceStatusByStoreIdRequest);

}

