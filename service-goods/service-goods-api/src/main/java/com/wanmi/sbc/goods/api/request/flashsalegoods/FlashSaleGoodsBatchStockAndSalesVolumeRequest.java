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
 * @ClassName FlashSaleGoodsBatchStockAndSalesVolumeRequest
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2019/6/22 10:10
 **/
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlashSaleGoodsBatchStockAndSalesVolumeRequest implements Serializable {

    private static final long serialVersionUID = -1339181738177513137L;
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @NotNull
    private Long id;

    /**
     * 商品数量
     */
    @ApiModelProperty(value = "商品数量")
    @NotNull
    private Integer num;
}
