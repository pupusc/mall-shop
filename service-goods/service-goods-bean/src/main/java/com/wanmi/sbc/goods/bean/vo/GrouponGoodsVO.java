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
 * <p>拼团活动商品信息表entity</p>
 *
 * @author chenli
 * @date 2019-05-21 14:49:12
 */

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponGoodsVO implements Serializable {
    private static final long serialVersionUID = -4936136905499805395L;

    /**
     * 拼团活动ID
     */
    @ApiModelProperty(value = "拼团活动ID")
    private String grouponActivityId;

    /**
     * SPU编号
     */
    @ApiModelProperty(value = "SPU编号")
    private String goodsId;

    /**
     * SKU编号
     */
    @ApiModelProperty(value = "SKU编号")
    private String goodsInfoId;

    /**
     * spu商品名称
     */
    @ApiModelProperty(value = "spu商品名称")
    private String goodsName;

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    private String goodsImg;

    /**
     * 商品Sku图片
     */
    @ApiModelProperty(value = "商品Sku图片")
    private String goodsInfoImg;

    /**
     * 商品市场价
     */
    @ApiModelProperty(value = "商品市场价")
    private BigDecimal marketPrice;

    /**
     * 拼团价格
     */
    @ApiModelProperty(value = "拼团价格")
    private BigDecimal grouponPrice;

    /**
     * 拼团人数
     */
    @ApiModelProperty(value = "拼团人数")
    private Integer grouponNum;

    /**
     * 已成团人数
     */
    @ApiModelProperty(value = "已成团人数")
    private Integer alreadyGrouponNum;

    /**
     * 拼团订单数量
     */
    @ApiModelProperty(value = "拼团订单数量")
    private Integer orderSalesNum;

    /**
     * 商品规格
     */
    @ApiModelProperty(value = "商品规格")
    private String specText;

}