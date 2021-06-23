package com.wanmi.sbc.marketing.bean.vo;

import com.wanmi.sbc.common.enums.DefaultFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-23 15:53
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponCacheVO implements Serializable {


    private static final long serialVersionUID = -6012270586980624950L;
    /**
     * 优惠券主键Id
     */
    @ApiModelProperty(value = "优惠券主键Id")
    private String id;

    /**
     * 优惠券活动配置表id
     */
    @ApiModelProperty(value = "优惠券活动配置表id")
    private String activityConfigId;

    /**
     * 优惠券是否有剩余
     */
    @ApiModelProperty(value = "优惠券是否有剩余")
    private DefaultFlag hasLeft;

    /**
     * 优惠券总张数
     */
    @ApiModelProperty(value = "优惠券总张数")
    private Long totalCount;

    /**
     * 优惠券活动Id
     */
    @ApiModelProperty(value = "优惠券活动Id")
    private String couponActivityId;

    /**
     * 优惠券Id
     */
    @ApiModelProperty(value = "优惠券Id")
    private String couponInfoId;

    /**
     * 优惠券关联 商品/分类/品牌 ids
     */
    @ApiModelProperty(value = "优惠券商品作用范围列表")
    private List<CouponMarketingScopeVO> scopes;

    /**
     * 优惠券缓存
     */
    @ApiModelProperty(value = "优惠券信息缓存")
    private CouponInfoCacheVO couponInfo;

    /**
     * 优惠券活动缓存
     */
    @ApiModelProperty(value = "优惠券活动缓存")
    private CouponActivityCacheVO couponActivity;

    /**
     * 当前优惠券分类Id
     */
    @ApiModelProperty(value = "当前优惠券分类Id集合")
    private List<String> couponCateIds;

}
