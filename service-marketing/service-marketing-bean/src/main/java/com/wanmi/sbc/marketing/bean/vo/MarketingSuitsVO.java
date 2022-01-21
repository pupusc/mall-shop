package com.wanmi.sbc.marketing.bean.vo;

import java.math.BigDecimal;
import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>组合商品主表VO</p>
 * @author zhk
 * @date 2020-04-01 20:54:00
 */
@ApiModel
@Data
public class MarketingSuitsVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	private Long id;

	/**
	 * 促销id
	 */
	@ApiModelProperty(value = "促销id")
	private Long marketingId;

	/**
	 * 套餐主图（图片url全路径）
	 */
	@ApiModelProperty(value = "套餐主图（图片url全路径）")
	private String mainImage;

	/**
	 * 套餐价格
	 */
	@ApiModelProperty(value = "套餐价格")
	private BigDecimal suitsPrice;

	@ApiModelProperty("场景1商详页组合优惠2商详页书单组合优惠")
	private Integer suitScene;

}