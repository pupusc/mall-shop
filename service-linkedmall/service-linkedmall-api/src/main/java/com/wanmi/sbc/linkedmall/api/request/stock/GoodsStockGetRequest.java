package com.wanmi.sbc.linkedmall.api.request.stock;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class GoodsStockGetRequest {

    @ApiModelProperty("spu集合")
    private List<Long> providerGoodsIds;

    @ApiModelProperty("配送区域编码")
    private String divisionCode;

    @ApiModelProperty("客户ip")
    private String ip;
}
