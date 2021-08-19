package com.wanmi.sbc.account.api.request.customerdrawcash;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.account.api.request.AccountBaseRequest;
import com.wanmi.sbc.account.bean.enums.*;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>会员提现管理修改参数</p>
 * @author chenyufei
 * @date 2019-02-25 17:22:24
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDrawCashModifyRequest extends AccountBaseRequest {

	private static final long serialVersionUID = 1L;

	/**
	 * 提现id
	 */
	@ApiModelProperty(value = "提现id")
	private String drawCashId;

	/**
	 * 提现单号(订单编号)
	 */
	@ApiModelProperty(value = "提现单号(订单编号)")
	private String drawCashNo;

	/**
	 * 申请时间
	 */
	@ApiModelProperty(value = "申请时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime applyTime;

	/**
	 * 会员id
	 */
	@ApiModelProperty(value = "会员id")
	private String customerId;

	/**
	 * 会员名称
	 */
	@ApiModelProperty(value = "会员名称")
	private String customerName;

	/**
	 * 会员账号
	 */
	@ApiModelProperty(value = "会员账号")
	private String customerAccount;

	/**
	 * 提现渠道 0:微信 1:支付宝
	 */
	@ApiModelProperty(value = "提现渠道 0:微信 1:支付宝")
	private DrawCashChannel drawCashChannel;

	/**
	 * 提现账户名称
	 */
	@ApiModelProperty(value = "提现账户名称")
	private String drawCashAccountName;

	/**
	 * 提现账户账号
	 */
	@ApiModelProperty(value = "提现账户账号")
	private String drawCashAccount;

	/**
	 * 本次提现金额
	 */
	@ApiModelProperty(value = "本次提现金额")
	private BigDecimal drawCashSum = BigDecimal.ZERO;

	/**
	 * 提现备注
	 */
	@ApiModelProperty(value = "提现备注")
	private String drawCashRemark;

	/**
	 * 运营端审核状态(0:待审核,1:审核不通过,2:审核通过)
	 */
	@ApiModelProperty(value = "运营端审核状态(0:待审核,1:审核不通过,2:审核通过)")
	private AuditStatus auditStatus;

	/**
	 * 运营端驳回原因
	 */
	@ApiModelProperty(value = "运营端驳回原因")
	private String rejectReason;

	/**
	 * 提现状态(0:未提现,1:提现失败,2:提现成功)
	 */
	@ApiModelProperty(value = "提现状态(0:未提现,1:提现失败,2:提现成功)")
	private DrawCashStatus drawCashStatus;

	/**
	 * 提现失败原因
	 */
	@ApiModelProperty(value = "提现失败原因")
	private String drawCashFailedReason;

	/**
	 * 用户操作状态(0:已申请,1:已取消)
	 */
	@ApiModelProperty(value = "用户操作状态(0:已申请,1:已取消)")
	private CustomerOperateStatus customerOperateStatus;

	/**
	 * 提现单完成状态(0:未完成,1:已完成)
	 */
	@ApiModelProperty(value = "提现单完成状态(0:未完成,1:已完成)")
	private FinishStatus finishStatus;

	/**
	 * 提现单完成时间
	 */
	@ApiModelProperty(value = "提现单完成时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime finishTime;

	/**
	 * 操作人
	 */
	@ApiModelProperty(value = "操作人")
	private String supplierOperateId;

	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTime;

	/**
	 * 删除标志(0:未删除,1:已删除)
	 */
	@ApiModelProperty(value = "删除标志(0:未删除,1:已删除)")
	private DeleteFlag delFlag;

	/**
	 * 微信openId
	 */
	@ApiModelProperty(value = "微信openId")
	private String openId;

	/**
	 * 微信openId来源 0:PC 1:MOBILE 2:App
	 */
	@ApiModelProperty(value = "微信openId来源")
	private DrawCashSource drawCashSource;

	/**
	 * 账户余额
	 */
	@ApiModelProperty(value = "账户余额")
	@Min(0)
	private BigDecimal accountBalance;

}