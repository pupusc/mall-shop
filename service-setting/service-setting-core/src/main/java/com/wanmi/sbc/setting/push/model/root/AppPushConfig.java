package com.wanmi.sbc.setting.push.model.root;

import com.wanmi.sbc.common.enums.DeleteFlag;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>消息推送实体类</p>
 * @author chenyufei
 * @date 2019-05-10 14:39:59
 */
@Data
@Entity
@Table(name = "app_push_config")
public class AppPushConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 消息推送配置编号
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "app_push_id")
	private Long appPushId;

	/**
	 * 消息推送配置名称
	 */
	@Column(name = "app_push_name")
	private String appPushName;

	/**
	 * 消息推送提供商  0:友盟
	 */
	@Column(name = "app_push_manufacturer")
	private String appPushManufacturer;

	/**
	 * Android App Key
	 */
	@Column(name = "android_app_key")
	private String androidAppKey;

	/**
	 * Android Umeng Message Secret
	 */
	@Column(name = "android_umeng_message_secret")
	private String androidUmengMessageSecret;

	/**
	 * Android App Master Secret
	 */
	@Column(name = "android_app_master_secret")
	private String androidAppMasterSecret;

	/**
	 * IOS App Key
	 */
	@Column(name = "ios_app_key")
	private String iosAppKey;

	/**
	 * IOS App Master Secret
	 */
	@Column(name = "ios_app_master_secret")
	private String iosAppMasterSecret;

	/**
	 * 状态,0:未启用1:已启用
	 */
	@Column(name = "status")
	private Integer status;

	/**
	 * 创建日期
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * 更新日期
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "update_time")
	private LocalDateTime updateTime;

	/**
	 * 删除日期
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "del_time")
	private LocalDateTime delTime;

	/**
	 * 删除标志
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;

}