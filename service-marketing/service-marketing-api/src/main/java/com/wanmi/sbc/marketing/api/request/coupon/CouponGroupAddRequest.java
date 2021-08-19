package com.wanmi.sbc.marketing.api.request.coupon;


import com.wanmi.sbc.marketing.bean.dto.DistributionRewardCouponDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author: Geek Wang
 * @createDate: 2019/4/19 16:47
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponGroupAddRequest implements Serializable {

	/**
	 * 邀请人会员id
	 */
	@NotBlank
	private String requestCustomerId;

	/**
	 * 优惠券ID和组数集合
	 */
	@NotNull
	private List<DistributionRewardCouponDTO> distributionRewardCouponDTOList;

}
