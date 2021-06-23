package com.wanmi.sbc.setting.wechatshareset.model.root;

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
 * <p>微信分享配置实体类</p>
 * @author lq
 * @date 2019-11-05 16:15:54
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "wechat_share_set")
public class WechatShareSet implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 微信分享参数配置主键
	 */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "share_set_id")
	private String shareSetId;

	/**
	 * 微信公众号App ID
	 */
	@Column(name = "share_app_id")
	private String shareAppId;

	/**
	 * 微信公众号 App Secret
	 */
	@Column(name = "share_app_secret")
	private String shareAppSecret;

	/**
	 * 门店id
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

}