package com.wanmi.sbc.goods.api.request.flashsalegoods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName FlashSaleGoodsBatchMinusStockRequest
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2019/6/21 10:59
 **/
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlashSaleGoodsBatchMinusStockRequest implements Serializable {

    private static final long serialVersionUID = -9206019404666296169L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @NotNull
    private Long id;

    /**
     * 扣减库存数
     */
    @ApiModelProperty(value = "扣减库存数")
    @NotNull
    private Integer stock;
}
