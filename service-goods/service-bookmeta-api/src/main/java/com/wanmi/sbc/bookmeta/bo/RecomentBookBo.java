package com.wanmi.sbc.bookmeta.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
@ApiModel
public class RecomentBookBo implements Serializable {

    private static final long serialVersionUID = 2305724054242646915L;
    /**
     * 商品SKU编号
     */
    @ApiModelProperty(value = "商品SKU编号")
    private String goodsInfoId;

    /**
     * 商品编号
     */
    @ApiModelProperty(value = "商品编号")
    private String goodsId;

    /**
     * 商品SKU名称
     */
    @ApiModelProperty(value = "商品SKU名称")
    private String goodsInfoName;

    /**
     * 商品SKU编码
     */
    @ApiModelProperty(value = "商品SKU编码")
    private String goodsInfoNo;

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    private String goodsInfoImg;
    /**
     * 商品市场价
     */
    @ApiModelProperty(value = "商品市场价")
    private BigDecimal marketPrice;

    /**
     * 最新计算的会员价
     * 为空，以市场价为准
     */
    @ApiModelProperty(value = "最新计算的会员价", notes = "为空，以市场价为准")
    private BigDecimal salePrice;

    public RecomentBookBo() {
    }

    public RecomentBookBo(String goodsInfoId, String goodsId, String goodsInfoName, String goodsInfoNo, String goodsInfoImg, BigDecimal marketPrice, BigDecimal salePrice) {
        this.goodsInfoId = goodsInfoId;
        this.goodsId = goodsId;
        this.goodsInfoName = goodsInfoName;
        this.goodsInfoNo = goodsInfoNo;
        this.goodsInfoImg = goodsInfoImg;
        this.marketPrice = marketPrice;
        this.salePrice = salePrice;
    }

    public String getGoodsInfoId() {
        return goodsInfoId;
    }

    public void setGoodsInfoId(String goodsInfoId) {
        this.goodsInfoId = goodsInfoId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsInfoName() {
        return goodsInfoName;
    }

    public void setGoodsInfoName(String goodsInfoName) {
        this.goodsInfoName = goodsInfoName;
    }

    public String getGoodsInfoNo() {
        return goodsInfoNo;
    }

    public void setGoodsInfoNo(String goodsInfoNo) {
        this.goodsInfoNo = goodsInfoNo;
    }

    public String getGoodsInfoImg() {
        return goodsInfoImg;
    }

    public void setGoodsInfoImg(String goodsInfoImg) {
        this.goodsInfoImg = goodsInfoImg;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }
}
