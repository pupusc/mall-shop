package com.wanmi.sbc.goods.api.response.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 商品SKU条件统计响应
 * Created by daiyitian on 2017/3/24.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsCountByConditionResponse implements Serializable {

    private static final long serialVersionUID = -4108347931869624603L;

    /**
     * 商品总数
     */
    @ApiModelProperty(value = "商品总数")
    private Long count;
}
