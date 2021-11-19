package com.wanmi.sbc.elastic.api.request.goods;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Data
public class EsGoodsAtmosphereRequest implements Serializable {

    @ApiModelProperty(value = "skuId")
    @NotEmpty
    private List<String> goodsInfoIds;

}
