package com.wanmi.sbc.goods.api.provider.livegoods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.livegoods.*;
import com.wanmi.sbc.goods.api.response.livegoods.LiveGoodsPageNewResponse;
import com.wanmi.sbc.goods.api.response.livegoods.LiveGoodsPageResponse;
import com.wanmi.sbc.goods.api.response.livegoods.LiveGoodsListResponse;
import com.wanmi.sbc.goods.api.response.livegoods.LiveGoodsByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>直播商品查询服务Provider</p>
 * @author zwb
 * @date 2020-06-10 11:05:45
 */
@FeignClient(value = "${application.goods.name}", contextId = "LiveGoodsQueryProvider")
public interface LiveGoodsQueryProvider {

	/**
	 * 分页查询直播商品API
	 *
	 * @author zwb
	 * @param liveGoodsPageReq 分页请求参数和筛选对象 {@link LiveGoodsPageRequest}
	 * @return 直播商品分页列表信息 {@link LiveGoodsPageResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/livegoods/page")
	BaseResponse<LiveGoodsPageResponse> page(@RequestBody @Valid LiveGoodsPageRequest liveGoodsPageReq);

	/**
	 * 列表查询直播商品API
	 *
	 * @author zwb
	 * @param liveGoodsListReq 列表请求参数和筛选对象 {@link LiveGoodsListRequest}
	 * @return 直播商品的列表信息 {@link LiveGoodsListResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/livegoods/list")
	BaseResponse<LiveGoodsListResponse> list(@RequestBody @Valid LiveGoodsListRequest liveGoodsListReq);

	/**
	 * 单个查询直播商品API
	 *
	 * @author zwb
	 * @param liveGoodsByIdRequest 单个查询直播商品请求参数 {@link LiveGoodsByIdRequest}
	 * @return 直播商品详情 {@link LiveGoodsByIdResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/livegoods/get-by-id")
	BaseResponse<LiveGoodsByIdResponse> getById(@RequestBody @Valid LiveGoodsByIdRequest liveGoodsByIdRequest);


	/**
	 * 分页查询直播商品API
	 *
	 * @author zwb
	 * @param request 分页请求参数和筛选对象 {@link LiveGoodsPageNewRequest}
	 * @return 直播商品分页列表信息 {@link LiveGoodsPageNewResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/livegoods/page-new")
	BaseResponse<LiveGoodsPageNewResponse> pageNew(@RequestBody @Valid LiveGoodsQueryRequest request);


}

