package com.wanmi.sbc.goods.api.request.standard;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * <p>根据id查询商品库</p>
 * author: sunkun
 * Date: 2018-11-07
 */
@ApiModel
@Data
public class StandardGoodsByIdRequest implements Serializable {

    private static final long serialVersionUID = 3222569144401497229L;

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    @NotBlank
    private String goodsId;
}
