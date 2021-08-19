package com.wanmi.sbc.goods.api.request.flashsalegoods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName FlashSaleGoodsBatchPlusStockRequest
 * @Description
 * @Author lvzhenwei
 * @Date 2019/6/22 9:43
 **/
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlashSaleGoodsBatchPlusSalesVolumeRequest implements Serializable {
    private static final long serialVersionUID = -4699562876825487090L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @NotNull
    private Long id;

    /**
     * 增加销量数
     */
    @ApiModelProperty(value = "增加销量数")
    @NotNull
    private Integer salesVolume;
}
