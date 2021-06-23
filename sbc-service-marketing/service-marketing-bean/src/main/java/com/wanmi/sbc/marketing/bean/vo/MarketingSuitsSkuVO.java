package com.wanmi.sbc.marketing.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>组合活动关联商品sku表VO</p>
 * @author zhk
 * @date 2020-04-02 10:51:12
 */
@ApiModel
@Data
public class MarketingSuitsSkuVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	private Long id;

	/**
	 * 组合id
	 */
	@ApiModelProperty(value = "组合id")
	private Long suitsId;

	/**
	 * 促销活动id
	 */
	@ApiModelProperty(value = "促销活动id")
	private Long marketingId;

	/**
	 * skuId
	 */
	@ApiModelProperty(value = "skuId")
	private String skuId;

	/**
	 * 单个优惠价
	 */
	@ApiModelProperty(value = "单个优惠价格")
	private BigDecimal discountPrice;

	/**
	 * sku数量
	 */
	@ApiModelProperty(value = "sku数量")
	private Long num;

	/**
	 * sku名称
	 */
	@ApiModelProperty(value = "sku名称")
	private String goodsInfoName;

	/**
	 * 规格
	 */
	@ApiModelProperty(value = "规格")
	private String specText;

	/**
	 * 分类名称
	 */
	@ApiModelProperty(value = "分类名称")
	private String cateName;

	/**
	 * 品牌名称
	 */
	@ApiModelProperty(value = "品牌名称")
	private String brandName;

	/**
	 * 市场价
	 */
	@ApiModelProperty(value = "市场价")
	private BigDecimal marketPrice;

	/**
	 * sku编码
	 */
	@ApiModelProperty(value = "sku编码")
	private String goodsInfoNo;

	/**
	 * 积分价
	 */
	@ApiModelProperty(value = "积分价")
	private Long buyPoint;

}