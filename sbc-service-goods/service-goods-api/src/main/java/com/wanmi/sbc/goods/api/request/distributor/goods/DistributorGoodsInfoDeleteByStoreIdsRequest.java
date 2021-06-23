package com.wanmi.sbc.goods.api.request.distributor.goods;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 分销员商品-根据店铺ID集合删除分销员商品对象
 * @author: Geek Wang
 * @createDate: 2019/2/28 14:22
 * @version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistributorGoodsInfoDeleteByStoreIdsRequest implements Serializable {

    /**
     * 店铺ID集合
     */
    @ApiModelProperty(value = "店铺ID集合")
    private List<Long> storeIds;
}
