package com.wanmi.sbc.marketing.api.provider.distributionrecord;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.distributionrecord.*;
import com.wanmi.sbc.marketing.api.response.distributionrecord.DistributionRecordAddResponse;
import com.wanmi.sbc.marketing.api.response.distributionrecord.DistributionRecordModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>DistributionRecord保存服务Provider</p>
 * @author baijz
 * @date 2019-02-27 18:56:40
 */
@FeignClient(value = "${application.marketing.name}", contextId = "DistributionRecordSaveProvider")
public interface DistributionRecordSaveProvider {

	/**
	 * 新增DistributionRecordAPI
	 *
	 * @author baijz
	 * @param distributionRecordAddRequest DistributionRecord新增参数结构 {@link DistributionRecordAddRequest}
	 * @return 新增的DistributionRecord信息 {@link DistributionRecordAddResponse}
	 */
	@PostMapping("/marketing/${application.marketing.version}/distributionrecord/add")
	BaseResponse<DistributionRecordAddResponse> add(@RequestBody @Valid DistributionRecordAddRequest
                                                            distributionRecordAddRequest);

	/**
	 * 修改DistributionRecordAPI
	 *
	 * @author baijz
	 * @param distributionRecordModifyRequest DistributionRecord修改参数结构 {@link DistributionRecordModifyRequest}
	 * @return 修改的DistributionRecord信息 {@link DistributionRecordModifyResponse}
	 */
	@PostMapping("/marketing/${application.marketing.version}/distributionrecord/modify")
	BaseResponse modify(@RequestBody @Valid DistributionRecordModifyRequest distributionRecordModifyRequest);

	/**
	 * 单个软删除DistributionRecordAPI
	 *
	 * @author baijz
	 * @param distributionRecordDelByTradeIdAndGoodsInfoIdRequest 单个软删除参数结构
	 * {@link DistributionRecordDelByTradeIdAndGoodsInfoIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/marketing/${application.marketing.version}/distributionrecord/delete-by-id")
	BaseResponse deleteByTradeIdAndGoodsInfoId(@RequestBody @Valid
													   DistributionRecordDelByTradeIdAndGoodsInfoIdRequest distributionRecordDelByTradeIdAndGoodsInfoIdRequest);

	/**
	 * 批量删除DistributionRecordAPI
	 *
	 * @author baijz
	 * @param distributionRecordDelByIdListRequest 批量删除参数结构 {@link DistributionRecordDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/marketing/${application.marketing.version}/distributionrecord/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid DistributionRecordDelByIdListRequest distributionRecordDelByIdListRequest);

	/**
	 * 根据订单id批量软删除DistributionRecordAPI
	 *
	 * @author baijz
	 * @param distributionRecordByTradeIdRequest 批量删除参数结构 {@link DistributionRecordByTradeIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/marketing/${application.marketing.version}/distributionrecord/delete-by-trade-id")
	BaseResponse deleteByTradeId(@RequestBody @Valid DistributionRecordByTradeIdRequest distributionRecordByTradeIdRequest);
}

