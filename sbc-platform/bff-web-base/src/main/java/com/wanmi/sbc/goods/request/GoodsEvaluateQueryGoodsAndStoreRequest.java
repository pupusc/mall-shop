package com.wanmi.sbc.goods.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Auther: jiaojiao
 * @Date: 2019/3/12 13:57
 * @Description:
 */
@ApiModel
@Data
public class GoodsEvaluateQueryGoodsAndStoreRequest {

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    @NotNull
    private Long storeId;

    /**
     * 货品id
     */
    @ApiModelProperty(value = "货品id")
    @NotNull
    private String skuId;

    /**
     * 订单ID
     */
    @ApiModelProperty(value = "订单ID")
    @NotNull
    private String tid;
}
