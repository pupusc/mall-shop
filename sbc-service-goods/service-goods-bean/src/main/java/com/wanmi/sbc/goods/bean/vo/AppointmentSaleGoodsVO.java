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
public class AppointmentSaleGoodsVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 预约活动id
     */
    @ApiModelProperty(value = "预约活动id")
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

    @ApiModelProperty(value = "商品信息")
    private GoodsInfoVO goodsInfoVO;

    @ApiModelProperty(value = "spu信息")
    private GoodsVO goodsVO;


    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    private String skuName;

    private String skuPic;

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
     * 预约开始时间
     */
    @ApiModelProperty(value = "预约开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime appointmentStartTime;

    /**
     * 预约结束时间
     */
    @ApiModelProperty(value = "预约结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime appointmentEndTime;

    /**
     * 抢购开始时间
     */
    @ApiModelProperty(value = "抢购开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime snapUpStartTime;

    /**
     * 抢购结束时间
     */
    @ApiModelProperty(value = "抢购结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime snapUpEndTime;

}