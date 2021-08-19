package com.wanmi.sbc.marketing.bean.dto;

import com.wanmi.sbc.common.enums.DefaultFlag;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Enumerated;
import java.io.Serializable;

/**
 * @author: Geek Wang
 * @createDate: 2019/8/7 10:17
 * @version: 1.0
 */
@Data
public class CouponActivityConfigAndCouponInfoDTO implements Serializable {

	/**
	 * 优惠券活动配置表id
	 */
	@ApiModelProperty(value = "优惠券活动配置表id")
	private String activityConfigId;

	/**
	 * 活动id
	 */
	@ApiModelProperty(value = "优惠券活动id")
	private String activityId;

	/**
	 * 优惠券id
	 */
	@ApiModelProperty(value = "优惠券id")
	private String couponId;


	/**
	 * 优惠券总张数
	 */
	@ApiModelProperty(value = "优惠券总张数")
	private Long totalCount;

	/**
	 * 是否有剩余, 1 有，0 没有
	 */
	@ApiModelProperty(value = "优惠券是否有剩余")
	@Enumerated
	private DefaultFlag hasLeft;

	@ApiModelProperty(value = "优惠券信息")
	private CouponInfoDTO couponInfoDTO;
}
