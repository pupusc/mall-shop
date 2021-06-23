package com.wanmi.sbc.marketing.api.request.distributionrecord;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.marketing.bean.enums.CommissionReceived;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>DistributionRecord新增参数</p>
 * @author baijz
 * @date 2019-02-27 18:56:40
 */
@Data
@ApiModel
public class DistributionRecordAddInfo {
	private static final long serialVersionUID = 1L;

	/**
	 * 货品Id
	 */
	@NotNull
	@Length(max=32)
	@ApiModelProperty(value = "货品Id")
	private String goodsInfoId;

	/**
	 * 订单交易号
	 */
	@NotNull
	@ApiModelProperty(value = "订单交易号")
	@Length(max=32)
	private String tradeId;

	/**
	 * 店铺Id
	 */
	@NotNull
	@ApiModelProperty(value = "店铺Id")
	private Long storeId;

	/**
	 * 会员Id
	 */
	@NotNull
	@ApiModelProperty(value = "会员Id")
	@Length(max=32)
	private String customerId;

	/**
	 * 分销员标识UUID
	 */
	@NotNull
	@ApiModelProperty(value = "分销员标识UUID")
	@Length(max=32)
	private String distributorId;

	/**
	 * 付款时间
	 */
	@NotNull
	@ApiModelProperty(value = "付款时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime payTime;

	/**
	 * 订单完成时间
	 */
	@ApiModelProperty(value = "订单完成时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime finishTime;

	/**
	 * 佣金入账时间
	 */
	@ApiModelProperty(value = "佣金入账时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime missionReceivedTime;

	/**
	 * 订单单个商品金额
	 */
	@NotNull
	@ApiModelProperty(value = "订单单个商品金额")
	private BigDecimal orderGoodsPrice;

	/**
	 * 商品的数量
	 */
	@NotNull
	@ApiModelProperty(value = "商品的数量")
	@Max(9999999999L)
	private Long orderGoodsCount;

	/**
	 * 佣金比例
	 */
	@NotNull
	@ApiModelProperty(value = "佣金比例")
	private BigDecimal commissionRate;

	/**
	 * 单个货品的佣金
	 */
	@NotNull
	@ApiModelProperty(value = "单个货品的佣金")
	private BigDecimal commissionGoods;

	/**
	 * 分销是否入账 0:未入账  1:已入账
	 */
	@ApiModelProperty(value = "分销是否入账")
	private CommissionReceived commissionState;

	/**
	 * 是否删除 0：未删除  1：已删除
	 */
	@ApiModelProperty(value = "是否删除 0：未删除  1：已删除")
	private DeleteFlag deleteFlag;

	@ApiModelProperty(value = "分销员的客户id")
	@NotNull
	private String distributorCustomerId;
}