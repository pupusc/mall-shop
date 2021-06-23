package com.wanmi.sbc.goods.api.provider.livegoods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.livegoods.*;
import com.wanmi.sbc.goods.api.response.livegoods.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * <p>直播商品保存服务Provider</p>
 * @author zwb
 * @date 2020-06-06 18:49:08
 */
@FeignClient(value = "${application.goods.name}", contextId = "LiveGoodsProvider")
public interface LiveGoodsProvider {

	/**
	 * 新增直播商品API
	 *
	 * @author zwb
	 * @param liveGoodsAddRequest 直播商品新增参数结构 {@link LiveGoodsAddRequest}
	 * @return 新增的直播商品信息 {@link LiveGoodsAddResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/livegoods/add")
	BaseResponse<LiveGoodsAddResponse> add(@RequestBody @Valid LiveGoodsAddRequest liveGoodsAddRequest);

	/**
	 * 修改直播商品API
	 *
	 * @author zwb
	 * @param liveGoodsModifyRequest 直播商品修改参数结构 {@link LiveGoodsModifyRequest}
	 * @return 修改的直播商品信息 {@link LiveGoodsModifyResponse}
	 */

	@PostMapping("/goods/${application.goods.version}/livegoods/modify")
	BaseResponse<LiveGoodsModifyResponse> modify(@RequestBody @Valid LiveGoodsModifyRequest liveGoodsModifyRequest);

	/**
	 * 修改直播商品状态
	 *
	 * @author zwb
	 * @param liveGoodsUpdateRequest 直播商品修改参数结构 {@link LiveGoodsModifyRequest}
	 * @return 修改的直播商品信息 {@link LiveGoodsModifyResponse}
	 */

	@PostMapping( "/goods/${application.goods.version}/livegoods/update")
	BaseResponse update(@RequestBody @Valid LiveGoodsUpdateRequest liveGoodsUpdateRequest);
	/**
	 * 单个删除直播商品API
	 *
	 * @author zwb
	 * @param liveGoodsDelByIdRequest 单个删除参数结构 {@link LiveGoodsDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/livegoods/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid LiveGoodsDelByIdRequest liveGoodsDelByIdRequest);

	/**
	 * 批量删除直播商品API
	 *
	 * @author zwb
	 * @param liveGoodsDelByIdListRequest 批量删除参数结构 {@link LiveGoodsDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/livegoods/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid LiveGoodsDelByIdListRequest liveGoodsDelByIdListRequest);

	/**
	 * 直播商品提交审核API
	 * @param liveGoodsAuditRequest
	 * @return
	 */
	@PostMapping("/goods/${application.goods.version}/livegoods/audit")
    BaseResponse audit(@RequestBody @Valid LiveGoodsAuditRequest liveGoodsAuditRequest);

	/**
	 * supplier端直播商品提交审核API
	 * @param supplierAddReq
	 * @return
	 */
	@PostMapping("/goods/${application.goods.version}/livegoods/supplier")
    BaseResponse<LiveGoodsSupplierAddResponse> supplier(LiveGoodsSupplierAddRequest supplierAddReq);
	/**
	 * 修改状态
	 * @param updateRequest
	 * @return
	 */
	@PostMapping("/goods/${application.goods.version}/livegoods/status")
	BaseResponse<LiveGoodsModifyResponse> status(LiveGoodsUpdateRequest updateRequest);
}

