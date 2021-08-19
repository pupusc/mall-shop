package com.wanmi.sbc.goods.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品Sku批量导入参数
 * @author lipeng
 * @dateTime 2018/11/6 下午2:29
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class BatchGoodsInfoDTO extends GoodsInfoDTO {

    private static final long serialVersionUID = 9040193270990319318L;

    /**
     * 模拟商品编号
     */
    @ApiModelProperty(value = "模拟商品编号")
    private String mockGoodsId;
}
