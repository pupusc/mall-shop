package com.wanmi.sbc.erp.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * @program: sbc-background
 * @description: ERP商品库存同步参数
 * @author: 0F3685-wugongjiang
 * @create: 2021-01-28 17:57
 **/
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SynGoodsInfoRequest implements Serializable {

    /**
     * 商品SPU代码
     */
    @ApiModelProperty(value = "商品SPU代码")
    private String spuCode;

    /**
     * 商品SKU编码
     */
    @ApiModelProperty(value = "商品SKU编码")
    private String skuCode;
}
