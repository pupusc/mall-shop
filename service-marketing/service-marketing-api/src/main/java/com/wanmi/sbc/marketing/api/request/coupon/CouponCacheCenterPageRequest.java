package com.wanmi.sbc.marketing.api.request.coupon;

import com.wanmi.sbc.marketing.bean.enums.CouponSceneType;
import com.wanmi.sbc.marketing.bean.enums.CouponType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
public class CouponCacheCenterPageRequest implements Serializable {

    private static final long serialVersionUID = 928232649462063730L;
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

    @ApiModelProperty(value = "优惠券使用场景1商详页+领券中心+购物车2专题页")
    private List<String> couponScene = Stream.of(CouponSceneType.DETAIL_CART_CENTER.getType().toString()).collect(Collectors.toList());

    @ApiModelProperty("活动Id")
    private List<String> activityIds;

    @ApiModelProperty("优惠券ID")
    private List<String> couponInfoIds;

    @ApiModelProperty("活动名称")
    private String activityName;


}
