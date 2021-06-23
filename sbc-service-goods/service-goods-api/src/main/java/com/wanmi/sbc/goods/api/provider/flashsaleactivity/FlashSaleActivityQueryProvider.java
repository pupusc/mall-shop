package com.wanmi.sbc.goods.api.provider.flashsaleactivity;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.flashsaleactivity.FlashSaleActivityListRequest;
import com.wanmi.sbc.goods.api.request.flashsaleactivity.FlashSaleActivityPageRequest;
import com.wanmi.sbc.goods.api.request.flashsaleactivity.FlashSaleActivityQueryRequest;
import com.wanmi.sbc.goods.api.response.flashsaleactivity.FlashSaleActivityListNewResponse;
import com.wanmi.sbc.goods.api.response.flashsaleactivity.FlashSaleActivityListResponse;
import com.wanmi.sbc.goods.api.response.flashsaleactivity.FlashSaleActivityPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>抢购活动查询服务Provider</p>
 * @author yxz
 * @date 2019-06-20 14:54:31
 */
@FeignClient(value = "${application.goods.name}", contextId = "FlashSaleActivityQueryProvider")
public interface FlashSaleActivityQueryProvider {

	/**
	 * 分页查询抢购活动API
	 *
	 * @author yxz
	 * @param flashSaleActivityPageRequest 分页请求参数和筛选对象 {@link FlashSaleActivityPageRequest}
	 * @return 抢购活动分页列表信息 {@link FlashSaleActivityPageResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/flashsaleactivity/page")
	BaseResponse<FlashSaleActivityPageResponse> page(@RequestBody @Valid FlashSaleActivityPageRequest
															flashSaleActivityPageRequest);

	/**
	 * 列表查询抢购活动API
	 *
	 * @author yxz
	 * @param flashSaleActivityListRequest 分页请求参数和筛选对象 {@link FlashSaleActivityListRequest}
	 * @return 抢购活动列表信息 {@link FlashSaleActivityListResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/flashsaleactivity/list")
	BaseResponse<FlashSaleActivityListResponse> list(@RequestBody @Valid FlashSaleActivityListRequest
															 flashSaleActivityListRequest);

	/**
	 * 列表查询抢购活动API
	 *
	 * @author yxz
	 * @param flashSaleActivityListRequest 分页请求参数和筛选对象 {@link FlashSaleActivityListRequest}
	 * @return 抢购活动列表信息 {@link FlashSaleActivityListResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/flashsaleactivity/list-new")
	BaseResponse<FlashSaleActivityListNewResponse> listNew(@RequestBody @Valid FlashSaleActivityQueryRequest
															 flashSaleActivityListRequest);
}

