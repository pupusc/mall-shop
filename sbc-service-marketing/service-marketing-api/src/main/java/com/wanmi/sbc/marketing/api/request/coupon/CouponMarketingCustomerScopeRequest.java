package com.wanmi.sbc.marketing.api.request.coupon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: Geek Wang
 * @createDate: 2019/8/6 17:20
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponMarketingCustomerScopeRequest implements Serializable {

	private String activityId;
}
