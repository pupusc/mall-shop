package com.wanmi.sbc.setting.api.provider.onlineservice;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.onlineservice.OnlineServiceAddRequest;
import com.wanmi.sbc.setting.api.response.onlineservice.OnlineServiceAddResponse;
import com.wanmi.sbc.setting.api.request.onlineservice.OnlineServiceModifyRequest;
import com.wanmi.sbc.setting.api.request.onlineservice.OnlineServiceDelByIdRequest;
import com.wanmi.sbc.setting.api.request.onlineservice.OnlineServiceDelByIdListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>onlineService保存服务Provider</p>
 * @author lq
 * @date 2019-11-05 16:10:28
 */
@FeignClient(value = "${application.setting.name}", contextId = "OnlineServiceSaveProvider")
public interface OnlineServiceSaveProvider {

	/**
	 * 修改的onlineService信息
	 *
	 * @author lq
	 * @param onlineServiceModifyRequest onlineService修改参数结构 {@link OnlineServiceModifyRequest}
	 * @return 修改的onlineService信息
	 */
	@PostMapping("/setting/${application.setting.version}/onlineservice/modify")
	BaseResponse modify(@RequestBody @Valid OnlineServiceModifyRequest
                                                             onlineServiceModifyRequest);
}

