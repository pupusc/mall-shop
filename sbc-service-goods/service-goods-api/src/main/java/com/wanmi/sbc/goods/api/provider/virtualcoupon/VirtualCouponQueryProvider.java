package com.wanmi.sbc.goods.api.provider.virtualcoupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.virtualcoupon.VirtualCouponPageRequest;
import com.wanmi.sbc.goods.api.response.virtualcoupon.VirtualCouponPageResponse;
import com.wanmi.sbc.goods.api.request.virtualcoupon.VirtualCouponListRequest;
import com.wanmi.sbc.goods.api.response.virtualcoupon.VirtualCouponListResponse;
import com.wanmi.sbc.goods.api.request.virtualcoupon.VirtualCouponByIdRequest;
import com.wanmi.sbc.goods.api.response.virtualcoupon.VirtualCouponByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>卡券查询服务Provider</p>
 * @author 梁善
 * @date 2021-01-25 10:19:19
 */
@FeignClient(value = "${application.goods.name}", contextId = "VirtualCouponQueryProvider")
public interface VirtualCouponQueryProvider {

	/**
	 * 分页查询卡券API
	 *
	 * @author 梁善
	 * @param virtualCouponPageReq 分页请求参数和筛选对象 {@link VirtualCouponPageRequest}
	 * @return 卡券分页列表信息 {@link VirtualCouponPageResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/virtualcoupon/page")
	BaseResponse<VirtualCouponPageResponse> page(@RequestBody @Valid VirtualCouponPageRequest virtualCouponPageReq);

	/**
	 * 列表查询卡券API
	 *
	 * @author 梁善
	 * @param virtualCouponListReq 列表请求参数和筛选对象 {@link VirtualCouponListRequest}
	 * @return 卡券的列表信息 {@link VirtualCouponListResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/virtualcoupon/list")
	BaseResponse<VirtualCouponListResponse> list(@RequestBody @Valid VirtualCouponListRequest virtualCouponListReq);

	/**
	 * 单个查询卡券API
	 *
	 * @author 梁善
	 * @param virtualCouponByIdRequest 单个查询卡券请求参数 {@link VirtualCouponByIdRequest}
	 * @return 卡券详情 {@link VirtualCouponByIdResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/virtualcoupon/get-by-id")
	BaseResponse<VirtualCouponByIdResponse> getById(@RequestBody @Valid VirtualCouponByIdRequest virtualCouponByIdRequest);

}

