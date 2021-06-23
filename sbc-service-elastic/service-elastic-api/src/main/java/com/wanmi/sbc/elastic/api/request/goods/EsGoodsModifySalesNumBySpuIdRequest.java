package com.wanmi.sbc.elastic.api.request.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class EsGoodsModifySalesNumBySpuIdRequest implements Serializable {

    /**
     * spuId
     */
    @ApiModelProperty(value = "spuId")
    private String spuId;

    @ApiModelProperty(value = "销量")
    private Long salesNum;

}
