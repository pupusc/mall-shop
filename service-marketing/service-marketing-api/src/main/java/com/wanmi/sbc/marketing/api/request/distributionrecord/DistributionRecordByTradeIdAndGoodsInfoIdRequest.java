package com.wanmi.sbc.marketing.api.request.distributionrecord;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * <p>单个查询DistributionRecord请求参数</p>
 * @author baijz
 * @date 2019-02-25 10:28:48
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class DistributionRecordByTradeIdAndGoodsInfoIdRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 分销记录表订单号
	 */
	@NotNull
	@ApiModelProperty(value = "分销记录表订单号")
	private String tradeId;

	/**
	 * 分销记录表货品Id
	 */
	@NotNull
	@ApiModelProperty(value = "分销记录表货品Id")
	private String goodsInfoId;
}