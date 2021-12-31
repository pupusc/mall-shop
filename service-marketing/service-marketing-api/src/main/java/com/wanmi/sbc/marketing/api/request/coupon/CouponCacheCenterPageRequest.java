package com.wanmi.sbc.marketing.api.request.coupon;

import com.wanmi.sbc.marketing.bean.enums.CouponType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-23 14:52
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponCacheCenterPageRequest {

    /**
     * 优惠券分类id
     */
    @ApiModelProperty(value = "优惠券分类id")
    private String couponCateId;

    /**
     * 优惠券类型
     */
    @ApiModelProperty(value = "优惠券类型")
    private CouponType couponType;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "客户id")
    private String customerId;

    /**
     * 分页页码
     */
    @ApiModelProperty(value = "分页页码")
    private int pageNum;

    /**
     * 每页数量
     */
    @ApiModelProperty(value = "每页数量")
    private int pageSize;

    @ApiModelProperty(value = "店铺Id")
    private Long storeId;

    @ApiModelProperty("优惠券使用场景")
    private Integer couponScene;

}
