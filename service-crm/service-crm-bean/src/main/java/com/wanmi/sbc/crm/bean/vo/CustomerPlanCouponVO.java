package com.wanmi.sbc.crm.bean.vo;

import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>运营计划与优惠券关联VO</p>
 * @author dyt
 * @date 2020-01-08 14:11:18
 */
@ApiModel
@Data
public class CustomerPlanCouponVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	private Long id;

	/**
	 * 计划id
	 */
	@ApiModelProperty(value = "计划id")
	private Long planId;

	/**
	 * 优惠券Id
	 */
	@ApiModelProperty(value = "优惠券Id")
	private String couponId;

	/**
	 * 赠送数量
	 */
	@ApiModelProperty(value = "赠送数量")
	private Integer giftCount;

}