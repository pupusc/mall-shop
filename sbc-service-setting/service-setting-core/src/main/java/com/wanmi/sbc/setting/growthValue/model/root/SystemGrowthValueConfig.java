package com.wanmi.sbc.setting.growthValue.model.root;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.setting.bean.enums.GrowthValueRule;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>系统成长值设置实体类</p>
 * @author yxz
 * @date 2019-04-03 11:43:28
 */
@Data
@Entity
@Table(name = "system_growth_value_config")
public class SystemGrowthValueConfig implements Serializable {
	private static final long serialVersionUID = -4194131814317040733L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "growth_value_config_id")
	private String growthValueConfigId;

	/**
	 * 成长值获取规则
	 */
	@Column(name = "rule")
	private GrowthValueRule rule;

	/**
	 * 成长值说明
	 */
	@Column(name = "remark")
	private String remark;

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
	 * 修改时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "update_time")
	private LocalDateTime updateTime;

}