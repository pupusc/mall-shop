package com.wanmi.sbc.marketing.bean.vo;

import com.wanmi.sbc.common.enums.DefaultFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-23
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponCateVO implements Serializable {

    private static final long serialVersionUID = -8478024407902817306L;

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
