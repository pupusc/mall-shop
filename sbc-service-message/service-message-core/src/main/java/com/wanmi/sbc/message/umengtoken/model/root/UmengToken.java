package com.wanmi.sbc.message.umengtoken.model.root;

import com.wanmi.sbc.message.bean.enums.PushPlatform;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>友盟推送设备与会员关系实体类</p>
 * @author bob
 * @date 2020-01-06 11:36:26
 */
@Data
@Entity
@Table(name = "umeng_token")
public class UmengToken implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 会员ID
	 */
	@Column(name = "customer_id")
	private String customerId;

	/**
	 * 友盟推送会员设备token
	 */
	@Column(name = "devlce_token")
	private String devlceToken;

	/**
	 * 友盟推送会员设备token平台类型
	 */
	@Column(name = "platform")
	@Enumerated
	private PushPlatform platform;

	/**
	 * 绑定时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "binding_time")
	private LocalDateTime bindingTime;

}