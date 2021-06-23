package com.wanmi.sbc.goods.bean.dto;

import com.wanmi.sbc.goods.bean.enums.SaleType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Enumerated;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>SKU市场价信息</p>
 * Created by of628-wenzhi on 2020-12-14-9:33 下午.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class GoodsInfoMarketingPriceDTO implements Serializable {
    private static final long serialVersionUID = 6756840498963102869L;

    /**
     * SKU ID
     */
    @ApiModelProperty(value = "SKU ID")
    private String goodsInfoId;

    /**
     * SKU NO
     */
    @ApiModelProperty(value = "SKU NO")
    private String goodsInfoNo;

    /**
     * SKU 名称
     */
    @ApiModelProperty(value = "SKU 名称")
    private String goodsInfoName;

    /**
     * SPU ID
     */
    private String goodsId;

    /**
     * 规格值明细，以空格间隔，例：红色 16G
     */
    @ApiModelProperty(value = "规格值明细，以空格间隔，例：红色 16G")
    private String specText;

    /**
     * 销售类型
     */
    @ApiModelProperty(value = "销售类型")
    @Enumerated
    private SaleType saleType;

    /**
     * 市场价
     */
    @ApiModelProperty(value = "市场价")
    private BigDecimal marketingPrice;

    /**
     * 供货价
     */
    @ApiModelProperty(value = "供货价")
    private BigDecimal supplyPrice;
}
