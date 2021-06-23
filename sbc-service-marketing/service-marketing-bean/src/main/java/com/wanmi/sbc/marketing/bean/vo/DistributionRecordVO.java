package com.wanmi.sbc.marketing.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailVO;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoSpecDetailRelVO;
import com.wanmi.sbc.marketing.bean.enums.CommissionReceived;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>DistributionRecordVO</p>
 * @author baijz
 * @date 2019-02-27 18:56:40
 */
@Data
@ApiModel
public class DistributionRecordVO implements Serializable {
	private static final long serialVersionUID = 1L;

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
	 * 付款时间
	 */
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
	 * 订单商品总金额
	 */
	@ApiModelProperty(value = "订单商品总金额")
	private BigDecimal orderGoodsPrice;

	/**
	 * 商品的数量
	 */
	@ApiModelProperty(value = "商品的数量")
	private Long orderGoodsCount;

	/**
	 * 货品的总佣金
	 */
	@ApiModelProperty(value = "货品的总佣金")
	private BigDecimal commissionGoods;

	/**
	 * 佣金比例
	 */
	@ApiModelProperty(value = "佣金比例")
	private BigDecimal commissionRate;

	/**
	 * 佣金是否入账
	 */
	@ApiModelProperty(value = "佣金是否入账")
	private CommissionReceived commissionState;

	/**
	 * 规格值信息
	 */
	@ApiModelProperty(value = "规格值信息")
	private List<GoodsInfoSpecDetailRelVO> goodsInfoSpecDetailRelVOS;

	/**
	 * 分销记录使用的货品信息
	 */
	@ApiModelProperty(value = "分销记录使用的货品信息")
	private GoodsInfoForDistribution goodsInfo;

	/**
	 * 会员信息
	 */
	@ApiModelProperty(value = "会员信息")
	private CustomerDetailVO customerDetailVO;

	/**
	 * 分销员信息
	 */
	@ApiModelProperty(value = "分销员信息")
	private DistributionCustomerVO distributionCustomerVO;

	/**
	 * 店铺信息
	 */
	@ApiModelProperty(value = "店铺信息")
	private StoreVO storeVO;

	/**
	 * 商家信息
	 */
	@ApiModelProperty(value = "商家信息")
	private CompanyInfoVO companyInfoVO;

	@ApiModelProperty(value = "分销员的客户id")
	private String distributorCustomerId;

	/**
	 * 是否删除 0：未删除  1：已删除
	 */
	@ApiModelProperty(value = "是否删除 0：未删除  1：已删除")
	private DeleteFlag deleteFlag;
}