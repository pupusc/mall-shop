package com.wanmi.sbc.goods.api.response.distributor.goods;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liutao
 * @date 2019/4/12 3:35 PM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistributorGoodsInfoCountsResponse implements Serializable {

    /**
     * 店铺分销商品个数
     */
    @ApiModelProperty(value = "店铺分销商品个数")
    private Long counts;

}
