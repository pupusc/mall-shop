package com.wanmi.sbc.elastic.api.request.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class EsGoodsInfoModifyDistributionGoodsStatusRequest implements Serializable{

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    /**
     * 分销商品状态，配合分销开关使用
     */
    @ApiModelProperty(value = "分销商品状态，配合分销开关使用")
    private Integer distributionGoodsStatus;


}
