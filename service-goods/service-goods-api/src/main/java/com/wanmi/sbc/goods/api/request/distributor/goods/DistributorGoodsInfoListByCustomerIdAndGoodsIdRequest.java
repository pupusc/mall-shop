package com.wanmi.sbc.goods.api.request.distributor.goods;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 分销员商品-根据分销员的会员ID和SPU编号查询分销员商品对象
 * @author: Geek Wang
 * @createDate: 2019/2/28 14:22
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistributorGoodsInfoListByCustomerIdAndGoodsIdRequest implements Serializable {

    /**
     * 分销员对应的会员ID
     */
    @ApiModelProperty(value = "分销员对应的会员ID")
    private String distributorId;

    /**
     * SKU编号
     */
    @ApiModelProperty(value = "SKU编号")
    private String goodsInfoId;

    /**
     * spu编号
     */
    @ApiModelProperty(value = "spu编号")
    private String goodsId;
}
