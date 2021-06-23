package com.wanmi.sbc.marketing.api.response.coupon;

import com.wanmi.sbc.marketing.bean.vo.CouponVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-23 15:29
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponCacheListForGoodsListResponse implements Serializable {

    /**
     * 优惠券数据
     */
    @ApiModelProperty(value = "优惠券数据")
    private List<CouponVO> couponViews;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称map<key为店铺id，value为店铺名称>")
    private Map<Long, String> storeMap;

}
