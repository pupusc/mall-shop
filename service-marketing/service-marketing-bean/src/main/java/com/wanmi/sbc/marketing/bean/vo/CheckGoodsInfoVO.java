package com.wanmi.sbc.marketing.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午10:31 2019/1/9
 * @Description:
 */
@ApiModel
@Data
public class CheckGoodsInfoVO {

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private String goodsInfoId;

    /**
     * 均摊价
     */
    @ApiModelProperty(value = "均摊价")
    private BigDecimal splitPrice;

}
