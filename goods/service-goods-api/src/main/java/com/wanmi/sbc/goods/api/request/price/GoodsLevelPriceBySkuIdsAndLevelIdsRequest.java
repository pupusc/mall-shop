package com.wanmi.sbc.goods.api.request.price;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 *
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsLevelPriceBySkuIdsAndLevelIdsRequest implements Serializable {

    private static final long serialVersionUID = -1690940262264267333L;

    @ApiModelProperty(value = "商品Id")
    private List<String> skuIds;

    @ApiModelProperty(value = "等级Id")
    private List<Long> levelIds;
}
