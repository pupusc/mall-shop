package com.wanmi.sbc.goods.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品图片批量导入参数
 *
 * @author lipeng
 * @dateTime 2018/11/8 下午1:57
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class BatchGoodsImageDTO extends GoodsImageDTO {

    private static final long serialVersionUID = 4424324134771264542L;

    /**
     * 模拟商品编号
     */
    @ApiModelProperty(value = "模拟商品编号")
    private String mockGoodsId;
}
