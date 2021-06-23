package com.wanmi.sbc.marketing.bean.vo;

import java.math.BigDecimal;
import lombok.Data;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>加价购活动VO</p>
 * @author he
 * @date 2021-02-04 16:11:01
 */
@ApiModel
@Data
public class MarkupLevelVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 加价购阶梯id
	 */
	@ApiModelProperty(value = "加价购阶梯id")
	private Long id;

	/**
	 * 加价购id
	 */
	@ApiModelProperty(value = "加价购id")
	private Long markupId;

	/**
	 * 加价购阶梯满足金额
	 */
	@ApiModelProperty(value = "加价购阶梯满足金额")
	private BigDecimal levelAmount;
	/**
	 * 加价购活动阶梯详情
	 */
	@ApiModelProperty(value = "加价购活动阶梯详情")
	private List<MarkupLevelDetailVO> markupLevelDetailList=new ArrayList<>();
	/**
	 * 该营销活动关联的订单商品id集合
	 */
	@ApiModelProperty(value = "该营销活动关联的订单商品id集合")
	private List<String> skuIds;
}