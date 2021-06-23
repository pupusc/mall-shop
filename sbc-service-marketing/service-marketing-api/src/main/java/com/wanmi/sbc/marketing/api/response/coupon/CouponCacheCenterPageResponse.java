package com.wanmi.sbc.marketing.api.response.coupon;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.marketing.bean.vo.CouponVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-23 15:04
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponCacheCenterPageResponse {

    /**
     * 优惠券分页数据
     */
    @ApiModelProperty(value = "优惠券分页数据")
    private MicroServicePage<CouponVO> couponViews;

    /**
     * 平台类目名称
     */
    @ApiModelProperty(value = "平台类目名称map<key为平台类目id，value为平台类目名称>")
    private Map<Long, String> cateMap;

    /**
     * 品牌名称
     */
    @ApiModelProperty(value = "品牌名称map<key为品牌id，value为品牌名称>")
    private Map<Long, String> brandMap;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称map<key为店铺id，value为店铺名称>")
    private Map<Long, String> storeMap;

    /**
     * 店铺分类名称
     */
    @ApiModelProperty(value = "店铺分类名称map<key为店铺分类id，value为店铺分类名称>")
    private Map<Long, String> storeCateMap;

}
