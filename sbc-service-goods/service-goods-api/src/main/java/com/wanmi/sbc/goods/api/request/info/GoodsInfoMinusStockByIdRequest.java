package com.wanmi.sbc.goods.api.request.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 商品SKU库存减量请求
 * Created by daiyitian on 2017/3/24.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsInfoMinusStockByIdRequest implements Serializable {


    private static final long serialVersionUID = -756726568883495098L;

    /**
     * 商品skuId
     */
    @ApiModelProperty(value = "商品skuId")
    @NotBlank
    private String goodsInfoId;

    /**
     * 库存减量
     */
    @ApiModelProperty(value = "库存减量")
    @NotNull
    private Long stock;

}
