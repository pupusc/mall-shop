package com.wanmi.sbc.goods.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 营销标签
 * Created by hht on 2018/9/18.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponLabelDTO implements Serializable {

    private static final long serialVersionUID = -1118062931014406949L;

    /**
     * 优惠券Id
     */
    @ApiModelProperty(value = "优惠券Id")
    private String couponInfoId;

    /**
     * 优惠券活动Id
     */
    @ApiModelProperty(value = "优惠券活动Id")
    private String couponActivityId;

    /**
     * 促销描述
     */
    @ApiModelProperty(value = "促销描述")
    private String couponDesc;

}
