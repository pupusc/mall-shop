package com.wanmi.sbc.goods.api.request.presellsale;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel
public class PresellSaleGoodsByIdRequest implements Serializable {

    @ApiModelProperty(value = "预售活动关联商品id")
    private String presellSaleGoodsId;
}
