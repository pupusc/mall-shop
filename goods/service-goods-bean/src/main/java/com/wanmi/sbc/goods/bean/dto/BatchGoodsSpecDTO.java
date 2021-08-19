package com.wanmi.sbc.goods.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 商品Sku规格批量导入参数
 *
 * @author lipeng
 * @dateTime 2018/11/8 下午1:58
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class BatchGoodsSpecDTO extends GoodsSpecDTO implements Serializable {

    private static final long serialVersionUID = -5070764699714565802L;

    /**
     * 模拟商品编号
     */
    @ApiModelProperty(value = "模拟商品编号")
    private String mockGoodsId;
}
