package com.wanmi.sbc.setting.syssms.model.root;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import java.time.LocalDateTime;

import lombok.Data;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;

/**
 * <p>系统短信配置实体类</p>
 * @author lq
 * @date 2019-11-05 16:13:47
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sys_sms")
public class SysSms implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "sms_id")
	private String smsId;

	/**
	 * 接口地址
	 */
	@Column(name = "sms_url")
	private String smsUrl;

	/**
	 * 名称
	 */
	@Column(name = "sms_name")
	private String smsName;

	/**
	 * SMTP密码
	 */
	@Column(name = "sms_pass")
	private String smsPass;

	/**
	 * 网关
	 */
	@Column(name = "sms_gateway")
	private String smsGateway;

	/**
	 * 是否开启(0未开启 1已开启)
	 */
	@Column(name = "is_open")
	private Integer isOpen;

	/**
	 * createTime
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * modifyTime
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "modify_time")
	private LocalDateTime modifyTime;

	/**
	 * smsAddress
	 */
	@Column(name = "sms_address")
	private String smsAddress;

	/**
	 * smsProvider
	 */
	@Column(name = "sms_provider")
	private String smsProvider;

	/**
	 * smsContent
	 */
	@Column(name = "sms_content")
	private String smsContent;
}