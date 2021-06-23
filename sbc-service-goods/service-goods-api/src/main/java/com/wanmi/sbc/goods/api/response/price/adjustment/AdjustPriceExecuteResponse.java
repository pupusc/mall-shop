package com.wanmi.sbc.goods.api.response.price.adjustment;

import com.wanmi.sbc.goods.bean.enums.PriceAdjustmentType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdjustPriceExecuteResponse implements Serializable {

    private static final long serialVersionUID = -5804736324442295566L;

    /**
     * 商品skuId
     */
    @ApiModelProperty(value = "商品skuId")
    private List<String> skuIds;

    /**
     * 调价类型
     */
    @ApiModelProperty(value = "调价类型")
    private PriceAdjustmentType type;
}
