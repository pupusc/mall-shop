package com.wanmi.sbc.elastic.api.request.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class EsGoodsInfoModifyDistributionBySpuIdRequest implements Serializable {

    /**
     * spuId
     */
    @ApiModelProperty(value = "spuId")
    @NonNull
    private String spuId;

}
