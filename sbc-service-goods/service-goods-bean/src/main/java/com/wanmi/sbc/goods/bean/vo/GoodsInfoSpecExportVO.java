package com.wanmi.sbc.goods.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
public class GoodsInfoSpecExportVO implements Serializable {

    private static final long serialVersionUID = 7902480693614543260L;

    /**
     * skuId
     */
    @ApiModelProperty(value = "skuId")
    private String goodsInfoId;

    /**
     * 规格名称
     */
    @ApiModelProperty(value = "规格名称")
    private String specName;

    /**
     * 规格值名称
     */
    @ApiModelProperty(value = "规格值名称")
    private String specDetailName;
}
