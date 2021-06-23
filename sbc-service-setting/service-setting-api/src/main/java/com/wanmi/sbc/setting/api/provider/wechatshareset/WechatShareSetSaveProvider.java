package com.wanmi.sbc.setting.api.provider.wechatshareset;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.wechatshareset.WechatShareSetAddRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>微信分享配置保存服务Provider</p>
 * @author lq
 * @date 2019-11-05 16:15:54
 */
@FeignClient(value = "${application.setting.name}", contextId = "WechatShareSetSaveProvider")
public interface WechatShareSetSaveProvider {

	/**
	 * 新增微信分享配置API
	 *
	 * @author lq
	 * @param wechatShareSetAddRequest 微信分享配置新增参数结构 {@link WechatShareSetAddRequest}
	 * @return 新增的微信分享配置信息 {@link }
	 */
	@PostMapping("/setting/${application.setting.version}/wechatshareset/add")
	BaseResponse add(@RequestBody @Valid WechatShareSetAddRequest wechatShareSetAddRequest);
}

