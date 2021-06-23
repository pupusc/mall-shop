package com.wanmi.sbc.goods.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 商品信息增量库存传输对象
 * @author lipeng
 * @dateTime 2018/11/6 下午2:29
 */
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
@Data
public class GoodsInfoPlusStockDTO implements Serializable {

    private static final long serialVersionUID = 9040193270990319318L;

    /**
     * 库存数
     */
    @ApiModelProperty(value = "库存数")
    private Long stock;

    /**
     * 商品skuId
     */
    @ApiModelProperty(value = "商品skuId")
    private String goodsInfoId;
}
