package com.wanmi.sbc.goods.bean.vo;

import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>周期购活动VO</p>
 * @author weiwenhao
 * @date 2021-01-21 19:42:48
 */
@ApiModel
@Data
public class CycleBuyGiftVO implements Serializable {


	private static final long serialVersionUID = 5643869047589822351L;

	/**
	 * 周期购赠品表Id
	 */
	@ApiModelProperty(value = "周期购赠品表Id")
	private Long id;

	/**
	 * 关联周期购主键Id
	 */
	@ApiModelProperty(value = "关联周期购主键Id")
	private Long cycleBuyId;

	/**
	 * 赠送商品Id
	 */
	@ApiModelProperty(value = "赠送商品Id")
	private String goodsInfoId;

	/**
	 * 赠品数量
	 */
	@ApiModelProperty(value = "赠品数量")
	private Long freeQuantity;

	/**
	 * 赠品sku信息
	 */
	@ApiModelProperty(value = "赠品sku信息")
	private GoodsInfoVO goodsInfoVO;

}