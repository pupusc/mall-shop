package com.wanmi.sbc.marketing.coupon.response;

import com.wanmi.sbc.marketing.coupon.model.vo.CouponView;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * 优惠券 领券中心
 */
@Data
@Builder
public class CouponCenterPageResponse {

    /**
     * 优惠券分页数据
     */
    private Page<CouponView> couponViews;

    /**
     * 平台类目名称
     */
    private Map<Long, String> cateMap;

    /**
     * 品牌名称
     */
    private Map<Long, String> brandMap;

    /**
     * 店铺名称
     */
    private Map<Long, String> storeMap;

    /**
     * 店铺分类名称
     */
    private Map<Long, String> storeCateMap;
}
