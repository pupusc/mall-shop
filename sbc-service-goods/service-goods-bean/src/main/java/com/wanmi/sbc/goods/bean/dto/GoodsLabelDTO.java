package com.wanmi.sbc.goods.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品标签DTO
 */
@ApiModel
@Data
public class GoodsLabelDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 标签id
	 */
	@ApiModelProperty(value = "标签id")
	private Long goodsLabelId;

	/**
	 * 标签名称
	 */
	@ApiModelProperty(value = "标签名称")
	private String labelName;

	/**
	 * 前端是否展示 0: 关闭 1:开启
	 */
	@ApiModelProperty(value = "前端是否展示 0: 关闭 1:开启")
	private Boolean labelVisible;

	/**
	 * 排序
	 */
	@ApiModelProperty(value = "排序")
	private Integer labelSort;
}