package com.wanmi.sbc.setting.api.provider.flashsalesetting;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.flashsalesetting.FlashSaleSettingListModifyRequest;
import com.wanmi.sbc.setting.api.response.flashsalesetting.FlashSaleSettingCancelListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>秒杀设置保存服务Provider</p>
 * @author yxz
 * @date 2019-06-11 13:48:53
 */
@FeignClient(value = "${application.setting.name}", contextId = "FlashSaleSettingSaveProvider")
public interface FlashSaleSettingSaveProvider {

	/**
	 * 修改秒杀设置API
	 *
	 * @author yxz
	 * @param flashSaleSettingListModifyRequest 秒杀设置修改参数结构 {@link FlashSaleSettingListModifyRequest}
	 */
	@PostMapping("/setting/${application.setting.version}/flashsalesetting/modifyList")
	BaseResponse<FlashSaleSettingCancelListResponse> modifyList(@RequestBody @Valid FlashSaleSettingListModifyRequest
										flashSaleSettingListModifyRequest);

}

