package com.wanmi.sbc.customer.fdpaidcast.model.root;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.enums.DeleteFlag;

import lombok.Data;
import javax.persistence.*;
import com.wanmi.sbc.common.base.BaseEntity;

/**
 * <p>樊登付费类型 映射商城付费类型实体类</p>
 * @author tzx
 * @date 2021-01-29 14:13:37
 */
@Data
@Entity
@Table(name = "fd_paid_cast")
public class FdPaidCast extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 樊登付费类型 映射商城付费类型主键
	 */
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	/**
	 * 樊登付费会员类型
	 */
	@Column(name = "fd_pay_type")
	private Integer fdPayType;

	/**
	 * 商城付费会员类型id
	 */
	@Column(name = "paid_card_id")
	private String paidCardId;

	/**
	 * 删除时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "delete_time")
	private LocalDateTime deleteTime;

	/**
	 * 删除标记  0：正常，1：删除
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;

}