package com.wanmi.sbc.account.customerdrawcash.model.root;

import com.wanmi.sbc.account.bean.enums.*;
import com.wanmi.sbc.common.enums.DeleteFlag;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>会员提现管理实体类</p>
 * @author chenyufei
 * @date 2019-02-25 17:22:24
 */
@Data
@Entity
@Table(name = "customer_draw_cash")
public class CustomerDrawCash implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 提现id
	 */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "draw_cash_id")
	private String drawCashId;

	/**
	 * 提现单号(订单编号)
	 */
	@Column(name = "draw_cash_no")
	private String drawCashNo;

	/**
	 * 申请时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "apply_time")
	private LocalDateTime applyTime;

	/**
	 * 会员id
	 */
	@Column(name = "customer_id")
	private String customerId;

	/**
	 * 会员名称
	 */
	@Column(name = "customer_name")
	private String customerName;

	/**
	 * 会员账号
	 */
	@Column(name = "customer_account")
	private String customerAccount;

	/**
	 * 提现渠道 0:微信 1:支付宝
	 */
	@Column(name = "draw_cash_channel")
	private DrawCashChannel drawCashChannel;

	/**
	 * 提现账户名称
	 */
	@Column(name = "draw_cash_account_name")
	private String drawCashAccountName;

	/**
	 * 提现账户账号
	 */
	@Column(name = "draw_cash_account")
	private String drawCashAccount;

	/**
	 * 本次提现金额
	 */
	@Column(name = "draw_cash_sum")
	private BigDecimal drawCashSum = BigDecimal.ZERO;

	/**
	 * 提现备注
	 */
	@Column(name = "draw_cash_remark")
	private String drawCashRemark;

	/**
	 * 运营端审核状态(0:待审核,1:审核不通过,2:审核通过)
	 */
	@Column(name = "audit_status")
	private AuditStatus auditStatus;

	/**
	 * 运营端驳回原因
	 */
	@Column(name = "reject_reason")
	private String rejectReason;

	/**
	 * 提现状态(0:未提现,1:提现失败,2:提现成功)
	 */
	@Column(name = "draw_cash_status")
	private DrawCashStatus drawCashStatus;

	/**
	 * 提现失败原因
	 */
	@Column(name = "draw_cash_failed_reason")
	private String drawCashFailedReason;

	/**
	 * 用户操作状态(0:已申请,1:已取消)
	 */
	@Column(name = "customer_operate_status")
	private CustomerOperateStatus customerOperateStatus;

	/**
	 * 提现单完成状态(0:未完成,1:已完成)
	 */
	@Column(name = "finish_status")
	private FinishStatus finishStatus;

	/**
	 * 提现单完成时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "finish_time")
	private LocalDateTime finishTime;

	/**
	 * 操作人
	 */
	@Column(name = "supplier_operate_id")
	private String supplierOperateId;

	/**
	 * 更新时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "update_time")
	private LocalDateTime updateTime;

	/**
	 * 删除标志(0:未删除,1:已删除)
	 */
	@Column(name = "del_flag")
	private DeleteFlag delFlag;

	/**
	 * 微信openId
	 */
	@Column(name = "open_id")
	private String openId;

	/**
	 * 微信openId来源 0:PC 1:H5 2:App
	 */
	@Column(name = "draw_cash_source")
	private DrawCashSource drawCashSource;

	/**
	 * 账户余额
	 */
	@Column(name = "account_balance")
	private BigDecimal accountBalance;

}