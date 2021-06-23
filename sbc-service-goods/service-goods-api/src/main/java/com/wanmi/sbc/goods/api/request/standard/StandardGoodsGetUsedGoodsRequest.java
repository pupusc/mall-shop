package com.wanmi.sbc.goods.api.request.standard;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-07
 */
@ApiModel
@Data
public class StandardGoodsGetUsedGoodsRequest implements Serializable {

    private static final long serialVersionUID = -8985682078650679560L;

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    @NotNull
    private List<String> goodsIds;
}
