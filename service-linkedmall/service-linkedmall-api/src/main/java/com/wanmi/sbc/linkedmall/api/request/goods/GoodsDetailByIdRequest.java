package com.wanmi.sbc.linkedmall.api.request.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class GoodsDetailByIdRequest implements Serializable {
    @ApiModelProperty("商品id")
    private Long providerGoodsId;
}
