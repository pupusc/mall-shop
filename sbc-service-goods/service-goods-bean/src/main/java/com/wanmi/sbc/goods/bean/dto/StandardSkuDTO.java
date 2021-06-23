package com.wanmi.sbc.goods.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品库SKU DTO
 * Created by dyt on 2017/4/11.
 */
@ApiModel
@Data
public class StandardSkuDTO implements Serializable {

    private static final long serialVersionUID = 1129941885356868738L;

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
     * 商品SKU编码
     */
    @ApiModelProperty(value = "商品SKU编码")
    private String goodsInfoNo;

    /**
     * 商品SKU名称
     */
    @ApiModelProperty(value = "商品SKU名称")
    private String goodsInfoName;

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
     * 商品成本价
     */
    @ApiModelProperty(value = "商品成本价")
    private BigDecimal costPrice;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    /**
     * 删除标记
     */
    @ApiModelProperty(value = "删除标记", notes = "0: 否, 1: 是")
    private DeleteFlag delFlag;

    /**
     * 新增时，模拟多个规格ID
     * 查询详情返回响应，扁平化多个规格ID
     */
    @ApiModelProperty(value = "新增时，模拟多个规格ID", notes = "查询详情返回响应，扁平化多个规格ID")
    private List<Long> mockSpecIds;

    /**
     * 新增时，模拟多个规格值 ID
     * 查询详情返回响应，扁平化多个规格值ID
     */
    @ApiModelProperty(value = "新增时，模拟多个规格值 ID", notes = "查询详情返回响应，扁平化多个规格值ID")
    private  List<Long> mockSpecDetailIds;

    /**
     * 商品分页，扁平化多个商品规格值ID
     */
    @ApiModelProperty(value = "商品分页，扁平化多个商品规格值ID")
    private List<Long> specDetailRelIds;

    /**
     * 规格名称规格值 颜色:红色;大小:16G
     */
    @ApiModelProperty(value = "规格名称规格值", example = " 颜色:红色;大小:16G")
    private String specText;

    /**
     * 供应商商品SKU编号
     */
    @ApiModelProperty(value = "供应商商品SKU编号")
    private String providerGoodsInfoId;

    /**
     * 第三方平台的skuId
     */
    @ApiModelProperty("third_platform_sku_id")
    private String thirdPlatformSkuId;

    /**
     * 第三方卖家id
     */
    @ApiModelProperty("seller_id")
    private Long sellerId;

    /**
     * 三方渠道类目id
     */
    @ApiModelProperty("third_cate_id")
    private Long thirdCateId;
}
