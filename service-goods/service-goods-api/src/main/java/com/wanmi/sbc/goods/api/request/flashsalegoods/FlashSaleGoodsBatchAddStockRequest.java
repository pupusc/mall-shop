package com.wanmi.sbc.goods.api.request.flashsalegoods;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName FlashSaleGoodsBatchAddStockRequest
 * @Description 增加秒杀商品活动对应的商品库存
 * @Author lvzhenwei
 * @Date 2019/7/1 16:01
 **/
@Data
public class FlashSaleGoodsBatchAddStockRequest implements Serializable {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @NotNull
    private Long id;

    /**
     * 增加库存数
     */
    @ApiModelProperty(value = "增加库存数")
    @NotNull
    private Integer stock;
}
