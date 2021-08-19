package com.wanmi.sbc.message.smssetting.model.root;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.message.bean.enums.SmsSettingType;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import java.time.LocalDateTime;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>短信配置实体类</p>
 * @author lvzhenwei
 * @date 2019-12-03 15:15:28
 */
@Data
@Entity
@Table(name = "sms_setting")
public class SmsSetting implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 调用api参数key
	 */
	@Column(name = "access_key_id")
	private String accessKeyId;

	/**
	 * 调用api参数secret
	 */
	@Column(name = "access_key_secret")
	private String accessKeySecret;

	/**
	 * 短信平台类型：0：阿里云短信平台
	 */
	@Column(name = "type")
	@Enumerated
	private SmsSettingType type;

	/**
	 * 是否启用：0：未启用；1：启用
	 */
	@Column(name = "status")
	@Enumerated
	private EnableStatus status;

	/**
	 * 删除标识：0：未删除；1：已删除
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;

	/**
	 * 创建时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "creat_time")
	private LocalDateTime creatTime;

}