package com.wanmi.sbc.goods.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
public class BookingSaleGoodsVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 预售id
     */
    @ApiModelProperty(value = "预售id")
    private Long bookingSaleId;

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
     * 定金
     */
    @ApiModelProperty(value = "定金")
    private BigDecimal handSelPrice;

    /**
     * 膨胀价格
     */
    @ApiModelProperty(value = "膨胀价格")
    private BigDecimal inflationPrice;

    /**
     * 预售价
     */
    @ApiModelProperty(value = "预售价")
    private BigDecimal bookingPrice;

    /**
     * 预售数量
     */
    @ApiModelProperty(value = "预售数量")
    private Integer bookingCount;

    /**
     * 实际可售数量
     */
    @ApiModelProperty(value = "实际可售数量")
    private Integer canBookingCount;

    /**
     * 定金支付数量
     */
    @ApiModelProperty(value = "定金支付数量")
    private Integer handSelCount;

    /**
     * 尾款支付数量
     */
    @ApiModelProperty(value = "尾款支付数量")
    private Integer tailCount;

    /**
     * 全款支付数量
     */
    @ApiModelProperty(value = "全款支付数量")
    private Integer payCount;

    @ApiModelProperty(value = "商品信息")
    private GoodsInfoVO goodsInfoVO;

    @ApiModelProperty(value = "spu信息")
    private GoodsVO goodsVO;

    private String skuName;

    private String skuPic;

    @ApiModelProperty(value = "店铺名称")
    private String storeName;


    /**
     * 商品规格
     */
    @ApiModelProperty(value = "商品规格")
    private String specText;

    /**
     * spu商品名称
     */
    @ApiModelProperty(value = "spu商品名称")
    private String goodsName;

    /**
     * 商品spu图片
     */
    @ApiModelProperty(value = "商品spu图片")
    private String goodsImg;

    /**
     * 商品sku图片
     */
    @ApiModelProperty(value = "商品sku图片")
    private String goodsInfoImg;

    /**
     * 商品市场价
     */
    @ApiModelProperty(value = "商品市场价")
    private BigDecimal marketPrice;

    /**
     * 划线价
     */
    @ApiModelProperty(value = "划线价")
    private BigDecimal linePrice;


    /**
     * 预售类型 0：全款预售  1：定金预售
     */
    @ApiModelProperty(value = "预售类型 0：全款预售  1：定金预售")
    private Integer bookingType;

    /**
     * 定金支付开始时间
     */
    @ApiModelProperty(value = "定金支付开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime handSelStartTime;

    /**
     * 定金支付结束时间
     */
    @ApiModelProperty(value = "定金支付结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime handSelEndTime;

    /**
     * 尾款支付开始时间
     */
    @ApiModelProperty(value = "尾款支付开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime tailStartTime;

    /**
     * 尾款支付结束时间
     */
    @ApiModelProperty(value = "尾款支付结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime tailEndTime;

    /**
     * 预售开始时间
     */
    @ApiModelProperty(value = "预售开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime bookingStartTime;

    /**
     * 预售结束时间
     */
    @ApiModelProperty(value = "预售结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime bookingEndTime;

    /**
     * 服务器时间
     */
    @ApiModelProperty(value = "服务器时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime serverTime;

}