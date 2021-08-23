package com.wanmi.sbc.marketing.api.request.distributionrecord;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.marketing.bean.enums.CommissionReceived;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>DistributionRecord列表查询请求参数</p>
 * @author baijz
 * @date 2019-02-27 18:56:40
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class DistributionRecordListRequest{
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-分销记录表主键List
	 */
	@ApiModelProperty(value = "分销记录表主键List")
	private List<String> recordIdList;

	/**
	 * 分销记录表主键
	 */
	@ApiModelProperty(value = "分销记录表主键")
	private String recordId;

	/**
	 * 货品Id
	 */
	@ApiModelProperty(value = "货品Id")
	private String goodsInfoId;

	/**
	 * 订单交易号
	 */
	@ApiModelProperty(value = "订单交易号")
	private String tradeId;

	/**
	 * 店铺Id
	 */
	@ApiModelProperty(value = "店铺Id")
	private Long storeId;

	/**
	 * 会员Id
	 */
	@ApiModelProperty(value = "会员Id")
	private String customerId;

	/**
	 * 分销员标识UUID
	 */
	@ApiModelProperty(value = "分销员标识UUID")
	private String distributorId;

	/**
	 * 搜索条件:付款时间开始
	 */
	@ApiModelProperty(value = "付款时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime payTimeBegin;
	/**
	 * 搜索条件:付款时间截止
	 */
	@ApiModelProperty(value = "付款时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime payTimeEnd;

	/**
	 * 搜索条件:订单完成时间开始
	 */
	@ApiModelProperty(value = "订单完成时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime finishTimeBegin;
	/**
	 * 搜索条件:订单完成时间截止
	 */
	@ApiModelProperty(value = "订单完成时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime finishTimeEnd;

	/**
	 * 搜索条件:佣金入账时间开始
	 */
	@ApiModelProperty(value = "佣金入账时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime missionReceivedTimeBegin;
	/**
	 * 搜索条件:佣金入账时间截止
	 */
	@ApiModelProperty(value = "佣金入账时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime missionReceivedTimeEnd;

	/**
	 * 订单单个商品金额
	 */
	@ApiModelProperty(value = "订单单个商品金额")
	private BigDecimal orderGoodsPrice;

	/**
	 * 商品的数量
	 */
	@ApiModelProperty(value = "商品的数量")
	private Long orderGoodsCount;

	/**
	 * 单个货品的佣金
	 */
	@ApiModelProperty(value = "单个货品的佣金")
	private BigDecimal commissionGoods;

	/**
	 * 佣金是否入账
	 */
	@ApiModelProperty(value = "佣金是否入账")
	private CommissionReceived commissionState;

	/**
	 * 分销员的客户id
	 */
	@ApiModelProperty(value = "分销员的客户id")
	private String distributorCustomerId;


	/**
	 * 是否删除 0：未删除  1：已删除
	 */
	@ApiModelProperty(value = "是否删除 0：未删除  1：已删除")
	private DeleteFlag deleteFlag;
}