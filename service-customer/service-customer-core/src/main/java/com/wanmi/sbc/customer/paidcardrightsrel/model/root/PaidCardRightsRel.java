package com.wanmi.sbc.customer.paidcardrightsrel.model.root;


import lombok.Data;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;

/**
 * <p>付费会员实体类</p>
 * @author xuhai
 * @date 2021-01-29 14:04:00
 */
@Data
@Entity
@Table(name = "paid_card_rights_rel")
public class PaidCardRightsRel implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@Column(name = "id")
	private String id;

	/**
	 * 所属会员权益id
	 */
	@Column(name = "paid_card_id")
	private String paidCardId;

	/**
	 * 权益id
	 */
	@Column(name = "rights_id")
	private Integer rightsId;

}