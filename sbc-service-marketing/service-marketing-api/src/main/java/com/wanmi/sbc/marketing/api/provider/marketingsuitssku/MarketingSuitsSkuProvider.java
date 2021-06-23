package com.wanmi.sbc.marketing.api.provider.marketingsuitssku;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.marketingsuits.MarketingSuitsSaveRequest;
import com.wanmi.sbc.marketing.api.request.marketingsuitssku.*;
import com.wanmi.sbc.marketing.api.response.marketingsuitssku.MarketingSuitsSkuAddResponse;
import com.wanmi.sbc.marketing.api.response.marketingsuitssku.MarketingSuitsSkuModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>组合活动关联商品sku表保存服务Provider</p>
 * @author zhk
 * @date 2020-04-02 10:51:12
 */
@FeignClient(value = "${application.marketing.name}", contextId = "MarketingSuitsSkuProvider")
public interface MarketingSuitsSkuProvider {

	/**
	 * 批量删除组合活动关联商品sku表API
	 *
	 * @author zhk
	 * @param marketingSuitsSkuDelByIdListRequest 批量删除参数结构 {@link MarketingSuitsSkuDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/marketing/${application.marketing.version}/marketingsuitssku/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid MarketingSuitsSkuDelByIdListRequest marketingSuitsSkuDelByIdListRequest);

}

