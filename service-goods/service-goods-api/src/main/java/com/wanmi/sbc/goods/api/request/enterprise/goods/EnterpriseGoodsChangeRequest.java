package com.wanmi.sbc.goods.api.request.enterprise.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @program: sbc-micro-service
 * @description:
 * @create: 2019-03-11 16:05
 **/
@ApiModel
@Data
public class EnterpriseGoodsChangeRequest implements Serializable {

    @ApiModelProperty(value = "spuId,商品ID")
    @NotNull
    private String goodsId;
}
