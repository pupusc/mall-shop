package com.wanmi.sbc.order.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午3:28 2019/3/6
 * @Description: 开店礼包购买请求
 */
@ApiModel
@Data
public class StoreBagsBuyRequest {

    /**
     * 商品skuId
     */
    @ApiModelProperty("商品skuId")
    @NotNull
    private String goodsInfoId;

}
