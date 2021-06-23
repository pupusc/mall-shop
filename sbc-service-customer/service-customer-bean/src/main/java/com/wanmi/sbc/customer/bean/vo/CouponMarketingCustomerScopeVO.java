package com.wanmi.sbc.customer.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>优惠券活动目标客户作用范围VO</p>
 * @author lq
 * @date 2019-08-02 14:50:57
 */
@ApiModel
@Data
public class CouponMarketingCustomerScopeVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	private String marketingCustomerScopeId;

	/**
	 * 优惠券活动id
	 */
	@ApiModelProperty(value = "优惠券活动id")
	private String activityId;

	/**
	 * 会员id
	 */
	@ApiModelProperty(value = "会员id")
	private String customerId;

}