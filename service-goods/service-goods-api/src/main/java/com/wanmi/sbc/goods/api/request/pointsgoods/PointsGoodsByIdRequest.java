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
 * <p>单个查询积分商品表请求参数</p>
 *
 * @author yang
 * @date 2019-05-07 15:01:41
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointsGoodsByIdRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 积分商品id
     */
    @ApiModelProperty(value = "积分商品id")
    @NotNull
    private String pointsGoodsId;
}