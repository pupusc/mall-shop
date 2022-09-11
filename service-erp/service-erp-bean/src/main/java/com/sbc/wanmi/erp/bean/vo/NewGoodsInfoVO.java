package com.sbc.wanmi.erp.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class NewGoodsInfoVO implements Serializable {

	/**
	 *  商品编码
	 */
	@ApiModelProperty(value = "商品编码")
	private String goodsCode;
	/**
	 *  商品名称
	 */
	@ApiModelProperty(value = "商品名称")
	private String name;

	/**
	 *  当前仓出仓成本
	 */
	@ApiModelProperty(value = "当前仓出仓成本")
	private Integer whStockCost;

	/**
	 * 总库存数
	 */
	@ApiModelProperty(value = "总库存数")
	private Integer whStockSum;

	/**
	 * 实际库存数
	 */
	@ApiModelProperty(value = "实际库存数")
	private Integer whStockActual;

	/**
	 * 仓库编码
	 */
	@ApiModelProperty(value = "仓库编码")
	private String whCode;

	/**
	 * 是否有效
	 */
	@ApiModelProperty(value = "是否有效")
	private Integer validFlag;

}
