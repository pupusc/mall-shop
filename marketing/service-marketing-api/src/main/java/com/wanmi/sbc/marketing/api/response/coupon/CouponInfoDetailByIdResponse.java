package com.wanmi.sbc.marketing.api.response.coupon;


import com.wanmi.sbc.marketing.bean.vo.CouponGoodsVO;
import com.wanmi.sbc.marketing.bean.vo.CouponInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 优惠券详情
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponInfoDetailByIdResponse implements Serializable {

    private static final long serialVersionUID = -972475934117464655L;

    /**
     * 优惠券实体对象
     */
    @ApiModelProperty(value = "优惠券实体对象")
    private CouponInfoVO couponInfo;


    /**
     * 优惠券指定商品
     */
    @ApiModelProperty(value = "优惠券指定商品")
    private CouponGoodsVO goodsList;
}
