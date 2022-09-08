package com.sbc.wanmi.erp.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class MetaStockInfoVO implements Serializable {

	/**
	 *  商品编码
	 */
	@ApiModelProperty(value = "商品编码")
	private String goodsCode;
	/**
	 *  商品名称
	 */
	@ApiModelProperty(value = "商品名称")
	private String goodsName;
	/**
	 *  SKU编码
	 */
	@ApiModelProperty(value = "SKU编码")
	private String skuCode;
	/**
	 *  SKU商品名称
	 */
	@ApiModelProperty(value = "SKU商品名称")
	private String skuName;
	/**
	 *  成本价
	 */
	@ApiModelProperty(value = "成本价")
	private Integer costPrice;

	/**
	 * 实际库存数
	 */
	@ApiModelProperty(value = "实际库存数")
	private Integer stockActual;

}
