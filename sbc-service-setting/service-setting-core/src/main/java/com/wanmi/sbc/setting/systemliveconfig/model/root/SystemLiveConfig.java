package com.wanmi.sbc.setting.systemliveconfig.model.root;

import com.wanmi.sbc.common.enums.DeleteFlag;

import com.wanmi.sbc.common.enums.EnableStatus;
import lombok.Data;
import javax.persistence.*;
import com.wanmi.sbc.common.base.BaseEntity;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.time.LocalDateTime;

/**
 * <p>小程序直播设置实体类</p>
 * @author zwb
 * @date 2020-06-19 16:11:36
 */
@Data
@Entity
@Table(name = "system_live_config")
public class SystemLiveConfig extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "system_live_config_id")
	private String systemLiveConfigId;

	/**
	 * 是否启用标志 0：停用，1：启用
	 */
	@Column(name = "status")
	private EnableStatus status;

	/**
	 * 是否删除标志 0：否，1：是
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;

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
	 * 创建人
	 */
	@Column(name = "create_person")
	private String createPerson;

	/**
	 * 修改人
	 */
	@Column(name = "update_person")
	private String updatePerson;

}