package com.wanmi.sbc.setting.systemprivacypolicy.model.root;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.enums.DeleteFlag;

import lombok.Data;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;

/**
 * <p>隐私政策实体类</p>
 * @author yangzhen
 * @date 2020-09-23 14:52:35
 */
@Data
@Entity
@Table(name = "system_privacy_policy")
public class SystemPrivacyPolicy implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 隐私政策id
	 */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "privacy_policy_id")
	private String privacyPolicyId;

	/**
	 * 隐私政策
	 */
	@Column(name = "privacy_policy")
	private String privacyPolicy;

	/**
	 * 隐私政策弹窗
	 */
	@Column(name = "privacy_policy_pop")
	private String privacyPolicyPop;

	/**
	 * 创建人
	 */
	@Column(name = "create_person")
	private String createPerson;

	/**
	 * 创建时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * 修改人
	 */
	@Column(name = "update_person")
	private String updatePerson;

	/**
	 * 修改时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "update_time")
	private LocalDateTime updateTime;

	/**
	 * 是否删除标志 0：否，1：是
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;

}