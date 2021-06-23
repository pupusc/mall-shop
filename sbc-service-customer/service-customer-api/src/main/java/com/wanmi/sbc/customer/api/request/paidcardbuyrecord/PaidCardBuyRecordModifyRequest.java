package com.wanmi.sbc.customer.api.request.paidcardbuyrecord;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import com.wanmi.sbc.customer.bean.enums.BuyTypeEnum;
import lombok.*;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;

/**
 * <p>付费会员修改参数</p>
 * @author xuhai
 * @date 2021-01-29 14:03:58
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaidCardBuyRecordModifyRequest extends CustomerBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 流水号
	 */
	@ApiModelProperty(value = "流水号")
	@Length(max=32)
	private String payCode;

	/**
	 * 用户id
	 */
	@ApiModelProperty(value = "用户id")
	@NotBlank
	@Length(max=32)
	private String customerId;

	/**
	 * 用户姓名
	 */
	@ApiModelProperty(value = "用户姓名")
	@NotBlank
	@Length(max=10)
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
	@NotBlank
	@Length(max=20)
	private String cardNo;

	/**
	 * 付费会员类型id
	 */
	@ApiModelProperty(value = "付费会员类型id")
	@NotBlank
	@Length(max=32)
	private String paidCardId;

	/**
	 * 付费会员类型名称
	 */
	@ApiModelProperty(value = "付费会员类型名称")
	@NotBlank
	@Length(max=15)
	private String paidCardName;

	/**
	 * 付费周期id
	 */
	@ApiModelProperty(value = "付费周期id")
	@NotBlank
	@Length(max=32)
	private String ruleId;

	/**
	 * 付费周期名称
	 */
	@ApiModelProperty(value = "付费周期名称")
	@NotBlank
	@Length(max=20)
	private String ruleName;

	/**
	 * 价格
	 */
	@ApiModelProperty(value = "价格")
	@NotNull
	private BigDecimal price;

	/**
	 * 购买类型 0：购买 1：续费
	 */
	@ApiModelProperty(value = "购买类型 0：购买 1：续费")
	@NotNull
	private BuyTypeEnum type;

	/**
	 * 用户账号
	 */
	@ApiModelProperty(value = "用户账号")
	@NotBlank
	@Length(max=20)
	private String customerAccount;

	/**
	 * 失效时间
	 */
	@ApiModelProperty(value = "失效时间")
	@NotNull
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime invalidTime;

	/**
	 * 付费卡实例ID
	 */
	@ApiModelProperty(value = "付费卡实例ID")
	private String customerPaidcardId;

}