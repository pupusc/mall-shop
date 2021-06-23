package com.wanmi.sbc.marketing.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 优惠券活动目标客户作用范围DTO
 */
@ApiModel
@Data
public class CouponMarketingCustomerScopeDTO {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "目标客户作用范围id")
    private String marketingCustomerScopeId;

    /**
     * 优惠券活动id
     */
    @ApiModelProperty(value = "优惠券活动id")
    private String activityd;


    /**
     * 会员id
     */
    @ApiModelProperty(value = "会员id")
    private String customerId;
}
