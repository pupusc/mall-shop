package com.wanmi.sbc.marketing.api.provider.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.response.distribution.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * <p>分销设置缓存查询服务Provider</p>
 * @author gaomuwei
 * @date 2019-02-19 10:08:02
 */
@FeignClient(value = "${application.marketing.name}", contextId = "DistributionCacheQueryProvider")
public interface DistributionCacheQueryProvider {

	/**
	 * 查询多级分销设置信息
	 * @return
	 */
	@PostMapping("/marketing/${application.marketing.version}/distribution/cache/get-multistage-setting")
	BaseResponse<MultistageSettingGetResponse> getMultistageSetting();
}

