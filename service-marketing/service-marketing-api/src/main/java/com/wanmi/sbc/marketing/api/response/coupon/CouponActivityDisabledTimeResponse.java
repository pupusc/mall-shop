package com.wanmi.sbc.marketing.api.response.coupon;

import com.wanmi.sbc.marketing.bean.vo.CouponActivityDisabledTimeVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
public class CouponActivityDisabledTimeResponse {

    @ApiModelProperty(value = "优惠券活动失效时间列表")
    private List<CouponActivityDisabledTimeVO> couponActivityDisabledTimeVOList;

}
