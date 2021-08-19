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
public class EsGoodsInitProviderGoodsInfoRequest implements Serializable {

    /**
     * 供应商关联的商家商品id
     */
    @ApiModelProperty(value = "供应商关联的商家商品id")
    private List<String> providerGoodsIds;

    @ApiModelProperty(value = "店铺Id")
    private Long storeId;

}
