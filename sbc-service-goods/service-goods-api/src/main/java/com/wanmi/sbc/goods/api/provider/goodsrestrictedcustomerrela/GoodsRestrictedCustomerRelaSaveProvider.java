package com.wanmi.sbc.goods.api.provider.goodsrestrictedcustomerrela;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.goodsrestrictedcustomerrela.GoodsRestrictedCustomerRelaAddRequest;
import com.wanmi.sbc.goods.api.response.goodsrestrictedcustomerrela.GoodsRestrictedCustomerRelaAddResponse;
import com.wanmi.sbc.goods.api.request.goodsrestrictedcustomerrela.GoodsRestrictedCustomerRelaModifyRequest;
import com.wanmi.sbc.goods.api.response.goodsrestrictedcustomerrela.GoodsRestrictedCustomerRelaModifyResponse;
import com.wanmi.sbc.goods.api.request.goodsrestrictedcustomerrela.GoodsRestrictedCustomerRelaDelByIdRequest;
import com.wanmi.sbc.goods.api.request.goodsrestrictedcustomerrela.GoodsRestrictedCustomerRelaDelByIdListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>限售配置会员关系表保存服务Provider</p>
 * @author baijz
 * @date 2020-04-08 11:32:28
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsRestrictedCustomerRelaSaveProvider")
public interface GoodsRestrictedCustomerRelaSaveProvider {

	/**
	 * 新增限售配置会员关系表API
	 *
	 * @author baijz
	 * @param goodsRestrictedCustomerRelaAddRequest 限售配置会员关系表新增参数结构 {@link GoodsRestrictedCustomerRelaAddRequest}
	 * @return 新增的限售配置会员关系表信息 {@link GoodsRestrictedCustomerRelaAddResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodsrestrictedcustomerrela/add")
	BaseResponse<GoodsRestrictedCustomerRelaAddResponse> add(@RequestBody @Valid GoodsRestrictedCustomerRelaAddRequest goodsRestrictedCustomerRelaAddRequest);

	/**
	 * 修改限售配置会员关系表API
	 *
	 * @author baijz
	 * @param goodsRestrictedCustomerRelaModifyRequest 限售配置会员关系表修改参数结构 {@link GoodsRestrictedCustomerRelaModifyRequest}
	 * @return 修改的限售配置会员关系表信息 {@link GoodsRestrictedCustomerRelaModifyResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodsrestrictedcustomerrela/modify")
	BaseResponse<GoodsRestrictedCustomerRelaModifyResponse> modify(@RequestBody @Valid GoodsRestrictedCustomerRelaModifyRequest goodsRestrictedCustomerRelaModifyRequest);

	/**
	 * 单个删除限售配置会员关系表API
	 *
	 * @author baijz
	 * @param goodsRestrictedCustomerRelaDelByIdRequest 单个删除参数结构 {@link GoodsRestrictedCustomerRelaDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodsrestrictedcustomerrela/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid GoodsRestrictedCustomerRelaDelByIdRequest goodsRestrictedCustomerRelaDelByIdRequest);

	/**
	 * 批量删除限售配置会员关系表API
	 *
	 * @author baijz
	 * @param goodsRestrictedCustomerRelaDelByIdListRequest 批量删除参数结构 {@link GoodsRestrictedCustomerRelaDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodsrestrictedcustomerrela/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid GoodsRestrictedCustomerRelaDelByIdListRequest goodsRestrictedCustomerRelaDelByIdListRequest);

}

