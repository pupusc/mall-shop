package com.wanmi.sbc.marketing.api.request.coupon;

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
 * @Date: 2018-11-23 15:31
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponCacheListForGoodsListRequest {

    /**
     * 商品信息id 列表
     */
    @ApiModelProperty(value = "商品信息id列表")
    private List<String> goodsInfoIds;

    /**
     * 会员id
     */
    @ApiModelProperty(value = "客户id")
    private String customerId;

    @ApiModelProperty(value = "店铺Id")
    private Long storeId;

}
