package com.wanmi.sbc.goods.api.request.goods;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class GoodsAuditModifyRequest extends GoodsModifyRequest implements Serializable {
    @ApiModelProperty("id")
    private Long id;
}
