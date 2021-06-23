package com.wanmi.sbc.marketing.api.response.coupon;


import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.marketing.bean.vo.CouponInfoVO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 修改优惠券
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponInfoModifyResponse implements Serializable {

    private static final long serialVersionUID = 7980447608311322872L;


    private CouponInfoVO couponInfoVO;
}
