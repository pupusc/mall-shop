package com.wanmi.sbc.goods.api.request.enterprise.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class EnterpriseByIdRequest {

    @ApiModelProperty("商品ID")
    private String goodsInfoId;

    @ApiModelProperty("店铺ID")
    private Long storeId;

}
