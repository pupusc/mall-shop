package com.wanmi.sbc.crm.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>运营计划与优惠券关联新增参数</p>
 * @author dyt
 * @date 2020-01-08 14:11:18
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPlanCouponDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 优惠券Id
	 */
	@ApiModelProperty(value = "优惠券Id")
	@NotBlank
	private String couponId;

	/**
	 * 赠送数量
	 */
	@ApiModelProperty(value = "赠送数量")
	@NotNull
	@Max(9999999999L)
	private Integer giftCount;

}