package com.wanmi.sbc.marketing.api.request.distributionrecord;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * <p>单个软删除DistributionRecord请求参数</p>
 * @author baijz
 * @date 2019-02-27 18:56:40
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributionRecordDelByTradeIdAndGoodsInfoIdRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 交易订单号
	 */
	@NotNull
	private String tradeId;

	/**
	 * 商品Id
	 */
	@NotNull
	private String goodsInfoId;
}