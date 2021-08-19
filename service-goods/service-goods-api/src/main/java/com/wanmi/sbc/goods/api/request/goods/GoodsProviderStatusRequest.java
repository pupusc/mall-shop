package com.wanmi.sbc.goods.api.request.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
@Builder
public class GoodsProviderStatusRequest {

    /**
     * 供应商店铺状态 0：关店 1：开店
     */
    @ApiModelProperty(value = "供应商店铺状态 0：关店 1：开店")
    @NotNull
    private Integer providerStatus;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    @NotEmpty
    private List<Long> storeIds;
}
