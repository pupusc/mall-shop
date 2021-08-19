package com.wanmi.sbc.marketing.bean.vo;

import java.math.BigDecimal;

import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>加价购活动VO</p>
 * @author he
 * @date 2021-02-04 16:11:24
 */
@ApiModel
@Data
public class MarkupLevelDetailVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 加价购阶梯详情id
	 */
	@ApiModelProperty(value = "加价购阶梯详情id")
	private Long id;

	/**
	 * 加价购活动关联id
	 */
	@ApiModelProperty(value = "加价购活动关联id")
	private Long markupId;

	/**
	 * 加价购阶梯关联id
	 */
	@ApiModelProperty(value = "加价购阶梯关联id")
	private Long markupLevelId;

	/**
	 * 加购商品加购价格
	 */
	@ApiModelProperty(value = "加购商品加购价格")
	private BigDecimal markupPrice;

	/**
	 * 加购商品关联sku 
	 */
	@ApiModelProperty(value = "加购商品关联sku ")
	private String goodsInfoId;
	/**
	 * 加购商品关联
	 */
	@ApiModelProperty(value = "加购商品关联 ")
	private GoodsInfoVO goodsInfo;
	/**
	 * 该营销活动关联的订单商品id集合
	 */
	@ApiModelProperty(value = "该营销活动关联的订单商品id集合")
	private List<String> skuIds;
}