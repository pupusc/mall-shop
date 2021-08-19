package com.wanmi.sbc.customer.paidcardbuyrecord.model.root;

import com.wanmi.sbc.customer.bean.enums.BuyTypeEnum;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import java.time.LocalDateTime;
import java.math.BigDecimal;

import lombok.Data;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;

/**
 * <p>付费会员实体类</p>
 * @author xuhai
 * @date 2021-01-29 14:03:58
 */
@Data
@Entity
@Table(name = "paid_card_buy_record")
public class PaidCardBuyRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 流水号
	 */
	@Id
	@Column(name = "pay_code")
	private String payCode;

	/**
	 * 用户id
	 */
	@Column(name = "customer_id")
	private String customerId;

	/**
	 * 用户姓名
	 */
	@Column(name = "customer_name")
	private String customerName;

	/**
	 * 创建时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * 卡号
	 */
	@Column(name = "card_no")
	private String cardNo;

	/**
	 * 付费会员类型id
	 */
	@Column(name = "paid_card_id")
	private String paidCardId;

	/**
	 * 付费会员类型名称
	 */
	@Column(name = "paid_card_name")
	private String paidCardName;

	/**
	 * 付费周期id
	 */
	@Column(name = "rule_id")
	private String ruleId;

	/**
	 * 付费周期名称
	 */
	@Column(name = "rule_name")
	private String ruleName;

	/**
	 * 价格
	 */
	@Column(name = "price")
	private BigDecimal price;

	/**
	 * 购买类型 0：购买 1：续费
	 */
	@Column(name = "type")
	private BuyTypeEnum type;

	/**
	 * 用户账号
	 */
	@Column(name = "customer_account")
	private String customerAccount;

	/**
	 * 失效时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "invalid_time")
	private LocalDateTime invalidTime;

	/**
	 * 付费卡实例ID
	 */
	@Column(name = "customer_paidcard_id")
	private String customerPaidcardId;

	/**
	 * 开始时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "begin_time")
	private LocalDateTime beginTime;

}