package com.wanmi.sbc.goods.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 营销标签
 * Created by hht on 2018/9/18.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponLabelVO {

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

    @ApiModelProperty(value = "使用场景，1专题2商详3领券中心，可多选，用，分隔")
    private String couponScene;

}
