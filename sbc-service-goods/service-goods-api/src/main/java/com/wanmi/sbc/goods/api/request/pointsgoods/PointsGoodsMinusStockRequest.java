package com.wanmi.sbc.goods.api.request.pointsgoods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>积分商品扣除库存请求参数</p>
 *
 * @author yinxianzhi
 * @date 2019-05-20 15:01:41
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointsGoodsMinusStockRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 积分商品id
     */
    @ApiModelProperty(value = "积分商品id")
    @NotNull
    private String pointsGoodsId;

    /**
     * 库存数
     */
    @ApiModelProperty(value = "库存数")
    private Long stock;

}