package com.wanmi.sbc.marketing.api.request.coupon;

import com.wanmi.sbc.marketing.bean.enums.CouponType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @ApiModelProperty("优惠券领券场景:1商详页2领券中心3购物车4专题页")
    private Integer couponScene;

    @ApiModelProperty("活动Id")
    private List<String> activityIds;

    @ApiModelProperty("优惠券ID")
    private List<String> couponInfoIds;

    @ApiModelProperty("活动名称")
    private String activityName;

    @ApiModelProperty("活动状态1生效中2生效中+待生效")
    private Integer activityStatus = 1 ;

}
