package com.wanmi.sbc.goods.api.provider.goodslabel;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.goodslabel.GoodsLabelByIdRequest;
import com.wanmi.sbc.goods.api.request.goodslabel.GoodsLabelListRequest;
import com.wanmi.sbc.goods.api.request.goodslabel.GoodsLabelPageRequest;
import com.wanmi.sbc.goods.api.response.goodslabel.GoodsLabelByIdResponse;
import com.wanmi.sbc.goods.api.response.goodslabel.GoodsLabelCacheListResponse;
import com.wanmi.sbc.goods.api.response.goodslabel.GoodsLabelListResponse;
import com.wanmi.sbc.goods.api.response.goodslabel.GoodsLabelPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>商品标签查询服务Provider</p>
 * @author dyt
 * @date 2020-09-29 13:57:19
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsLabelQueryProvider")
public interface GoodsLabelQueryProvider {

	/**
	 * 分页查询商品标签API
	 *
	 * @author dyt
	 * @param goodsLabelPageReq 分页请求参数和筛选对象 {@link GoodsLabelPageRequest}
	 * @return 商品标签分页列表信息 {@link GoodsLabelPageResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodslabel/page")
    BaseResponse<GoodsLabelPageResponse> page(@RequestBody @Valid GoodsLabelPageRequest goodsLabelPageReq);

	/**
	 * 列表查询商品标签API
	 *
	 * @author dyt
	 * @param goodsLabelListReq 列表请求参数和筛选对象 {@link GoodsLabelListRequest}
	 * @return 商品标签的列表信息 {@link GoodsLabelListResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodslabel/list")
	BaseResponse<GoodsLabelListResponse> list(@RequestBody @Valid GoodsLabelListRequest goodsLabelListReq);

	/**
	 * 缓存级查询商品标签API
	 *
	 * @author dyt
	 * @return 商品标签的列表信息 {@link GoodsLabelListResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodslabel/cache-list")
	BaseResponse<GoodsLabelCacheListResponse> cacheList();

	/**
	 * 单个查询商品标签API
	 *
	 * @author dyt
	 * @param goodsLabelByIdRequest 单个查询商品标签请求参数 {@link GoodsLabelByIdRequest}
	 * @return 商品标签详情 {@link GoodsLabelByIdResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodslabel/get-by-id")
    BaseResponse<GoodsLabelByIdResponse> getById(@RequestBody @Valid GoodsLabelByIdRequest goodsLabelByIdRequest);


}

