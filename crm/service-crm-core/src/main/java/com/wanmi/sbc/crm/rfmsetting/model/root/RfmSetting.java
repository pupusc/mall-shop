package com.wanmi.sbc.crm.rfmsetting.model.root;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.crm.bean.enums.Period;
import com.wanmi.sbc.crm.bean.enums.RFMType;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>rfm参数配置实体类</p>
 * @author zhanglingke
 * @date 2019-10-14 14:33:42
 */
@Data
@Entity
@Table(name = "rfm_setting")
public class RfmSetting implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 参数
	 */
	@Column(name = "param")
	private Integer param;

	/**
	 * 得分
	 */
	@Column(name = "score")
	private Integer score;

	/**
	 * 参数类型：0:R,1:F,2:M
	 */
	@Column(name = "type")
	private RFMType type;

	/**
	 * 统计周期：0:近一个月，1:近3个月，2:近6个月，3:近一年
	 */
	@Column(name = "period")
	@Enumerated
	private Period period;

	/**
	 * 创建时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * 创建人
	 */
	@Column(name = "create_person")
	private String createPerson;

	/**
	 * 更新时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "update_time")
	private LocalDateTime updateTime;

	/**
	 * 更新人
	 */
	@Column(name = "update_person")
	private String updatePerson;

	/**
	 * 删除标识,0:未删除，1:已删除
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;

}