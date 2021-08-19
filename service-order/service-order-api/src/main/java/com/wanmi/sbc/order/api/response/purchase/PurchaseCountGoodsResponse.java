package com.wanmi.sbc.order.api.response.purchase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-12-07
 */
@Data
@ApiModel
public class PurchaseCountGoodsResponse implements Serializable {

    private static final long serialVersionUID = 6248671223640691297L;

    @ApiModelProperty(value = "总数",example = "0")
    private Integer total;
}
