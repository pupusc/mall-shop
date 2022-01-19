package com.wanmi.sbc.marketing.api.request.coupon;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponPageQueryRequest extends BaseQueryRequest {

    private static final long serialVersionUID = 7159572823580317033L;

    private String activityName;

    private String couponScene;
}
