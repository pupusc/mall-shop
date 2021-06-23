package com.wanmi.sbc.elastic.api.request.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class EsGoodsDeleteStoreCateRequest implements Serializable {

    @ApiModelProperty(value = "店铺分类ids")
    private List<Long> storeCateIds;

    @ApiModelProperty(value = "店铺Id")
    private Long storeId;
}
