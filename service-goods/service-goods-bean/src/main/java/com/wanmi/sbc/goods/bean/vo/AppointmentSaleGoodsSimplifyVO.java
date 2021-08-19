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
 * <p>预约抢购VO</p>
 *
 * @author zxd
 * @date 2020-05-21 13:47:11
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSaleGoodsSimplifyVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * skuID
     */
    @ApiModelProperty(value = "skuID")
    private String goodsInfoId;

    /**
     * 预约价
     */
    @ApiModelProperty(value = "预约价")
    private BigDecimal price;
}