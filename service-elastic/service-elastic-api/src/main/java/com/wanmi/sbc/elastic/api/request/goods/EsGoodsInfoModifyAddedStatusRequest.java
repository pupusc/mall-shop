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
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
@Builder
public class EsGoodsInfoModifyAddedStatusRequest implements Serializable {

    /**
     * 上下架状态
     */
    @ApiModelProperty(value = "上下架状态")
    private Integer addedFlag;

    @ApiModelProperty(value = "商品id列表")
    private List<String> goodsIds;

    @ApiModelProperty(value = "商品skuId列表")
    private List<String> goodsInfoIds;

}
