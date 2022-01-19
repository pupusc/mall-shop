package com.wanmi.sbc.marketing.api.request.coupon;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import lombok.Data;

@Data
public class CouponPageQueryRequest extends BaseQueryRequest {

    private String activityName;

    private String couponScene;
}
