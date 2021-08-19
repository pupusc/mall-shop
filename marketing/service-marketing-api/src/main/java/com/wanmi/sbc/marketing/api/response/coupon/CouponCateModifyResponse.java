package com.wanmi.sbc.marketing.api.response.coupon;

import com.wanmi.sbc.common.enums.DefaultFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-23
 */
@ApiModel
@Data
public class CouponCateModifyResponse implements Serializable {

    private static final long serialVersionUID = 1386170428239712766L;

    /**
     * 优惠券分类Id
     */
    @ApiModelProperty(value = "优惠券分类Id")
    private String couponCateId;

    /**
     * 优惠券分类名称
     */
    @ApiModelProperty(value = "优惠券分类名称")
    private String couponCateName;

    /**
     * 是否平台专用 0：否，1：是
     */
    @ApiModelProperty(value = "是否平台专用")
    private DefaultFlag onlyPlatformFlag;

}
