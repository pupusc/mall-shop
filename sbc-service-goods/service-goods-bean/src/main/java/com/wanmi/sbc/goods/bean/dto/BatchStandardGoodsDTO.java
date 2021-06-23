package com.wanmi.sbc.goods.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>商品批量商品库 spu dto</p>
 * author: sunkun
 * Date: 2018-11-07
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class BatchStandardGoodsDTO extends StandardGoodsDTO {

    private static final long serialVersionUID = -2685575266562593591L;

    /**
     * 模拟商品编号
     */
    @ApiModelProperty(value = "模拟商品编号")
    private String mockGoodsId;
}
