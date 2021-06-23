package com.wanmi.sbc.goods.api.request.groupongoodsinfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p> 根据活动ID、SKU编号更新商品销售量、订单量、交易额</p>
 * @author groupon
 * @date 2019-05-15 14:49:12
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponGoodsInfoModifyStatisticsNumRequest implements Serializable{
	private static final long serialVersionUID = 1L;

	/**
	 * 拼团活动ID
	 */
	@ApiModelProperty(value = " 拼团活动ID")
	@NotBlank
	private String grouponActivityId;

	/**
	 * sku编号
	 */
	@ApiModelProperty(value = "SKU编号")
	@NotBlank
	private String goodsInfoId;

	/**
	 * 商品销售量
	 */
	@ApiModelProperty(value = "商品销售量")
	@NotNull
	private Integer goodsSalesNum;

	/**
	 * 订单量
	 */
	@ApiModelProperty(value = "订单量")
	@NotNull
	private Integer orderSalesNum;

	/**
	 * 交易额
	 */
	@ApiModelProperty(value = "交易额")
	@NotNull
	private BigDecimal tradeAmount;
}