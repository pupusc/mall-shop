package com.wanmi.sbc.goods.api.provider.pointsgoodscate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.pointsgoodscate.PointsGoodsCatePageRequest;
import com.wanmi.sbc.goods.api.response.pointsgoodscate.PointsGoodsCatePageResponse;
import com.wanmi.sbc.goods.api.request.pointsgoodscate.PointsGoodsCateListRequest;
import com.wanmi.sbc.goods.api.response.pointsgoodscate.PointsGoodsCateListResponse;
import com.wanmi.sbc.goods.api.request.pointsgoodscate.PointsGoodsCateByIdRequest;
import com.wanmi.sbc.goods.api.response.pointsgoodscate.PointsGoodsCateByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>积分商品分类表查询服务Provider</p>
 * @author yang
 * @date 2019-05-13 09:50:07
 */
@FeignClient(value = "${application.goods.name}", contextId = "PointsGoodsCateQueryProvider")
public interface PointsGoodsCateQueryProvider {

	/**
	 * 分页查询积分商品分类表API
	 *
	 * @author yang
	 * @param pointsGoodsCatePageReq 分页请求参数和筛选对象 {@link PointsGoodsCatePageRequest}
	 * @return 积分商品分类表分页列表信息 {@link PointsGoodsCatePageResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/pointsgoodscate/page")
	BaseResponse<PointsGoodsCatePageResponse> page(@RequestBody @Valid PointsGoodsCatePageRequest pointsGoodsCatePageReq);

	/**
	 * 列表查询积分商品分类表API
	 *
	 * @author yang
	 * @param pointsGoodsCateListReq 列表请求参数和筛选对象 {@link PointsGoodsCateListRequest}
	 * @return 积分商品分类表的列表信息 {@link PointsGoodsCateListResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/pointsgoodscate/list")
	BaseResponse<PointsGoodsCateListResponse> list(@RequestBody @Valid PointsGoodsCateListRequest pointsGoodsCateListReq);

	/**
	 * 单个查询积分商品分类表API
	 *
	 * @author yang
	 * @param pointsGoodsCateByIdRequest 单个查询积分商品分类表请求参数 {@link PointsGoodsCateByIdRequest}
	 * @return 积分商品分类表详情 {@link PointsGoodsCateByIdResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/pointsgoodscate/get-by-id")
	BaseResponse<PointsGoodsCateByIdResponse> getById(@RequestBody @Valid PointsGoodsCateByIdRequest pointsGoodsCateByIdRequest);

}

