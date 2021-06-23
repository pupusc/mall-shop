package com.wanmi.sbc.customer.customersignrecord.model.root;

import com.wanmi.sbc.common.enums.DeleteFlag;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>用户签到记录实体类</p>
 * @author wangtao
 * @date 2019-10-05 16:13:04
 */
@Data
@Entity
@Table(name = "customer_sign_record")
public class CustomerSignRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户签到记录表id
	 */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "sign_record_id")
	private String signRecordId;

	/**
	 * 用户id
	 */
	@Column(name = "customer_id")
	private String customerId;

	/**
	 * 签到日期记录
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "sign_record")
	private LocalDateTime signRecord;

	/**
	 * 删除区分：0 未删除，1 已删除
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;

	/**
	 * 签到ip
	 */
	@Column(name = "sign_ip")
	private String signIp;

	/**
	 * 签到终端：pc,wechat,app,minipro
	 */
	@Column(name = "sign_terminal")
	private String signTerminal;

}