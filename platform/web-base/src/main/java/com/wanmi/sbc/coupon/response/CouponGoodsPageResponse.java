package com.wanmi.sbc.coupon.response;

import com.wanmi.sbc.elastic.api.response.goods.EsGoodsInfoResponse;
import com.wanmi.sbc.marketing.api.response.coupon.CouponGoodsListResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 优惠券凑单页返回
 */
@ApiModel
@Data
public class CouponGoodsPageResponse {

    /**
     * 商品列表数据
     */
    @ApiModelProperty(value = "商品列表数据")
    private EsGoodsInfoResponse esGoodsInfoResponse;

    /**
     * 优惠券信息
     */
    @ApiModelProperty(value = "优惠券信息")
    private CouponGoodsListResponse couponInfo;
}
