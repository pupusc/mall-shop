package com.wanmi.sbc.goods.api.request.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 代销商品可售参数
 * 1、更改spu时，只需传入spuId
 * 2、只更改sku，不影响spu的可售状态时只需传入skuId
 * 3、如果更改sku，同时更改了spu的可售状态时，spuId和skuId都需要
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
@Builder
public class ProviderGoodsNotSellRequest implements Serializable {

    /**
     * spuIds
     */
    @ApiModelProperty(value = "spuIds")
    private List<String> goodsIds = new ArrayList<>();

    /**
     * skuIds
     */
    @ApiModelProperty(value = "skuIds")
    private List<String> goodsInfoIds = new ArrayList<>();

    /**
     * 是否需要根据商品状态（上架、删除、审核）校验
     */
    @NotNull
    private Boolean checkFlag;

    /**
     * 是否需要更新库存
     */
    private Boolean stockFlag;


}
