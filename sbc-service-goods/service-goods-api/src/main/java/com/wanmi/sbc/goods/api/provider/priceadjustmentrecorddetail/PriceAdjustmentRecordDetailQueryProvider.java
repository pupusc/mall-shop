package com.wanmi.sbc.goods.api.provider.priceadjustmentrecorddetail;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.priceadjustmentrecorddetail.PriceAdjustmentRecordDetailByIdRequest;
import com.wanmi.sbc.goods.api.request.priceadjustmentrecorddetail.PriceAdjustmentRecordDetailListRequest;
import com.wanmi.sbc.goods.api.request.priceadjustmentrecorddetail.PriceAdjustmentRecordDetailPageRequest;
import com.wanmi.sbc.goods.api.response.price.adjustment.PriceAdjustmentRecordDetailByIdResponse;
import com.wanmi.sbc.goods.api.response.price.adjustment.PriceAdjustmentRecordDetailListResponse;
import com.wanmi.sbc.goods.api.response.price.adjustment.PriceAdjustmentRecordDetailPageByNoResponse;
import com.wanmi.sbc.goods.api.response.price.adjustment.PriceAdjustmentRecordDetailPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>调价单详情表查询服务Provider</p>
 *
 * @author chenli
 * @date 2020-12-09 19:55:41
 */
@FeignClient(value = "${application.goods.name}", contextId = "PriceAdjustmentRecordDetailQueryProvider")
public interface PriceAdjustmentRecordDetailQueryProvider {

    /**
     * 分页查询调价单详情表API
     *
     * @param priceAdjustmentRecordDetailPageReq 分页请求参数和筛选对象 {@link PriceAdjustmentRecordDetailPageRequest}
     * @return 调价单详情表分页列表信息 {@link PriceAdjustmentRecordDetailPageByNoResponse}
     * @author chenli
     */
    @PostMapping("/goods/${application.goods.version}/price-adjustment-record-detail/page")
    BaseResponse<PriceAdjustmentRecordDetailPageByNoResponse> page(@RequestBody @Valid PriceAdjustmentRecordDetailPageRequest priceAdjustmentRecordDetailPageReq);

//    /**
//     * 分页查询市场价调价单详情
//     *
//     * @param request 带分页信息的调价详情查询参数 {@link PriceAdjustmentDetailPageByTypeRequest}
//     * @return 市场价调价详情列表 {@link PriceAdjustmentRecordDetailPageByTypeResponse}
//     */
//    @PostMapping("/goods/${application.goods.version}/price-adjustment-record-detail/page-by-type")
//    BaseResponse<PriceAdjustmentRecordDetailPageByTypeResponse> pageByType(@RequestBody @Valid
//																				   PriceAdjustmentDetailPageByTypeRequest request);
	/**
	 * 确认调价分页查询调价单详情表API
	 *
	 * @author chenli
	 * @param priceAdjustmentRecordDetailPageReq 分页请求参数和筛选对象 {@link PriceAdjustmentRecordDetailPageRequest}
	 * @return 调价单详情表分页列表信息 {@link PriceAdjustmentRecordDetailPageResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/price-adjustment-record-detail/page-for-confirm")
	BaseResponse<PriceAdjustmentRecordDetailPageResponse> pageForConfirm(@RequestBody @Valid PriceAdjustmentRecordDetailPageRequest priceAdjustmentRecordDetailPageReq);

	/**
	 * 列表查询调价单详情表API
	 *
	 * @param priceAdjustmentRecordDetailListReq 列表请求参数和筛选对象 {@link PriceAdjustmentRecordDetailListRequest}
	 * @return 调价单详情表的列表信息 {@link PriceAdjustmentRecordDetailListResponse}
	 * @author chenli
	 */
	@PostMapping("/goods/${application.goods.version}/price-adjustment-record-detail/list")
	BaseResponse<PriceAdjustmentRecordDetailListResponse> list(@RequestBody @Valid PriceAdjustmentRecordDetailListRequest priceAdjustmentRecordDetailListReq);


	/**
	 * 单个查询调价单详情表API
	 *
	 * @author chenli
	 * @param priceAdjustmentRecordDetailByIdRequest 单个查询调价单详情表请求参数 {@link PriceAdjustmentRecordDetailByIdRequest}
	 * @return 调价单详情表详情 {@link PriceAdjustmentRecordDetailByIdResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/price-adjustment-record-detail/get-by-id")
	BaseResponse<PriceAdjustmentRecordDetailByIdResponse> getById(@RequestBody @Valid PriceAdjustmentRecordDetailByIdRequest priceAdjustmentRecordDetailByIdRequest);

}

