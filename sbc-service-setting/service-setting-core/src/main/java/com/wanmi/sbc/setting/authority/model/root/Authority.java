package com.wanmi.sbc.setting.authority.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.Platform;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统权限信息
 * Created by bail on 2017-12-28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "authority")
public class Authority implements Serializable {

	private static final long serialVersionUID = -8349705525996917628L;

	/**
	 * 权限标识
	 */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "authority_id")
	private String authorityId;

	/**
	 * 系统类别(如:s2b平台,s2b商家)
	 */
	@Column(name = "system_type_cd")
	@NotNull
	private Platform systemTypeCd;

	/**
	 * 功能id
	 */
	@Column(name = "function_id")
	private String functionId;

	/**
	 * 权限显示名
	 */
	@Column(name = "authority_title")
	private String authorityTitle;

	/**
	 * 权限名称
	 */
	@Column(name = "authority_name")
	private String authorityName;

	/**
	 * 权限路径
	 */
	@Column(name = "authority_url")
	private String authorityUrl;

	/**
	 * 权限请求类别(GET,POST,PUT,DELETE)
	 */
	@Column(name = "request_type")
	private String requestType;

	/**
	 * 备注
	 */
	@Column(name = "remark")
	private String remark;

	/**
	 * 排序号
	 */
	@Column(name = "sort")
	private Integer sort;

	/**
	 * 创建时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * 删除标识,0:未删除1:已删除
	 */
	@Column(name = "del_flag")
	@NotNull
	private DeleteFlag delFlag;
	
}
