package com.wanmi.sbc.goods.bean.dto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * <p>周期购活动商品信息传输对象DTO</p>
 * @author weiwenhao
 * @date 2021-01-21 20:01:50
 */
@ApiModel
@Data
public class CycleBuyGoodsInfoDTO extends GoodsInfoDTO {

	/**
	 * 周期购商品-期数
	 */
	@ApiModelProperty(value = "周期购商品-期数")
	private String numberPeriods;



}