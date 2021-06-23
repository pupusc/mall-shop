package com.wanmi.sbc.goods.api.provider.goodsrestrictedsale;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.goodsrestrictedsale.GoodsRestrictedSaleAddRequest;
import com.wanmi.sbc.goods.api.response.goodsrestrictedsale.GoodsRestrictedSaleAddResponse;
import com.wanmi.sbc.goods.api.request.goodsrestrictedsale.GoodsRestrictedSaleModifyRequest;
import com.wanmi.sbc.goods.api.response.goodsrestrictedsale.GoodsRestrictedSaleModifyResponse;
import com.wanmi.sbc.goods.api.request.goodsrestrictedsale.GoodsRestrictedSaleDelByIdRequest;
import com.wanmi.sbc.goods.api.request.goodsrestrictedsale.GoodsRestrictedSaleDelByIdListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>限售配置保存服务Provider</p>
 * @author baijz
 * @date 2020-04-08 11:20:28
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsRestrictedSaleSaveProvider")
public interface GoodsRestrictedSaleSaveProvider {

	/**
	 * 新增限售配置API
	 *
	 * @author baijz
	 * @param goodsRestrictedSaleAddRequest 限售配置新增参数结构 {@link GoodsRestrictedSaleAddRequest}
	 * @return 新增的限售配置信息 {@link GoodsRestrictedSaleAddResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodsrestrictedsale/add")
	BaseResponse<GoodsRestrictedSaleAddResponse> addBatch(@RequestBody @Valid GoodsRestrictedSaleAddRequest goodsRestrictedSaleAddRequest);

	/**
	 * 修改限售配置API
	 *
	 * @author baijz
	 * @param goodsRestrictedSaleModifyRequest 限售配置修改参数结构 {@link GoodsRestrictedSaleModifyRequest}
	 * @return 修改的限售配置信息 {@link GoodsRestrictedSaleModifyResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodsrestrictedsale/modify")
	BaseResponse<GoodsRestrictedSaleModifyResponse> modify(@RequestBody @Valid GoodsRestrictedSaleModifyRequest goodsRestrictedSaleModifyRequest);

	/**
	 * 单个删除限售配置API
	 *
	 * @author baijz
	 * @param goodsRestrictedSaleDelByIdRequest 单个删除参数结构 {@link GoodsRestrictedSaleDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodsrestrictedsale/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid GoodsRestrictedSaleDelByIdRequest goodsRestrictedSaleDelByIdRequest);

	/**
	 * 批量删除限售配置API
	 *
	 * @author baijz
	 * @param goodsRestrictedSaleDelByIdListRequest 批量删除参数结构 {@link GoodsRestrictedSaleDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodsrestrictedsale/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid GoodsRestrictedSaleDelByIdListRequest goodsRestrictedSaleDelByIdListRequest);

}

