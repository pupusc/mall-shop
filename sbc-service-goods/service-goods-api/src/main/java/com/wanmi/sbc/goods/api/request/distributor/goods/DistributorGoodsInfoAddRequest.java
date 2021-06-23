package com.wanmi.sbc.goods.api.request.distributor.goods;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 分销员商品-新增对象
 * @author: Geek Wang
 * @createDate: 2019/2/28 14:22
 * @version: 1.0
 */
@Data
public class DistributorGoodsInfoAddRequest implements Serializable {

    /**
     * 分销员对应的会员ID
     */
    @ApiModelProperty(value = "分销员对应的会员ID")
    private String customerId;

    /**
     * 分销商品SKU编号
     */
    @ApiModelProperty(value = "分销商品SKU编号")
    private String goodsInfoId;

    /**
     * 分销商品SPU编号
     */
    @ApiModelProperty(value = "分销商品SPU编号")
    private String goodsId;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;
}
