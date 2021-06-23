package com.wanmi.sbc.goods.api.request.pointsgoods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName PointsGoodsSalesModifyRequest
 * @Description 积分商品增加销量Request
 * @Author lvzhenwei
 * @Date 2019/5/29 10:35
 **/
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointsGoodsSalesModifyRequest implements Serializable {

    private static final long serialVersionUID = 4520723432947715340L;

    /**
     * 积分商品id
     */
    @ApiModelProperty(value = "积分商品id")
    private String pointsGoodsId;

    /**
     * 商品销量
     */
    @ApiModelProperty(value = "商品销量")
    private Long salesNum;
}
