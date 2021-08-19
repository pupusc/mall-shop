package com.wanmi.sbc.goods.api.provider.pointsgoodscate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.pointsgoodscate.*;
import com.wanmi.sbc.goods.api.response.pointsgoodscate.PointsGoodsCateAddResponse;
import com.wanmi.sbc.goods.api.response.pointsgoodscate.PointsGoodsCateModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>积分商品分类表保存服务Provider</p>
 * @author yang
 * @date 2019-05-13 09:50:07
 */
@FeignClient(value = "${application.goods.name}", contextId = "PointsGoodsCateSaveProvider")
public interface PointsGoodsCateSaveProvider {

	/**
	 * 新增积分商品分类表API
	 *
	 * @author yang
	 * @param pointsGoodsCateAddRequest 积分商品分类表新增参数结构 {@link PointsGoodsCateAddRequest}
	 * @return 新增的积分商品分类表信息 {@link PointsGoodsCateAddResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/pointsgoodscate/add")
	BaseResponse<PointsGoodsCateAddResponse> add(@RequestBody @Valid PointsGoodsCateAddRequest pointsGoodsCateAddRequest);

	/**
	 * 修改积分商品分类表API
	 *
	 * @author yang
	 * @param pointsGoodsCateModifyRequest 积分商品分类表修改参数结构 {@link PointsGoodsCateModifyRequest}
	 * @return 修改的积分商品分类表信息 {@link PointsGoodsCateModifyResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/pointsgoodscate/modify")
	BaseResponse<PointsGoodsCateModifyResponse> modify(@RequestBody @Valid PointsGoodsCateModifyRequest pointsGoodsCateModifyRequest);

	/**
	 * 单个删除积分商品分类表API
	 *
	 * @author yang
	 * @param pointsGoodsCateDelByIdRequest 单个删除参数结构 {@link PointsGoodsCateDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/pointsgoodscate/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid PointsGoodsCateDelByIdRequest pointsGoodsCateDelByIdRequest);

	/**
	 * 拖拽排序
	 *
	 * @param queryRequest 拖拽排序参数结构 {@link PointsGoodsCateSortRequest}
	 * @return 删除结果 {@link BaseResponse}
	 * @author minchen
	 */
	@PostMapping("/goods/${application.customer.version}/pointsgoodscate/edit-sort")
	BaseResponse editSort(@RequestBody @Valid PointsGoodsCateSortRequest queryRequest);
}

