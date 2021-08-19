package com.wanmi.sbc.goods.api.request.distributor.goods;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 分销员商品-根据分销员-会员ID和SKU编号删除分销员商品对象
 * @author: Geek Wang
 * @createDate: 2019/2/28 14:22
 * @version: 1.0
 */
@Data
public class DistributorGoodsInfoDeleteRequest implements Serializable {

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
}
