package com.wanmi.sbc.goods.provider.impl.priceadjustmentrecord;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.priceadjustmentrecord.PriceAdjustmentRecordProvider;
import com.wanmi.sbc.goods.api.request.priceadjustmentrecord.PriceAdjustmentRecordAddRequest;
import com.wanmi.sbc.goods.api.request.priceadjustmentrecord.PriceAdjustmentRecordDelByTimeRequest;
import com.wanmi.sbc.goods.api.response.price.adjustment.PriceAdjustmentRecordAddResponse;
import com.wanmi.sbc.goods.priceadjustmentrecord.model.root.PriceAdjustmentRecord;
import com.wanmi.sbc.goods.priceadjustmentrecord.service.PriceAdjustmentRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>调价记录表保存服务接口实现</p>
 * @author chenli
 * @date 2020-12-09 19:57:21
 */
@RestController
@Validated
public class PriceAdjustmentRecordController implements PriceAdjustmentRecordProvider {
	@Autowired
	private PriceAdjustmentRecordService priceAdjustmentRecordService;

	@Override
	public BaseResponse<PriceAdjustmentRecordAddResponse> add(@RequestBody @Valid PriceAdjustmentRecordAddRequest priceAdjustmentRecordAddRequest) {
		PriceAdjustmentRecord priceAdjustmentRecord = KsBeanUtil.convert(priceAdjustmentRecordAddRequest, PriceAdjustmentRecord.class);
		return BaseResponse.success(new PriceAdjustmentRecordAddResponse(
				priceAdjustmentRecordService.wrapperVo(priceAdjustmentRecordService.add(priceAdjustmentRecord))));
	}

	@Override
	public BaseResponse deleteByTime(@RequestBody @Valid PriceAdjustmentRecordDelByTimeRequest request) {
		priceAdjustmentRecordService.deleteUnconfirmRecord(request.getTime());
		return BaseResponse.SUCCESSFUL();
	}

}

