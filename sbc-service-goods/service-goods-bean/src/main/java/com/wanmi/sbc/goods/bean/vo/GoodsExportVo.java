package com.wanmi.sbc.goods.bean.vo;

import com.wanmi.sbc.common.annotation.CanEmpty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class GoodsExportVo implements Serializable {

    /**
     * 商品SKU名称
     */
    @ApiModelProperty(value = "商品SKU名称")
    private String goodsInfoName;

    /**
     * SPU编码
     */
    @ApiModelProperty(value = "SPU编码")
    private String goodsNo;

    /**
     * 平台类目
     */
    @ApiModelProperty(value = "平台类目")
    private String cateName;

    /**
     * 店铺分类
     */
    @ApiModelProperty(value = "店铺分类")
    private String storeCateName;

    /**
     * 销售类型 0:批发, 1:零售
     */
    @ApiModelProperty(value = "销售类型", dataType = "com.wanmi.sbc.goods.bean.enums.SaleType")
    private Integer saleType;

    /**
     * 计算单位
     */
    @ApiModelProperty(value = "计算单位")
    private String goodsUnit;

    /**
     * 品牌名称
     */
    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    /**
     * 商品主图
     */
    @ApiModelProperty(value = "商品主图")
    @CanEmpty
    private String goodsImg;

    /**
     * 商品视频地址
     */
    @ApiModelProperty(value = "商品视频地址")
    @CanEmpty
    private String goodsVideo;

    /**
     * 商品详情
     */
    @ApiModelProperty(value = "商品详情")
    private String goodsDetail;

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
     * 规格
     */
    @ApiModelProperty(value = "规格")
    private List<GoodsInfoSpecExportVO> goodsSpecVOList;

    /**
     * 商品库存
     */
    @ApiModelProperty(value = "商品库存")
    private Long stock;

    /**
     * 商品条形码
     */
    @ApiModelProperty(value = "商品条形码")
    private String goodsInfoBarcode;

    /**
     * 商品市场价
     */
    @ApiModelProperty(value = "商品市场价")
    private BigDecimal marketPrice;

    /**
     * 上下架状态
     */
    @ApiModelProperty(value = "上下架状态", dataType = "com.wanmi.sbc.goods.bean.enums.AddedFlag")
    private Integer addedFlag;

    /**
     * 运费模板名称
     */
    @ApiModelProperty(value = "运费模板名称")
    @Transient
    private String freightTempName;

    /**
     * 商品重量
     */
    @ApiModelProperty(value = "商品重量")
    private BigDecimal goodsWeight;

    /**
     * 商品体积 单位：m3
     */
    @ApiModelProperty(value = "商品体积", notes = "单位：m3")
    private BigDecimal goodsCubage;

    /**
     * 购买积分
     */
    @ApiModelProperty(value = "购买积分")
    private Long buyPoint;

    /**
     * 购买方式 0立即购买,1购物车,内容以,相隔
     */
    @ApiModelProperty(value = "购买方式 0立即购买 1购物车,内容以,相隔")
    private String goodsBuyTypes;

    /**
     * 设价类型 0按客户 1按订货量 2按市场价
     */
    @ApiModelProperty(value = "设价类型", dataType = "com.wanmi.sbc.goods.bean.enums.GoodsPriceType")
    private Integer priceType;

    /**
     * 供货价
     */
    @ApiModelProperty(value = "供货价")
    @CanEmpty
    private BigDecimal supplyPrice;

    /**
     * 商品副标题
     */
    @ApiModelProperty(value = "商品副标题")
    private String goodsSubtitle;


}
