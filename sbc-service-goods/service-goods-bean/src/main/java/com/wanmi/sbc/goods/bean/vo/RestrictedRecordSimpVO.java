package com.wanmi.sbc.goods.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>限售记录中间量</p>
 * @author 限售记录
 * @date 2020-04-11 15:59:01
 */
@ApiModel
@Data
public class RestrictedRecordSimpVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 货品主键
	 */
	@ApiModelProperty(value = "货品主键")
	private String skuId;

	/**
	 * 购买的数量
	 */
	@ApiModelProperty(value = "购买的数量")
	private Long num;

}