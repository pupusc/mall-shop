package com.wanmi.sbc.marketing.api.response.coupon;

import com.wanmi.sbc.marketing.bean.vo.CouponActivityVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponActivityModifyResponse implements Serializable {

    @ApiModelProperty(value = "优惠券活动")
    private CouponActivityVO couponActivity;

}
