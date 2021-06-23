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
 * 系统功能信息(相当于给权限做了一次分类)
 * Created by bail on 2017-01-04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "function_info")
public class FunctionInfo implements Serializable {

	private static final long serialVersionUID = -8349705525996917628L;

	/**
	 * 功能标识
	 */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "function_id")
	private String functionId;

	/**
	 * 系统类别(如:s2b平台,s2b商家)
	 */
	@Column(name = "system_type_cd")
	@NotNull
	private Platform systemTypeCd;

	/**
	 * 菜单id
	 */
	@Column(name = "menu_id")
	private String menuId;

	/**
	 * 功能显示名
	 */
	@Column(name = "function_title")
	private String functionTitle;

	/**
	 * 功能名称
	 */
	@Column(name = "function_name")
	private String functionName;

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
