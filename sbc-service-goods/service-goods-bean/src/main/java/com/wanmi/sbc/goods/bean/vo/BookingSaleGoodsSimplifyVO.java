package com.wanmi.sbc.goods.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>预售商品信息VO</p>
 *
 * @author dany
 * @date 2020-06-05 10:51:35
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingSaleGoodsSimplifyVO implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * skuID
     */
    @ApiModelProperty(value = "skuID")
    private String goodsInfoId;

    /**
     * 预售价
     */
    @ApiModelProperty(value = "预售价")
    private BigDecimal bookingPrice;


}