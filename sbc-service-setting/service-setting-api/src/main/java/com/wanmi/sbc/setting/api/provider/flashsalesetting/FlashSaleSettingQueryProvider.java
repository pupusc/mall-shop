package com.wanmi.sbc.setting.api.provider.flashsalesetting;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.flashsalesetting.FlashSaleSettingListRequest;
import com.wanmi.sbc.setting.api.response.flashsalesetting.FlashSaleSettingListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>秒杀设置查询服务Provider</p>
 * @author yxz
 * @date 2019-06-11 13:48:53
 */
@FeignClient(value = "${application.setting.name}", contextId = "FlashSaleSettingQueryProvider")
public interface FlashSaleSettingQueryProvider {

	/**
	 * 列表查询秒杀设置API
	 *
	 * @author yxz
	 * @param flashSaleSettingListReq 列表请求参数和筛选对象 {@link FlashSaleSettingListRequest}
	 * @return 秒杀设置的列表信息 {@link FlashSaleSettingListResponse}
	 */
	@PostMapping("/setting/${application.setting.version}/flashsalesetting/list")
	BaseResponse<FlashSaleSettingListResponse> list(@RequestBody @Valid FlashSaleSettingListRequest flashSaleSettingListReq);

}

