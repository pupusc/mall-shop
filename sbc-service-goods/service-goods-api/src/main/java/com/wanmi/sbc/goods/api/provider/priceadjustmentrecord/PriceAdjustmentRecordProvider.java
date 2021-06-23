package com.wanmi.sbc.goods.api.provider.priceadjustmentrecord;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.priceadjustmentrecord.PriceAdjustmentRecordAddRequest;
import com.wanmi.sbc.goods.api.request.priceadjustmentrecord.PriceAdjustmentRecordDelByTimeRequest;
import com.wanmi.sbc.goods.api.response.price.adjustment.PriceAdjustmentRecordAddResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>调价记录表保存服务Provider</p>
 * @author chenli
 * @date 2020-12-09 19:57:21
 */
@FeignClient(value = "${application.goods.name}", contextId = "PriceAdjustmentRecordProvider")
public interface PriceAdjustmentRecordProvider {

	/**
	 * 新增调价记录表API
	 *
	 * @author chenli
	 * @param priceAdjustmentRecordAddRequest 调价记录表新增参数结构 {@link PriceAdjustmentRecordAddRequest}
	 * @return 新增的调价记录表信息 {@link PriceAdjustmentRecordAddResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/price-adjustment-record/add")
	BaseResponse<PriceAdjustmentRecordAddResponse> add(@RequestBody @Valid PriceAdjustmentRecordAddRequest priceAdjustmentRecordAddRequest);

	/**
	 * 删除未确认的调价记录API
	 *
	 * @author chenli
	 * @param priceAdjustmentRecordDelByTimeRequest 参数结构 {@link PriceAdjustmentRecordDelByTimeRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/price-adjustment-record/delete-by-time")
	BaseResponse deleteByTime(@RequestBody @Valid PriceAdjustmentRecordDelByTimeRequest priceAdjustmentRecordDelByTimeRequest);


}

