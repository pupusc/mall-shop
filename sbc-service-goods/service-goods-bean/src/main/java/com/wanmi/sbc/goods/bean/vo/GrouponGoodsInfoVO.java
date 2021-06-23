package com.wanmi.sbc.goods.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>拼团活动商品信息表VO</p>
 * @author groupon
 * @date 2019-05-15 14:49:12
 */
@ApiModel
@Data
public class GrouponGoodsInfoVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 拼团商品ID
	 */
    @ApiModelProperty(value = "拼团商品ID")
	private String grouponGoodsId;

	/**
	 * 单品Id
	 */
    @ApiModelProperty(value = "SKU编号")
	private String goodsInfoId;

	/**
	 * 拼团价格
	 */
    @ApiModelProperty(value = "拼团价格")
	private BigDecimal grouponPrice;

	/**
	 * 起售数量
	 */
    @ApiModelProperty(value = "起售数量")
	private Integer startSellingNum;

	/**
	 * 限购数量
	 */
    @ApiModelProperty(value = "限购数量")
	private Integer limitSellingNum;

	/**
	 * 拼团活动ID
	 */
    @ApiModelProperty(value = "拼团活动ID")
	private String grouponActivityId;

	/**
	 * 拼团分类ID
	 */
    @ApiModelProperty(value = "拼团分类ID")
	private String grouponCateId;

	/**
	 * 店铺ID
	 */
    @ApiModelProperty(value = "店铺ID")
	private String storeId;

	/**
	 * SPU编号
	 */
    @ApiModelProperty(value = "SPU编号")
	private String goodsId;

	/**
	 * 商品销售数量
	 */
    @ApiModelProperty(value = "商品销售数量")
	private Integer goodsSalesNum;

	/**
	 * 订单数量
	 */
    @ApiModelProperty(value = "订单数量")
	private Integer orderSalesNum;

	/**
	 * 交易额
	 */
    @ApiModelProperty(value = "交易额")
	private BigDecimal tradeAmount;

	/**
	 * 成团后退单数量
	 */
    @ApiModelProperty(value = "成团后退单数量")
	private Integer refundNum;

	/**
	 * 已成团人数
	 */
	@ApiModelProperty(value = "已成团人数")
	private Integer alreadyGrouponNum;

	/**
	 * 成团后退单金额
	 */
    @ApiModelProperty(value = "成团后退单金额")
	private BigDecimal refundAmount;

	/**
	 * 商品名称
	 */
	@ApiModelProperty(value = "商品名称")
	private String goodsInfoName;

	/**
	 * SKU编码
	 */
	@ApiModelProperty(value = "SKU编码")
	private String goodsInfoNo;

	/**
	 * 市场价
	 */
	@ApiModelProperty(value = "市场价")
	private BigDecimal marketPrice;

	/**
	 * 活动开始时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	@ApiModelProperty(value = "活动开始时间")
	private LocalDateTime startTime;

	/**
	 * 活动结束时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	@ApiModelProperty(value = "活动结束时间")
	private LocalDateTime endTime;

	/**
	 * 规格名称
	 */
	@ApiModelProperty(value = "规格名称")
	private String specText;

	@ApiModelProperty(value = "商品信息")
	private GoodsInfoVO goodsInfo;

	@ApiModelProperty(value = "分类名称")
	private String cateName;

	@ApiModelProperty(value = "活动状态 0：即将开始 1：进行中 2：已结束")
	private Integer status;

    /**
     * 拼团人数
     */
    @ApiModelProperty(value = "拼团人数")
    private Integer grouponNum;

	/**
	 * 店铺名称
	 */
	@ApiModelProperty(value = "店铺名称")
	private String storeName;

	/**
	 * 商品主图片
	 */
	@ApiModelProperty(value = "商品主图片")
	private String goodsImg;
}