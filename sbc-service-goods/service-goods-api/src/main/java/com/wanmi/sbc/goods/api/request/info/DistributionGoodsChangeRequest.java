package com.wanmi.sbc.goods.api.request.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @program: sbc-micro-service
 * @description:
 * @create: 2019-03-11 16:05
 **/
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DistributionGoodsChangeRequest implements Serializable {

    @ApiModelProperty(value = "spuId,商品ID")
    @NotNull
    private String goodsId;

    @ApiModelProperty(value = "是否返回规格明细")
    private Boolean showSpecFlag;

    @ApiModelProperty(value = "是否返回可售性")
    private Boolean showVendibilityFlag;

    @ApiModelProperty(value = "是否返回供应商商品相关信息")
    private Boolean showProviderInfoFlag;

    @ApiModelProperty(value = "是否填充LM商品库存")
    private Boolean fillLmInfoFlag;
}
