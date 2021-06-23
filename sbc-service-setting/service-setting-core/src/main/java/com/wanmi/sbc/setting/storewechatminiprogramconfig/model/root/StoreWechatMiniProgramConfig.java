package com.wanmi.sbc.setting.storewechatminiprogramconfig.model.root;

import com.wanmi.sbc.common.enums.DeleteFlag;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>门店微信小程序配置实体类</p>
 * @author tangLian
 * @date 2020-01-16 11:47:15
 */
@Data
@Entity
@Table(name = "store_wechat_mini_program_config")
public class StoreWechatMiniProgramConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id")
	private String id;

	/**
	 * 微信appId
	 */
	@Column(name = "app_Id")
	private String appId;

	/**
	 * 微信appSecret
	 */
	@Column(name = "app_secret")
	private String appSecret;

	/**
	 * 门店id
	 */
	@Column(name = "store_id")
	private Long storeId;

	/**
	 * 商家id
	 */
	@Column(name = "company_info_id")
	private Long companyInfoId;

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
	 * 删除标志
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;

}