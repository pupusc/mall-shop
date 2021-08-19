package com.wanmi.sbc.setting.sensitivewords.model.root;

import com.wanmi.sbc.common.enums.DeleteFlag;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>实体类</p>
 * @author wjj
 * @date 2019-02-22 16:09:48
 */
@Data
@Entity
@Table(name = "sensitive_words")
public class SensitiveWords implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 敏感词id 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sensitive_id")
	private Long sensitiveId;

	/**
	 * 敏感词内容
	 */
	@Column(name = "sensitive_words")
	private String sensitiveWords;

	/**
	 * 是否删除
	 */
	@Column(name = "del_flag")
	private DeleteFlag delFlag;

	/**
	 * 创建人
	 */
	@Column(name = "create_user")
	private String createUser;

	/**
	 * 创建时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * 修改人
	 */
	@Column(name = "update_user")
	private String updateUser;

	/**
	 * 修改时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "update_time")
	private LocalDateTime updateTime;

	/**
	 * 删除人
	 */
	@Column(name = "delete_user")
	private String deleteUser;

	/**
	 * 删除时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "delete_time")
	private LocalDateTime deleteTime;

}