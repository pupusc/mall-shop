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
public class GoodsCustomerPriceBySkuIdsAndCustomerIdRequest implements Serializable {

    private static final long serialVersionUID = 6464640157898240315L;

    @ApiModelProperty(value = "商品Id")
    private List<String> skuIds;

    @ApiModelProperty(value = "用户Id")
    private String customerId;
}
