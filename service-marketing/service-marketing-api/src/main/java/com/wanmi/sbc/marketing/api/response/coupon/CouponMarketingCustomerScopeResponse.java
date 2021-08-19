package com.wanmi.sbc.marketing.api.response.coupon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author: Geek Wang
 * @createDate: 2019/8/6 17:20
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponMarketingCustomerScopeResponse implements Serializable {

	private List<String> customerIds;
}
