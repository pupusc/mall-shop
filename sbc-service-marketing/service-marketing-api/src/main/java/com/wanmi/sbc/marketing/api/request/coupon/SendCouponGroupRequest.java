package com.wanmi.sbc.marketing.api.request.coupon;

import com.wanmi.sbc.marketing.api.response.coupon.GetCouponGroupResponse;
import com.wanmi.sbc.marketing.bean.vo.CouponInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ApiModel
@Data
public class SendCouponGroupRequest implements Serializable {

    @ApiModelProperty(value = "客户id")
    @NotNull
    private String customerId;

    @ApiModelProperty(value = "活动id")
    @NotNull
    private String activityId;

    @ApiModelProperty(value = "优惠券ids")
    private List<GetCouponGroupResponse> couponInfos = new ArrayList<>();;


}
