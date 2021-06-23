package com.wanmi.sbc.setting.api.provider.storewechatminiprogramconfig;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.storewechatminiprogramconfig.StoreWechatMiniProgramConfigAddRequest;
import com.wanmi.sbc.setting.api.request.storewechatminiprogramconfig.StoreWechatMiniProgramConfigModifyRequest;
import com.wanmi.sbc.setting.api.response.storewechatminiprogramconfig.StoreWechatMiniProgramConfigAddResponse;
import com.wanmi.sbc.setting.api.response.storewechatminiprogramconfig.StoreWechatMiniProgramConfigModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>门店微信小程序配置保存服务Provider</p>
 * @author tangLian
 * @date 2020-01-16 11:47:15
 */
@FeignClient(value = "${application.setting.name}",contextId = "StoreWechatMiniProgramConfigSaveProvider")
public interface StoreWechatMiniProgramConfigSaveProvider {

	/**
	 * 新增门店微信小程序配置API
	 *
	 * @author tangLian
	 * @param storeWechatMiniProgramConfigAddRequest 门店微信小程序配置新增参数结构 {@link StoreWechatMiniProgramConfigAddRequest}
	 * @return 新增的门店微信小程序配置信息 {@link StoreWechatMiniProgramConfigAddResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/storewechatminiprogramconfig/add")
	BaseResponse<StoreWechatMiniProgramConfigAddResponse> add(@RequestBody @Valid StoreWechatMiniProgramConfigAddRequest storeWechatMiniProgramConfigAddRequest);

	/**
	 * 修改门店微信小程序配置API
	 *
	 * @author tangLian
	 * @param storeWechatMiniProgramConfigModifyRequest 门店微信小程序配置修改参数结构 {@link StoreWechatMiniProgramConfigModifyRequest}
	 * @return 修改的门店微信小程序配置信息 {@link StoreWechatMiniProgramConfigModifyResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/storewechatminiprogramconfig/modify")
	BaseResponse<StoreWechatMiniProgramConfigModifyResponse> modify(@RequestBody @Valid StoreWechatMiniProgramConfigModifyRequest storeWechatMiniProgramConfigModifyRequest);

}

