package com.wanmi.sbc.customer.paidcardcustomerrel.model.root;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.enums.DeleteFlag;

import lombok.Data;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;

/**
 * <p>付费会员实体类</p>
 * @author xuhai
 * @date 2021-01-29 14:03:59
 */
@Data
@Entity
@Table(name = "paid_card_customer_rel")
public class PaidCardCustomerRel implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@Column(name = "id")
	private String id;

	/**
	 * 会员id
	 */
	@Column(name = "customer_id")
	private String customerId;

	/**
	 * 付费会员类型ID
	 */
	@Column(name = "paid_card_id")
	private String paidCardId;

	/**
	 * 开始时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "begin_time")
	private LocalDateTime beginTime;

	/**
	 * 结束时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "end_time")
	private LocalDateTime endTime;

	/**
	 * 状态： 0：未删除 1：删除
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;

	/**
	 * 卡号
	 */
	@Column(name = "card_no")
	private String cardNo;

	/**
	 * 是否了发送即将过期短信
	 */
	@Column(name = "send_msg_flag")
	private Boolean sendMsgFlag;

	/**
	 * 是否了发送已过期短信
	 */
	@Column(name = "send_expire_msg_flag")
	private Boolean sendExpireMsgFlag;

	@Transient
	private String phone;

	@Transient
	private String paidCardName;

	/**
	 * 状态： 0：樊登同步的会员 1：商城的会员
	 */
	@Column(name = "paid_source")
	private Integer paidSource;

	/**
	 * 临时唯一id 自增
	 */
	@Column(name = "tmp_id")
	private Integer tmpId;
}