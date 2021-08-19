package com.wanmi.sbc.goods.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 商品规格值批量导入参数
 *
 * @author lipeng
 * @dateTime 2018/11/8 下午1:59
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class BatchGoodsSpecDetailDTO extends GoodsSpecDetailDTO implements Serializable {

    private static final long serialVersionUID = -6786453141635611679L;

    /**
     * 模拟商品编号
     */
    @ApiModelProperty(value = "模拟商品编号")
    private String mockGoodsId;
}
