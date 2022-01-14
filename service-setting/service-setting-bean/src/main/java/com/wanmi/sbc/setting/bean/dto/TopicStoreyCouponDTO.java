package com.wanmi.sbc.setting.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class TopicStoreyCouponDTO implements Serializable {
    private static final long serialVersionUID = 1542610117889056399L;

    @ApiModelProperty("活动ID")
    private String activityId;

    @ApiModelProperty("优惠券Id")
    private String couponId;

    @ApiModelProperty("立即领取图片")
    private String receiveImageUrl;

    @ApiModelProperty("立即使用图片")
    private String useImageUrl;

    @ApiModelProperty("领完图片")
    private String doneImageUrl;

    @ApiModelProperty("活动名称")
    private String activityName;

    @ApiModelProperty(value = "购满多少钱")
    private Double fullBuyPrice;

    @ApiModelProperty(value = "购满类型0：无门槛，1：满N元可使用")
    private Integer fullBuyType;

    @ApiModelProperty(value = "优惠券面值")
    private Double denomination;

    @ApiModelProperty(value = "优惠券Id")
    private String activityConfigId;

}
