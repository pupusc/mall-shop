package com.wanmi.sbc.setting.wechatloginset.model.root;

import com.wanmi.sbc.common.enums.DefaultFlag;
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
 * <p>微信授权登录配置实体类</p>
 * @author lq
 * @date 2019-11-05 16:15:25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "wechat_login_set")
public class WechatLoginSet implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 微信授权登录配置主键
	 */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "wechat_set_id")
	private String wechatSetId;

	/**
	 * h5-微信授权登录是否启用 0 不启用， 1 启用
	 */
	@Column(name = "mobile_server_status")
	private DefaultFlag mobileServerStatus;

	/**
	 * h5-AppID(应用ID)
	 */
	@Column(name = "mobile_app_id")
	private String mobileAppId;

	/**
	 * h5-AppSecret(应用密钥)
	 */
	@Column(name = "mobile_app_secret")
	private String mobileAppSecret;

	/**
	 * pc-微信授权登录是否启用 0 不启用， 1 启用
	 */
	@Column(name = "pc_server_status")
	private DefaultFlag pcServerStatus;

	/**
	 * pc-AppID(应用ID)
	 */
	@Column(name = "pc_app_id")
	private String pcAppId;

	/**
	 * pc-AppSecret(应用密钥)
	 */
	@Column(name = "pc_app_secret")
	private String pcAppSecret;

	/**
	 * app-微信授权登录是否启用 0 不启用， 1 启用
	 */
	@Column(name = "app_server_status")
	private DefaultFlag appServerStatus;

	/**
	 * 门店id 平台默认storeId=0
	 */
	@Column(name = "store_id")
	private Long storeId;

	/**
	 * 创建时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "update_time")
	private LocalDateTime updateTime;

	/**
	 * 操作人
	 */
	@Column(name = "operate_person")
	private String operatePerson;

	public static WechatLoginSet getDefault(){
		return WechatLoginSet.builder()
				.mobileServerStatus(DefaultFlag.NO)
				.pcServerStatus(DefaultFlag.NO)
				.appServerStatus(DefaultFlag.NO).build();
	}

}