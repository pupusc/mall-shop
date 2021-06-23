package com.wanmi.sbc.goods.api.provider.priceadjustmentrecord;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.priceadjustmentrecord.PriceAdjustmentRecordByAdjustNoRequest;
import com.wanmi.sbc.goods.api.request.priceadjustmentrecord.PriceAdjustmentRecordPageRequest;
import com.wanmi.sbc.goods.api.response.price.adjustment.PriceAdjustmentRecordPageResponse;
import com.wanmi.sbc.goods.api.request.priceadjustmentrecord.PriceAdjustmentRecordListRequest;
import com.wanmi.sbc.goods.api.response.price.adjustment.PriceAdjustmentRecordListResponse;
import com.wanmi.sbc.goods.api.request.priceadjustmentrecord.PriceAdjustmentRecordByIdRequest;
import com.wanmi.sbc.goods.api.response.price.adjustment.PriceAdjustmentRecordByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>调价记录表查询服务Provider</p>
 * @author chenli
 * @date 2020-12-09 19:57:21
 */
@FeignClient(value = "${application.goods.name}", contextId = "PriceAdjustmentRecordQueryProvider")
public interface PriceAdjustmentRecordQueryProvider {

	/**
	 * 分页查询调价记录表API
	 *
	 * @author chenli
	 * @param priceAdjustmentRecordPageReq 分页请求参数和筛选对象 {@link PriceAdjustmentRecordPageRequest}
	 * @return 调价记录表分页列表信息 {@link PriceAdjustmentRecordPageResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/price-adjustment-record/page")
	BaseResponse<PriceAdjustmentRecordPageResponse> page(@RequestBody @Valid PriceAdjustmentRecordPageRequest priceAdjustmentRecordPageReq);

	/**
	 * 列表查询调价记录表API
	 *
	 * @author chenli
	 * @param priceAdjustmentRecordListReq 列表请求参数和筛选对象 {@link PriceAdjustmentRecordListRequest}
	 * @return 调价记录表的列表信息 {@link PriceAdjustmentRecordListResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/price-adjustment-record/list")
	BaseResponse<PriceAdjustmentRecordListResponse> list(@RequestBody @Valid PriceAdjustmentRecordListRequest priceAdjustmentRecordListReq);

	/**
	 * 单个查询调价记录表API
	 *
	 * @author chenli
	 * @param priceAdjustmentRecordByIdRequest 单个查询调价记录表请求参数 {@link PriceAdjustmentRecordByIdRequest}
	 * @return 调价记录表详情 {@link PriceAdjustmentRecordByIdResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/price-adjustment-record/get-by-id")
	BaseResponse<PriceAdjustmentRecordByIdResponse> getById(@RequestBody @Valid PriceAdjustmentRecordByIdRequest priceAdjustmentRecordByIdRequest);

	/**
	 * 单个查询调价记录表API
	 *
	 * @author
	 * @param priceAdjustmentRecordByIdRequest 单个查询调价记录表请求参数 {@link PriceAdjustmentRecordByIdRequest}
	 * @return 调价记录表详情 {@link PriceAdjustmentRecordByIdResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/price-adjustment-record/get-by-adjustno")
	BaseResponse<PriceAdjustmentRecordByIdResponse> getByAdjustNo(@RequestBody @Valid PriceAdjustmentRecordByAdjustNoRequest priceAdjustmentRecordByIdRequest);

}

