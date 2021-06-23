package com.wanmi.sbc.marketing.coupon.response;


import com.wanmi.sbc.goods.api.response.info.GoodsInfoResponse;
import com.wanmi.sbc.marketing.bean.vo.CouponInfoVO;
import lombok.Data;

/**
 * 优惠券详情
 */
@Data
public class CouponInfoResponse {
    /**
     * 优惠券实体对象
     */
    private CouponInfoVO couponInfo;


    /**
     * 指定商品
     */
    private GoodsInfoResponse goodsList;


}
