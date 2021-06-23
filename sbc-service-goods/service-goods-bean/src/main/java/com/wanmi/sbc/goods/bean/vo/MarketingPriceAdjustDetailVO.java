package com.wanmi.sbc.goods.bean.vo;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.goods.bean.enums.GoodsPriceType;
import com.wanmi.sbc.goods.bean.enums.PriceAdjustmentResult;
import com.wanmi.sbc.goods.bean.enums.SaleType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>营销商品调价详情VO</p>
 * Created by of628-wenzhi on 2020-12-16-11:20 上午.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MarketingPriceAdjustDetailVO implements Serializable {
    private static final long serialVersionUID = -9187334617544477611L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 调价单号
     */
    @ApiModelProperty(value = "调价单号")
    private String priceAdjustmentNo;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsInfoName;

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    private String goodsInfoImg;

    /**
     * SKU编码
     */
    @ApiModelProperty(value = "SKU编码")
    private String goodsInfoNo;

    /**
     * SKU ID
     */
    @ApiModelProperty(value = "SKU ID")
    private String goodsInfoId;

    /**
     * 商品规格
     */
    @ApiModelProperty(value = "商品规格")
    private String goodsSpecText;

    /**
     * 销售类别(0:批发,1:零售)
     */
    @ApiModelProperty(value = "销售类别(0:批发,1:零售)")
    private SaleType saleType;

    /**
     * 设价方式,0:按客户(等级)1:按订货量(阶梯价)2:按市场价
     */
    @ApiModelProperty(value = "设价类型,0:按客户(等级)1:按订货量(阶梯价)2:按市场价")
    private GoodsPriceType priceType;

    /**
     * 原市场价
     */
    @ApiModelProperty(value = "原市场价")
    private BigDecimal originalMarketPrice;

    /**
     * 调整后市场价
     */
    @ApiModelProperty(value = "调整后市场价")
    private BigDecimal adjustedMarketPrice;

    /**
     * 差异
     */
    @ApiModelProperty(value = "差异")
    private BigDecimal priceDifference;

    /**
     * 执行结果：0 未执行、1 执行成功、2 执行失败
     */
    @ApiModelProperty(value = "执行结果：0 未执行、1 执行成功、2 执行失败")
    private PriceAdjustmentResult adjustResult;

    /**
     * 失败原因
     */
    @ApiModelProperty(value = "失败原因")
    private String failReason;

    /**
     * 是否确认：0 未确认、1 已确认
     */
    @ApiModelProperty(value = "是否确认：0 未确认、1 已确认")
    private DefaultFlag confirmFlag;

}
