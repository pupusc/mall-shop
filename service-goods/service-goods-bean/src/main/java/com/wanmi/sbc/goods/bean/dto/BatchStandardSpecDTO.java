package com.wanmi.sbc.goods.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>批量商品库规格 dto</p>
 * author: sunkun
 * Date: 2018-11-07
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class BatchStandardSpecDTO extends StandardSpecDTO {

    private static final long serialVersionUID = 458795948021348872L;

    /**
     * 模拟商品编号
     */
    @ApiModelProperty(value = "模拟商品编号")
    private String mockGoodsId;
}
