package com.wanmi.sbc.elastic.api.request.goods;

import com.wanmi.sbc.goods.bean.enums.PriceAdjustmentType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class EsGoodsInfoAdjustPriceRequest implements Serializable {

    /**
     * skuId
     */
    @ApiModelProperty(value = "skuId")
    @NotEmpty
    private List<String> goodsInfoIds;

    /**
     * 调价类型
     */
    @ApiModelProperty(value = "调价类型")
    @NonNull
    private PriceAdjustmentType type;
}
