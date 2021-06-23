package com.wanmi.sbc.goods.api.request.adjustprice;

import com.wanmi.sbc.goods.bean.enums.PriceAdjustmentType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Api
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PriceAdjustmentTemplateExportRequest implements Serializable {

    private static final long serialVersionUID = 6734574270946732544L;

    @ApiModelProperty(value = "调价类型：0 市场价、 1 等级价、2 阶梯价、3 供货价")
    @NotNull
    private PriceAdjustmentType priceAdjustmentType;

    @ApiModelProperty(value = "店铺id")
    @NotNull
    private Long storeId;
}
