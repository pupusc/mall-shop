package com.wanmi.sbc.goods.bean.dto;

import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>预约抢购DTO</p>
 *
 * @author zxd
 * @date 2020-05-21 13:47:11
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSaleGoodsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 预约id
     */
    @ApiModelProperty(value = "预约id")
    private Long appointmentSaleId;

    /**
     * 商户id
     */
    @ApiModelProperty(value = "商户id")
    private Long storeId;

    /**
     * skuID
     */
    @ApiModelProperty(value = "skuID")
    private String goodsInfoId;

    /**
     * spuID
     */
    @ApiModelProperty(value = "spuID")
    private String goodsId;

    /**
     * 预约价
     */
    @ApiModelProperty(value = "预约价")
    private BigDecimal price;

    /**
     * 预约数量
     */
    @ApiModelProperty(value = "预约数量")
    private Integer appointmentCount;

    /**
     * 购买数量
     */
    @ApiModelProperty(value = "购买数量")
    private Integer buyerCount;

    @ApiModelProperty(value = "sku", hidden = true)
    private GoodsInfoVO goodsInfoVO;

    /**
     * stock
     */
    @ApiModelProperty(value = "stock")
    private Integer stock;

}