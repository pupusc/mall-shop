package com.wanmi.sbc.goods.api.request.price;

import com.wanmi.sbc.goods.bean.enums.PriceType;
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
public class GoodsLevelPriceBySkuIdsRequest implements Serializable {

    private static final long serialVersionUID = -1869817916426505479L;

    @ApiModelProperty(value = "商品Id")
    private List<String> skuIds;

    @ApiModelProperty(value = "商品类型")
    private PriceType type;

}
