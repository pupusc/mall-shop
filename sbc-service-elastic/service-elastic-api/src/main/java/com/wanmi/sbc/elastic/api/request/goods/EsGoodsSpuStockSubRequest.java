package com.wanmi.sbc.elastic.api.request.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class EsGoodsSpuStockSubRequest implements Serializable {

    @ApiModelProperty(value = "spuId")
    private String spuId;

    @ApiModelProperty(value = "库存")
    private Long stock;

    @ApiModelProperty(value = "批量更新ES库存")
    private Map<String, Integer> spusMap;
}
