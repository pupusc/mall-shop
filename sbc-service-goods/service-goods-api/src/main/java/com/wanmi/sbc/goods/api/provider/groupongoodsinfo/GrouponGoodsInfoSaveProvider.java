package com.wanmi.sbc.goods.api.provider.groupongoodsinfo;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.groupongoodsinfo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>拼团活动商品信息表保存服务Provider</p>
 * @author groupon
 * @date 2019-05-15 14:49:12
 */
@FeignClient(value = "${application.goods.name}", contextId = "GrouponGoodsInfoSaveProvider")
public interface GrouponGoodsInfoSaveProvider {

	/**
	 * 批量新增拼团活动商品
	 * @param request 批量新增拼团活动商品请求对象 {@link GrouponGoodsInfoBatchAddRequest}
	 * @return {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/groupon/goodsinfo/batch-add")
	BaseResponse batchAdd(@RequestBody @Valid GrouponGoodsInfoBatchAddRequest request);


	/**
	 * 批量修改拼团活动商品
	 * @param request 批量修改拼团活动商品请求对象  {@link GrouponGoodsInfoBatchEditRequest}
	 * @return {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/groupon/goodsinfo/batch-edit")
	BaseResponse batchEdit(@RequestBody @Valid GrouponGoodsInfoBatchEditRequest request);


	/**
	 *  根据拼团活动ID删除拼团活动商品
	 * @param request {@link GrouponGoodsInfoDeleteByGrouponActivityIdRequest}
	 * @return {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/groupon/goodsinfo/delete-by-groupon-activity-id")
	BaseResponse deleteByGrouponActivityId(@RequestBody @Valid GrouponGoodsInfoDeleteByGrouponActivityIdRequest request);

	/**
	 * 根据拼团活动id修改成团后的商品退单信息
	 * @param modifyRequest 修改参数 {@link GrouponGoodsInfoReturnModifyRequest}
	 * @return {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/groupon/goodsinfo/modify-return-info")
	BaseResponse modifyReturnInfo(@RequestBody @Valid GrouponGoodsInfoReturnModifyRequest modifyRequest);


	/**
	 * 根据活动ID批量更新审核状态
	 * @param request {@link GrouponGoodsInfoModifyAuditStatusRequest}
	 * @return {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/groupon/goodsinfo/modify-audit-status")
	BaseResponse modifyAuditStatusByGrouponActivityIds(@RequestBody @Valid GrouponGoodsInfoModifyAuditStatusRequest request);

	/**
	 * 根据活动ID批量更新是否精选
	 * @param request {@link GrouponGoodsInfoModifyStickyRequest}
	 * @return {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/groupon/goodsinfo/modify-stick")
	BaseResponse modifyStickyByGrouponActivityIds(@RequestBody @Valid GrouponGoodsInfoModifyStickyRequest request);
}

