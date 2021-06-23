package com.wanmi.sbc.setting.api.provider.wechatloginset;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.wechatloginset.WechatLoginSetAddRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>微信授权登录配置保存服务Provider</p>
 * @author lq
 * @date 2019-11-05 16:15:25
 */
@FeignClient(value = "${application.setting.name}", contextId = "WechatLoginSetSaveProvider")
public interface WechatLoginSetSaveProvider {

	/**
	 * 新增微信授权登录配置API
	 *
	 * @author lq
	 * @param wechatLoginSetAddRequest 微信授权登录配置新增参数结构 {@link WechatLoginSetAddRequest}
	 * @return 新增的微信授权登录配置信息
	 */
	@PostMapping("/setting/${application.setting.version}/wechatloginset/add")
	BaseResponse add(@RequestBody @Valid WechatLoginSetAddRequest wechatLoginSetAddRequest);

}

