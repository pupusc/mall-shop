package com.wanmi.sbc.setting.umengpushconfig.model.root;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * <p>友盟push接口配置实体类</p>
 * @author bob
 * @date 2020-01-07 10:34:04
 */
@Data
@Entity
@Table(name = "umeng_push_config")
public class UmengPushConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@Id
	@Column(name = "id")
	private Integer id;

	/**
	 * androidKeyId
	 */
	@Column(name = "android_key_id")
	private String androidKeyId;

	/**
	 * androidMsgSecret
	 */
	@Column(name = "android_msg_secret")
	private String androidMsgSecret;

	/**
	 * androidKeySecret
	 */
	@Column(name = "android_key_secret")
	private String androidKeySecret;

	/**
	 * iosKeyId
	 */
	@Column(name = "ios_key_id")
	private String iosKeyId;

	/**
	 * iosKeySecret
	 */
	@Column(name = "ios_key_secret")
	private String iosKeySecret;

}