package com.wanmi.sbc.customer.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import java.math.BigDecimal;

import com.wanmi.sbc.customer.bean.enums.BuyTypeEnum;
import com.wanmi.sbc.customer.bean.enums.EffectStatusEnum;
import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>付费会员VO</p>
 * @author xuhai
 * @date 2021-01-29 14:03:58
 */
@ApiModel
@Data
public class PaidCardBuyRecordVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 流水号
	 */
	@ApiModelProperty(value = "流水号")
	private String payCode;

	/**
	 * 用户id
	 */
	@ApiModelProperty(value = "用户id")
	private String customerId;

	/**
	 * 用户姓名
	 */
	@ApiModelProperty(value = "用户姓名")
	private String customerName;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * 卡号
	 */
	@ApiModelProperty(value = "卡号")
	private String cardNo;

	/**
	 * 付费会员类型id
	 */
	@ApiModelProperty(value = "付费会员类型id")
	private String paidCardId;

	/**
	 * 付费会员类型名称
	 */
	@ApiModelProperty(value = "付费会员类型名称")
	private String paidCardName;

	/**
	 * 付费周期id
	 */
	@ApiModelProperty(value = "付费周期id")
	private String ruleId;

	/**
	 * 付费周期名称
	 */
	@ApiModelProperty(value = "付费周期名称")
	private String ruleName;

	/**
	 * 价格
	 */
	@ApiModelProperty(value = "价格")
	private BigDecimal price;

	/**
	 * 购买类型 0：购买 1：续费
	 */
	@ApiModelProperty(value = "购买类型 0：购买 1：续费")
	private BuyTypeEnum type;

	/**
	 * 用户账号
	 */
	@ApiModelProperty(value = "用户账号")
	private String customerAccount;

	/**
	 * 失效时间
	 */
	@ApiModelProperty(value = "失效时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime invalidTime;

	/**
	 * 付费卡实例ID
	 */
	@ApiModelProperty(value = "付费卡实例ID")
	private String customerPaidcardId;

	/**
	 * 生效状态
	 */
	private EffectStatusEnum effectStatus = EffectStatusEnum.EFFECT;

	/**
	 * 开始时间
	 */
	@ApiModelProperty(value = "开始时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime beginTime;



}