package com.wanmi.sbc.linkedmall.api.request.cate;

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
public class CateChainByGoodsIdRequest implements Serializable {
    private static final long serialVersionUID = -9007433167357512718L;
    @ApiModelProperty("商品id")
    private Long goodsId;
}
