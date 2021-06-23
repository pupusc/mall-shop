package com.wanmi.sbc.goods.api.provider.priceadjustmentrecorddetail;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.adjustprice.*;
import com.wanmi.sbc.goods.api.request.priceadjustmentrecorddetail.PriceAdjustmentRecordDetailAddBatchRequest;
import com.wanmi.sbc.goods.api.request.priceadjustmentrecorddetail.PriceAdjustmentRecordDetailAddRequest;
import com.wanmi.sbc.goods.api.request.priceadjustmentrecorddetail.PriceAdjustmentRecordDetailModifyRequest;
import com.wanmi.sbc.goods.api.response.price.adjustment.AdjustPriceExecuteResponse;
import com.wanmi.sbc.goods.api.response.price.adjustment.PriceAdjustmentRecordDetailAddResponse;
import com.wanmi.sbc.goods.api.response.price.adjustment.PriceAdjustmentRecordDetailModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>调价单详情表保存服务Provider</p>
 * @author chenli
 * @date 2020-12-09 19:55:41
 */
@FeignClient(value = "${application.goods.name}", contextId = "PriceAdjustmentRecordDetailProvider")
public interface PriceAdjustmentRecordDetailProvider {

	/**
	 * 新增调价单详情表API
	 *
	 * @author chenli
	 * @param priceAdjustmentRecordDetailAddRequest 调价单详情表新增参数结构 {@link PriceAdjustmentRecordDetailAddRequest}
	 * @return 新增的调价单详情表信息 {@link PriceAdjustmentRecordDetailAddResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/price-adjustment-record-detail/add")
	BaseResponse<PriceAdjustmentRecordDetailAddResponse> add(@RequestBody @Valid PriceAdjustmentRecordDetailAddRequest priceAdjustmentRecordDetailAddRequest);

	/**
	 * 批量新增调价单详情表API
	 *
	 * @author chenli
	 * @param priceAdjustmentRecordDetailAddBatchRequest 调价单详情批量新增参数结构 {@link PriceAdjustmentRecordDetailAddBatchRequest}
	 * @return  {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/price-adjustment-record-detail/add-batch")
	BaseResponse addBatch(@RequestBody @Valid PriceAdjustmentRecordDetailAddBatchRequest priceAdjustmentRecordDetailAddBatchRequest);

	/**
	 * 修改调价单详情表API
	 *
	 * @author chenli
	 * @param priceAdjustmentRecordDetailModifyRequest 调价单详情表修改参数结构 {@link PriceAdjustmentRecordDetailModifyRequest}
	 * @return 修改的调价单详情表信息 {@link PriceAdjustmentRecordDetailModifyResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/price-adjustment-record-detail/modify")
	BaseResponse<PriceAdjustmentRecordDetailModifyResponse> modify(@RequestBody @Valid PriceAdjustmentRecordDetailModifyRequest priceAdjustmentRecordDetailModifyRequest);

	/**
	 * 编辑市场价调价单详情
	 * @param request 市场价编辑请求参数{@link MarketingPriceAdjustDetailModifyRequest}
	 * @return {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/price-adjustment-record-detail/modify-marketing-price")
	BaseResponse modifyMarketingPrice(@RequestBody @Valid MarketingPriceAdjustDetailModifyRequest request);

	/**
	 * 编辑客户等级价调价单详情
	 * @param request 客户等级编辑请求参数{@link CustomerLevelPriceAdjustDetailModifyRequest}
	 * @return {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/price-adjustment-record-detail/modify-customer-level-price")
	BaseResponse modifyCustomerLevelPrice(@RequestBody @Valid CustomerLevelPriceAdjustDetailModifyRequest request);

	/**
	 * 编辑区间价调价详情
	 * @param request 区间价编辑请求参数{@link IntervalPriceAdjustDetailModifyRequest}
	 * @return {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/price-adjustment-record-detail/modify-interval-price")
	BaseResponse modifyIntervalPrice(@RequestBody @Valid IntervalPriceAdjustDetailModifyRequest request);

	/**
	 * 删除调价详情
	 * @param request 删除调价详情请求参数 {@link AdjustPriceDetailDeleteRequest}
	 * @return {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/price-adjustment-record-detail/delete")
	BaseResponse delete(@RequestBody @Valid AdjustPriceDetailDeleteRequest request);

	/**
	 * 编辑供货价调价单详情
	 * @param request 供货价编辑请求参数{@link MarketingPriceAdjustDetailModifyRequest}
	 * @return {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/price-adjustment-record-detail/modify-supply-price")
	BaseResponse modifySupplyPrice(@RequestBody @Valid SupplyPriceAdjustDetailModifyRequest request);

	/**
	 * 立即调价
	 * @param request 调价参数 {@link AdjustPriceNowRequest}
	 * @return {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/price-adjustment-record-detail/adjust-price-now")
	BaseResponse adjustPriceNow(@RequestBody @Valid AdjustPriceNowRequest request);

	/**
	 * 确认调价
	 * @param request 调价参数 {@link AdjustPriceConfirmRequest}
	 * @return {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/price-adjustment-record-detail/adjust-price-confirm")
	BaseResponse adjustPriceConfirm(@RequestBody @Valid AdjustPriceConfirmRequest request);

	/**
	 * 执行调价
	 * @param request 调价参数 {@link AdjustPriceExecuteRequest}
	 * @return {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/price-adjustment-record-detail/adjust-price-execute")
	BaseResponse<AdjustPriceExecuteResponse> adjustPriceExecute(@RequestBody @Valid AdjustPriceExecuteRequest request);

	/**
	 * 调价失败
	 * @param request 调价参数 {@link AdjustPriceExecuteRequest}
	 * @return {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/price-adjustment-record-detail/adjust-price-execute-fail")
	BaseResponse adjustPriceExecuteFail(@RequestBody @Valid AdjustPriceExecuteFailRequest request);


}

