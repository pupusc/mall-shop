package com.wanmi.sbc.goods.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 批量商品库SKU DTO
 * Created by dyt on 2017/4/11.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class BatchStandardSkuDTO extends StandardSkuDTO {

    private static final long serialVersionUID = 1129941885356868738L;

    /**
     * 模拟商品编号
     */
    @ApiModelProperty(value = "模拟商品编号")
    private String mockGoodsId;

}
