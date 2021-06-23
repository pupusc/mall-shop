package com.wanmi.sbc.goods.api.request.distributor.goods;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 分销员商品-根据店铺ID删除分销员商品对象
 * @author: Geek Wang
 * @createDate: 2019/2/28 14:22
 * @version: 1.0
 */
@Data
public class DistributorGoodsInfoDeleteByStoreIdRequest implements Serializable {

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;
}
