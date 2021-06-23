package com.wanmi.sbc.marketing.api.response.coupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
public class GetRegisterOrStoreCouponResponse {

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String desc;

    /**
     * 优惠券列表
     */
    @ApiModelProperty(value = "优惠券列表")
    private List<GetCouponGroupResponse> couponList;

}
