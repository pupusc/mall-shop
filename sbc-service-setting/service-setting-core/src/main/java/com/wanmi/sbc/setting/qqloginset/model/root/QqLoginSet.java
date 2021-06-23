package com.wanmi.sbc.setting.qqloginset.model.root;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import java.time.LocalDateTime;

import lombok.Data;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;

/**
 * <p>qq登录信息实体类</p>
 * @author lq
 * @date 2019-11-05 16:11:28
 */
@Data
@Entity
@Table(name = "qq_login_set")
public class QqLoginSet implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * qqSetId
	 */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "qq_set_id")
	private String qqSetId;

	/**
	 * mobileServerStatus
	 */
	@Column(name = "mobile_server_status")
	private Integer mobileServerStatus;

	/**
	 * mobileAppId
	 */
	@Column(name = "mobile_app_id")
	private String mobileAppId;

	/**
	 * mobileAppSecret
	 */
	@Column(name = "mobile_app_secret")
	private String mobileAppSecret;

	/**
	 * pcServerStatus
	 */
	@Column(name = "pc_server_status")
	private Integer pcServerStatus;

	/**
	 * pcAppId
	 */
	@Column(name = "pc_app_id")
	private String pcAppId;

	/**
	 * pcAppSecret
	 */
	@Column(name = "pc_app_secret")
	private String pcAppSecret;

	/**
	 * appServerStatus
	 */
	@Column(name = "app_server_status")
	private Integer appServerStatus;

	/**
	 * createTime
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * updateTime
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "update_time")
	private LocalDateTime updateTime;

	/**
	 * operatePerson
	 */
	@Column(name = "operate_person")
	private String operatePerson;

}