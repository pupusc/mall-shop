package com.wanmi.sbc.setting.api.provider.push;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.push.AppPushConfigAddRequest;
import com.wanmi.sbc.setting.api.request.push.AppPushConfigModifyRequest;
import com.wanmi.sbc.setting.api.response.push.AppPushConfigAddResponse;
import com.wanmi.sbc.setting.api.response.push.AppPushConfigModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>消息推送保存服务Provider</p>
 * @author chenyufei
 * @date 2019-05-10 14:39:59
 */
@FeignClient(value = "${application.setting.name}", contextId = "AppPushConfigSaveProvider")
public interface AppPushConfigSaveProvider {

	/**
	 * 新增消息推送API
	 *
	 * @author chenyufei
	 * @param appPushConfigAddRequest 消息推送新增参数结构 {@link AppPushConfigAddRequest}
	 * @return 新增的消息推送信息 {@link AppPushConfigAddResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/apppushconfig/add")
	BaseResponse<AppPushConfigAddResponse> add(@RequestBody @Valid AppPushConfigAddRequest appPushConfigAddRequest);

	/**
	 * 修改消息推送API
	 *
	 * @author chenyufei
	 * @param appPushConfigModifyRequest 消息推送修改参数结构 {@link AppPushConfigModifyRequest}
	 * @return 修改的消息推送信息 {@link AppPushConfigModifyResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/apppushconfig/modify")
	BaseResponse<AppPushConfigModifyResponse> modify(@RequestBody @Valid AppPushConfigModifyRequest appPushConfigModifyRequest);

}

